package ch.sparkpudding.coreengine;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;

/**
 * Class controlling screen position
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba
 * 
 */
public class Camera {
	/**
	 * Linear = constant speed Smooth = the further it is from the target, the
	 * faster it goes Spring = spring effect that overshoot the target and goes back
	 * to it
	 */
	public enum Mode {
		LINEAR, SMOOTH, SPRING;
	}

	private Mode translateMode;
	private Mode scaleMode;

	// Position
	private Point2D position;
	private Point2D targetPosition;

	private Point2D cameraForce;
	private Point2D springConstant;
	private Point2D speedCoeff;

	// Scaling
	private float scaling;
	private float targetScaling;

	private float scalingSpringConstant;
	private float scalingForce;
	private float scalingCoeff;

	/**
	 * ctor
	 */
	public Camera() {
		translateMode = Mode.SPRING;
		scaleMode = Mode.SPRING;
		position = new Point2D.Double(0.0, 0.0);
		scaling = 1;
	}

	/**
	 * To be called in the update loop, handle moving camera to target position
	 */
	public void update() {
		// position.setLocation(position.getX(), position.getY());
	}

	/**
	 * Apply translate and scale to the context. Context must be saved and restaured
	 * manually
	 * 
	 * @param g2d
	 */
	public void applyTransforms(Graphics2D g2d) {
		g2d.translate(-position.getX(), -position.getY());
		g2d.scale(scaling, scaling);
	}

	/**
	 * Teleport the camera to the specified position, cancel all momentum
	 * 
	 * @param x
	 * @param y
	 */
	public void setPosition(float x, float y) {
		position.setLocation(x, y);
	}

	/**
	 * Set the target position for the camera to move to
	 * 
	 * @param x
	 * @param y
	 */
	public void setTargetPosition(float x, float y) {
		targetPosition.setLocation(x, y);
	}

	/**
	 * Change translate mode
	 * 
	 * @param mode
	 */
	public void setTranslateMode(Mode mode) {
		translateMode = mode;
	}

	/**
	 * Change scale mode
	 * 
	 * @param mode
	 */
	public void setScaleMode(Mode mode) {
		scaleMode = mode;
	}
}
