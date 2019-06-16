package ch.sparkpudding.coreengine.api;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

import ch.sparkpudding.coreengine.Camera.Mode;
import ch.sparkpudding.coreengine.Lel;

/**
 * Expose camera features to lua APIs
 *
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba
 *
 */
public class Camera {
	private static Camera instance;

	/**
	 * Get Camera API instance
	 *
	 * @return Camera
	 */
	public static Camera getInstance() {
		if (instance == null) {
			instance = new Camera();
		}

		return instance;
	}

	/**
	 * ctor
	 */
	private Camera() {
	}

	/**
	 * Apply translate and scale to the context. Context must be saved and restaured
	 * manually
	 *
	 * @param g2d Graphic context
	 */
	public void applyTransforms(Graphics2D g2d) {
		Lel.coreEngine.getCamera().applyTransforms(g2d);
	}

	/**
	 * Apply translate and scale to the context. Context must be saved and restaured
	 * manually
	 *
	 * @param g2d Graphic context
	 */
	public void resetTransforms(Graphics2D g2d) {
		Lel.coreEngine.getCamera().resetTransforms(g2d);
	}

	/**
	 * Teleport the camera to the specified world position, cancel all momentum
	 *
	 * @param x coordinate
	 * @param y coordinate
	 */
	public void setPosition(float x, float y) {
		Lel.coreEngine.getCamera().setWorldPosition(x, y);
	}

	/**
	 * Set the target scale of the camera
	 *
	 * @param s scale
	 */
	public void setTargetScaling(float s) {
		Lel.coreEngine.getCamera().setTargetScaling(s);
	}

	/**
	 * Set the target position to the current position
	 */
	public void setTargetToPosition() {
		Lel.coreEngine.getCamera().setTargetToPosition();
	}

	/**
	 * Center the target of the camera at the specified location. Center of entity
	 * is calculated automatically if width and height are given
	 *
	 * @param x top-left position
	 * @param y top-left position
	 * @param w width of the entity
	 * @param h height of the entity
	 */
	public void centerTargetAt(float x, float y, float w, float h) {
		Lel.coreEngine.getCamera().centerTargetAt(x, y, w, h);
	}

	/**
	 * Center the camera at the specified location. Center of entity is calculated
	 * automatically if width and height are given
	 *
	 * @param x top-left position
	 * @param y top-left position
	 * @param w width of the entity
	 * @param h height of the entity
	 */
	public void centerAt(float x, float y, float w, float h) {
		Lel.coreEngine.getCamera().centerAt(x, y, w, h);
	}

	/**
	 * Center the camera at the specified location. Center of entity is calculated
	 * automatically if width and height are given
	 *
	 * @param x top-left position
	 * @param y top-left position
	 */
	public void centerAt(float x, float y) {
		centerAt(x, y, 0, 0);
	}

	/**
	 * Center the target of camera at the specified location. Center of entity is
	 * calculated automatically if width and height are given
	 *
	 * @param x top-left position
	 * @param y top-left position
	 */
	public void centerTargetAt(float x, float y) {
		centerTargetAt(x, y, 0, 0);
	}

	/**
	 * Set the target position for the camera to move to
	 *
	 * @param x coordinate
	 * @param y coordinate
	 */
	public void setTargetPosition(float x, float y) {
		Lel.coreEngine.getCamera().setTargetPosition(x, y);
	}

	/**
	 * Change translate mode
	 *
	 * @param mode to change the camera to
	 */
	public void setMode(String mode) {
		Lel.coreEngine.getCamera().setTranslateMode(Mode.valueOf(mode));
	}

	/**
	 * Clear current boundary
	 */
	public void clearBoundary() {
		Lel.coreEngine.getCamera().setBoundary(null);
	}

