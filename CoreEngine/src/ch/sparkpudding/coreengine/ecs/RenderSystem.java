package ch.sparkpudding.coreengine.ecs;

import java.awt.Graphics2D;
import java.io.File;

import org.luaj.vm2.LuaValue;

import ch.sparkpudding.coreengine.CoreEngine;

public class RenderSystem extends UpdateSystem {
	public static final String LUA_FILE_NAME = "render.lua";
	private LuaValue renderMethod;
	
	public RenderSystem(File file, CoreEngine coreEngine) {
		super(file, coreEngine);
	}

	public void render(Graphics2D g) {
		
	}
}
