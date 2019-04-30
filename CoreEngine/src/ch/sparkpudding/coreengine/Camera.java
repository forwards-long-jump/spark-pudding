package ch.sparkpudding.coreengine;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

/**
 * Class controlling screen position
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba
 * 
 */
public class Camera {
	private static final double SPRING_MIN_FORCE_REQUIRED = 0.001;

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

	private Dimension boundary;

	// Position
	private Point2D position;
	private Point2D targetPosition;

	// Spring
	private Point2D springTranslateForce;
	private Point2D springTranslateConstant;
	private Point2D springTranslateSpeedCoeff;
	// Smooth
	private Point2D smoothSpeedCoeff;
	// Linear
	private Point2D linearSpeedDelta;

	// Scaling
	private float scaling;
	private float targetScaling;

	private float scalingSpringConstant;
	private float scalingForce;
	private float scalingCoeff;
	private float scalingSpeed;

	/**
	 * ctor
	 */
	public Camera() {
		translateMode = Mode.SPRING;
		scaleMode = Mode.SMOOTH;

		// Position
		position = new Point2D.Double(0.0, 0.0);
		targetPosition = new Point2D.Double(0.0, 0.0);

		springTranslateForce = new Point2D.Double(0.0, 0.0);
		springTranslateConstant = new Point2D.Double(1.7, 1.7);
		springTranslateSpeedCoeff = new Point2D.Double(0.2, 0.2);

		smoothSpeedCoeff = new Point2D.Double(0.1, 0.1);

		linearSpeedDelta = new Point2D.Double(3, 3);

		// Scaling
		scaling = 1;
		targetScaling = 1;

		scalingSpringConstant = 1;
		scalingForce = 0;
		scalingCoeff = 1;
		scalingSpeed = 1;

	}

	/**
	 * To be called in the update loop, handle moving camera to target position
	 */
	public void update() {
		double x = position.getX();
		double y = position.getY();

		switch (translateMode) {
		case LINEAR:
			// x
			if (Math.abs(targetPosition.getX() - x) > 1) {
				if (targetPosition.getX() > x) {
					x += linearSpeedDelta.getX();
					if (targetPosition.getX() < x) {
						x = targetPosition.getX();
					}
				} else {
					x -= linearSpeedDelta.getX();
					if (targetPosition.getX() > x) {
						x = targetPosition.getX();
					}
				}
			}

			// y
			if (Math.abs(targetPosition.getY() - y) > 1) {
				if (targetPosition.getY() > y) {
					y += linearSpeedDelta.getY();
					if (targetPosition.getY() < y) {
						y = targetPosition.getY();
					}
				} else {
					y -= linearSpeedDelta.getY();
					if (targetPosition.getY() > y) {
						y = targetPosition.getY();
					}
				}
			}
			break;
		case SMOOTH:
			x += (targetPosition.getX() - x) * smoothSpeedCoeff.getX();
			y += (targetPosition.getY() - y) * smoothSpeedCoeff.getY();
			break;
		case SPRING:
			double forceX = springTranslateForce.getX();
			double forceY = springTranslateForce.getY();

			forceX += (targetPosition.getX() - x) * springTranslateSpeedCoeff.getX();
			forceY += (targetPosition.getY() - y) * springTranslateSpeedCoeff.getY();

			forceX /= springTranslateConstant.getX();
			forceY /= springTranslateConstant.getY();
			
			if (Math.abs(forceX) < SPRING_MIN_FORCE_REQUIRED) {
				forceX = 0;
			}
			if (Math.abs(forceY) < SPRING_MIN_FORCE_REQUIRED) {
				forceY = 0;
			}
			
			x += forceX;
			y += forceY;

			springTranslateForce.setLocation(forceX, forceY);
			break;
		}

		position.setLocation(x, y);
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
		resetForces();
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

	/**
	 * Reset current forces applied to the camera to 0
	 */
	private void resetForces() {
		springTranslateForce = new Point2D.Double(0.0, 0.0);
		scalingForce = 0;
	}
}
