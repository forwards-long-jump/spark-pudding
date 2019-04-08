package ch.sparkpudding.coreengine.filereader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba
 * 
 */
public class LelFile {
	private String directory;

	private Map<String, File> mapComponents;
	private Map<String, File> mapScenes;
	private Map<String, File> mapEntityTemplates;
	private Map<String, File> mapSystems;

	private Map<String, File> mapSounds;
	private Map<String, File> mapMusic;
	private Map<String, File> mapTextures;

	final String[] requiredSubFolders = { "components", "assets/textures", "assets/sounds", "assets/music", "scenes",
			"entitytemplates", "systems" };

	public LelFile(String directory) throws Exception {
		this.directory = directory;

		if (!isValidLel())
			throw new FileNotFoundException();


		mapComponents = new HashMap<String, File>();
		populateMaps(new File(directory + "/components"), mapComponents);

		mapScenes = new HashMap<String, File>();
		populateMaps(new File(directory + "/scenes"), mapScenes);

		mapEntityTemplates = new HashMap<String, File>();
		populateMaps(new File(directory + "/entitytemplates"), mapEntityTemplates);

		mapSystems = new HashMap<String, File>();
		populateMaps(new File(directory + "/systems"), mapSystems);

		mapSounds = new HashMap<String, File>();
		populateMaps(new File(directory + "/assets/sounds"), mapSounds);

		mapMusic = new HashMap<String, File>();
		populateMaps(new File(directory + "/assets/music"), mapMusic);

		mapSounds = new HashMap<String, File>();
		populateMaps(new File(directory + "/assets/sounds"), mapSounds);
	}

	private void populateMaps(File folder, Map<String, File> map) {
		for (File file : folder.listFiles()) {
			if (file.isDirectory()) {
				populateMaps(file, map);
			} else {
				map.put(file.getAbsolutePath().substring(this.directory.length()), file);
			}
		}
	}

	private boolean isValidLel() {
		return (new File(this.directory + "/metadata.xml").exists());
	}

	public Collection<File> getComponentsXML() {
		return  mapComponents.values();
	}

	public Collection<File> getScenesXML() {
		return mapScenes.values();
	}

	public Collection<File> getEntityTemplatesXML() {
		return mapEntityTemplates.values();
	}

	public Collection<File> getTextures() {
		return mapTextures.values();
	}

	public Collection<File> getMusics() {
		return mapMusic.values();
	}

	public Collection<File> getSounds() {
		return mapSounds.values();
	}
}
