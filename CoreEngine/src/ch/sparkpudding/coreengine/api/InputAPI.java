package ch.sparkpudding.coreengine.api;

import java.awt.Point;

import ch.sparkpudding.coreengine.Input;
import ch.sparkpudding.coreengine.Lel;

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
	 * Return the mouse position relative to the jpanel
	 * 
	 * @return mouse position relative to the jpanel
	 */
	public Point getMousePosition() {
		return getMousePosition();
	}
}
