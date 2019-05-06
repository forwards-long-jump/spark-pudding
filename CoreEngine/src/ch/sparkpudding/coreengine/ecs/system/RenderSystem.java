package ch.sparkpudding.coreengine.ecs.system;

import java.awt.Graphics2D;
import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import ch.sparkpudding.coreengine.CoreEngine;
import ch.sparkpudding.coreengine.Lel;
import ch.sparkpudding.coreengine.api.ColorFactory;

public class RenderSystem extends System {
	public static final String LUA_FILE_NAME = "render.lua";
	private LuaValue renderMethod;

	/**
	 * Constructs the render system from its lua file
	 *
	 * @param file
	 * @param coreEngine
	 */
	public RenderSystem(File file, CoreEngine coreEngine) {
		super(file, coreEngine);
		reload();
	}

	/**
	 * Get a LuaValue reference to update and isPausable lua methods
	 */
	@Override
	protected void readMethodsFromLua() {
		super.readMethodsFromLua();

		renderMethod = globals.get("render");
	}

	/**
	 * Reloads the system from file
	 */
	@Override
	public void reload() {
		super.reload();

		if (!loadingFailed) {
			readMethodsFromLua();
			loadRenderApis();
		}
	}

	/**
	 * Load render related APIs
	 */
	private void loadRenderApis() {
		apiTable.set("color", CoerceJavaToLua.coerce(ColorFactory.getInstance()));
	}

	/**
	 * Runs the render function of the Lua script on every entity
	 *
	 * @param g Graphics2D context
	 */
	public void render(Graphics2D g) {
		if (!loadingFailed) {
			Future<?> future = executor.submit(() -> {
				sandboxedRender(g);
			});

			try {
				// Join the "thread"
				future.get(MAX_EXECUTION_TIME_IN_SECONDS, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				java.lang.System.out.println(e.getCause());
				Lel.coreEngine.notifyLuaError(new LuaError("A LEL internal error occured."));
			} catch (ExecutionException e) {
				java.lang.System.out.println(e.getCause());
				Lel.coreEngine.notifyLuaError(new LuaError("A LEL internal error occured."));
			} catch (TimeoutException e) {
				// Interrupt our thread
				loadingFailed = true;
				future.cancel(true);
				Lel.coreEngine.notifyLuaError(new LuaError("renderer system took more than the "
						+ MAX_EXECUTION_TIME_IN_SECONDS + " seconds allowed to render."));
			}
		}
	}

	/**
	 * Update the lua system in a new thread to prevent lua crashing the main app
	 */
	private void sandboxedRender(Graphics2D g) {
		LuaValue luaG = CoerceJavaToLua.coerce(g);
		try {
			renderMethod.call(luaG);
		} catch (LuaError error) {
			Lel.coreEngine.notifyLuaError(error);
		} catch (StackOverflowError error) {
			Lel.coreEngine.notifyLuaError(new LuaError(
					"Stack overflow in the renderer system. This sometimes happens when trying to read an inexisting field from a component."));
		}
	}
}
