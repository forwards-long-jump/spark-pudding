package ch.sparkpudding.sceneeditor.filewriter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

import ch.sparkpudding.coreengine.CoreEngine;
import ch.sparkpudding.coreengine.ecs.component.Component;
import ch.sparkpudding.coreengine.ecs.entity.Entity;
import ch.sparkpudding.coreengine.ecs.entity.Scene;

public class FileWriter {

	/**
	 * Save all the specified CoreEngine in the specified directory
	 * 
	 * @param cs
	 * @param directory
	 * @throws IOException
	 */
	public void writeLel(CoreEngine cs, String directory) throws IOException {
		Map<String, Scene> scenes = cs.getScenes();
		// Check general architecture
		String[] requiredFiles = { "", "assets", "assets/musics", "assets/sounds", "assets/textures", "components",
				"entitytemplates", "scenes", "systems" };
		for (String fileName : requiredFiles) {
			new File(directory + fileName).mkdir();
		}
		new File(directory + "metadata.xml").createNewFile();

		// Overwrite Scenes
		for (Map.Entry<String, Scene> sceneEntry : scenes.entrySet()) {
			File fScene = new File(directory + "/scenes/" + sceneEntry.getKey() + ".xml");
			fScene.createNewFile();
			String xmlScene = xmlFromScene(sceneEntry.getValue());
			Files.write(fScene.toPath(), xmlScene.getBytes());
		}

		// Overwrite EntityTemplates
		for (Map.Entry<String, Entity> templateEntry : Entity.getTemplates().entrySet()) {
			File fTemplate = new File(directory + "/scenes/" + templateEntry.getKey() + ".xml");
			fTemplate.createNewFile();
			String xmlScene = xmlFromEntityTemplate(templateEntry.getValue());
			Files.write(fTemplate.toPath(), xmlScene.getBytes());
		}

		// Overwrite Components
		for (Map.Entry<String, Component> componentEntry : Component.getTemplates().entrySet()) {
			File fComponent = new File(directory + "/scenes/" + componentEntry.getKey() + ".xml");
			fComponent.createNewFile();
			String xmlScene = xmlFromComponent(componentEntry.getValue());
			Files.write(fComponent.toPath(), xmlScene.getBytes());
		}

	}

	/**
	 * Serialize a scene to a xml-formatted string
	 * 
	 * @param Scene to serialize
	 * @return String to write in the xml file of the scene
	 */
	private String xmlFromScene(Scene scene) {
		return null;
	}

	/**
	 * Serialize an entity template to a xml-formatted string
	 * 
	 * @param EntityTemplate to serialize
	 * @return String to write in the xml file of the entity template
	 */
	private String xmlFromEntityTemplate(Entity entity) {
		return null;
	}

	/**
	 * Serialize a component to a xml-formatted string
	 * 
	 * @param Component to serialize
	 * @return String to write in the xml file of the component
	 */
	private String xmlFromComponent(Component component) {
		return null;
	}
}
