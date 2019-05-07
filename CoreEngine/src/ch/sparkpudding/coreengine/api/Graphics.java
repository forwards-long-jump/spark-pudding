package ch.sparkpudding.coreengine.api;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

import ch.sparkpudding.coreengine.Lel;
import ch.sparkpudding.coreengine.ResourceLocator;
import ch.sparkpudding.coreengine.ecs.component.Field;
import ch.sparkpudding.coreengine.utils.Pair;

public class Graphics {
	private static Graphics instance;

	private Graphics2D g2d;
	private ResourceLocator resourceLocator;

	private Graphics() {
		this.resourceLocator = Lel.coreEngine.getResourceLocator();
	}

	public static Graphics getInstance() {
		if (instance == null) {
			instance = new Graphics();
		}
		return instance;
	}

	/**
	 * To be called before calling the render system, give the graphical context to
	 * this API
	 * 
	 * @param g2d valid Graphics2D context
	 */
	public void setGraphicalContext(Graphics2D g2d) {
		this.g2d = g2d;
	}

	/**
	 * To be called after the render system, disposes of the g2d graphical context
	 */
	public void dispose() {
		g2d.dispose();
		g2d = null;
	}

	/**
	 * Getter for the current graphical context
	 * 
	 * @return Graphics2D
	 */
	public Graphics2D getContext() {
		return g2d;
	}

	/**
	 * Draws the image on the given rectangle
	 * 
	 * @param name   Name of the image, must be in assets/textures directory
	 * @param x      x coordinate of the top left corner of the rectangle
	 * @param y      y coordinate of the top left corner of the rectangle
	 * @param width  width of the rectangle
	 * @param height height of the rectangle
	 */
	public void drawImage(String name, int x, int y, int width, int height) {
		Image img = resourceLocator.getTexture(name);
		g2d.drawImage(img, x, y, width, height, null);
	}

	/**
	 * Draws the image on the given rectangle
	 * 
	 * @param name  Name of the image, must be in assets/textures directory
	 * @param lpos  LuaTable containing the position
	 * @param lsize LuaTable containing the size
	 */
	public void drawImage(String name, LuaTable lpos, LuaTable lsize) {
		Pair<Integer, Integer> pos = fromLuaTable(lpos);
		Pair<Integer, Integer> size = fromLuaTable(lsize);
		drawImage(name, pos.first(), pos.second(), size.first(), size.second());
	}

	/**
	 * Changes the current color used by the painter
	 * 
	 * @param color New color to use
	 */
	public void setColor(Color color) {
		g2d.setColor(color);
	}

	/**
	 * Draws the rectangle and fills it with the current color
	 * 
	 * @param x      x coordinate of the top left corner of the rectangle
	 * @param y      y coordinate of the top left corner of the rectangle
	 * @param width  width of the rectangle
	 * @param height height of the rectangle
	 */
	public void fillRect(int x, int y, int width, int height) {
		g2d.fillRect(x, y, width, height);
	}

	/**
	 * Draws the rectangle and fills it with the current color
	 * 
	 * @param lpos  LuaTable containing the position
	 * @param lsize LuaTable containing the size
	 */
	public void fillRect(LuaTable lpos, LuaTable lsize) {
		Pair<Integer, Integer> pos = fromLuaTable(lpos);
		Pair<Integer, Integer> size = fromLuaTable(lsize);
		fillRect(pos.first(), pos.second(), size.first(), size.second());
	}

	/**
	 * Draws the given string at the given position
	 * 
	 * @param text Text to draw
	 * @param x    x coordinate of the text zone
	 * @param y    y coordinate of the text zone
	 */
	public void drawString(String text, int x, int y) {
		g2d.drawString(text, x, y);
	}

	/**
	 * Draws the given string at the given position
	 * 
	 * @param text Text to draw
	 * @param lpos LuaTable containing the position
	 */
	public void drawString(String text, LuaTable lpos) {
		Pair<Integer, Integer> pos = fromLuaTable(lpos);
		drawString(text, pos.first(), pos.second());
	}

	/**
	 * Sets the background color
	 * 
	 * @param color New color for the background
	 */
	public void setBackground(Color color) {
		g2d.setBackground(color);
	}

