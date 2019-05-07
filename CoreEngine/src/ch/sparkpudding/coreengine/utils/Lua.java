package ch.sparkpudding.coreengine.utils;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LoadState;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.compiler.LuaC;
import org.luaj.vm2.lib.PackageLib;
import org.luaj.vm2.lib.StringLib;
import org.luaj.vm2.lib.jse.JseBaseLib;

/**
 * Utils class storing lua related helpers
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba
 */
public class Lua {
	// Metatable is the same for all systems
	private static LuaValue metatableSetterMethod;
	static {
		createMetableSetter();
	}

	public static LuaValue getMetatableSetterMethod() {
		return metatableSetterMethod;
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

		LuaValue chunk = metaTableGlobals.load("function setMetatable(component)\n"
				+ "local mt = {}\n"
					+ "mt.__index = function (self, key)\n"
						+ "return self[\"_\" .. key]:getValue()\n"
					+ "end\n"
					+ "mt.__newindex = function (self, key, value)\n" + "self[\"_\" .. key]:setValue(value)\n"
				+ "end\n"
				+ "setmetatable(component, mt)\n" + "end");
		chunk.call();

		metatableSetterMethod = metaTableGlobals.get("setMetatable");
	}
}
