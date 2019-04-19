package ch.sparkpudding.coreengine.ecs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LoadState;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.compiler.LuaC;
import org.luaj.vm2.lib.PackageLib;
import org.luaj.vm2.lib.StringLib;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.JseBaseLib;
import org.luaj.vm2.lib.jse.JseMathLib;

import ch.sparkpudding.coreengine.CoreEngine;

/**
 * Part of the ECS design pattern. Works on entities which have components
 * necessary to the system. All of its logic is to be described in its Lua file.
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba
 * 
 */
public class UpdateSystem extends System {
	private boolean pausable;
	private LuaValue updateMethod;
	private LuaValue isPausableMethod;

	public UpdateSystem(File file, CoreEngine coreEngine) {
		super(file, coreEngine);
		// (re)Load system from filepath
		reload();
	}

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

		loadRequiredComponents();
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
