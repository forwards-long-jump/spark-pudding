package ch.sparkpudding.coreengine.filereader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages Ludic Engine in Lua game files, supports reading from folder and from
 * .lel
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba
 */
public class LelReader {
	private String directory;

	private Map<String, File> mapComponents;
	private Map<String, File> mapScenes;
	private Map<String, File> mapEntityTemplates;
	private List<File> listSystems;

	private Map<String, File> mapSounds;
	private Map<String, File> mapMusic;
	private Map<String, File> mapTextures;

	final String[] requiredSubFolders = { "components", "assets/textures", "assets/sounds", "assets/music", "scenes",
			"entitytemplates", "systems" };

	/**
	 * Reads a game file and exposes all of the files in differents maps
	 * 
	 * @param directory The path to the directory or LEL file
	 * @throws Exception
	 */
	public LelReader(String directory) throws Exception {
		this.directory = directory;

		if (!isValidLel())
			throw new FileNotFoundException();

		mapComponents = new HashMap<String, File>();
		populateMaps(new File(directory + "/components"), mapComponents);

		mapScenes = new HashMap<String, File>();
		populateMaps(new File(directory + "/scenes"), mapScenes);

		mapEntityTemplates = new HashMap<String, File>();
		populateMaps(new File(directory + "/entitytemplates"), mapEntityTemplates);

		listSystems = new ArrayList<File>();
		populateList(new File(directory + "/systems"), listSystems);
		sortSystem(listSystems);

		mapSounds = new HashMap<String, File>();
		populateMaps(new File(directory + "/assets/sounds"), mapSounds);

		mapMusic = new HashMap<String, File>();
		populateMaps(new File(directory + "/assets/music"), mapMusic);

		mapSounds = new HashMap<String, File>();
		populateMaps(new File(directory + "/assets/sounds"), mapSounds);
	}

	private void sortSystem(List<File> list) {
		// TODO Better sort of the systems
		Collections.sort(list);
	}

	/**
	 * Populate lists from the files present in the game folder
	 * 
	 * @param folder Folder to read the files from
	 * @param list   List to populate
	 */
	private void populateList(File folder, List<File> list) {
		for (File file : folder.listFiles()) {
			if (file.isDirectory()) {
				populateList(file, list);
			} else {
				list.add(file);
			}
		}
	}

	/**
	 * Populate maps from the files present in the game folder
	 * 
	 * @param folder Folder to read the files from
	 * @param map    Map to populate
	 */
	private void populateMaps(File folder, Map<String, File> map) {
		for (File file : folder.listFiles()) {
			if (file.isDirectory()) {
				populateMaps(file, map);
			} else {
				map.put(file.getAbsolutePath().substring(this.directory.length()), file);
			}
		}
	}

	/**
	 * Check whether a LEL folder is valid or not using its metadata.xml file
	 * 
	 * @return validity of the LEL folder
	 */
	private boolean isValidLel() {
		return (new File(this.directory + "/metadata.xml").exists());
	}

	/**
	 * Get components files
	 * 
	 * @return component files
	 */
	public Collection<File> getComponentsXML() {
		return mapComponents.values();
	}

	/**
	 * Get scenes files
	 * 
	 * @return scenes files
	 */
	public Collection<File> getScenesXML() {
		return mapScenes.values();
	}

	/**
	 * Get entity templates files
	 * 
	 * @return entity templates files
	 */
	public Collection<File> getEntityTemplatesXML() {
		return mapEntityTemplates.values();
	}

	/**
	 * Get textures files
	 * 
	 * @return textures files
	 */
	public Collection<File> getTextures() {
		return mapTextures.values();
	}

	/**
	 * Get musics files
	 * 
	 * @return music files
	 */
	public Collection<File> getMusics() {
		return mapMusic.values();
	}

	/**
	 * Get sounds files
	 * 
	 * @return sounds files
	 */
	public Collection<File> getSounds() {
		return mapSounds.values();
	}

	/**
	 * Get system files
	 * 
	 * @return
	 */
	public Collection<File> getSystems() {
		return listSystems;
	}
}
