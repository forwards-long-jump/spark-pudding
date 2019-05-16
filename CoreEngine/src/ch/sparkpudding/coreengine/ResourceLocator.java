package ch.sparkpudding.coreengine;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import ch.sparkpudding.coreengine.filereader.LelReader;

/**
 * Handles getting files from the asset folder of the .lel file
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba
 */
public class ResourceLocator {
	private LelReader lelReader;

	private Map<String, Image> textures;
	// FIXME: We must find another solution, because using Clips prevents us from
	// playing the same sound multiple times in quick succession
	private Map<String, Clip> sounds;
	private Map<String, Clip> musics;

	/**
	 * ctor
	 * 
	 * @param lelReader lelReader from whose .lel file this will read
	 */
	public ResourceLocator(LelReader lelReader) {
		this.lelReader = lelReader;

		try {
			loadTextures();
			loadSounds();
			loadMusics();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Loads all the textures of assets/textures into the field
	 * 
	 * @throws IOException
	 */
	private void loadTextures() throws IOException {
		textures = new HashMap<String, Image>();
		for (Entry<String, InputStream> file : lelReader.getTextures().entrySet()) {
			// TODO : remove as soon as .keep files are removed
			if (file.getKey().equals(".keep")) {
				continue;
			}

			textures.put(file.getKey(), ImageIO.read(file.getValue()));
		}
	}

	/**
	 * Loads all the sounds of assets/sounds into the field
	 * 
	 * @throws UnsupportedAudioFileException
	 * @throws IOException
	 * @throws LineUnavailableException
	 */
	private void loadSounds() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		sounds = new HashMap<String, Clip>();
		for (Entry<String, InputStream> file : lelReader.getSounds().entrySet()) {
			// TODO : remove as soon as .keep files are removed
			if (file.getKey().equals(".keep")) {
				continue;
			}

			AudioInputStream audio = AudioSystem.getAudioInputStream(file.getValue());
			Clip clip = AudioSystem.getClip();
			clip.open(audio);
			sounds.put(file.getKey(), clip);
		}
	}

	/**
	 * Loads all the musics of assets/music into the field
	 * 
	 * @throws IOException
	 * @throws LineUnavailableException
	 */
	private void loadMusics() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		musics = new HashMap<String, Clip>();
		for (Entry<String, InputStream> file : lelReader.getMusics().entrySet()) {
			// TODO : remove as soon as .keep files are removed
			if (file.getKey().equals(".keep")) {
				continue;
			}

			AudioInputStream audio = AudioSystem.getAudioInputStream(file.getValue());
			Clip clip = AudioSystem.getClip();
			clip.open(audio);
			musics.put(file.getKey(), clip);
		}
	}

	/**
	 * Gets the texture by name
	 * 
	 * @param name Name of the image / texture
	 * @return Image, or null when nothing is found
	 */
	public Image getTexture(String name) {
		return textures.get(name);
	}

	/**
	 * Gets the sound by name
	 * 
	 * @param name Name of the sound
	 * @return Clip, or null when nothing is found
	 */
	public Clip getSound(String name) {
		return sounds.get(name);
	}

	/**
	 * Gets music by name
	 * 
	 * @param name Name of the music
	 * @return Clip, or null when nothing is found
	 */
	public Clip getMusic(String name) {
		return musics.get(name);
	}
}
