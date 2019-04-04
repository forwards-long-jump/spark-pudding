package ch.sparkpudding.coreengine.filereader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class LelManager {
	private String directory;

	private Map<String, String> mapComponents;
	private Map<String, String> mapScenes;
	private Map<String, String> mapEntityTemplates;
	private Map<String, String> mapSystems;

	private Map<String, File> mapSounds;
	private Map<String, File> mapMusic;
	private Map<String, File> mapTextures;

	final String[] requiredSubFolders = { "components", "assets/textures", "assets/sounds", "assets/music", "scenes",
			"entitytemplates", "systems" };

	public LelManager(String directory) throws Exception {
		this.directory = directory;

		if (!isValidLEL())
			throw new FileNotFoundException();

		File folder = new File(directory);

		mapComponents = new HashMap<String, String>();
		populateStringMaps(new File(directory + "/components"), mapComponents);

		mapScenes = new HashMap<String, String>();
		populateStringMaps(new File(directory + "/scenes"), mapScenes);

		mapEntityTemplates = new HashMap<String, String>();
		populateStringMaps(new File(directory + "/entitytemplates"), mapEntityTemplates);

		mapSystems = new HashMap<String, String>();
		populateStringMaps(new File(directory + "/systems"), mapSystems);

		mapSounds = new HashMap<String, File>();
		populateFileMaps(new File(directory + "/assets/sounds"), mapSounds);

		mapMusic = new HashMap<String, File>();
		populateFileMaps(new File(directory + "/assets/music"), mapMusic);

		mapSounds = new HashMap<String, File>();
		populateFileMaps(new File(directory + "/assets/sounds"), mapSounds);
	}

	private static String readFile(File file) throws IOException {
		byte[] encoded = Files.readAllBytes(file.toPath());
		return new String(encoded, StandardCharsets.UTF_8);
	}

	private void populateStringMaps(File folder, Map<String, String> map) throws IOException {
		for (File file : folder.listFiles()) {
			if (file.isDirectory()) {
				populateStringMaps(file, map);
			} else {
				map.put(file.getAbsolutePath().substring(this.directory.length()), readFile(file));
			}
		}
	}

	private void populateFileMaps(File folder, Map<String, File> map) {
		for (File file : folder.listFiles()) {
			if (file.isDirectory()) {
				populateFileMaps(file, map);
			} else {
				map.put(file.getAbsolutePath().substring(this.directory.length()), file);
			}
		}
	}

	private boolean isValidLEL() {
		return (new File(this.directory + "/metadata.xml").exists());
	}

	public Collection<String> getComponentsXML() {
		return  mapComponents.values();
	}

	public Collection<String> getScenesXML() {
		return mapScenes.values();
	}

	public Collection<String> getEntityTemplatesXML() {
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
