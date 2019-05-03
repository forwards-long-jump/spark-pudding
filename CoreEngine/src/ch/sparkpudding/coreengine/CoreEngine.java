package ch.sparkpudding.coreengine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import javax.swing.JPanel;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import ch.sparkpudding.coreengine.ecs.component.Component;
import ch.sparkpudding.coreengine.ecs.entity.Entity;
import ch.sparkpudding.coreengine.ecs.entity.Scene;
import ch.sparkpudding.coreengine.ecs.system.RenderSystem;
import ch.sparkpudding.coreengine.ecs.system.UpdateSystem;
import ch.sparkpudding.coreengine.filereader.LelReader;
import ch.sparkpudding.coreengine.filereader.XMLParser;
import ch.sparkpudding.coreengine.utils.Pair;

/**
 * Class keeping track of all the elements of the ECS, and responsible of
 * running it. Also owns inputs and outputs of the game.
 *
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba
 *
 */
@SuppressWarnings("serial")
public class CoreEngine extends JPanel {

	private double msPerUpdate = (1000 / 60);
	private boolean exit = false;

	private Input input;

	private LelReader lelFile;

	private Map<String, Scene> scenes;
	private Scene currentScene;

	private List<UpdateSystem> systems;
	private RenderSystem renderSystem;

	private boolean pause = false;
	private boolean pauseAll = false;

	private Dimension renderSize;
	private Color blackBarColor;
	private Semaphore renderLock;

	private int tick;
	private int fpsCount;
	private int fps;

	private List<Entity> entitesToDeleteAfterUpdate;
	private List<Pair<Entity, String>> componentsToRemoveAfterUpdate;

