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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import ch.sparkpudding.coreengine.Scheduler.Trigger;
import ch.sparkpudding.coreengine.api.Sound;
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
	private int editingTick;

	private Scheduler scheduler;

	private Exception gameError;

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
		setCurrentScene(scenes.get("main"), false);
	}

	/**
	 * Create a CoreEngine whose goal is to be run inside of scene editor
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
		try {
			setCurrentScene(scenes.get("main"), false);
		} catch (Exception e) {
			notifyErrorAndClose("Could not find main scene.");
		}
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

		this.editingTick = 0;

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

		if (renderSystem == null) {
			setEditingPause(true);
			notifyErrorAndClose("No render system found. Make sure to create at least one system named render.lua");
		}
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
	 */
	private void populateScenes() {
		scenes = new HashMap<String, Scene>();

		for (File xmlFile : lelFile.getScenesXML()) {
			try {
				Scene scene = new Scene(XMLParser.parse(xmlFile));
				addScene(scene.getName(), scene);
			} catch (Exception e) {
				notifyErrorAndClose("The following error occured while parsing " + xmlFile + "\n\n" + e.getMessage());
			}
		}

		if (scenes.size() == 0) {
			notifyErrorAndClose("No scenes found. Please create at least one scene named main.xml");
		}
	}

	/**
	 * Populates entity templates list with entity template files
	 */
	private void populateEntityTemplates() {
		for (File xmlFile : lelFile.getEntityTemplatesXML()) {
			try {
				Entity e = new Entity(XMLParser.parse(xmlFile));
				Entity.addTemplate(e);
			} catch (Exception e) {
				notifyErrorAndClose("The following error occured while parsing " + xmlFile + "\n\n" + e.getMessage());
			}
		}

		if (Entity.getTemplates().size() == 0) {
			notifyErrorAndClose("No entity templates found. Please create at least one entity template.");
		}
	}

	/**
	 * Populates component templates list with component template files
	 */
	private void populateComponentTemplates() {
		for (File xmlFile : lelFile.getComponentsXML()) {
			try {
				Component c = new Component(XMLParser.parse(xmlFile));
				Component.addTemplate(c);
			} catch (Exception e) {
				notifyErrorAndClose("The following error occured while parsing " + xmlFile + "\n\n" + e.getMessage());
			}
		}

		if (Component.getTemplates().size() == 0) {
			notifyErrorAndClose("No component templates found. Please create at least one template.");
		}
	}

	/**
	 * Populates component templates list with component template files <br>
	 * TODO: Merge this function and the one above as they are very similar
	 */
	private void populateEditingComponentTemplates() {
		for (File xmlFile : lelFile.getEditingComponentsXML()) {
			try {
				Component c = new Component(XMLParser.parse(xmlFile));
				Component.addTemplate(c);
			} catch (Exception e) {
				notifyErrorAndClose("The following error occured while parsing " + xmlFile + "\n\n" + e.getMessage());
			}

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

		boolean needRender = true;

		double lastFpsTime = java.lang.System.currentTimeMillis();

		while (!exit) {
			scheduler.trigger(Trigger.GAME_LOOP_START);
			double current = java.lang.System.currentTimeMillis();
			double elapsed = current - previous;

			previous = current;
			lag += elapsed;

			needRender = false;

			while (lag >= msPerUpdate) {
				input.update();
				needRender = true;

				// We let external great power handle this
				if (editingSystems == null) {
					handleLuaErrors();
				}

				if (gameError == null) {
					update();
				}
				lag -= msPerUpdate;
			}

			if (needRender) {
				render();
				renderLock.acquire();
			}

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
		if (gameError != null) {
			// Try to continue
			if (input.isKeyDown(KeyEvent.VK_SPACE)) {
				setEditingPause(false);
				clearError();
			} else if (input.isKeyDown(KeyEvent.VK_ENTER)) {
				reloadSystemsFromDisk();
				clearError();
				setEditingPause(false);
			}
			input.resetAllKeys();
		}
	}

	/**
	 * Clear current game error
	 */
	public void clearError() {
		gameError = null;
	}

	/**
	 * Reload systems from disk, live
	 */
	public void reloadSystemsFromDisk() {
		if (editingSystems != null) {
			editingRenderSystem = loadSystemsFromFiles(editingSystems, lelFile.getEditingSystems());
		}

		renderSystem = loadSystemsFromFiles(systems, lelFile.getSystems());
		setCurrentScene(getCurrentScene(), false);
	}

	/**
	 * Runs all systems once
	 */
	private void update() {
		scheduler.trigger(Trigger.BEFORE_UPDATE);

		currentScene.getCamera().update();

		// Only update editing systems when the game is paused
		if (editingPause && editingSystems != null) {
			this.editingTick++;

			for (UpdateSystem system : editingSystems) {
				system.update();
			}
		} else {
			currentScene.incrementTick();

			for (UpdateSystem system : systems) {
				system.update();
			}
		}
	}

	/**
	 * Runs the renderer system
	 */
	private void render() {
		repaint();
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
		scheduler.trigger(Trigger.EDITING_STATE_CHANGED, pause);
		editingPause = pause;
	}

	/**
	 * Change the pause state
	 * 
	 * @param isPause
	 */
	public void setPause(boolean isPause) {
		pause = isPause;
	}

	/**
	 * Change the editingPause without trigger the scheduler
	 *
	 * @param pause
	 * @param noTrigger set to true to not trigger the scheduler
	 */
	public void setEditingPause(boolean pause, boolean noTrigger) {
		if (!noTrigger) {
			scheduler.trigger(Trigger.EDITING_STATE_CHANGED, pause);
		}

		editingPause = pause;

		if (editingPause) {
			Sound.getInstance().pauseMusic();
		} else {
			Sound.getInstance().resumeMusic();
		}
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
		scheduler.trigger(Trigger.SCENE_LIST_CHANGED);
	}

	/**
	 * Removes scene from scenes list
	 * 
	 * @param name Name of the scene
	 */
	public void deleteScene(String name) {
		if (currentScene.getName().equals(name)) {
			scheduler.schedule(Trigger.GAME_LOOP_START, new Runnable() {

				@Override
				public void run() {
					setCurrentScene(scenes.get("main"), false);
					scenes.remove(name);
					scheduler.trigger(Trigger.SCENE_LIST_CHANGED);
				}
			});
		} else {
			scenes.remove(name);
			scheduler.trigger(Trigger.SCENE_LIST_CHANGED);
		}
	}

	/**
	 * Sets scene as current scene, and reloads it if demanded
	 *
	 * @param name  Name of the Scene
	 * @param reset The scene will be reloaded when set to true
	 */
	public void setScene(String name, boolean reset, boolean useDirectlyDefaultEntities) {
		if (reset) {
			scenes.get(name).reset(useDirectlyDefaultEntities);
		}
		setCurrentScene(scenes.get(name), useDirectlyDefaultEntities);
	}

	/**
	 * Reset the current scene. To call be call before updating
	 */
	public void resetCurrentScene(boolean useDirectlyDefaultEntities) {
		this.editingTick = 0;
		Sound.getInstance().stopMusic();
		currentScene.reset(useDirectlyDefaultEntities);
		setCurrentScene(currentScene, useDirectlyDefaultEntities);
	}

	/**
	 * Renames the scene of the given name with the new name
	 * 
	 * @param oldName name of the scene to rename
	 * @param newName new name for the scene
	 * @return true if such a scene was found, false else
	 */
	public boolean renameScene(String oldName, String newName) {
		Scene scene = scenes.get(oldName);
		if (scene != null) {
			scenes.remove(oldName);
			scene.setName(newName);
			scenes.put(newName, scene);
			scheduler.trigger(Trigger.SCENE_LIST_CHANGED);
			return true;
		}
		return false;
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
	public void setCurrentScene(Scene newScene, boolean useDirectlyDefaultEntities) {
		this.currentScene = newScene;
		List<Entity> entities;
		if (useDirectlyDefaultEntities) {
			entities = newScene.getDefaultEntities();
		} else {
			entities = newScene.getEntities();
		}

		for (UpdateSystem system : systems) {
			system.setEntities(entities);
		}

		if (renderSystem != null) {
			renderSystem.setEntities(entities);
		}

		if (editingSystems != null) {
			for (UpdateSystem system : editingSystems) {
				system.setEntities(entities);
			}
		}

		if (editingRenderSystem != null) {
			editingRenderSystem.setEntities(entities);
		}
		scheduler.trigger(Trigger.SCENE_CHANGED, newScene);
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
		AffineTransform defaultTransformationState = g2d.getTransform();

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Calculate screen ratio for width / height
		double scaleRatio = getScaleRatio();
		// Calculate translation to center the game
		Point translation = getGameTranslation();

		// Apply transforms
		g2d.translate(translation.getX(), translation.getY());
		g2d.scale(scaleRatio, scaleRatio);

		AffineTransform gameTransformationState = g2d.getTransform();
		fps++;
		if (renderSystem != null) {
			renderSystem.render(g2d);
		}

		if (editingPause && editingRenderSystem != null) {
			g2d.setTransform(gameTransformationState);
			editingRenderSystem.render(g2d);
		}

		g2d.setTransform(defaultTransformationState);

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
		if (gameError != null) {
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

			y = Drawing.drawWrappedString(gameError.getMessage(), x, y, maxWidth, g2d);
			// Let external great forces of nature handling this if they exists
			if (editingSystems == null) {
				y += smallFontSize;

				g2d.setColor(Color.LIGHT_GRAY);
				y = Drawing.drawWrappedString("Press [SPACE] to ignore this error and attempt to continue.", x, y,
						maxWidth, g2d);
				Drawing.drawWrappedString("Press [ENTER] to reload systems from disk.", x, y, maxWidth, g2d);
			}
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
	 * Getter for the renderSystem
	 * 
	 * @return The renderSystem
	 */
	public RenderSystem getRenderSystems() {
		return renderSystem;
	}

	/**
	 * Getter for the different systems
	 * 
	 * @return The list of system
	 */
	public List<UpdateSystem> getSystems() {
		return systems;
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

		getCurrentScene().addEntity(e);
	}

	/**
	 * Notify all systems that a z-index has changed
	 */
	public void notifyZIndexChange() {
		renderSystem.sortEntities();
		for (UpdateSystem system : systems) {
			system.sortEntities();
		}
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
		currentScene.removeEntity(entity);

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
	 * Delete a default entity
	 * 
	 * @param defaultEntity Default entity to be deleted
	 */
	public void deleteDefaultEntity(Entity entity) {
		currentScene.removeDefaultEntity(entity);
	}

	/**
	 * Removes the named component from the entity, and removes the entity from
	 * systems where it is no longer needed
	 *
	 * @param entity        Entity to work on
	 * @param componentName Name of the component to remove
	 */
	public void deleteComponent(Entity entity, String componentName) {
		if (entity.removeComponent(componentName)) {
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
	public void notifyGameError(Exception exception) {
		// We only display the first error encountered so we can fix it first
		if (this.gameError == null) {
			scheduler.schedule(Trigger.GAME_LOOP_START, new Runnable() {
				@Override
				public void run() {
					gameError = exception;
					setEditingPause(true);
				}
			});

		}
	}

	/**
	 * Notify about a critical error and close the app
	 * 
	 * @param string message to show
	 */
	public void notifyErrorAndClose(String string) {
		JOptionPane.showMessageDialog(this, string, "A fatal error occured", JOptionPane.ERROR_MESSAGE);
		System.exit(1);
	}

	/**
	 * Set black bars color
	 *
	 * @param color of the black bar
	 */
	public void setBlackBarsColor(Color color) {
		blackBarColor = color;
	}

	/**
	 * Return true if the game has an uncleared error
	 *
	 * @return true if the game has an uncleared error
	 */
	public boolean isInError() {
		return gameError != null;
	}

	/**
	 * Get editing tick
	 * 
	 * @return editing tick
	 */
	public int getEditingTick() {
		return editingTick;
	}

	/**
	 * Get the directory of the Game
	 * 
	 * @return the directory containing the game
	 */
	public String getGameFolder() {
		return lelFile.getDirectory();
	}

}
