package ch.sparkpudding.coreengine.ecs;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba
 * 
 */
public class Scene {

	private List<Entity> entities;
	
	/**
	 * Default constructor
	 */
	public Scene()
	{
		this.entities = new ArrayList<Entity>();
	}
	
	//TODO: constructor from file portion
	
	/**
	 * Adds an entity to the scene
	 * @param e Entity to add
	 */
	public void add(Entity e)
	{
		entities.add(e);
	}
	
	/**
	 * Removes an entity from the scene
	 * @param e Entity to remove
	 */
	public void remove(Entity e)
	{
		entities.remove(e);
	}
}
