package ch.sparkpudding.coreengine.ecs.entity;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ch.sparkpudding.coreengine.Camera;

/**
 * Scene of the game, described by the entities it contains.
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba
 * 
 */
public class Scene {

	private List<Entity> entities;
	private List<Entity> defaultEntities;
	private String name;
	private Camera camera;
	private int tick;

	/**
	 * Default constructor
	 */
	public Scene() {
		this.name = "DEFAULT NAME";
		this.entities = new ArrayList<Entity>();
		this.defaultEntities = new ArrayList<Entity>();
		this.camera = new Camera();
		this.tick = 0;
	}

	/**
	 * Create an empty scene, but with a given name
	 * 
	 * @param name name of the scene
	 */
	public Scene(String name) {
		this();
		this.name = name;
	}

	/**
	 * Create a scene from a parsed XML Document and populates its entities
	 * 
	 * @param document A properly formated Document to get entities from
	 */
	public Scene(Document document) {
		Element sceneElement = document.getDocumentElement();

		this.name = sceneElement.getAttribute("name");
		this.tick = 0;
		this.entities = new ArrayList<Entity>();
		this.defaultEntities = new ArrayList<Entity>();
		NodeList entities = sceneElement.getChildNodes();
		for (int i = 0; i < entities.getLength(); i++) {
			Node node = entities.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element entityElement = (Element) entities.item(i);
				this.add(new Entity(entityElement));
				this.addDefault(new Entity(entityElement));
			}
		}

		this.camera = new Camera();
	}

	/**
	 * Adds an entity to the scene
	 * 
	 * @param e Entity to add
	 */
	public void add(Entity e) {
		entities.add(e);
	}

	/**
	 * Adds a default entity to the scene
	 * 
	 * @param e Entity to add
	 */
	public void addDefault(Entity e) {
		defaultEntities.add(e);
	}

	/**
	 * Reset the current scene using its default entities
	 */
	public void reset() {
		entities.clear();
		this.tick = 0;
		// Clone entities into the "live" list
		for (Entity entity : defaultEntities) {
			entities.add(new Entity(entity));
		}
	}

	/**
	 * Get all entities present on this scene
	 * 
	 * @return
	 */
	public List<Entity> getEntities() {
		return entities;
	}

	/**
	 * Get all default entities present on this scene
	 * 
	 * @return
	 */
	public List<Entity> getDefaultEntities() {
		return defaultEntities;
	}

	/**
	 * Removes an entity from the scene
	 * 
	 * @param e Entity to remove
	 */
	public void remove(Entity e) {
		entities.remove(e);
	}

	/**
	 * Return the name of this scene
	 * 
	 * @return name of this scene
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name of the scene
	 * 
	 * @param name new name of the scene
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Return true if this scene is the main scene
	 * 
	 * @return true if this scene is the main scene
	 */
	public boolean isStartScene() {
		return name.equals("main");
	}

	/**
	 * Get camera
	 * 
	 * @return camera associated to the scene
	 */
	public Camera getCamera() {
		return camera;
	}

	/**
	 * Increment game tick
	 */
	public void incrementTick() {
		this.tick++;
	}

	/**
	 * Get tick
	 * 
	 * @return tick
	 */
	public int getTick() {
		return tick;
	}

	/**
	 * Set a new camera for this scene
	 * 
	 * @param camera to use
	 */
	public void setCamera(Camera camera) {
		this.camera = camera;
	}
}
