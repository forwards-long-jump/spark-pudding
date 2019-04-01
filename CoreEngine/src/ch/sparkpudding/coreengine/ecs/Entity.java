package ch.sparkpudding.coreengine.ecs;

import java.util.HashMap;
import java.util.Map;

public class Entity {

	private Map<String, Component> components;
	
	public Entity() {
		this.components = new HashMap<String, Component>();
	}
	
	/**
	 * Add a component to the entity
	 * @param c
	 */
	public void add(Component c) {
		components.put(c.getType(), c);
	}
	
	/**
	 * Add a component to the entity
	 * @param c
	 */
	public void remove(String type) {
		components.remove(type);
	}
}
