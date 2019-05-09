package ch.sparkpudding.coreengine;

import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import ch.sparkpudding.coreengine.filereader.LelReader;
import sun.audio.AudioData;

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
	private Map<String, AudioData> sounds;
	private Map<String, AudioData> musics;

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
	 * @throws UnsupportedAudioFileException
	 * @throws IOException
	 * @throws LineUnavailableException
	 */
	private void loadSounds() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		sounds = new HashMap<String, AudioData>();
		for (File file : lelReader.getSounds()) {
			// TODO : remove as soon as .keep files are removed
			if (file.getName().equals(".keep")) {
				continue;
			}
			byte[] byteFile = readFileToByteArray(file);
			// Create the AudioData object from the byte array
			AudioData audioData = new AudioData(byteFile);

			sounds.put(file.getName(), audioData);
		}
	}

	/**
	 * Loads all the musics of assets/music into the field
	 * 
	 * @throws IOException
	 * @throws LineUnavailableException
	 */
	private void loadMusics() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		musics = new HashMap<String, AudioData>();
		for (File file : lelReader.getMusics()) {
			// TODO : remove as soon as .keep files are removed
			if (file.getName().equals(".keep")) {
				continue;
			}
			byte[] byteFile = readFileToByteArray(file);
			// Create the AudioData object from the byte array
			AudioData audioData = new AudioData(byteFile);

			musics.put(file.getName(), audioData);
		}
	}
	
	// Return a byte array from the file
	private static byte[] readFileToByteArray(File file){
        FileInputStream fis = null;
        // Creating a byte array using the length of the file
        // file.length returns long which is cast to int
        byte[] bArray = new byte[(int) file.length()];
        try{
            fis = new FileInputStream(file);
            fis.read(bArray);
            fis.close();        
            
        }catch(IOException ioExp){
            ioExp.printStackTrace();
        }
        return bArray;
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
	public AudioData getSound(String name) {
		return sounds.get(name);
	}

	/**
	 * Gets music by name
	 * 
	 * @param name Name of the music
	 * @return Clip, or null when nothing is found
	 */
	public AudioData getMusic(String name) {
		return musics.get(name);
	}
}
