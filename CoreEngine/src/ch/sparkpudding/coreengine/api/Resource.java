package ch.sparkpudding.coreengine.api;

import java.applet.AudioClip;
import java.awt.Image;

import ch.sparkpudding.coreengine.Lel;
import ch.sparkpudding.coreengine.ResourceLocator;

/**
 * API that wraps the ResourceLocator class in order to only give access to
 * resources consultation from Lua systems
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba<br/>
 *         Creation Date : 6 mai 2019
 *
 */
public class Resource {
	private static Resource instance;

	private ResourceLocator resourceLocator;

	private Resource() {
		this.resourceLocator = Lel.coreEngine.getResourceLocator();
	}

	public static Resource getInstance() {
		if (instance == null) {
			instance = new Resource();
		}
		return instance;
	}

	/**
	 * Gets the texture by name
	 * 
	 * @param name Name of the image / texture
	 * @return Image, or null when nothing is found
	 */
	public Image getTexture(String name) {
		return resourceLocator.getTexture(name);
	}

	/**
	 * Gets the sound by name
	 * 
	 * @param name Name of the sound
	 * @return Clip, or null when nothing is found
	 */
	public AudioClip getSound(String name) {
		return resourceLocator.getSound(name);
	}

	/**
	 * Gets music by name
	 * 
	 * @param name Name of the music
	 * @return Clip, or null when nothing is found
	 */
	public AudioClip getMusic(String name) {
		return resourceLocator.getMusic(name);
	}
}
