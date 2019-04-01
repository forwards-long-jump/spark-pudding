package ch.sparkpudding.coreengine.ecs;

import java.util.ArrayList;
import java.util.List;

public class Scene {

	private List<Entity> entities;
	
	public Scene()
	{
		this.entities = new ArrayList<Entity>();
	}
	
	//TODO: constructor from file portion
	
	public void add(Entity e)
	{
		entities.add(e);
	}
	
	public void remove(Entity e)
	{
		entities.remove(e);
	}
}
