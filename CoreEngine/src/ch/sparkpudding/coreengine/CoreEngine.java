package ch.sparkpudding.coreengine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import javax.swing.JPanel;
import javax.xml.parsers.ParserConfigurationException;

import org.luaj.vm2.LuaError;
import org.xml.sax.SAXException;

import ch.sparkpudding.coreengine.Scheduler.Trigger;
import ch.sparkpudding.coreengine.ecs.component.Component;
import ch.sparkpudding.coreengine.ecs.entity.Entity;
import ch.sparkpudding.coreengine.ecs.entity.Scene;
import ch.sparkpudding.coreengine.ecs.system.RenderSystem;
import ch.sparkpudding.coreengine.ecs.system.UpdateSystem;
import ch.sparkpudding.coreengine.filereader.LelReader;
import ch.sparkpudding.coreengine.filereader.XMLParser;
import ch.sparkpudding.coreengine.utils.Collision;
import ch.sparkpudding.coreengine.utils.Drawing;

/**
 * Class keeping track of all the elements of the ECS, and responsible of
 * running it. Also owns inputs and outputs of the game.
 *
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba
 *
 */
@SuppressWarnings("serial")
public class CoreEngine extends JPanel {

	private double msPerUpdate;
	private boolean exit;

	private Input input;
	private ResourceLocator resourceLocator;

	private LelReader lelFile;

	private Map<String, Scene> scenes;
	private Scene currentScene;

	private List<UpdateSystem> systems;
	private List<UpdateSystem> editingSystems;
	private RenderSystem renderSystem;
	private RenderSystem editingRenderSystem;

	private boolean pause;
	private boolean editingPause;

	private Dimension renderSize;
	private Color blackBarColor;
	private Semaphore renderLock;

	private int fpsCount;
	private int fps;

	private Scheduler scheduler;

	private LuaError luaError;

	/**
	 * The heart of the Ludic Engine in Lua
	 * 
	 * @param gameFolder Location of the game file
	 * @throws Exception All kind of things, really. Ranging from thread to lua
	 *                   errors.
	 */
	public CoreEngine(String gameFolder) throws Exception {
		init(gameFolder);
		startGame();
		setCurrentScene(scenes.get("main"));
	}

	/**
	 * Create a CoreEngine which goal is to be run inside of scene editor
	 * 
	 * @param gameFolder         Location of the game file
	 * @param editingToolsFolder The path to a folder containing "editing" systems
	 *                           and components
	 * @throws Exception
	 */
	public CoreEngine(String gameFolder, String editingToolsFolder) throws Exception {
		init(gameFolder);
		this.lelFile.loadEditingTools(editingToolsFolder);

		populateEditingComponentTemplates();
		editingSystems = new ArrayList<UpdateSystem>();
		editingRenderSystem = loadSystemsFromFiles(editingSystems, lelFile.getEditingSystems());

		startGame();
		setCurrentScene(scenes.get("main"));
	}

	/**
	 * Shared constructor
	 * 
	 * @param gameFolder Location of the game file
	 * @throws Exception
	 */
	private void init(String gameFolder) throws Exception {
		Lel.coreEngine = this;
		this.input = new Input(this);

		this.fps = 0;
		this.fpsCount = 0;
		this.msPerUpdate = (1000 / 60);
		this.exit = false;

		this.pause = false;
		this.editingPause = false;

		this.scheduler = new Scheduler();

		this.renderSize = new Dimension(1280, 720);
		this.blackBarColor = Color.BLACK;

		this.renderLock = new Semaphore(0);

		this.lelFile = new LelReader(gameFolder);
		this.resourceLocator = new ResourceLocator(this.lelFile);

		populateComponentTemplates();
		populateEntityTemplates();
		populateScenes();

		systems = new ArrayList<UpdateSystem>();
		renderSystem = loadSystemsFromFiles(systems, lelFile.getSystems());
	}

