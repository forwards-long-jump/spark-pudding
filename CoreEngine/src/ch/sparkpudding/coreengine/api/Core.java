package ch.sparkpudding.coreengine.api;

import org.luaj.vm2.LuaValue;

import ch.sparkpudding.coreengine.CoreEngine;
import ch.sparkpudding.coreengine.ecs.entity.Entity;
import ch.sparkpudding.coreengine.ecs.system.System;

/**
 * Expose features from the CoreEngine mainly for lua apis
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba
 * 
 */
public class Core {
	private CoreEngine coreEngine;

	/**
	 * ctor
	 * @param coreEngine
	 */
	public Core(CoreEngine coreEngine) {
		this.coreEngine = coreEngine;
	}

	/**
	 * Get current tick
	 * @return current tick
	 */
	public int getTick() {
		return coreEngine.getTick();
	}
	
	/**
	 * Change current scene
	 * @param name of the scene to display
	 */
	public void setScene(String name) {
		coreEngine.setScene(name);
	}
	
	/**
	 * Change current scene
	 * @param name of the scene to display
	 * @param reset or not the new scene
	 */
	public void setScene(String name, boolean reset) {
		coreEngine.setScene(name, reset);
	}
	
	/**
	 * Change current scene
	 * @param name of the scene to display
	 * @param reset or not the new scene
	 */
	public LuaValue createEntity(String templateName) {
		Entity e = new Entity(Entity.getTemplates().get(templateName));
		coreEngine.addEntity(e);
		return e.coerceToLua(System.metatableSetterMethod);
	}
}
