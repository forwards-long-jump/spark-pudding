package ch.sparkpudding.sceneeditor.filewriter;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.spi.FileSystemProvider;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JOptionPane;

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
	 * @param coreEngine Is the core engine who will be saved in the Lel folder. Can
	 *                   be null to initialize a new game
	 * @param directory  Is the Lel directory path
	 * @throws IOException
	 */
	public void save(CoreEngine coreEngine, String directory) throws IOException {
		// Check general architecture
		String[] requiredFiles = { "", "assets", "assets/music", "assets/sounds", "assets/textures", "components",
				"entitytemplates", "scenes", "systems" };
		for (String fileName : requiredFiles) {
			new File(directory + fileName).mkdir();
		}
		new File(directory + "metadata.xml").createNewFile();
		Files.write(Paths.get(directory + "metadata.xml"), xmlFromMetadata().getBytes());

		Map<String, Scene> scenes = coreEngine.getScenes();
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
	 * Create a new game folder on the specified directory, based on the empty game
	 * of the SceneEditor
	 * 
	 * @param directory Is the location of the new game
	 */
	public void create(String directory, boolean isEmptyGame) {
		Path src;
		try {
			if (isEmptyGame) {
				src = Paths.get("./resources/emptygame");
			} else {
				src = Paths.get("./resources/basicgame");
			}
			Path dest = Paths.get(directory);

		
			if ("jar".equals(src.toUri().getScheme())) {
				for (FileSystemProvider provider : FileSystemProvider.installedProviders()) {
					if (provider.getScheme().equalsIgnoreCase("jar")) {
						try {
							provider.getFileSystem(src.toUri());
						} catch (FileSystemNotFoundException e) {
							// in this case we need to initialize it first:
							provider.newFileSystem(src.toUri(), Collections.emptyMap());
						}
					}
				}
			}
			
			Files.walk(src).forEach(source -> {
				try {
					Files.copy(source, dest.resolve(src.relativize(source)), StandardCopyOption.REPLACE_EXISTING);
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, e.getStackTrace(), "IO Error", JOptionPane.ERROR_MESSAGE);
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getStackTrace(), "IO Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Serialize XML from meta data<br>
	 * TODO: Implement using metadata
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
					if (!component.isAttached()
							|| !Entity.getTemplates().get(entity.getTemplate()).hasComponent(component.getName())) {
						xml += "\t\t<component template=\"" + component.getTemplateName() + "\">\n";
						if (!component.isAttached()) {
							for (Field field : component.getFields().values()) {
								xml += "\t\t\t<field name=\"" + field.getName() + "\">" + field.getValue()
										+ "</field>\n";
							}
						}
						xml += "\t\t</component>\n";
					}
				}
			}
			for (Entry<String, Component> component : Entity.getTemplates().get(entity.getTemplate())) {
				if (!component.getValue().getName().startsWith("se-")) {
					if (!entity.getComponents().keySet().contains(component.getValue().getName())) {
						xml += "<component template=\"" + component.getValue().getName() + "\" deleted=\"true\"/>";
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
