package ch.sparkpudding.coreengine.api;

import java.awt.geom.Point2D;

import ch.sparkpudding.coreengine.Lel;

/**
 * API that wraps the Input class in order to only give access to input
 * consultation from Lua systems
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba
 * 
 */
public class Input {
	private static Input instance;

	private ch.sparkpudding.coreengine.Input input;

	private Input() {
		this.input = Lel.coreEngine.getInput();
	}

	public static Input getInstance() {
		if (instance == null) {
			instance = new Input();
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
	 * Returns the number of notches (and partial notches) clicked during the wheel move
	 * 
	 * A negative number means the wheel rolled up (away from the user)
	 *  	
	 * @return number of notches clicked
	 */
	public double getMouseWheelRotation() {
		return input.getMouseWheelRotation();
	}

	/**
	 * Return the mouse position relative to the ui
	 * 
	 * @return mouse position relative to the ui
	 */
	public Point2D getUIMousePosition() {
		return Lel.coreEngine.panelPositionToGame(input.getMousePosition());
	}

	/**
	 * Return the mouse position relative to the world
	 * 
	 * @return mouse position relative to the world
	 */
	public Point2D getMousePosition() {
		return Lel.coreEngine.panelPositionToWorld(input.getMousePosition());
	}
	
	/**
	 * Return the mouse speed relative to the ui
	 * 
	 * @return mouse speed relative to the ui
	 */
	public Point2D getUIMouseSpeed() {
		return Lel.coreEngine.panelVectorToGame(input.getMouseSpeed());
	}
	
	/**
	 * Return the mouse speed relative to the world
	 * 
	 * @return mouse speed relative to the world
	 */
	public Point2D getMouseSpeed() {
		return Lel.coreEngine.panelVectorToWorld(input.getMouseSpeed());
	}

	/**
	 * Return the X mouse position in ui coordinates
	 * 
	 * @return X mouse position in ui coordinates
	 */
	public double getUIMouseX() {
		return getUIMousePosition().getX();
	}

	/**
	 * Return the Y mouse position in ui coordinates
	 * 
	 * @return Y mouse position in ui coordinates
	 */
	public double getUIMouseY() {
		return getUIMousePosition().getY();
	}

	/**
	 * Return the X mouse position in world coordinates
	 * 
	 * @return X mouse position in world coordinates
	 */
	public double getMouseX() {
		return getMousePosition().getX();
	}

	/**
	 * Return the Y mouse position in world coordinates
	 * 
	 * @return Y mouse position in world coordinates
	 */
	public double getMouseY() {
		return getMousePosition().getY();
	}
	
	/**
	 * Return the diff of the last two X mouse position in ui coordinates
	 * 
	 * @return diff of the last two X mouse position in ui coordinates
	 */
	public double getUIMouseDX() {
		return getUIMouseSpeed().getX();
	}

	/**
	 * Return the diff of the last two Y mouse position in ui coordinates
	 * 
	 * @return diff of the last two Y mouse position in ui coordinates
	 */
	public double getUIMouseDY() {
		return getUIMouseSpeed().getY();
	}

	/**
	 * Return the diff of the last two X mouse position in world coordinates
	 * 
	 * @return diff of the last two X mouse position in world coordinates
	 */
	public double getMouseDX() {
		return getMouseSpeed().getX();
	}

	/**
	 * Return the diff of the last two Y mouse position in world coordinates
	 * 
	 * @return diff of the last two Y mouse position in world coordinates
	 */
	public double getMouseDY() {
		return getMouseSpeed().getY();
	}
	
	/**
	 * Return the X of the mouse position when it was clicked in world coordinates
	 * 
	 * @return the X of the mouse position when it was clicked in world coordinates
	 */
	public double getMouseClickedX() {
		return Lel.coreEngine.panelPositionToWorld(input.getMouseClickedPosition()).getX();
	}
	
	/**
	 * Return the Y of the mouse position when it was clicked in world coordinates
	 * 
	 * @return the Y of the mouse position when it was clicked in world coordinates
	 */
	public double getMouseClickedY() {
		return Lel.coreEngine.panelPositionToWorld(input.getMouseClickedPosition()).getY();
	}
}
