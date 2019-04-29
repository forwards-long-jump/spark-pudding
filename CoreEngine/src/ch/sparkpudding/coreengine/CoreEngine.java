package ch.sparkpudding.coreengine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import ch.sparkpudding.coreengine.api.Core;
import ch.sparkpudding.coreengine.api.Input;
import ch.sparkpudding.coreengine.ecs.component.Component;
import ch.sparkpudding.coreengine.ecs.entity.Entity;
import ch.sparkpudding.coreengine.ecs.entity.Scene;
import ch.sparkpudding.coreengine.ecs.system.RenderSystem;
import ch.sparkpudding.coreengine.ecs.system.UpdateSystem;
import ch.sparkpudding.coreengine.filereader.LelFile;
import ch.sparkpudding.coreengine.filereader.XMLParser;

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

	public Input input;

	private LelFile lelFile;

	private Map<String, Scene> scenes;
	private Scene currentScene;

	private List<UpdateSystem> systems;
	private RenderSystem renderSystem;

	private boolean pause = false;
	private boolean pauseAll = false;

	private Dimension renderSize;
	private Color blackBarColor;

	private int tick;

	/**
	 * The heart of the Ludic Engine in Lua
	 * 
	 * @param gameFolder Location of the game file
	 * @throws Exception All kind of things, really. Ranging from thread to lua
	 *                   errors.
	 */
	public CoreEngine(String gameFolder) throws Exception {
		initAPIs();

		this.input = Input.getInstance();

		this.renderSize = new Dimension(1280, 720);
		this.blackBarColor = Color.BLACK;
		this.tick = 0;

		this.lelFile = new LelFile(gameFolder);

		populateComponentTemplates();
		populateEntityTemplates();
		populateScenes();
		loadSystems();

		setCurrentScene(scenes.get("main"));

		new Thread(() -> {
			startGame();
		}).start();
	}

	/**
	 * Init all singleton APIs
	 */
	private void initAPIs() {
		Core.init(this);
		Input.init(this);
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
	 */
	private void startGame() {
		double previous = java.lang.System.currentTimeMillis();
		double lag = 0.0;

		while (!exit) {
			double current = java.lang.System.currentTimeMillis();
			double elapsed = current - previous;

			previous = current;
			lag += elapsed;

			input.update();

			if (lag >= msPerUpdate) {
				do {
					tick++;
					update();
					lag -= msPerUpdate;
				} while (lag >= msPerUpdate);
				render();
			}
		}
	}

	/**
	 * Runs all systems once
	 */
	private void update() {
		if (pauseAll)
			return;

		for (UpdateSystem system : systems) {
			system.update();
		}
		// TODO : give priority to certain system, i.e. the input systems
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

	public Scene getCurrentScene() {
		return currentScene;
	}

	public void setCurrentScene(Scene currentScene) {
		this.currentScene = currentScene;
		for (UpdateSystem system : systems) {
			system.setEntities(currentScene.getEntities());
		}
		renderSystem.setEntities(currentScene.getEntities());
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

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

		// Calculate translation to center the game
		int realGameWidth = (int) (scaleRatio * renderSize.getWidth());
		int realGameHeight = (int) (scaleRatio * renderSize.getHeight());

		int translateX = getWidth() / 2 - realGameWidth / 2;
		int translateY = getHeight() / 2 - realGameHeight / 2;

		Graphics2D g2d = (Graphics2D) g;

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2d.translate(translateX, translateY);
		g2d.scale(scaleRatio, scaleRatio);

		renderSystem.render((Graphics2D) g);

		g2d.scale(1 / scaleRatio, 1 / scaleRatio);
		g2d.translate(-translateX, -translateY);

		// Draw black bar
		g2d.setColor(blackBarColor);
		if (widthScaleRatio > heightScaleRatio) {
			// Vertical
			g2d.fillRect(0, 0, translateX, getHeight());
			g2d.fillRect(translateX + realGameWidth, 0, translateX + 1, getHeight() + 1);
		} else {
			// Horizontal
			g2d.fillRect(0, 0, getWidth(), translateY);
			g2d.fillRect(0, translateY + realGameHeight, getWidth() + 1, translateY + 1);
		}

		g.dispose();
		// Source:
		// https://stackoverflow.com/questions/33257540/java-window-lagging-on-ubuntu-but-not-windows-when-code-isnt-lagging
		java.awt.Toolkit.getDefaultToolkit().sync();
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
}
