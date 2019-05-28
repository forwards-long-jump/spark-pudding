package ch.sparkpudding.coreengine.api;

import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;

import ch.sparkpudding.coreengine.Lel;
import ch.sparkpudding.coreengine.utils.Collision;

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
	 * Returns the number of notches (and partial notches) clicked during the wheel
	 * move
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

	/**
	 * Return true if the mouse is over the specified rectangle in world coordinates
	 * 
	 * @param x of the rectangle
	 * @param y of the rectangle
	 * @param w of the rectangle
	 * @param h of the rectangle
	 * @return
	 */
	public boolean isMouseInRectangle(double x, double y, double w, double h) {
		Point2D mouseLocation = Lel.coreEngine.panelPositionToWorld(input.getMousePosition());
		return Collision.intersectRect(mouseLocation.getX(), mouseLocation.getY(), x, y, w, h);
	}

	/**
	 * Get a keycode from a given string Based on
	 * 
	 * @param str
	 * @return keycode corresponding to a key
	 */
	public int keyFromString(String str) {

		switch (str.toUpperCase()) {
		case "UP":
			return KeyEvent.VK_UP;
		case "DOWN":
			return KeyEvent.VK_DOWN;
		case "LEFT":
			return KeyEvent.VK_LEFT;
		case "RIGHT":
			return KeyEvent.VK_RIGHT;
		case "SHIFT":
			return KeyEvent.VK_SHIFT;
		case "CTRL":
			return KeyEvent.VK_CONTROL;
		case "ESCAPE":
			return KeyEvent.VK_ESCAPE;
		case "SPACE":
			return KeyEvent.VK_SPACE;
		default:
			switch (str.toUpperCase().charAt(0)) {
			case 'A':
				return KeyEvent.VK_A;
			case 'B':
				return KeyEvent.VK_B;
			case 'C':
				return KeyEvent.VK_C;
			case 'D':
				return KeyEvent.VK_D;
			case 'E':
				return KeyEvent.VK_E;
			case 'F':
				return KeyEvent.VK_F;
			case 'G':
				return KeyEvent.VK_G;
			case 'H':
				return KeyEvent.VK_H;
			case 'I':
				return KeyEvent.VK_I;
			case 'J':
				return KeyEvent.VK_J;
			case 'K':
				return KeyEvent.VK_K;
			case 'L':
				return KeyEvent.VK_L;
			case 'M':
				return KeyEvent.VK_M;
			case 'N':
				return KeyEvent.VK_N;
			case 'O':
				return KeyEvent.VK_O;
			case 'P':
				return KeyEvent.VK_P;
			case 'Q':
				return KeyEvent.VK_Q;
			case 'R':
				return KeyEvent.VK_R;
			case 'S':
				return KeyEvent.VK_S;
			case 'T':
				return KeyEvent.VK_T;
			case 'U':
				return KeyEvent.VK_U;
			case 'V':
				return KeyEvent.VK_V;
			case 'W':
				return KeyEvent.VK_W;
			case 'X':
				return KeyEvent.VK_X;
			case 'Y':
				return KeyEvent.VK_Y;
			case 'Z':
				return KeyEvent.VK_Z;
			case '0':
				return KeyEvent.VK_0;
			case '1':
				return KeyEvent.VK_1;
			case '2':
				return KeyEvent.VK_2;
			case '3':
				return KeyEvent.VK_3;
			case '4':
				return KeyEvent.VK_4;
			case '5':
				return KeyEvent.VK_5;
			case '6':
				return KeyEvent.VK_6;
			case '7':
				return KeyEvent.VK_7;
			case '8':
				return KeyEvent.VK_8;
			case '9':
				return KeyEvent.VK_9;
			}
			break;
		}

		return 0;
	}
}
