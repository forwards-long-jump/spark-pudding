package ch.sparkpudding.coreengine.api;

import java.awt.Image;

import javax.sound.sampled.Clip;

import ch.sparkpudding.coreengine.Lel;
import ch.sparkpudding.coreengine.ResourceLocator;

public class ResourceAPI {
	private static ResourceAPI instance;
	
	private ResourceLocator resourceLocator;
	
	private ResourceAPI() {
		this.resourceLocator = Lel.coreEngine.getResourceLocator();
	}
	
	public static ResourceAPI getInstance() {
		if (instance == null) {
			instance = new ResourceAPI();
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
	public Clip getSound(String name) {
		return resourceLocator.getSound(name);
	}

	/**
	 * Gets music by name
	 * 
	 * @param name Name of the music
	 * @return Clip, or null when nothing is found
	 */
	public Clip getMusic(String name) {
		return resourceLocator.getMusic(name);
	}
}