	/**
	 * Create System from files and place them in a container, return the render
	 * system
	 * 
	 * @param systemContainer
	 * @param systemList
	 * @return RenderSystem to be stored somewhere
	 */
	private RenderSystem loadSystemsFromFiles(List<UpdateSystem> systemContainer, Collection<File> systemList) {
		RenderSystem renderSystem = null;
		systemContainer.clear();

		for (File systemFile : systemList) {
			if (systemFile.getName().equals(RenderSystem.LUA_FILE_NAME)) {
				renderSystem = new RenderSystem(systemFile);
			} else {
				systemContainer.add(new UpdateSystem(systemFile));
			}
		}

		return renderSystem;
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
	 * Populates component templates list with component template files <br>
	 * TODO: Merge this function and the one above
	 * 
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	private void populateEditingComponentTemplates() throws ParserConfigurationException, SAXException, IOException {
		for (File xmlFile : lelFile.getEditingComponentsXML()) {
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
		new Thread(() -> {
			try {
				runGameLoop();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();
	}

	/**
	 * Run the game loop until the game ends
	 * 
	 * @throws InterruptedException
	 */
	private void runGameLoop() throws InterruptedException {
		double previous = java.lang.System.currentTimeMillis();
		double lag = 0.0;

		double lastFpsTime = java.lang.System.currentTimeMillis();

		while (!exit) {
			scheduler.trigger(Trigger.GAME_LOOP_START);
			double current = java.lang.System.currentTimeMillis();
			double elapsed = current - previous;

			previous = current;
			lag += elapsed;

			while (lag >= msPerUpdate) {
				input.update();

				handleLuaErrors();

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
	 * To be called before upading, handle lua error actions
	 */
	private void handleLuaErrors() {
		if (luaError != null) {
			editingPause = true;
			// Try to continue
			if (input.isKeyDown(KeyEvent.VK_SPACE)) {
				editingPause = false;
				luaError = null;
			} else if (input.isKeyDown(KeyEvent.VK_ENTER)) {
				reloadSystemsFromDisk();
				editingPause = false;
			}
			input.resetAllKeys();
		}
	}

	/**
	 * Reload systems from disk, live
	 */
	public void reloadSystemsFromDisk() {
		if (editingSystems != null) {
			editingRenderSystem = loadSystemsFromFiles(editingSystems, lelFile.getEditingSystems());
		}

		if (luaError != null) {
			luaError = null; // Let's remove the error as reloading systems may fix it
		}

		renderSystem = loadSystemsFromFiles(systems, lelFile.getSystems());
		setCurrentScene(getCurrentScene());
	}

	/**
	 * Runs all systems once
	 */
	private void update() {
		scheduler.trigger(Trigger.BEFORE_UPDATE);

		currentScene.getCamera().update();

		// Only update editing systems when the game is paused
		if (editingPause && editingSystems != null) {
			for (UpdateSystem system : editingSystems) {
				system.update();
			}
		} else {
			currentScene.incrementTick();

			for (UpdateSystem system : systems) {
				system.update();
			}
		}

		scheduler.trigger(Trigger.AFTER_UPDATE);
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
	public void toggleEditingPause() {
		editingPause = !editingPause;
	}

	/**
	 * Return true if paused for editing
	 * 
	 * @return true if paused for editing
	 */
	public boolean isEditingPause() {
		return editingPause;
	}

	/**
	 * Change the editingPause state
	 * 
	 * @param pause
	 */
	public void setEditingPause(boolean pause) {
		editingPause = pause;
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
		if (reset) {
			scenes.get(name).reset();
		}
		setCurrentScene(scenes.get(name));
	}

	/**
	 * Reset the current scene. To call be call before updating
	 */
	public void resetCurrentScene() {
		currentScene.reset();
		setCurrentScene(currentScene);
	}

	/**
	 * Get the task scheduler
	 * 
	 * @return the task scheduler
	 */
	public Scheduler getScheduler() {
		return scheduler;
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
	 * @param newScene to switch to
	 */
	public void setCurrentScene(Scene newScene) {
		this.currentScene = newScene;
		for (UpdateSystem system : systems) {
			system.setEntities(newScene.getEntities());
		}

		renderSystem.setEntities(newScene.getEntities());

		if (editingSystems != null) {
			for (UpdateSystem system : editingSystems) {
				system.setEntities(newScene.getEntities());
			}
		}

		if (editingRenderSystem != null) {
			editingRenderSystem.setEntities(newScene.getEntities());
		}
	}

	/**
	 * Return the translation of the game (the one that keep it centered in the
	 * middle of black bars)
	 * 
	 * @return Point the translation of the game (the one using black bars)
	 */
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
	 * Calculate scale ratio
	 * 
	 * @return the max w/h or h/w scale ratio
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

		if (editingPause && editingRenderSystem != null) {
			editingRenderSystem.render(g2d);
		}

		g2d.setTransform(transformationState);

		// Draw black bars
		drawBlackBars(g2d, scaleRatio, translation);

		// Render info about any lua error if any
		renderLuaError(g2d);
		// Source:
		// https://stackoverflow.com/questions/33257540/java-window-lagging-on-ubuntu-but-not-windows-when-code-isnt-lagging
		java.awt.Toolkit.getDefaultToolkit().sync();
		g.dispose();

		renderLock.release();
	}

	/**
	 * Render an overlay giving help about a lua error that could've occurer
	 * 
	 * @param g2d graphics context
	 */
	private void renderLuaError(Graphics2D g2d) {
		if (luaError != null) {
			int x = 20;
			int y = 40;
			int maxWidth = getWidth() - x;
			int smallFontSize = 15;
			int bigFontSize = 30;

			// Make it look like the game is paused
			g2d.setColor(new Color(0, 0, 0, 150));
			g2d.fillRect(0, 0, getWidth(), getHeight());

			g2d.setColor(new Color(0, 0, 0, 255));
			g2d.fillRect(0, 0, getWidth(), 170); // Estimated error size

			g2d.setColor(Color.WHITE);
			g2d.setFont(new Font(Font.DIALOG, Font.BOLD, bigFontSize));
			y = Drawing.drawWrappedString("Something went wrong", x, y, maxWidth, g2d);

			g2d.setColor(Color.RED);
			g2d.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, smallFontSize));

			y = Drawing.drawWrappedString(luaError.getMessage(), x, y, maxWidth, g2d);
			y += smallFontSize;

			g2d.setColor(Color.LIGHT_GRAY);
			y = Drawing.drawWrappedString("Press [SPACE] to ignore this error and attempt to continue.", x, y, maxWidth,
					g2d);
			Drawing.drawWrappedString("Press [ENTER] to reload systems from disk.", x, y, maxWidth, g2d);
		}
	}

	/**
	 * Draw black bars hiding the game
	 * 
	 * @param g2d
	 * @param scaleRatio  current scale ratio used for the game
	 * @param translation translation used for the game
	 */
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
	 * Getter for camera.
	 * 
	 * @return camera attached for current scene
	 */
	public Camera getCamera() {
		return currentScene.getCamera();
	}

	/**
	 * Getter for input.
	 * 
	 * @return input api
	 */
	public Input getInput() {
		return input;
	}

	/**
	 * Getter for resourceLocator
	 * 
	 * @return resourceLocator
	 */
	public ResourceLocator getResourceLocator() {
		return resourceLocator;
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

		if (editingRenderSystem != null) {
			editingRenderSystem.tryAdd(e);
		}
		if (editingSystems != null) {
			for (UpdateSystem system : editingSystems) {
				system.tryAdd(e);
			}
		}

		getCurrentScene().add(e);
	}

	/**
	 * Get current framerate of the game
	 * 
	 * @return current framerate
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

		if (editingSystems != null) {
			for (UpdateSystem system : editingSystems) {
				system.tryRemove(entity);
			}
		}
		if (editingRenderSystem != null) {
			editingRenderSystem.tryRemove(entity);
		}
	}

	/**
	 * Removes the named component from the entity, and removes the entity from
	 * systems where it is no longer needed
	 * 
	 * @param entity        Entity to work on
	 * @param componentName Name of the component to remove
	 */
	public void removeComponent(Entity entity, String componentName) {
		if (entity.remove(componentName)) {
			for (UpdateSystem system : systems) {
				system.notifyRemovedComponent(entity, componentName);
			}
			renderSystem.notifyRemovedComponent(entity, componentName);

			if (editingSystems != null) {
				for (UpdateSystem system : editingSystems) {
					system.notifyRemovedComponent(entity, componentName);
				}
			}
			if (editingRenderSystem != null) {
				editingRenderSystem.notifyRemovedComponent(entity, componentName);
			}
		}
	}

	/**
	 * When an entity receives a new component, notify the systems of this in case
	 * they now need it
	 * 
	 * @param entity        Entity which has received a component
	 * @param componentName Name of the new component
	 */
	public void notifySystemsOfNewComponent(Entity entity, Component component) {
		String componentName = component.getName();
		for (UpdateSystem system : systems) {
			system.notifyNewComponent(entity, componentName);
		}
		renderSystem.notifyNewComponent(entity, componentName);

		if (editingSystems != null) {
			for (UpdateSystem system : editingSystems) {
				system.notifyNewComponent(entity, componentName);
			}
		}
		if (editingRenderSystem != null) {
			editingRenderSystem.notifyNewComponent(entity, componentName);
		}
	}

	/**
	 * Get game height (not jpanel height)
	 * 
	 * @return game height
	 */
	public double getGameHeight() {
		return this.renderSize.getHeight();
	}

	/**
	 * Get game width (not jpanel height)
	 * 
	 * @return game width
	 */
	public double getGameWidth() {
		return this.renderSize.getWidth();
	}

	/**
	 * Convert a panel position to the game (UI) position
	 * 
	 * @param p position to convert
	 * @return Point2D new position
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
	 * @param p point to convert
	 * @return Point2D point converted
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

	/**
	 * Convert a vector in pixels (panel units) to the game (UI) unit
	 * 
	 * @param v vector to convert
	 * @return Point2D new vector
	 */
	public Point2D panelVectorToGame(Point2D v) {
		double dx = v.getX();
		double dy = v.getY();

		double scaleRatio = getScaleRatio();

		// Game ratio scaling
		dx /= scaleRatio;
		dy /= scaleRatio;

		return new Point2D.Double(dx, dy);
	}

	/**
	 * Convert a vector in pixels (panel units) to the game world unit
	 * 
	 * @param v vector to convert
	 * @return Point2D new vector
	 */
	public Point2D panelVectorToWorld(Point2D v) {
		double dx = v.getX();
		double dy = v.getY();

		// Camera scaling
		dx /= currentScene.getCamera().getScaling();
		dy /= currentScene.getCamera().getScaling();

		return new Point2D.Double(dx, dy);
	}

	/**
	 * Get all entities intersecting given point. Note that the point should usually
	 * be converted manually into world coordinates
	 * 
	 * @param p point to get entities at
	 * @return List<Entity> all entities below the mouse
	 */
	public List<Entity> getEntitiesAt(Point2D p) {
		List<Entity> entities = new ArrayList<Entity>();
		List<String> requiredComponents = new ArrayList<String>();
		requiredComponents.add("position");
		requiredComponents.add("size");

		for (Entity entity : currentScene.getEntities()) {
			if (entity.hasComponents(requiredComponents)) {
				Map<String, Component> components = entity.getComponents();
				// warning: this does not check that x y width and height exists.
				if (components.get("position") != null && components.get("size") != null) {
					if (Collision.intersectRect(p.getX(), p.getY(),
							components.get("position").getField("x").getDouble(),
							components.get("position").getField("y").getDouble(),
							components.get("size").getField("width").getDouble(),
							components.get("size").getField("height").getDouble())) {
						entities.add(entity);
					}
				}
			}
		}
		return entities;
	}

	/**
	 * Notify the engine that a lua error occured. Pause the game and display the
	 * error
	 * 
	 * @param LuaError to display
	 */
	public void notifyLuaError(LuaError error) {
		// We only display the first error encountered so we can fix it first
		if (this.luaError == null) {
			error.printStackTrace();
			editingPause = true;
			this.luaError = error;
		}
	}

	/**
	 * Set black bars color
	 * 
	 * @param color of the black bar
	 */
	public void setBlackBarsColor(Color color) {
		blackBarColor = color;
	}
}
