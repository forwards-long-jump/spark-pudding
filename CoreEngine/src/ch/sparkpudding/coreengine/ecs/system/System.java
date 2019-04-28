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
import ch.sparkpudding.coreengine.ecs.component.Component;
import ch.sparkpudding.coreengine.ecs.component.Field;
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

	private LuaValue metatableSetterMethod;
	private LuaValue getRequiredComponentsMethod;

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
		injectMetatableSetter();
		readMethodsFromLua();
		// TODO: coerce APIs

		loadRequiredComponents();
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
		metatableSetterMethod = globals.get("setMetatable");
		getRequiredComponentsMethod = globals.get("getRequiredComponents");
	}

	/**
	 * Add a setMetatable lua function to the system globals
	 * 
	 * setMetatable(luaComponent) creates the metatable to get and set component
	 * fields value easily
	 */
	private void injectMetatableSetter() {
		// See the Lua doc about metatables to get a better idea on what's happening
		// here
		LuaValue chunk = globals.load("function setMetatable(component)\n" + "local mt = {}\n"
				+ "mt.__index = function (self, key)\n" + "return self[\"_\" .. key]:getValue()\n" + "end\n"
				+ "mt.__newindex = function (self, key, value)\n" + "self[\"_\" .. key]:setValue(value)\n" + "end\n"
				+ "setmetatable(component, mt)\n" + "end");
		chunk.call();
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
		
		// entry.arg(2) is either {"comp1", "comp2"} or "comp" depending on the returned value
		if (entry.arg(2).istable()) {
			// System needs multiple lists of entities
			while (!(key = entry.arg(1)).isnil()) {
				List<String> components = new ArrayList<String>();
				String fieldName = entry.arg(1).tojstring();

				LuaValue innerKey = LuaValue.NIL;
				Varargs innerEntry = entry.arg(2).next(innerKey);
				// Read all strings from the table
				while (!(innerKey = innerEntry.arg(1)).isnil()) {
					components.add(innerEntry.arg(2).tojstring());
					innerEntry = entry.arg(2).next(innerKey);
				}

				componentGroups.put(fieldName, components);

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

			// Build Lua instances of entites for greater ergonomy in lua code
			LuaTable entitiesTableLua = new LuaTable();
			for (int i = 0; i < entities.size(); ++i) {
				Entity entity = entities.get(i);
				
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
				
				// Lua table starts at 1
				entitiesTableLua.set(i + 1, entityLua);
			}

			// Lua code has access to all of these entities via the name of the list
			globals.set(componentList.getKey(), entitiesTableLua);
		}
	}

}