	/**
	 * Change the camera boundary
	 * 
	 * @param x of the boundary
	 * @param y of the boundary
	 * @param w of the boundary
	 * @param h of the boundary
	 */
	public void setBoundary(float x, float y, float w, float h) {
		Lel.coreEngine.getCamera().setBoundary(new Rectangle((int) x, (int) y, (int) w, (int) h));
	}

	/**
	 * Set spring force for translate camera
	 * 
	 * @param x force
	 * @param y force
	 */
	public void setSpringTranslateForce(float x, float y) {
		Lel.coreEngine.getCamera().setSpringTranslateForce(new Point2D.Double(x, y));
	}

	/**
	 * Set spring constant for translate camera
	 * 
	 * @param x constant
	 * @param y constant
	 */
	public void setSpringTranslateConstant(float x, float y) {
		Lel.coreEngine.getCamera().setSpringTranslateConstant(new Point2D.Double(x, y));
	}

	/**
	 * Set spring speed coeff for translate camera
	 * 
	 * @param x speed coeff
	 * @param y speed coeff
	 */
	public void setSpringTranslateSpeedCoeff(float x, float y) {
		Lel.coreEngine.getCamera().setSpringTranslateSpeedCoeff(new Point2D.Double(x, y));
	}

	/**
	 * Set smooth speed coeff for translate camera
	 * 
	 * @param x speed coeff
	 * @param y speed coeff
	 */
	public void setSmoothSpeedCoeff(float x, float y) {
		Lel.coreEngine.getCamera().setSmoothSpeedCoeff(new Point2D.Double(x, y));
	}

	/**
	 * Set speed delta for linear translate camera
	 * 
	 * @param x speed delta
	 * @param y speed delta
	 */
	public void setLinearSpeedDelta(float x, float y) {
		Lel.coreEngine.getCamera().setLinearSpeedDelta(new Point2D.Double(x, y));
	}

	/**
	 * @param smoothScaleSpeedCoeff the smoothScaleSpeedCoeff to set
	 */
	public void setSmoothScaleSpeedCoeff(float smoothScaleSpeedCoeff) {
		Lel.coreEngine.getCamera().setSmoothScaleSpeedCoeff(smoothScaleSpeedCoeff);
	}

	/**
	 * Set the scaling point of the camera in UI coordinates
	 * 
	 * @param x coordinate
	 * @param y coordinate
	 */
	public void setScalingPoint(double x, double y) {
		Lel.coreEngine.getCamera().setScalingPoint(new Point2D.Double(x, y));
	}

	/**
	 * Make the camera shake
	 *
	 * @param intensity in pixel
	 * @param duration  in tick
	 */
	public void shake(float intensity, int duration) {
		Lel.coreEngine.getCamera().shake(intensity, duration);
	}

	/**
	 * Return true if the given rectangle is visible
	 *
	 * @param x      coordinates of the rectangle
	 * @param y      coordinates of the rectangle
	 * @param width  of the rectangle
	 * @param height of the rectangle
	 * @return true if the given rectangle is visible
	 */
	public boolean isInView(float x, float y, float width, float height) {
		return Lel.coreEngine.getCamera().isInView(x, y, width, height);
	}

	/**
	 * Get the X coordinate of the camera
	 * 
	 * @return x coordinate
	 */
	public double getX() {
		return Lel.coreEngine.getCamera().getPosition().getX();
	}

	/**
	 * Get the Y coordinate of the camera
	 * 
	 * @return y coordinate
	 */
	public double getY() {
		return Lel.coreEngine.getCamera().getPosition().getY();
	}

	/**
	 * Get current camera scaling
	 * 
	 * @return camera scaling
	 */
	public float getScaling() {
		return Lel.coreEngine.getCamera().getScaling();
	}

	/**
	 * Return true if close to target scaling
	 * 
	 * @return true if close to target scaling
	 */
	public boolean isAtTargetScaling() {
		return Math
				.abs(Lel.coreEngine.getCamera().getScaling() - Lel.coreEngine.getCamera().getTargetScaling()) < 0.0001;
	}
}
