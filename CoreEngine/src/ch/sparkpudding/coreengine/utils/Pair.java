package ch.sparkpudding.coreengine.utils;

/**
 * Basic class storing two values as a couple
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba
 */
public class Pair<X, Y> {
	private final X x;
	private final Y y;

	/**
	 * ctor
	 * 
	 * @param x first value
	 * @param y second value
	 */
	public Pair(X x, Y y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Getter for the first value
	 * 
	 * @return the first value
	 */
	public X first() {
		return x;
	}

	/**
	 * Getter for the second value
	 * 
	 * @return the second value
	 */
	public Y second() {
		return y;
	}
}
