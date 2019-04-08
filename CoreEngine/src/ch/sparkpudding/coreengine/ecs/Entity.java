package ch.sparkpudding.coreengine.ecs;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba
 * 
 */
public class Entity {

	private Map<String, Component> components;
	
	/**
	 * Default constructor
	 */
	public Entity() {
		this.components = new HashMap<String, Component>();
	}
	
	/**
	 * Adds a component to the entity
	 * @param c Component to be added
	 */
	public void add(Component c) {
		components.put(c.getType(), c);
	}
	
	/**
	 * Removes a component to the entity
	 * @param name Name of the component to be removed 
	 */
	public void remove(String name) {
		components.remove(name);
	}
}
