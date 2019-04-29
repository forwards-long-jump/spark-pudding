package ch.sparkpudding.coreengine.ecs.system;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import ch.sparkpudding.coreengine.api.Core;
import ch.sparkpudding.coreengine.ecs.entity.Entity;

/**
 * Read components required by a lua script, builds a list of entities affected
 * by this system that can be passed to lua functions. Expose CoreEngine APIs
 * Part of the ECS design pattern
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba
 * 
 */
public abstract class System {
	private String filepath;

	private Map<String, List<String>> componentGroups;

	// Metatable is the same for all systems
	public static LuaValue metatableSetterMethod;
	static {
		createMetableSetter();
	}

	private LuaValue getRequiredComponentsMethod;

	protected LuaValue apiTable;
	protected Globals globals;
	protected CoreEngine coreEngine;

	/**
	 * Constructs the system from the Lua file
	 * 
	 * @param file       Lua script file
	 * @param coreEngine Reference to the CoreEngine for API access
	 */
	public System(File file, CoreEngine coreEngine) {
		this.filepath = file.getAbsolutePath();
		this.coreEngine = coreEngine;

		// (re)Load this system
		reload();
	}

	/**
	 * (re)Loads the system from file
	 */
	public void reload() {
		globals = new Globals();
		componentGroups = new HashMap<String, List<String>>();

		loadLuaLibs();
		loadLuaSystem();
		readMethodsFromLua();
		loadApis();

		loadRequiredComponents();
	}

	/**
	 * Load APIs and make them accessible trough the "game" variable
	 */
	private void loadApis() {
		apiTable = new LuaTable();
		globals.set("game", apiTable);

		apiTable.set("core", CoerceJavaToLua.coerce(Core.getInstance()));
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
	 * Get a LuaValue reference to all methods that should be defined in all system
	 * globals
	 */
	protected void readMethodsFromLua() {
		getRequiredComponentsMethod = globals.get("getRequiredComponents");
	}

	/**
	 * Add a setMetatable lua function to the system globals
	 * 
	 * setMetatable(luaComponent) creates the metatable to get and set component
	 * fields value easily
	 */
	private static void createMetableSetter() {
		Globals metaTableGlobals = new Globals();
		metaTableGlobals.load(new JseBaseLib());
		metaTableGlobals.load(new PackageLib());
		metaTableGlobals.load(new StringLib());

		LoadState.install(metaTableGlobals); // http://luaj.org/luaj/3.0/api/org/luaj/vm2/LoadState.html
		LuaC.install(metaTableGlobals); // Install the compiler

		LuaValue chunk = metaTableGlobals.load("function setMetatable(component)\n" + "local mt = {}\n"
				+ "mt.__index = function (self, key)\n" + "return self[\"_\" .. key]:getValue()\n" + "end\n"
				+ "mt.__newindex = function (self, key, value)\n" + "self[\"_\" .. key]:setValue(value)\n" + "end\n"
				+ "setmetatable(component, mt)\n" + "end");
		chunk.call();

		metatableSetterMethod = metaTableGlobals.get("setMetatable");
	}

	/**
	 * Updates the component names list according to lua file
	 */
	private void loadRequiredComponents() {
		componentGroups.clear();
		LuaTable list = (LuaTable) getRequiredComponentsMethod.call(); // Return { entity = {"comp1", "comp2"}}

		// list iteration in LuaJ
		LuaValue key = LuaValue.NIL;
		Varargs entry = list.next(key);

		// entry.arg(1): key
		// entry.arg(2): value

		// entry.arg(2) is either {"comp1", "comp2"} or "comp" depending on the returned
		// value
		if (entry.arg(2).istable()) {
			// System needs multiple lists of entities
			while (!(key = entry.arg(1)).isnil()) {
				List<String> components = new ArrayList<String>();
				String groupName = entry.arg(1).tojstring();

				LuaValue innerKey = LuaValue.NIL;
				Varargs innerEntry = entry.arg(2).next(innerKey);
				// Read all strings from the table
				while (!(innerKey = innerEntry.arg(1)).isnil()) {
					components.add(innerEntry.arg(2).tojstring());
					innerEntry = entry.arg(2).next(innerKey);
				}

				componentGroups.put(groupName, components);

				entry = list.next(key);
			}

		} else {
			// System needs only one list of entities
			List<String> components = new ArrayList<String>();
			// Read all strings from the table
			while (!(key = entry.arg(1)).isnil()) {
				components.add(entry.arg(2).tojstring());
				entry = list.next(key);
			}
			componentGroups.put("entities", components);
		}
	}

	/**
	 * Sets the entities list, to be called after a scene change
	 * 
	 * @param newEntities List of entities of the new scene
	 */
	public void setEntities(List<Entity> newEntities) {
		for (Entry<String, List<String>> componentList : componentGroups.entrySet()) {

			List<Entity> entities = new ArrayList<Entity>();

			// Check entities for compatibility with system
			for (Entity entity : newEntities) {
				if (entity.hasComponents(componentList.getValue())) {
					entities.add(entity);
				}
			}

			// Build Lua instances of entities
			LuaTable entitiesTableLua = new LuaTable();
			for (int i = 0; i < entities.size(); ++i) {
				Entity entity = entities.get(i);

				// Lua table starts at 1
				entitiesTableLua.set(i + 1, entity.coerceToLua(metatableSetterMethod, componentList.getValue()));
			}

			// Lua code has access to all of these entities via the name of the list
			globals.set(componentList.getKey(), entitiesTableLua);
		}
	}

	/**
	 * Check if an entity should be handled by this system and add it if it's the
	 * case
	 * 
	 * @param e entity
	 */
	public void tryAdd(Entity entity) {
		for (Entry<String, List<String>> componentList : componentGroups.entrySet()) {
			// Check entities for compatibility with system
			if (entity.hasComponents(componentList.getValue())) {
				LuaTable entityGroup = (LuaTable) globals.get(componentList.getKey());
				entityGroup.set(entityGroup.keyCount() + 1,
						entity.coerceToLua(metatableSetterMethod, componentList.getValue()));
			}
		}
	}
}
