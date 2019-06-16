package ch.sparkpudding.coreengine.ecs.system;

import java.io.File;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import ch.sparkpudding.coreengine.Lel;
import ch.sparkpudding.coreengine.api.Input;

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
	 * @param file The system
	 */
	public UpdateSystem(File file) {
		super(file);

		sandboxThread = new Thread(() -> {
			sandboxedUpdate();
		});
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

		if (updateMethod.isnil()) {
			updateMethod = null;
		}
	}

	/**
	 * Reloads the system from file
	 */
	@Override
	public void reload() {
		super.reload();

		if (!loadingFailed) {
			readMethodsFromLua();

			if (isPausableMethod.isnil()) {
				pausable = false;
			} else {
				pausable = isPausableMethod.call().toboolean();
			}

			loadUpdateApis();

			// Find if user declared updateXxx functions
			for (Entry<String, List<String>> entry : componentGroups.entrySet()) {
				LuaValue func = globals
						.get("update" + entry.getKey().substring(0, 1).toUpperCase() + entry.getKey().substring(1));
				if (func != LuaValue.NIL) {
					componentGroupsLuaFunctions.put(entry.getKey(), func);
				}
			}
		}
	}

	/**
	 * Load update related APIs
	 */
	private void loadUpdateApis() {
		apiTable.set("input", CoerceJavaToLua.coerce(Input.getInstance()));
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
	 * Update the lua system in a new thread to prevent lua crashing the main app
	 */
	private void sandboxedUpdate() {
		try {
			for (int i = 0; i < sortedEntities.size(); i++) {
				LuaValue func = componentGroupsLuaFunctions.get(sortedEntities.get(i).first());
				if (func != null) {
					func.call(sortedEntities.get(i).second().getLuaEntity());
				}
			}

			if (updateMethod != null) {
				updateMethod.call();
			}
		} catch (LuaError error) {
			Lel.coreEngine.notifyGameError(error);
		} catch (StackOverflowError error) {
			Lel.coreEngine.notifyGameError(new Exception("Stack overflow in " + filepath
					+ ". This sometimes happens when trying to read an inexisting field from a component."));
		}
	}

	/**
	 * Runs the update function of the Lua script, entities can be accessed using
	 * "global" lua variables
	 */
	public void update() {
		if (!loadingFailed) {
			Future<?> future = executor.submit(sandboxThread);

			try {
				// Join the "thread"
				future.get(MAX_EXECUTION_TIME_IN_SECONDS, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				Lel.coreEngine.notifyGameError(new Exception("A LEL internal error occured."));
			} catch (ExecutionException e) {
				Lel.coreEngine.notifyGameError(new Exception("A LEL internal error occured."));
			} catch (TimeoutException e) {
				// Interrupt our thread
				future.cancel(true);
				Lel.coreEngine.notifyGameError(new Exception(filepath + " took more than the "
						+ MAX_EXECUTION_TIME_IN_SECONDS + " seconds allowed to update."));
			}
		}
	}
}
