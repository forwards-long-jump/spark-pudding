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

import ch.sparkpudding.coreengine.Lel;
import ch.sparkpudding.coreengine.api.ColorFactory;
import ch.sparkpudding.coreengine.api.Graphics;

public class RenderSystem extends System {
	public static final String LUA_FILE_NAME = "render.lua";
	private LuaValue renderMethod;

	/**
	 * Constructs the render system from its lua file
	 *
	 * @param file The system
	 */
	public RenderSystem(File file) {
		super(file);
		reload();
	}

	/**
	 * Get a LuaValue reference to update and isPausable lua methods
	 */
	@Override
	protected void readMethodsFromLua() {
		super.readMethodsFromLua();

		renderMethod = globals.get("render");
		if(renderMethod.isnil()) {
			Lel.coreEngine.notifyGameError(new Exception("render function missing in " + filepath));
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
			loadRenderApis();
		}
	}

	/**
	 * Load render related APIs
	 */
	private void loadRenderApis() {
		apiTable.set("color", CoerceJavaToLua.coerce(ColorFactory.getInstance()));
		globals.set("g", CoerceJavaToLua.coerce(Graphics.getInstance()));
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
				Lel.coreEngine.notifyGameError(new Exception("A LEL internal error occured."));
			} catch (ExecutionException e) {
				Lel.coreEngine.notifyGameError(new Exception("A LEL internal error occured."));
			} catch (TimeoutException e) {
				// Interrupt our thread
				loadingFailed = true;
				future.cancel(true);
				Lel.coreEngine.notifyGameError(new Exception(filepath + " took more than the "
						+ MAX_EXECUTION_TIME_IN_SECONDS + " seconds allowed to render."));
			}
		}
	}

	/**
	 * Update the lua system in a new thread to prevent lua crashing the main app
	 * 
	 * @param g Graphics2D context
	 */
	private void sandboxedRender(Graphics2D g) {
		try {
			Graphics.getInstance().setGraphicalContext(g);
			renderMethod.call();
			Graphics.getInstance().dispose();
		} catch (LuaError error) {
			Lel.coreEngine.notifyGameError(error);
		} catch (StackOverflowError error) {
			Lel.coreEngine.notifyGameError(new Exception("Stack overflow in " + filepath
					+ ". This sometimes happens when trying to read an inexisting field from a component."));
		}
	}
}
