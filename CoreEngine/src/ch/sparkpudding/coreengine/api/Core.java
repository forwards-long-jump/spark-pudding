package ch.sparkpudding.coreengine.api;

import java.awt.Dimension;

import org.luaj.vm2.LuaValue;

import ch.sparkpudding.coreengine.Lel;
import ch.sparkpudding.coreengine.ecs.entity.Entity;

/**
 * Expose features from the CoreEngine mainly for lua apis
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba
 * 
 */
public class Core {
	private static Core instance;

	private Core() {
	}

	public static Core getInstance() {
		if (instance == null) {
			instance = new Core();
		}
		return instance;
	}

	/**
	 * Get current tick
	 * 
	 * @return current tick
	 */
	public int getTick() {
		return Lel.coreEngine.getTick();
	}

	/**
	 * Get game width
	 * 
	 * @return game width
	 */
	public int getGameWidth() {
		return Lel.coreEngine.getWidth();
	}

	/**
	 * Get game height
	 * 
	 * @return game height
	 */
	public int getGameHeight() {
		return Lel.coreEngine.getHeight();
	}

	/**
	 * Change current scene
	 * 
	 * @param name of the scene to display
	 */
	public void setScene(String name) {
		Lel.coreEngine.setScene(name);
	}

	/**
	 * Change current scene
	 * 
	 * @param name  of the scene to display
	 * @param reset or not the new scene
	 */
	public void setScene(String name, boolean reset) {
		Lel.coreEngine.setScene(name, reset);
	}

	/**
	 * Create a new entity and adds it to the scene. Also returns the LuaValue of it
	 * 
	 * @param templateName Name of the entity template
	 * @return
	 */
	public LuaValue createEntity(String templateName) {
		Entity e = new Entity(Entity.getTemplates().get(templateName));
		Lel.coreEngine.addEntity(e);
		return e.getLuaEntity();
	}
}
