package ch.sparkpudding.coreengine;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JPanel;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import ch.sparkpudding.coreengine.ecs.*;
import ch.sparkpudding.coreengine.ecs.System;
import ch.sparkpudding.coreengine.filereader.LelFile;
import ch.sparkpudding.coreengine.filereader.XMLParser;

/**
 * Class keeping track of all the elements of the ECS, and responsible of running it.
 * Also owns inputs and outputs of the game.
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba
 * 
 */
public class CoreEngine {

	private double msPerUpdate = (1000 / 60);
	private boolean exit = false;
	
	private JPanel panel;
	public Input input;
	
	private LelFile lelFile;
	
	private Map<String, Scene> scenes;
	private Scene currentScene;
	
	private List<System> systems;
	private System renderSystem;	

	public CoreEngine(JPanel panel, String gameFolder) throws Exception {
		this.panel = panel;
		this.input = new Input(panel);
		
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
	 * Populates systems list with system files
	 */
	private void loadSystems() {
		systems = new ArrayList<System>();
		renderSystem = null;
		
		for (File systemFile : lelFile.getSystems()) {
			systems.add(new System(systemFile, this));
		}
	}

	/**
	 * Populates scenes list with scene files
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

			while (lag >= msPerUpdate) {
				update();
				lag -= msPerUpdate;
			}

			render();
		}
	}

	/**
	 * Runs all systems once
	 */
	private void update() {
		for (System system : systems) {
			system.update();
		}
		// TODO : give priority to certain system, i.e. the input systems
	}

	/**
	 * Runs the renderer system
	 */
	private void render() {
		// TODO: Render logic
		// using panel and renderSystem
	}
	
	/**
	 * Pauses all systems indescriminately
	 */
	public void pauseAll()
	{
		// TODO: pause
	}
	
	/**
	 * Pauses all systems which are labelled "pausable"
	 */
	public void pause()
	{
		// TODO: pause (toggle)
	}
	
	/**
	 * Add scene to scenes list
	 * @param name Name of the scene
	 * @param s Scene
	 */
	public void addScene(String name, Scene s)
	{
		scenes.put(name, s);
	}
	
	/**
	 * Sets scene as current scene, without reloading
	 * @param name Name of the scene
	 */
	public void setScene(String name)
	{
		setScene(name, false);
	}
	
	/**
	 * Sets scene as current scene, and reloads it if demanded
	 * @param name Name of the Scene
	 * @param reset The scene will be reloaded when set to true
	 */
	public void setScene(String name, boolean reset)
	{
		// TODO: set current scene
	}
	
	/**
	 * Resets current scene
	 */
	public void resetScene()
	{
		// TODO: reset current scene
	}

	public Scene getCurrentScene() {
		return currentScene;
	}

	public void setCurrentScene(Scene currentScene) {
		this.currentScene = currentScene;
		for (System system : systems) {
			system.setEntities(currentScene.getEntities());
		}
	}
}
