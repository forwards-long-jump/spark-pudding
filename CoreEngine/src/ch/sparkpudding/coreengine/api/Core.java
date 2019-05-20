package ch.sparkpudding.coreengine.api;

import org.luaj.vm2.LuaValue;

import ch.sparkpudding.coreengine.Lel;
import ch.sparkpudding.coreengine.Scheduler.Trigger;
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

	/**
	 * Singleton design pattern
	 * 
	 * @return instance
	 */
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
		return Lel.coreEngine.getCurrentScene().getTick();
	}

	/**
	 * Get current editing tick
	 * 
	 * @return current editing tick
	 */
	public int getEditingTick() {
		return Lel.coreEngine.getEditingTick();
	}

	/**
	 * Get game width
	 * 
	 * @return game width
	 */
	public double getGameWidth() {
		return Lel.coreEngine.getGameWidth();
	}

	/**
	 * Get game height
	 * 
	 * @return game height
	 */
	public double getGameHeight() {
		return Lel.coreEngine.getGameHeight();
	}

	/**
	 * Get current frameRate
	 * 
	 * @return current tick
	 */
	public int getFPS() {
		return Lel.coreEngine.getFPS();
	}

	/**
	 * Change current scene
	 * 
	 * @param name of the scene to display
	 */
	public void setScene(String name) {
		setScene(name, false);
	}

	/**
	 * Change current scene
	 * 
	 * @param name  of the scene to display
	 * @param reset or not the new scene
	 */
	public void setScene(String name, boolean reset) {
		Lel.coreEngine.getScheduler().schedule(Trigger.GAME_LOOP_START, new Runnable() {
			@Override
			public void run() {
				Lel.coreEngine.setScene(name, reset);
			}
		});
	}

	/**
	 * Create a new entity and adds it to the scene. Also returns the LuaValue of it
	 * 
	 * @param templateName Name of the entity template
	 * @return Lua entity
	 */
	public LuaValue createEntity(String templateName) {
		Entity e = new Entity(Entity.getTemplates().get(templateName));

		Lel.coreEngine.getScheduler().schedule(Trigger.GAME_LOOP_START, new Runnable() {
			@Override
			public void run() {
				Lel.coreEngine.addEntity(e);
			}
		});

		return e.getLuaEntity();
	}
}