	/**
	 * Clears the specified rectangle by filling it with the background color of the
	 * current drawing surface.
	 * 
	 * @param x      x coordinate of the top left corner of the rectangle
	 * @param y      y coordinate of the top left corner of the rectangle
	 * @param width  width of the rectangle
	 * @param height height of the rectangle
	 */
	public void clearRect(int x, int y, int width, int height) {
		g2d.clearRect(x, y, width, height);
	}

	/**
	 * Clears the specified rectangle by filling it with the background color of the
	 * current drawing surface.
	 * 
	 * @param lpos  LuaTable containing the position
	 * @param lsize LuaTable containing the size
	 */
	public void clearRect(LuaTable lpos, LuaTable lsize) {
		Pair<Integer, Integer> pos = fromLuaTable(lpos);
		Pair<Integer, Integer> size = fromLuaTable(lsize);
		clearRect(pos.first(), pos.second(), size.first(), size.second());
	}

	/**
	 * Draws the outline of a circular or elliptical arc covering the specified
	 * rectangle.
	 * 
	 * @param x          x coordinate of the top left corner of the rectangle
	 * @param y          y coordinate of the top left corner of the rectangle
	 * @param width      width of the rectangle
	 * @param height     height of the rectangle
	 * @param startAngle angle at which the arc begins
	 * @param arcAngle   angle that the arc spans
	 */
	public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
		g2d.drawArc(x, y, width, height, startAngle, arcAngle);
	}

	/**
	 * Draws the outline of a circular or elliptical arc covering the specified
	 * rectangle.
	 * 
	 * @param lpos       LuaTable containing the position
	 * @param lsize      LuaTable containing the size
	 * @param startAngle angle at which the arc begins
	 * @param arcAngle   angle that the arc spans
	 */
	public void drawArc(LuaTable lpos, LuaTable lsize, int startAngle, int arcAngle) {
		Pair<Integer, Integer> pos = fromLuaTable(lpos);
		Pair<Integer, Integer> size = fromLuaTable(lsize);
		drawArc(pos.first(), pos.second(), size.first(), size.second(), startAngle, arcAngle);
	}

	/**
	 * Draws a line, using the current color, between the points (x1, y1) and (x2,
	 * y2) in this graphics context's coordinate system.
	 * 
	 * @param x1 x coord of the first point
	 * @param y1 y coord of the first point
	 * @param x2 x coord of the second point
	 * @param y2 y coord of the second point
	 */
	public void drawLine(int x1, int y1, int x2, int y2) {
		g2d.drawLine(x1, y1, x2, y2);
	}

	/**
	 * Draws a line, using the current color, between the points p1 and p2 in this
	 * graphics context's coordinate system.
	 * 
	 * @param lp1 LuaTable containing the first point
	 * @param lp2 LuaTable containing the second point
	 */
	public void drawLine(LuaTable lp1, LuaTable lp2) {
		Pair<Integer, Integer> p1 = fromLuaTable(lp1);
		Pair<Integer, Integer> p2 = fromLuaTable(lp2);
		drawLine(p1.first(), p1.second(), p2.first(), p2.second());
	}

	/**
	 * Draws the outline of an oval inscribed in the given rectangle
	 * 
	 * @param x      x coordinate of the top left corner of the rectangle
	 * @param y      y coordinate of the top left corner of the rectangle
	 * @param width  width of the rectangle
	 * @param height height of the rectangle
	 */
	public void drawOval(int x, int y, int width, int height) {
		g2d.drawOval(x, y, width, height);
	}

	/**
	 * Draws the outline of an oval inscribed in the given rectangle
	 * 
	 * @param lpos  LuaTable containing the position
	 * @param lsize LuaTable containing the size
	 */
	public void drawOval(LuaTable lpos, LuaTable lsize) {
		Pair<Integer, Integer> pos = fromLuaTable(lpos);
		Pair<Integer, Integer> size = fromLuaTable(lsize);
		g2d.drawOval(pos.first(), pos.second(), size.first(), size.second());
	}

	/**
	 * Draws a closed polygon defined by arrays of x and y coordinates
	 * 
	 * @param xPoints Array of the x coordinates
	 * @param yPoints Array of the y coordinates
	 * @param nPoints Number of points
	 */
	public void drawPolygon(LuaTable points) {
		int nPoints = points.length();
		System.out.println(nPoints);
		int[] xPoints = new int[nPoints];
		int[] yPoints = new int[nPoints];
		for (int i = 0; i < nPoints; i++) {
			xPoints[i] = points.get(i + 1).get(1).toint();
			yPoints[i] = points.get(i + 1).get(2).toint();
		}

		g2d.drawPolygon(xPoints, yPoints, nPoints);
	}

	/**
	 * Fills a closed polygon defined by arrays of x and y coordinates
	 * 
	 * @param xPoints Array of the x coordinates
	 * @param yPoints Array of the y coordinates
	 * @param nPoints Number of points
	 */
	public void fillPolygon(LuaTable points) {
		int nPoints = points.length();

		int[] xPoints = new int[nPoints];
		int[] yPoints = new int[nPoints];
		for (int i = 0; i < nPoints; i++) {
			xPoints[i] = points.get(i + 1).get(1).toint();
			yPoints[i] = points.get(i + 1).get(2).toint();
		}

		g2d.fillPolygon(xPoints, yPoints, nPoints);
	}

	/**
	 * Draws the outline of the specified rectangle
	 * 
	 * @param x      x coordinate of the top left corner of the rectangle
	 * @param y      y coordinate of the top left corner of the rectangle
	 * @param width  width of the rectangle
	 * @param height height of the rectangle
	 */
	public void drawRect(int x, int y, int width, int height) {
		g2d.drawRect(x, y, width, height);
	}

	/**
	 * Draws the outline of the specified rectangle
	 * 
	 * @param lpos  LuaTable containing the position
	 * @param lsize LuaTable containing the size
	 */
	public void drawRect(LuaTable lpos, LuaTable lsize) {
		Pair<Integer, Integer> pos = fromLuaTable(lpos);
		Pair<Integer, Integer> size = fromLuaTable(lsize);
		g2d.drawRect(pos.first(), pos.second(), size.first(), size.second());
	}

	/**
	 * Draws an outlined round-cornered rectangle using this graphics context's
	 * current color.
	 * 
	 * @param x         x coordinate of the top left corner of the rectangle
	 * @param y         y coordinate of the top left corner of the rectangle
	 * @param width     width of the rectangle
	 * @param height    height of the rectangle
	 * @param arcWidth  width of the arcs at the corners
	 * @param arcHeight height of the arcs at the corners
	 */
	public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
		g2d.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
	}

	/**
	 * Draws an outlined round-cornered rectangle using this graphics context's
	 * current color.
	 * 
	 * @param lpos      LuaTable containing the position
	 * @param lsize     LuaTable containing the size
	 * @param arcWidth  width of the arcs at the corners
	 * @param arcHeight height of the arcs at the corners
	 */
	public void drawRoundRect(LuaTable lpos, LuaTable lsize, int arcWidth, int arcHeight) {
		Pair<Integer, Integer> pos = fromLuaTable(lpos);
		Pair<Integer, Integer> size = fromLuaTable(lsize);
		g2d.drawRoundRect(pos.first(), pos.second(), size.first(), size.second(), arcWidth, arcHeight);
	}

	/**
	 * Fills a circular or elliptical arc covering the specified rectangle.
	 * 
	 * @param x          x coordinate of the top left corner of the rectangle
	 * @param y          y coordinate of the top left corner of the rectangle
	 * @param width      width of the rectangle
	 * @param height     height of the rectangle
	 * @param startAngle angle at which the arc begins
	 * @param arcAngle   angle that the arc spans
	 */
	public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
		g2d.fillArc(x, y, width, height, startAngle, arcAngle);
	}

	/**
	 * Fills a circular or elliptical arc covering the specified rectangle.
	 * 
	 * @param lpos       LuaTable containing the position
	 * @param lsize      LuaTable containing the size
	 * @param startAngle angle at which the arc begins
	 * @param arcAngle   angle that the arc spans
	 */
	public void fillArc(LuaTable lpos, LuaTable lsize, int startAngle, int arcAngle) {
		Pair<Integer, Integer> pos = fromLuaTable(lpos);
		Pair<Integer, Integer> size = fromLuaTable(lsize);
		g2d.fillArc(pos.first(), pos.second(), size.first(), size.second(), startAngle, arcAngle);
	}

	/**
	 * Fills an oval bounded by the specified rectangle with the current color.
	 * 
	 * @param x      x coordinate of the top left corner of the rectangle
	 * @param y      y coordinate of the top left corner of the rectangle
	 * @param width  width of the rectangle
	 * @param height height of the rectangle
	 */
	public void fillOval(int x, int y, int width, int height) {
		g2d.fillOval(x, y, width, height);
	}

	/**
	 * Fills an oval bounded by the specified rectangle with the current color.
	 * 
	 * @param lpos  LuaTable containing the position
	 * @param lsize LuaTable containing the size
	 */
	public void fillOval(LuaTable lpos, LuaTable lsize) {
		Pair<Integer, Integer> pos = fromLuaTable(lpos);
		Pair<Integer, Integer> size = fromLuaTable(lsize);
		g2d.fillOval(pos.first(), pos.second(), size.first(), size.second());
	}

	/**
	 * Fills the specified rounded corner rectangle with the current color.
	 * 
	 * @param x         x coordinate of the top left corner of the rectangle
	 * @param y         y coordinate of the top left corner of the rectangle
	 * @param width     width of the rectangle
	 * @param height    height of the rectangle
	 * @param arcWidth  width of the arcs at the corners
	 * @param arcHeight height of the arcs at the corners
	 */
	public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
		g2d.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
	}

	/**
	 * Fills the specified rounded corner rectangle with the current color.
	 * 
	 * @param lpos      LuaTable containing the position
	 * @param lsize     LuaTable containing the size
	 * @param arcWidth  width of the arcs at the corners
	 * @param arcHeight height of the arcs at the corners
	 */
	public void fillRoundRect(LuaTable lpos, LuaTable lsize, int arcWidth, int arcHeight) {
		Pair<Integer, Integer> pos = fromLuaTable(lpos);
		Pair<Integer, Integer> size = fromLuaTable(lsize);
		g2d.fillRoundRect(pos.first(), pos.second(), size.first(), size.second(), arcWidth, arcHeight);
	}

	/**
	 * Converts a 2-dimensional LuaTable into an integer point
	 * 
	 * @param lpoint Lua point or size
	 * @return Pair of coordinates (x, y) of integer components
	 */
	private Pair<Integer, Integer> fromLuaTable(LuaTable lpoint) {
		LuaValue firstkey = lpoint.keys()[0];

		if (firstkey.isint()) {
			return new Pair<Integer, Integer>(lpoint.get(1).toint(), lpoint.get(2).toint());
		} else if (lpoint.keys()[0].toString().length() > 2) {
			return fromLuaSize(lpoint);
		} else {
			return fromLuaPoint(lpoint);
		}
	}

	/**
	 * Converts a 2-dimensional LuaTable into an integer point when the lua table
	 * describes a point component
	 * 
	 * @param lpoint Lua point or size
	 * @return Pair of coordinates (x, y) of integer components
	 */
	private Pair<Integer, Integer> fromLuaPoint(LuaTable lpoint) {
		return new Pair<Integer, Integer>((int) ((Field) lpoint.get("_x").touserdata()).getValue(),
				(int) ((Field) lpoint.get("_y").touserdata()).getValue());
	}

	/**
	 * Converts a 2-dimensional LuaTable into an integer point when the lua
	 * describes a size component
	 * 
	 * @param lpoint Lua point or size
	 * @return Pair of coordinates (x, y) of integer components
	 */
	private Pair<Integer, Integer> fromLuaSize(LuaTable lpoint) {
		return new Pair<Integer, Integer>((int) ((Field) lpoint.get("_width").touserdata()).getValue(),
				(int) ((Field) lpoint.get("_height").touserdata()).getValue());
	}
}
