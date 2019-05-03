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
	 * @param x  x position of the point
	 * @param y  y position of the point
	 * @param rx x position of the rectangle
	 * @param ry y position of the rectangle
	 * @param rw width of the rectangle
	 * @param rh height of the rectangle
	 * @return true if point specified by x and y intersects rectangle given by rx
	 *         ry rw and rh
	 */
	public static boolean intersectRect(double x, double y, double rx, double ry, double rw, double rh) {
		return (x > rx && y > ry) && (x < rx + rw && y < ry + rh);
	}

	/**
	 * Return true if first rect intersects second rect
	 * 
	 * @param x  of rect1
	 * @param y  of rect1
	 * @param w  of rect1
	 * @param h  of rect1
	 * @param x2 of rect2
	 * @param y2 of rect2
	 * @param w2 of rect2
	 * @param h2 of rect2
	 * @return true if first rect intersects second rect
	 */
	public static boolean rectIntersectRect(double x, double y, double w, double h, double x2, double y2, double w2,
			double h2) {
		return (x + w > x2 && y + h > y2 && x < x2 + w2 && y < y2 + h2);
	}
}
