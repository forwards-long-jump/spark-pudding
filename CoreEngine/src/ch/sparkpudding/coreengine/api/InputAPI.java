package ch.sparkpudding.coreengine.api;

import java.awt.geom.Point2D;

import ch.sparkpudding.coreengine.Input;
import ch.sparkpudding.coreengine.Lel;

/**
 * API that wraps the Input class in order to only give access to input
 * consultation from Lua systems
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba
 * 
 */
public class InputAPI {
	private static InputAPI instance;

	private Input input;

	private InputAPI() {
		this.input = Lel.coreEngine.getInput();
	}

	public static InputAPI getInstance() {
		if (instance == null) {
			instance = new InputAPI();
		}
		return instance;
	}

	/**
	 * Return the state of specified key
	 * 
	 * @param keyCode
	 * @return the state of specified key
	 */
	public boolean isKeyDown(int keyCode) {
		return input.isKeyDown(keyCode);
	}

	/**
	 * Return the state of specified mouse button
	 * 
	 * @param keyCode
	 * @return the state of specified mouse button
	 */
	public boolean isMouseButtonDown(int keyCode) {
		return input.isMouseButtonDown(keyCode);
	}

	/**
	 * Return true if the mouse was clicked
	 * 
	 * @return true if the mouse was clicked
	 */
	public boolean isMouseClicked() {
		return input.isMouseClicked();
	}

	/**
	 * Return the mouse position relative to the game
	 * 
	 * @return mouse position relative to the jpanel
	 */
	public Point2D getUIMousePosition() {
		return Lel.coreEngine.panelPositionToGame(input.getMousePosition());
	}

	/**
	 * Return the mouse position relative to the world
	 * 
	 * @return mouse position relative to the jpanel
	 */
	public Point2D getMousePosition() {
		return Lel.coreEngine.panelPositionToWorld(input.getMousePosition());
	}

	/**
	 * Return the X mouse position in game coordinates
	 * 
	 * @return X mouse position in game coordinates
	 */
	public double getUIMouseX() {
		return getUIMousePosition().getX();
	}

	/**
	 * Return the Y mouse position in game coordinates
	 * 
	 * @return Y mouse position in game coordinates
	 */
	public double getUIMouseY() {
		return getUIMousePosition().getY();
	}

	/**
	 * Return the X mouse position in world coordinates
	 * 
	 * @return X mouse position in game coordinates
	 */
	public double getMouseX() {
		return getMousePosition().getX();
	}

	/**
	 * Return the Y mouse position in world coordinates
	 * 
	 * @return Y mouse position in game coordinates
	 */
	public double getMouseY() {
		return getMousePosition().getY();
	}
}
