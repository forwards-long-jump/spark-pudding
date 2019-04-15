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
import org.luaj.vm2.lib.jse.JseBaseLib;
import org.luaj.vm2.lib.jse.JseMathLib;

/**
 * Part of the ECS design pattern. Works on entities which have components
 * necessary to the system. All of its logic is to be described in its Lua file.
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba
 * 
 */
public class System {

	private String filename;
	private List<String> componentNames;
	private List<Entity> entities;

	LuaValue updateMethod;
	LuaValue isPausableMethod;
	LuaValue getRequiredComponentsMethod;

	/**
	 * Constructs the system from the Lua filename
	 * 
	 * @param filename
	 */
	public System(File file) {
		Globals globals = new Globals();
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

		componentNames = new ArrayList<String>();
		loadRequiredComponents();
		setEntities();
	}

	/**
	 * 
	 */
	public void reload() {
		// TODO: reload
	}

	/**
	 * 
	 */
	public void loadRequiredComponents() {
		componentNames.clear();
		LuaTable list = (LuaTable) getRequiredComponentsMethod.call();
		LuaValue k = LuaValue.NIL;
		while (true) {
			Varargs n = list.next(k);
			if ((k = n.arg(1)).isnil()) {
				break;
			}
			componentNames.add(n.arg(2).tojstring());
		}
		java.lang.System.out.println(componentNames);
	}

	/**
	 * 
	 */
	public void setEntities() {
		// LuaValue luaObject = CoerceJavaToLua.coerce(this);
		// globals.set("entities", luaObject);

		// TODO: call to API
	}

	/**
	 * Runs the update function of the Lua script
	 */
	public void update() {
		updateMethod.call();
	}
}
