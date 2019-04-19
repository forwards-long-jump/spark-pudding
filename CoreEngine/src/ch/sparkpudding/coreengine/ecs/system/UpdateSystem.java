package ch.sparkpudding.coreengine.ecs.system;

import java.io.File;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

import ch.sparkpudding.coreengine.CoreEngine;

/**
 * Handle systems that will be updated. These systems can also be paused
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba
 * 
 */
public class UpdateSystem extends System {
	private boolean pausable;
	private LuaValue updateMethod;
	private LuaValue isPausableMethod;

	/**
	 * Constructs the update system from its lua file
	 * 
	 * @param file
	 * @param coreEngine
	 */
	public UpdateSystem(File file, CoreEngine coreEngine) {
		super(file, coreEngine);
		// (re)Load system from filepath
		reload();
	}

	/**
	 * Get a LuaValue reference to update and isPausable lua methods
	 */
	@Override
	protected void readMethodsFromLua() {
		super.readMethodsFromLua();

		updateMethod = globals.get("update");
		isPausableMethod = globals.get("isPausable");
	}

	/**
	 * Reloads the system from file
	 */
	@Override
	public void reload() {
		super.reload();

		readMethodsFromLua();

		pausable = isPausableMethod.call().toboolean();
	}

	/**
	 * Returns whether the system should be affected by the in-game pause
	 * 
	 * @return boolean True if pausable
	 */
	public boolean isPausable() {
		return pausable;
	}

	/**
	 * Runs the update function of the Lua script on every entity
	 */
	public void update() {
		for (LuaTable entityLua : entitiesLua) {
			updateMethod.call(entityLua);
		}
	}
}
