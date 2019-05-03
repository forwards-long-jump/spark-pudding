package ch.sparkpudding.coreengine.utils;

/**
 * Class keeping track of all the elements of the ECS, and responsible of
 * running it. Also owns inputs and outputs of the game.
 *
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba
 *
 */
public class Collision {
	/**
	 * Return true if point specified by x and y intersects rectangle given by rx ry
	 * rw and rh
	 * 
	 * @param x
	 * @param y
	 * @param rx
	 * @param ry
	 * @param rw
	 * @param rh
	 * @return true if point specified by x and y intersects rectangle given by rx
	 *         ry rw and rh
	 */
	public static boolean intersectRect(double x, double y, double rx, double ry, double rw, double rh) {
		return (x > rx && y > ry) && (x < rx + rw && y < ry + rh);
	}
}
