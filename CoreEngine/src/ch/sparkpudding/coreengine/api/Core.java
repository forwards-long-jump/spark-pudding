package ch.sparkpudding.coreengine.api;

import ch.sparkpudding.coreengine.CoreEngine;

/**
 * Expose features from the CoreEngine mainly for lua apis
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba
 * 
 */
public class Core {
	private CoreEngine coreEngine;

	/**
	 * ctor
	 * @param coreEngine
	 */
	public Core(CoreEngine coreEngine) {
		this.coreEngine = coreEngine;
	}

	/**
	 * Get current tick
	 * @return current tick
	 */
	public int getTick() {
		return coreEngine.getTick();
	}
}
