package ch.sparkpudding.coreengine.ecs.entity;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Scene of the game, described by the entities it contains.
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba
 * 
 */
public class Scene {

	private List<Entity> entities;
	private String name;
	
	/**
	 * Default constructor
	 */
	public Scene()
	{
		this.name = "DEFAULT NAME";
		this.entities = new ArrayList<Entity>();
	}
	
	/**
	 * Create a scene from a parsed XML Document and populates its entities
	 * @param document A properly formated Document to get entities from
	 */
	public Scene(Document document) {
		Element sceneElement = document.getDocumentElement();
		
		this.name = sceneElement.getAttribute("name");
		
		this.entities = new ArrayList<Entity>();
		NodeList entities = sceneElement.getChildNodes();
		for (int i = 0; i < entities.getLength(); i++) {
			Node node = entities.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element entityElement = (Element) entities.item(i);
				this.add(new Entity(entityElement));
			}
		}
	}
	
	/**
	 * Adds an entity to the scene
	 * @param e Entity to add
	 */
	public void add(Entity e)
	{
		entities.add(e);
	}
	
	public List<Entity> getEntities() {
		return entities;
	}
	
	/**
	 * Removes an entity from the scene
	 * @param e Entity to remove
	 */
	public void remove(Entity e)
	{
		entities.remove(e);
	}

	public String getName() {
		return name;
	}
	
	public boolean isStartScene() {
		return name.equals("main");
	}
}
