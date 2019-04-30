package ch.sparkpudding.coreengine.api;

import ch.sparkpudding.coreengine.CoreEngine;

/**
 * Expose camera features to lua APIs
 *
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba
 *
 */
public class Camera {
	private CoreEngine coreEngine;
	private static Camera instance;

	/**
	 * Get Camera API instance
	 *
	 * @return Camera
	 */
	public static Camera getInstance() {
		return instance;
	}

	// TODO: Replace this with lel.CoreEngine
	private Camera(CoreEngine coreEngine) {
		this.coreEngine = coreEngine;
	}

	// TODO: Replace this with lel.CoreEngine
	public static void init(CoreEngine coreEngine) {
		instance = new Camera(coreEngine);
	}

	/**
	 * Set instantly the position of the camera at the specified coordinates
	 *
	 * @param x
	 * @param y
	 */
	public void setPosition(float x, float y) {
		this.coreEngine.getCamera().setPosition(x, y);
	}

	/**
	 * Set the position where the camera should go using its current mode
	 *
	 * @param x
	 * @param y
	 */
	public void setTargetPosition(float x, float y) {
		// TODO
	}
}
