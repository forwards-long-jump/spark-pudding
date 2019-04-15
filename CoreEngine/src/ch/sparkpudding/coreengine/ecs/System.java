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
public class System {

	private CoreEngine coreEngine;

	private String filepath;
	private List<String> componentNames;
	private List<Entity> entities;
	private List<LuaTable> entitiesLua;

	private boolean pausable;

	private Globals globals;
	private LuaValue metatableSetterMethod;
	private LuaValue updateMethod;
	private LuaValue isPausableMethod;
	private LuaValue getRequiredComponentsMethod;

	/**
	 * Constructs the system from the Lua file
	 * 
	 * @param file       Lua script file
	 * @param coreEngine Reference to the CoreEngine for API access
	 */
	public System(File file, CoreEngine coreEngine) {
		this.filepath = file.getAbsolutePath();
		this.coreEngine = coreEngine;

		// (re)Load system from filepath
		reload();
	}

	/**
	 * Get a LuaValue reference to all methods that should be defined in the system
	 * globals
	 */
	private void readMethodsFromLua() {
		metatableSetterMethod = globals.get("setmt");
		updateMethod = globals.get("update");
		isPausableMethod = globals.get("isPausable");
		getRequiredComponentsMethod = globals.get("getRequiredComponents");
	}

	/**
	 * Add a setmt lua function to the system globals
	 * 
	 * setmt(luaComponent) creates the metatable to get and set component fields
	 * value easily
	 */
	private void injectMetatableSetter() {
		// See the Lua doc about metatables to get a better idea on what's happening here
		LuaValue chunk = globals.load("function setmt(component)\n" + "local mt = {}\n"
				+ "mt.__index = function (self, key)\n" + "return self[\"_\" .. key]:getValue()\n" + "end\n"
				+ "mt.__newindex = function (self, key, value)\n" + "self[\"_\" .. key]:setValue(value)\n" + "end\n"
				+ "setmetatable(component, mt)\n" + "end");
		chunk.call();
	}

	/**
	 * Load basic libraries that are exposed to systems
	 */
	private void loadLuaLibs() {
		globals.load(new JseBaseLib());
		globals.load(new PackageLib());
		globals.load(new JseMathLib());
		globals.load(new StringLib());

		LoadState.install(globals); // http://luaj.org/luaj/3.0/api/org/luaj/vm2/LoadState.html
		LuaC.install(globals); // Install the compiler
	}

	/**
	 * Load the system from the specified filepath
	 */
	private void loadLuaSystem() {
		globals.get("dofile").call(LuaValue.valueOf(filepath));
	}

	/**
	 * Reloads the system from file
	 */
	public void reload() {
		java.lang.System.out.println("Reloaded system");
		globals = new Globals();
		componentNames = new ArrayList<String>();
		entitiesLua = new ArrayList<LuaTable>();
		entities = new ArrayList<Entity>();

		loadLuaLibs();
		loadLuaSystem();
		injectMetatableSetter();
		readMethodsFromLua();

		pausable = isPausableMethod.call().toboolean();

		loadRequiredComponents();
	}

	/**
	 * Updates the component names list according to lua file
	 */
	public void loadRequiredComponents() {
		componentNames.clear();
		LuaTable list = (LuaTable) getRequiredComponentsMethod.call();

		// list iteration in LuaJ
		LuaValue k = LuaValue.NIL;
		Varargs n = list.next(k);
		while (!(k = n.arg(1)).isnil()) {
			componentNames.add(n.arg(2).tojstring());
			n = list.next(k);
		}
	}

	/**
	 * Sets the entities list, to be called after a scene change
	 * 
	 * @param newEntities List of entities of the new scene
	 */
	public void setEntities(List<Entity> newEntities) {
		entities.clear();
		// Check entities for compatibility with system
		for (Entity entity : newEntities) {
			if (entity.hasComponents(componentNames)) {
				entities.add(entity);
			}
		}

		// Build Lua instances of entites for greater ergonomy in lua code
		LuaTable entitiesTableLua = new LuaTable();
		for (Entity entity : entities) {
			// entity
			LuaTable entityLua = new LuaTable();
			for (Component component : entity.getComponents().values()) {
				// entity.component
				LuaTable componentLua = new LuaTable();

				for (Field field : component.getFields().values()) {
					// entity.component.field
					LuaValue fieldLua = CoerceJavaToLua.coerce(field);
					componentLua.set("_" + field.getName(), fieldLua);
				}

				metatableSetterMethod.call(componentLua);
				entityLua.set(component.getName(), componentLua);
			}
			entitiesTableLua.set(entity.getName(), entityLua);
			entitiesLua.add(entityLua);
		}

		// Lua code has access to all of these entites
		globals.set("entities", entitiesTableLua);

		// TODO: coerce APIs
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
