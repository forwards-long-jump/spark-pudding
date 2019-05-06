package ch.sparkpudding.coreengine.utils;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Utils class containing drawing related methods
 *
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba
 *
 */
public class Drawing {
	// We don't really want to create these new items every time something is
	// rendered
	private static StringBuilder builder = new StringBuilder();
	private static StringBuilder builderNewline = new StringBuilder();
	private static List<String> lines = new ArrayList<String>();

	/**
	 * Render a string making sure it's not longer than maxWidth by breaking it into
	 * multiple lines
	 * 
	 * @param s        string to write
	 * @param x        x coordinate of the string to render
	 * @param y        y coordinate of the string to render
	 * @param maxWidth maximum width available in pixel
	 * @param g2d      context to render on
	 * 
	 * @return the y value used by the last line
	 */
	public static int drawWrappedString(String s, int x, int y, int maxWidth, Graphics2D g2d) {
		if (maxWidth < 0) {
			return y; // Nothing we can't do in this case
		}

		// Start by splitting all new lines
		String[] newLines = s.split("\n");
		builder.setLength(0);
		lines.clear();

		// Build the lines that will be rendered
		for (String line : newLines) {
			int width = g2d.getFontMetrics().stringWidth(line);
			// One line is bigger than max width, split this line in multiple lines
			if (width > maxWidth) {
				String[] words = line.split(" ");

				// Try to split at blank chars
				for (String word : words) {
					// Can we add one more word?
					if (g2d.getFontMetrics().stringWidth(builder.toString() + word + " ") < maxWidth) {
						builder.append(word);
						builder.append(" ");
					} else {
						// Nope, add the line to the rendered line
						if (g2d.getFontMetrics().stringWidth(builder.toString()) < maxWidth) {
							lines.add(builder.toString());
						} else {
							// One line is still too big, this is bad but should work for most cases
							builderNewline.setLength(0);
							while (g2d.getFontMetrics().stringWidth(builder.toString()) > maxWidth) {
								builderNewline.insert(0, builder.substring(builder.length() - 1));
								builder.setLength(builder.length() - 1);
							}

							lines.add(builder.toString());
							lines.add(builderNewline.toString());
						}
						// clear build and append next work
						builder.setLength(0);
						builder.append(word);
						builder.append(" ");
					}
				}
				lines.add(builder.toString());
			} else {
				lines.add(line);
			}
		}

		// Render the lines
		int height = g2d.getFontMetrics().getHeight();
		int renderY = y;
		for (int i = 0; i < lines.size(); i++) {
			// Make sure to not render empty lines
			if (lines.get(i).length() > 0) {
				g2d.drawString(lines.get(i), x, renderY);
				renderY += height;
			}
		}

		return renderY;
	}
}
