package ch.sparkpudding.coreengine.ecs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LoadState;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.ast.Chunk;
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
	
	private String filename;
	private List<String> componentNames;
	private List<Entity> entities;
	private List<LuaTable> entitiesLua;
	
	private boolean pausable;
	
	Globals globals;
	private LuaValue updateMethod;
	private LuaValue isPausableMethod;
	private LuaValue getRequiredComponentsMethod;

	/**
	 * Constructs the system from the Lua filename
	 * 
	 * @param filename
	 */
	public System(File file, CoreEngine coreEngine) {
		this.filename = filename;
		this.coreEngine = coreEngine;
		
		globals = new Globals();
		globals.load(new JseBaseLib());
		globals.load(new PackageLib());
		globals.load(new JseMathLib());
		globals.load(new StringLib());

		LoadState.install(globals);
		LuaC.install(globals);

		globals.get("dofile").call(LuaValue.valueOf(file.getAbsolutePath()));

		updateMethod = globals.get("update");
		isPausableMethod = globals.get("isPausable");
		getRequiredComponentsMethod = globals.get("getRequiredComponents");

		pausable = isPausableMethod.call().toboolean();
		
		componentNames = new ArrayList<String>();
		entitiesLua = new ArrayList<LuaTable>();
		loadRequiredComponents();
		entities = new ArrayList<Entity>();
	}

	/**
	 * 
	 */
	public void reload() {
		// TODO: reload
	}

	/**
	 * Updates the component names list according to lua file
	 */
	public void loadRequiredComponents() {
		componentNames.clear();
		LuaTable list = (LuaTable) getRequiredComponentsMethod.call();
		
		// list iteration in LuaJ
		LuaValue k = LuaValue.NIL;
		while (true) {
			Varargs n = list.next(k);
			if ((k = n.arg(1)).isnil()) {
				break;
			}
			componentNames.add(n.arg(2).tojstring());
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
					LuaValue fieldLua = CoerceJavaToLua.coerce(field.getValue());
					componentLua.set(field.getName(), fieldLua);
				}
				entityLua.set(component.getName(), componentLua);
			}
			entitiesTableLua.set(entity.getName(), entityLua);
			entitiesLua.add(entityLua);
		}
		
		// Lua code has access to all of these entites
		globals.set("entities", entitiesTableLua);
		
		// TODO: coerce APIs
	}
	
	public boolean isPausable() {
		return pausable;
	}

	/**
	 * Runs the update function of the Lua script
	 */
	public void update() {
		for (LuaTable entityLua : entitiesLua) {
			//java.lang.System.out.println(entityLua);
			updateMethod.call(entityLua);
		}
	}
}
