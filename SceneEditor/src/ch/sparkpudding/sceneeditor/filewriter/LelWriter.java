package ch.sparkpudding.sceneeditor.filewriter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Map.Entry;

import ch.sparkpudding.coreengine.CoreEngine;
import ch.sparkpudding.coreengine.ecs.component.Component;
import ch.sparkpudding.coreengine.ecs.component.Field;
import ch.sparkpudding.coreengine.ecs.entity.Entity;
import ch.sparkpudding.coreengine.ecs.entity.Scene;

/**
 * The class to write all the file of a game from the SceneEditor
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba<br/>
 *         Creation Date : 29 April 2019
 *
 */
public class LelWriter {

	/**
	 * Save all the specified CoreEngine in the specified directory
	 * 
	 * @param coreEngine is the core engine who will be saved in the Lel folder.
	 * @param directory  is the Lel directory path
	 * @throws IOException
	 */
	public void write(CoreEngine coreEngine, String directory) throws IOException {
		// TODO : Adapt the code when we have a proper way to deal with default entities
		// values and metadata in core engine.

		Map<String, Scene> scenes = coreEngine.getScenes();
		// Check general architecture
		String[] requiredFiles = { "", "assets", "assets/music", "assets/sounds", "assets/textures", "components",
				"entitytemplates", "scenes", "systems" };
		for (String fileName : requiredFiles) {
			new File(directory + fileName).mkdir();
		}
		new File(directory + "metadata.xml").createNewFile();
		Files.write(Paths.get(directory + "metadata.xml"), xmlFromMetadata().getBytes());

		// Overwrite Scenes
		for (Map.Entry<String, Scene> sceneEntry : scenes.entrySet()) {
			File fScene = new File(directory + "/scenes/" + sceneEntry.getKey() + ".xml");
			String xmlScene = xmlFromScene(sceneEntry.getValue());
			Files.write(fScene.toPath(), xmlScene.getBytes());
		}

		// Overwrite EntityTemplates
		for (Map.Entry<String, Entity> templateEntry : Entity.getTemplates().entrySet()) {
			File fTemplate = new File(directory + "/entitytemplates/" + templateEntry.getKey() + ".xml");
			String xmlEntity = xmlFromEntityTemplate(templateEntry.getValue());
			Files.write(fTemplate.toPath(), xmlEntity.getBytes());
		}

		// Overwrite Components
		for (Map.Entry<String, Component> componentEntry : Component.getTemplates().entrySet()) {
			if (!componentEntry.getValue().getName().startsWith("se-")) {
				File fComponent = new File(directory + "/components/" + componentEntry.getKey() + ".xml");
				String xmlComponent = xmlFromComponent(componentEntry.getValue());
				Files.write(fComponent.toPath(), xmlComponent.getBytes());
			}
		}

	}

	/**
	 * Serialize XML from meta data TODO: Fix this
	 * 
	 * @return String to write to the xml metadata
	 */
	private String xmlFromMetadata() {
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<lel>\n" + "	<version>0.1</version>\n" + "</lel>";
	}

	/**
	 * Serialize a scene to a xml-formatted string
	 * 
	 * @param Scene to serialize
	 * @return String to write in the xml file of the scene
	 */
	private String xmlFromScene(Scene scene) {
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
		xml += "<scene name=\"" + scene.getName() + "\">\n";
		for (Entity entity : scene.getDefaultEntities()) {
			xml += "\t<entity name=\"" + entity.getName() + "\" template=\"" + entity.getTemplate() + "\" z-index=\""
					+ entity.getZIndex() + "\">\n";
			for (Component component : entity.getComponents().values()) {
				if (!component.getName().startsWith("se-")) {
					xml += "\t\t<component template=\"" + component.getTemplateName() + "\">\n";
					if (!component.isAttached()) {
						for (Field field : component.getFields().values()) {
							xml += "\t\t\t<field name=\"" + field.getName() + "\">" + field.getValue() + "</field>\n";
						}
					}
					xml += "</component>\n";
				}
			}
			for (Entry<String, Component> component : Entity.getTemplates().get(entity.getTemplate())) {
				if (!component.getValue().getName().startsWith("se-")) {
					if (!entity.getComponents().keySet().contains(component.getValue().getName())) {
						xml += "<component name=\"" + component.getValue().getName() + "\" deleted=\"true\"/>";
					}
				}
			}
			xml += "</entity>\n";
		}
		xml += "</scene>";
		return xml;
	}

	/**
	 * Serialize an entity template to a xml-formatted string
	 * 
	 * @param EntityTemplate to serialize
	 * @return String to write in the xml file of the entity template
	 */
	private String xmlFromEntityTemplate(Entity entity) {
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
		xml += "<entity-template name=\"" + entity.getTemplate() + "\">\n";
		for (Component component : entity.getComponents().values()) {
			xml += "\t<component template=\"" + component.getTemplateName() + "\">\n";
			for (Field field : component.getFields().values()) {
				xml += "\t\t<field name=\"" + field.getName() + "\">" + field.getValue() + "</field>\n";
			}
			xml += "\t</component>\n";
		}
		xml += "</entity-template>";
		return xml;
	}

	/**
	 * Serialize a component to a xml-formatted string
	 * 
	 * @param Component to serialize
	 * @return String to write in the xml file of the component
	 */
	private String xmlFromComponent(Component component) {
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
		xml += "<component name=\"" + component.getName() + "\">\n";
		for (Field field : component.getFields().values()) {
			xml += "\t<field type=\"" + field.getType() + "\" name=\"" + field.getName() + "\">" + field.getValue()
					+ "</field>\n";
		}
		xml += "</component>";
		return xml;
	}
}
