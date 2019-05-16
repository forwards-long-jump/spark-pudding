package ch.sparkpudding.coreengine.filereader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Manages Ludic Engine in Lua game files, supports reading from .lel
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba
 */
public class LelReader {
	private String lelFilepath;
	private String editingLelFilepath;

	private ZipFile lelFile;
	private ZipFile editingLelFile;

	private Map<String, InputStream> mapComponents;
	private Map<String, InputStream> mapScenes;
	private Map<String, InputStream> mapEntityTemplates;
	private Map<String, InputStream> mapSystems;
	private List<String> systemOrder;

	private Map<String, InputStream> mapSounds;
	private Map<String, InputStream> mapMusic;
	private Map<String, InputStream> mapTextures;

	// Editing tools
	private Map<String, InputStream> mapEditingComponents;
	private Map<String, InputStream> mapEditingSystems;
	private List<String> editingSystemOrder;

	final String[] requiredSubFolders = { "components", "assets/textures", "assets/sounds", "assets/music", "scenes",
			"entitytemplates", "systems" };

	/**
	 * Reads a game file and exposes all of the files in different maps
	 * 
	 * @param lelFilepath path to the LEL file
	 * @throws Exception
	 */
	public LelReader(String lelFilepath) throws Exception {
		this.lelFilepath = lelFilepath;

		reload();
	}

	/**
	 * Reads a game file and exposes all of the files in different maps, also reads
	 * the editing tools from the .lel of the scene editor
	 * 
	 * @param lelFilepath        path to the LEL file
	 * @param editingLelFilepath path to the editing tools LEL file
	 * @throws Exception
	 */
	public LelReader(String lelFilepath, String editingLelFilepath) throws Exception {
		this(lelFilepath);
		this.editingLelFilepath = editingLelFilepath;

		reloadEditingTools();
	}

	/**
	 * Reloads the streams from the LEL file
	 * 
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws FileNotFoundException
	 */
	public void reload() throws IOException, ParserConfigurationException, SAXException, FileNotFoundException {
		lelFile = new ZipFile(lelFilepath);
		// We must not close the lel file, or else all the inputstreams we got out of it
		// will be closed as well

		if (!isValidLel(lelFile)) {
			lelFile.close();
			throw new FileNotFoundException();
		}

		mapComponents = new HashMap<String, InputStream>();
		populateMaps(lelFile, "components/", mapComponents);

		mapScenes = new HashMap<String, InputStream>();
		populateMaps(lelFile, "scenes/", mapScenes);

		mapEntityTemplates = new HashMap<String, InputStream>();
		populateMaps(lelFile, "entitytemplates/", mapEntityTemplates);

		mapSystems = new HashMap<String, InputStream>();
		populateMaps(lelFile, "systems/", mapSystems);

		mapSounds = new HashMap<String, InputStream>();
		populateMaps(lelFile, "assets/sounds/", mapSounds);

		mapMusic = new HashMap<String, InputStream>();
		populateMaps(lelFile, "assets/music/", mapMusic);

		mapTextures = new HashMap<String, InputStream>();
		populateMaps(lelFile, "assets/textures/", mapTextures);
	}

	/**
	 * Load editing systems and components from .lel file (editing folder must have
	 * /components and /systems)
	 * 
	 * @throws IOException
	 */
	public void reloadEditingTools() throws IOException {
		editingLelFile = new ZipFile(editingLelFilepath);

		mapEditingComponents = new HashMap<String, InputStream>();
		populateMaps(editingLelFile, "components/", mapEditingComponents);

		mapEditingSystems = new HashMap<String, InputStream>();
		populateMaps(editingLelFile, "systems/", mapEditingSystems);
		editingSystemOrder = new ArrayList<String>(mapEditingSystems.keySet());
		editingSystemOrder.remove("render.lua");
	}

	/**
	 * Populate maps from the files present in the game folder
	 * 
	 * @param lelFile source file
	 * @param folder  Folder to read the files from
	 * @param map     Map to populate
	 * @throws IOException
	 */
	private void populateMaps(ZipFile lelFile, String prefix, Map<String, InputStream> map) throws IOException {
		Enumeration<? extends ZipEntry> entries = lelFile.entries();

		while (entries.hasMoreElements()) {
			ZipEntry entry = entries.nextElement();
			if (entry.getName().startsWith(prefix)) {
				// TODO: remove .keep condition when it is no longer needed
				if (!entry.isDirectory() && !entry.getName().equals(".keep")) {
					map.put(entry.getName().substring(prefix.length()), lelFile.getInputStream(entry));
				}
			}
		}
	}

	/**
	 * Check whether a LEL file is valid or not using its metadata.xml file. Also
	 * reads the order of the systems described within
	 * 
	 * @param lelFile LEL file to check
	 * @return validity of the LEL folder
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	private boolean isValidLel(ZipFile lelFile) throws ParserConfigurationException, SAXException, IOException {
		ZipEntry metadataZip;
		if ((metadataZip = lelFile.getEntry("metadata.xml")) == null) {
			return false;
		}
		this.systemOrder = new ArrayList<String>();

		Document metadata = XMLParser.parse(lelFile.getInputStream(metadataZip));
		NodeList systemsXML = metadata.getElementsByTagName("system");
		for (int i = 0; i < systemsXML.getLength(); i++) {
			systemOrder.add(systemsXML.item(i).getTextContent() + ".lua");
		}
		systemOrder.remove("render.lua");

		return true;
	}

	/**
	 * Get components files
	 * 
	 * @return component files
	 */
	public Map<String, InputStream> getComponentsXML() {
		return mapComponents;
	}

	/**
	 * Get components files
	 * 
	 * @return component files
	 */
	public Map<String, InputStream> getEditingComponentsXML() {
		return mapEditingComponents;
	}

	/**
	 * Get scenes files
	 * 
	 * @return scenes files
	 */
	public Map<String, InputStream> getScenesXML() {
		return mapScenes;
	}

	/**
	 * Get entity templates files
	 * 
	 * @return entity templates files
	 */
	public Map<String, InputStream> getEntityTemplatesXML() {
		return mapEntityTemplates;
	}

	/**
	 * Get textures files
	 * 
	 * @return textures files
	 */
	public Map<String, InputStream> getTextures() {
		return mapTextures;
	}

	/**
	 * Get musics files
	 * 
	 * @return music files
	 */
	public Map<String, InputStream> getMusics() {
		return mapMusic;
	}

	/**
	 * Get sounds files
	 * 
	 * @return sounds files
	 */
	public Map<String, InputStream> getSounds() {
		return mapSounds;
	}

	/**
	 * Get system files
	 * 
	 * @return system files
	 */
	public Map<String, InputStream> getSystems() {
		return mapSystems;
	}

	/**
	 * Get the system order
	 * 
	 * @return system order
	 */
	public List<String> getSystemOrder() {
		return systemOrder;
	}

	/**
	 * Get the editing system order
	 * 
	 * @return editing system order
	 */
	public List<String> getEditingSystemOrder() {
		return editingSystemOrder;
	}

	/**
	 * Get editing system files
	 * 
	 * @return editing system files
	 */
	public Map<String, InputStream> getEditingSystems() {
		return mapEditingSystems;
	}
}