	/**
	 * The heart of the Ludic Engine in Lua
	 * 
	 * @param gameFolder Location of the game file
	 * @throws Exception All kind of things, really. Ranging from thread to lua
	 *                   errors.
	 */
	public CoreEngine(String gameFolder) throws Exception {
		Lel.coreEngine = this;
		this.input = new Input(this);

		entitesToDeleteAfterUpdate = new ArrayList<Entity>();
		componentsToRemoveAfterUpdate = new ArrayList<Pair<Entity, String>>();

		this.renderSize = new Dimension(1280, 720);
		this.blackBarColor = Color.BLACK;
		this.tick = 0;
		this.fps = 0;
		this.fpsCount = 0;

		this.lelFile = new LelReader(gameFolder);

		populateComponentTemplates();
		populateEntityTemplates();
		populateScenes();
		loadSystems();

		setCurrentScene(scenes.get("main"));

		renderLock = new Semaphore(0);

		new Thread(() -> {
			try {
				startGame();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();
	}

	/**
	 * Populates systems list with system files
	 */
	private void loadSystems() {
		systems = new ArrayList<UpdateSystem>();
		renderSystem = null;

		for (File systemFile : lelFile.getSystems()) {
			if (systemFile.getName().equals(RenderSystem.LUA_FILE_NAME)) {
				renderSystem = new RenderSystem(systemFile, this);
			} else {
				systems.add(new UpdateSystem(systemFile, this));
			}
		}
	}

	/**
	 * Populates scenes list with scene files
	 *
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	private void populateScenes() throws ParserConfigurationException, SAXException, IOException {
		scenes = new HashMap<String, Scene>();

		for (File xmlFile : lelFile.getScenesXML()) {
			Scene scene = new Scene(XMLParser.parse(xmlFile));
			addScene(scene.getName(), scene);
		}
	}

	/**
	 * Populates entity templates list with entity template files
	 *
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	private void populateEntityTemplates() throws ParserConfigurationException, SAXException, IOException {
		for (File xmlFile : lelFile.getEntityTemplatesXML()) {
			Entity e = new Entity(XMLParser.parse(xmlFile));
			Entity.addTemplate(e);
		}
	}

	/**
	 * Populates component templates list with component template files
	 *
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	private void populateComponentTemplates() throws ParserConfigurationException, SAXException, IOException {
		for (File xmlFile : lelFile.getComponentsXML()) {
			Component c = new Component(XMLParser.parse(xmlFile));
			Component.addTemplate(c);
		}
	}

	/**
	 * Runs update and render loops
	 * 
	 * @throws InterruptedException
	 */
	private void startGame() throws InterruptedException {
		double previous = java.lang.System.currentTimeMillis();
		double lag = 0.0;

		double lastFpsTime = java.lang.System.currentTimeMillis();

		while (!exit) {
			double current = java.lang.System.currentTimeMillis();
			double elapsed = current - previous;

			previous = current;
			lag += elapsed;

			input.update();

			while (lag >= msPerUpdate) {
				tick++;
				update();
				lag -= msPerUpdate;
			}

			render();
			renderLock.acquire();

			if (java.lang.System.currentTimeMillis() - lastFpsTime >= 1000) {
				lastFpsTime = java.lang.System.currentTimeMillis();
				fpsCount = fps;
				fps = 0;
			}
		}
	}

	/**
	 * Runs all systems once
	 */
	private void update() {
		if (pauseAll) {
			return;
		}

		for (UpdateSystem system : systems) {
			system.update();
		}

		currentScene.getCamera().update();

		for (Pair<Entity, String> pair : componentsToRemoveAfterUpdate) {
			removeComponent(pair.first(), pair.second());
		}
		for (Entity entity : entitesToDeleteAfterUpdate) {
			deleteEntity(entity);
		}
	}

	/**
	 * Runs the renderer system
	 */
	private void render() {
		repaint();
	}

	/**
	 * Pauses all systems indescriminately
	 */
	public void togglePauseAll() {
		pauseAll = !pauseAll;
	}

	/**
	 * Pauses all systems which are labelled "pausable"
	 */
	public void togglePause() {
		pause = !pause;
	}

	/**
	 * Return all the scenes
	 * 
	 * @return Scenes
	 */
	public Map<String, Scene> getScenes() {
		return scenes;
	}

	/**
	 * Add scene to scenes list
	 *
	 * @param name Name of the scene
	 * @param s    Scene
	 */
	public void addScene(String name, Scene s) {
		scenes.put(name, s);
	}

	/**
	 * Sets scene as current scene, without reloading
	 *
	 * @param name Name of the scene
	 */
	public void setScene(String name) {
		setScene(name, false);
	}

	/**
	 * Sets scene as current scene, and reloads it if demanded
	 *
	 * @param name  Name of the Scene
	 * @param reset The scene will be reloaded when set to true
	 */
	public void setScene(String name, boolean reset) {
		setCurrentScene(scenes.get(name));
		if (reset) {
			resetScene();
		}
	}

	/**
	 * Resets current scene
	 */
	public void resetScene() {
		// TODO: reset current scene
	}

	/**
	 * Get the current scene
	 * 
	 * @return the current scene
	 */
	public Scene getCurrentScene() {
		return currentScene;
	}

	/**
	 * Change current scene to new scene
	 * 
	 * @param newScene
	 */
	public void setCurrentScene(Scene newScene) {
		this.currentScene = newScene;
		for (UpdateSystem system : systems) {
			system.setEntities(newScene.getEntities());
		}
		renderSystem.setEntities(newScene.getEntities());
	}

	private Point getGameTranslation() {
		double scaleRatio = getScaleRatio();
		// Calculate translation to center the game
		int realGameWidth = (int) (scaleRatio * renderSize.getWidth());
		int realGameHeight = (int) (scaleRatio * renderSize.getHeight());

		int translateX = getWidth() / 2 - realGameWidth / 2;
		int translateY = getHeight() / 2 - realGameHeight / 2;

		return new Point(translateX, translateY);
	}

	/**
	 * Calculate height
	 * 
	 * @return
	 */
	private double getScaleRatio() {
		// Calculate screen ratio for width / height
		double scaleRatio = 1.0;
		double heightScaleRatio = getHeight() / renderSize.getHeight();
		double widthScaleRatio = getWidth() / renderSize.getWidth();

		// Make sure the whole game is displayed by picking the smallest ratio
		if (widthScaleRatio > heightScaleRatio) {
			scaleRatio = heightScaleRatio;
		} else {
			scaleRatio = widthScaleRatio;
		}

		return scaleRatio;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;
		AffineTransform transformationState = g2d.getTransform();

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Calculate screen ratio for width / height
		double scaleRatio = getScaleRatio();
		// Calculate translation to center the game
		Point translation = getGameTranslation();

		// Apply transforms
		g2d.translate(translation.getX(), translation.getY());
		g2d.scale(scaleRatio, scaleRatio);

		fps++;
		renderSystem.render(g2d);

		g2d.setTransform(transformationState);

		// Draw black bars
		drawBlackBars(g2d, scaleRatio, translation);

		// Source:
		// https://stackoverflow.com/questions/33257540/java-window-lagging-on-ubuntu-but-not-windows-when-code-isnt-lagging
		java.awt.Toolkit.getDefaultToolkit().sync();
		g.dispose();

		renderLock.release();
	}

	private void drawBlackBars(Graphics2D g2d, double scaleRatio, Point translation) {
		// Draw black bar
		g2d.setColor(blackBarColor);
		double heightScaleRatio = getHeight() / renderSize.getHeight();
		double widthScaleRatio = getWidth() / renderSize.getWidth();
		int realGameWidth = (int) (scaleRatio * renderSize.getWidth());
		int realGameHeight = (int) (scaleRatio * renderSize.getHeight());
		int translateX = (int) translation.getX();
		int translateY = (int) translation.getY();

		if (widthScaleRatio > heightScaleRatio) {
			// Vertical
			g2d.fillRect(0, 0, translateX, getHeight());
			g2d.fillRect(translateX + realGameWidth, 0, translateX + 1, getHeight() + 1);
		} else {
			// Horizontal
			g2d.fillRect(0, 0, getWidth(), translateY);
			g2d.fillRect(0, translateY + realGameHeight, getWidth() + 1, translateY + 1);
		}
	}

	/**
	 * Getter for tick. Tick is increased by 1 every update
	 *
	 * @return current tick
	 */
	public int getTick() {
		return tick;
	}

	/**
	 * Getter for camera.
	 * 
	 * @return camera
	 */
	public Camera getCamera() {
		return currentScene.getCamera();
	}

	/**
	 * Getter for input.
	 * 
	 * @return
	 */
	public Input getInput() {
		return input;
	}

	/**
	 * Add an entity to current scene and notify systems
	 * 
	 * @param e entity to add
	 */
	public void addEntity(Entity e) {
		renderSystem.tryAdd(e);
		for (UpdateSystem system : systems) {
			system.tryAdd(e);
		}

		getCurrentScene().add(e);
	}

	/**
	 * Get current framerate of the game
	 * 
	 * @return
	 */
	public int getFPS() {
		return fpsCount;
	}

	/**
	 * Delete an entity and removes it from the scene and systems
	 * 
	 * @param entity Entity to be deleted
	 */
	public void deleteEntity(Entity entity) {
		for (UpdateSystem system : systems) {
			system.tryRemove(entity);
		}
		renderSystem.tryRemove(entity);
		currentScene.remove(entity);
	}

	/**
	 * Removes the named component from the entity, and removes the entity from
	 * systems where it is no longer needed
	 * 
	 * @param entity        Entity to work on
	 * @param componentName Name of the component to remove
	 */
	public void removeComponent(Entity entity, String componentName) {
		entity.remove(componentName);
		for (UpdateSystem system : systems) {
			system.notifyRemovedComponent(entity, componentName);
		}
		renderSystem.notifyRemovedComponent(entity, componentName);
	}

	/**
	 * Adds the given entity to have the given component removed from it after the
	 * update
	 * 
	 * @param entity        Entity to work on
	 * @param componentName Name of the component to remove
	 */
	public void removeComponentAfterUpdate(Entity entity, String componentName) {
		componentsToRemoveAfterUpdate.add(new Pair<Entity, String>(entity, componentName));
	}

	/**
	 * When an entity receives a new component, notify the systems of this in case
	 * they now need it
	 * 
	 * @param entity        Entity which has received a component
	 * @param componentName Name of the new component
	 */
	public void notifySystemsOfNewComponent(Entity entity, String componentName) {
		for (UpdateSystem system : systems) {
			system.notifyNewComponent(entity, componentName);
		}
		renderSystem.notifyNewComponent(entity, componentName);
	}

	/**
	 * Adds the given entity to the list of deletable entities
	 * 
	 * @param entity
	 */
	public void deleteEntityAfterUpdate(Entity entity) {
		entitesToDeleteAfterUpdate.add(entity);
	}

	/**
	 * Get game height (not jpanel height)
	 * 
	 * @return
	 */
	public double getGameHeight() {
		return this.renderSize.getHeight();
	}

	/**
	 * Get game width (not jpanel height)
	 * 
	 * @return
	 */
	public double getGameWidth() {
		return this.renderSize.getWidth();
	}

	/**
	 * Convert a panel position to the game (UI) position
	 * 
	 * @param p
	 * @return Point2D
	 */
	public Point2D panelPositionToGame(Point2D p) {
		double x = p.getX();
		double y = p.getY();

		double scaleRatio = getScaleRatio();
		Point translation = getGameTranslation();

		x -= translation.getX();
		y -= translation.getY();

		// Game ratio scaling
		x /= scaleRatio;
		y /= scaleRatio;

		return new Point2D.Double(x, y);
	}

	/**
	 * Convert a panel position to the game world position
	 * 
	 * @param p
	 * @return Point2D
	 */
	public Point2D panelPositionToWorld(Point2D p) {
		Point2D gamePosition = panelPositionToGame(p);
		double x = gamePosition.getX();
		double y = gamePosition.getY();

		x += currentScene.getCamera().getPosition().getX();
		y += currentScene.getCamera().getPosition().getY();

		x /= currentScene.getCamera().getScaling();
		y /= currentScene.getCamera().getScaling();

		return new Point2D.Double(x, y);
	}
}
