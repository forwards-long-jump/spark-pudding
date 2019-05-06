package ch.sparkpudding.coreengine.api;

import javax.sound.sampled.Clip;

import ch.sparkpudding.coreengine.Lel;
import ch.sparkpudding.coreengine.ResourceLocator;

/**
 * API that allow to play sound from the systems
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba<br/>
 *         Creation Date : 6 mai 2019
 *
 */
public class Sound {
	private static Sound instance;
	private ResourceLocator resourceLocator;

	private Clip currentMusic;

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
		Clip clip = resourceLocator.getMusic(name);
		if (clip == null) {
			return;
		}
		currentMusic = clip;
		currentMusic.loop(Clip.LOOP_CONTINUOUSLY);
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
			currentMusic.loop(Clip.LOOP_CONTINUOUSLY);
		}
	}

	/**
	 * Plays a sound once
	 * 
	 * @param clip
	 */
	public void play(String name) {
		Clip clip = resourceLocator.getSound(name);
		if (clip == null) {
			return;
		}
		clip.setFramePosition(0);
		clip.start();
	}
}
