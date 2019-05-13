package ch.sparkpudding.coreengine.api;

import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

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
	 * @param name The name of the music
	 * @throws IOException                   If the file is unreachable
	 * @throws LineUnavailableException      If the line is unavailable
	 * @throws InterruptedException
	 * @throws UnsupportedAudioFileException
	 */
	public void playMusic(String name)
			throws LineUnavailableException, IOException, InterruptedException, UnsupportedAudioFileException {

		AudioInputStream audioInputStream = resourceLocator.getMusic(name);

		if (audioInputStream == null)
			return;

		audioInputStream.reset();

		if (currentMusic != null)
			currentMusic.stop();

		currentMusic = AudioSystem.getClip();
		currentMusic.open(audioInputStream);
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
			currentMusic.start();
		}
	}

	/**
	 * Plays a sound once
	 * 
	 * @param name The name of the sound to play
	 * @throws IOException              If the file is unreachable
	 * @throws LineUnavailableException If the line is unavailable
	 */
	public void play(String name) throws LineUnavailableException, IOException {
		AudioInputStream audioInputStream = resourceLocator.getSound(name);

		if (audioInputStream == null)
			return;

		audioInputStream.reset();

		Clip clip = AudioSystem.getClip();
		clip.open(audioInputStream);
		clip.start();

	}
}
