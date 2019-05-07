package ch.sparkpudding.coreengine.api;

import java.applet.Applet;
import java.applet.AudioClip;

import ch.sparkpudding.coreengine.Lel;
import ch.sparkpudding.coreengine.ResourceLocator;

public class Sound {
	private static Sound instance;
	private ResourceLocator resourceLocator;
	
	private AudioClip currentMusic;
	
	private Sound() {
		this.resourceLocator = Lel.coreEngine.getResourceLocator();
	}
	
	public static Sound getInstance() {
		if (instance == null) {
			instance = new Sound();
		}
		return instance;
	}
	
	/**
	 * Plays the given music
	 * 
	 * @param clip Music (intended to be aquired via the resource locator)
	 */
	public void playMusic(String name) {
		AudioClip clip = resourceLocator.getMusic(name);
		if (clip == null) {
			return;
		}
		currentMusic = clip;
		currentMusic.loop();
	}
	
	/**
	 * Pauses the currently playing music if there is one
	 */
	public void pauseMusic() {
		if (currentMusic != null) {
			currentMusic.stop();
		}
	}
	
	/**
	 * Resumes the music if it was paused
	 */
	public void resumeMusic() {
		if (currentMusic != null) {
			currentMusic.loop();
		}
	}
	
	/**
	 * Plays a sound once
	 * 
	 * @param clip
	 */
	public void play(String name) {
		AudioClip clip = resourceLocator.getSound(name);
		if (clip == null) {
			return;
		}
		clip.play();
	}
}
