package ch.sparkpudding.sceneeditor.utils;

import java.net.URL;

import javax.swing.ImageIcon;

/**
 * A class which contains static method to load image
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba<br/>
 *         Creation Date : 3 May 2019
 *
 */
public class ImageLoader {

	/**
	 * Load an image from the ressources
	 * 
	 * @param filename Name of the file
	 * @return A usable ImageIcon
	 */
	public static ImageIcon loadImage(String filename) {
		URL url = ClassLoader.getSystemResource(filename);
		return new ImageIcon(url);
	}

}
