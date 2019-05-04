package ch.sparkpudding.coreengine;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
			// TODO try again ? error message ?
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
		for (File file : lelReader.getTextures()) {
			// TODO : remove as soon as .keep files are removed
			if (file.getName().equals(".keep")) {
				continue;
			}
			
			textures.put(file.getName(), ImageIO.read(file));
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
		for (File file : lelReader.getSounds()) {
			// TODO : remove as soon as .keep files are removed
			if (file.getName().equals(".keep")) {
				continue;
			}
			
			System.out.println(file.getName());
			
			AudioInputStream audio = AudioSystem.getAudioInputStream(file);
			Clip clip = AudioSystem.getClip();
			clip.open(audio);
			sounds.put(file.getName(), clip);
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
		for (File file : lelReader.getMusics()) {
			// TODO : remove as soon as .keep files are removed
			if (file.getName().equals(".keep")) {
				continue;
			}
			
			AudioInputStream audio = AudioSystem.getAudioInputStream(file);
			Clip clip = AudioSystem.getClip();
			clip.open(audio);
			musics.put(file.getName(), clip);
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
