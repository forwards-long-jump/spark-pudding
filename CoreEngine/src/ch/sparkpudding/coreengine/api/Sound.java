package ch.sparkpudding.coreengine.api;

import java.applet.AudioClip;

import ch.sparkpudding.coreengine.Lel;
import ch.sparkpudding.coreengine.ResourceLocator;
import sun.audio.AudioData;
import sun.audio.AudioDataStream;
import sun.audio.AudioPlayer;
import sun.audio.ContinuousAudioDataStream;

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

	private AudioClip currentMusic;

	/**
	 * Private constructor for the singleton
	 */
	private Sound() {
		this.resourceLocator = Lel.coreEngine.getResourceLocator();
	}

	/**
	 * Create instance if it doesn't exist and return it anyway
	 * 
	 * @return The instance of Sound
	 */
	public static Sound getInstance() {
		if (instance == null) {
			instance = new Sound();
		}
		return instance;
	}

	/**
	 * Plays the given music
	 * 
	 * @param name The name of the music (intended to be acquired via the resource
	 *             locator)
	 */
	public void playMusic(String name) {
		AudioData audioData = resourceLocator.getMusic(name);
		if (audioData == null) {
			return;
		}

		// Create an ContinuousAudioDataStream to play back continuously
		ContinuousAudioDataStream loopMusicStream = new ContinuousAudioDataStream(audioData);
		// Play the sound
		AudioPlayer.player.start(loopMusicStream);
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
	 * @param name The name of the sound to play
	 */
	public void play(String name) {
		AudioData audioData = resourceLocator.getSound(name);
		if (audioData == null) {
			return;
		}
		// Create an AudioDataStream to play back
		AudioDataStream audioStream = new AudioDataStream(audioData);
		// Play the sound
		AudioPlayer.player.start(audioStream);
	}
}
