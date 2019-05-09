package ch.sparkpudding.coreengine.api;

import java.applet.AudioClip;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import ch.sparkpudding.coreengine.CoreEngine;
import ch.sparkpudding.coreengine.Lel;
import ch.sparkpudding.coreengine.ResourceLocator;
import sun.audio.AudioData;
import sun.audio.AudioDataStream;
import sun.audio.AudioPlayer;

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
	 * @param name The name of the sound to play
	 */
	public void play(String name) {
		AudioClip clip = resourceLocator.getSound(name);
		if (clip == null) {
			return;
		}
		String path = "C:\\Users\\johnl\\Documents\\Ecole\\P2\\spark-pudding\\TestEngine\\bin\\testgame\\assets\\sounds\\slap.wav";
		File f = new File(path);
		byte[] b = readFileToByteArray(f);
		// Create the AudioData object from the byte array
		AudioData audiodata = new AudioData(b);
		// Create an AudioDataStream to play back
		AudioDataStream audioStream = new AudioDataStream(audiodata);
		// Play the sound
		AudioPlayer.player.start(audioStream);
	}
	
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
}
