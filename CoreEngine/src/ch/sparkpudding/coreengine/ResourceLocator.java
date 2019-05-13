package ch.sparkpudding.coreengine;

import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
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
	private Map<String, AudioInputStream> sounds;
	private Map<String, AudioInputStream> musics;

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
	 * @throws UnsupportedAudioFileException If the file has an unsupported
	 *                                       extension
	 * @throws IOException                   If the file is unreachable
	 */
	private void loadSounds() throws UnsupportedAudioFileException, IOException {
		sounds = new HashMap<String, AudioInputStream>();
		for (File file : lelReader.getSounds()) {
			// TODO : remove as soon as .keep files are removed
			if (file.getName().equals(".keep")) {
				continue;
			}

			AudioInputStream audioInputStream = createReusableAudioInputStream(file);

			sounds.put(file.getName(), audioInputStream);
		}
	}

	/**
	 * Loads all the musics of assets/music into the field
	 * 
	 * @throws UnsupportedAudioFileException If the file has an unsupported
	 *                                       extension
	 * @throws IOException                   If the file is unreachable
	 */
	private void loadMusics() throws UnsupportedAudioFileException, IOException {
		musics = new HashMap<String, AudioInputStream>();
		for (File file : lelReader.getMusics()) {
			// TODO : remove as soon as .keep files are removed
			if (file.getName().equals(".keep")) {
				continue;
			}
			AudioInputStream audioInputStream = createReusableAudioInputStream(file);

			musics.put(file.getName(), audioInputStream);
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
	 * @return AudioInputStream, or null when nothing is found
	 */
	public AudioInputStream getSound(String name) {
		return sounds.get(name);
	}

	/**
	 * Gets music by name
	 * 
	 * @param name Name of the music
	 * @return AudioInputStream, or null when nothing is found
	 */
	public AudioInputStream getMusic(String name) {
		return musics.get(name);
	}

	/**
	 * Return an AudioInputStream that can be reset
	 * 
	 * Comes from https://stackoverflow.com/a/10000439
	 * 
	 * @param file The origin file of the stream
	 * @return A resettable AudioInputStream
	 * @throws IOException                   When the file cannot be accessed
	 * @throws UnsupportedAudioFileException When the file format isn't supported
	 */
	private static AudioInputStream createReusableAudioInputStream(File file)
			throws IOException, UnsupportedAudioFileException {
		AudioInputStream ais = null;
		try {
			ais = AudioSystem.getAudioInputStream(file);
			byte[] buffer = new byte[1024 * 32];
			int read = 0;
			ByteArrayOutputStream baos = new ByteArrayOutputStream(buffer.length);
			while ((read = ais.read(buffer, 0, buffer.length)) != -1) {
				baos.write(buffer, 0, read);
			}
			AudioInputStream reusableAis = new AudioInputStream(new ByteArrayInputStream(baos.toByteArray()),
					ais.getFormat(), AudioSystem.NOT_SPECIFIED);
			return reusableAis;
		} finally {
			if (ais != null) {
				ais.close();
			}
		}
	}
}
