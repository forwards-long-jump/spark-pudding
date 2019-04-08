package ch.sparkpudding.coreengine.ecs;

import java.util.List;

/**
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba
 * 
 */
public class System {

	private String filename;
	private List<String> componentNames;
	private List<Entity> entities;
	
	public System(String filename)
	{
		// TODO : create lua contexte from lua file
		// infer componentNames from lua file
	}
	
	public void reload()
	{
		// TODO: reload
	}
	
	public void setEntities()
	{
		// TODO: call to API
	}
	
	public void update()
	{
		// TODO: lua
	}
}
