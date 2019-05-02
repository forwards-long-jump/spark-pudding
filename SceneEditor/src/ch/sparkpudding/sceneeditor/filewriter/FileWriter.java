package ch.sparkpudding.sceneeditor.filewriter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import ch.sparkpudding.coreengine.CoreEngine;
import ch.sparkpudding.coreengine.ecs.component.Component;
import ch.sparkpudding.coreengine.ecs.component.Field;
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
		// TODO : Adapt the code when we have a proper way to deal with default entites values and metadata in core engine.
		
		Map<String, Scene> scenes = cs.getScenes();
		// Check general architecture
		String[] requiredFiles = { "", "assets", "assets/musics", "assets/sounds", "assets/textures", "components",
				"entitytemplates", "scenes", "systems" };
		for (String fileName : requiredFiles) {
			new File(directory + fileName).mkdir();
		}
		new File(directory + "metadata.xml").createNewFile();
		Files.write(Paths.get(directory + "metadata.xml"), "TODO".getBytes());

		// Overwrite Scenes
		for (Map.Entry<String, Scene> sceneEntry : scenes.entrySet()) {
			File fScene = new File(directory + "/scenes/" + sceneEntry.getKey() + ".xml");
			String xmlScene = xmlFromScene(sceneEntry.getValue());
			Files.write(fScene.toPath(), xmlScene.getBytes());
		}

		// Overwrite EntityTemplates
		for (Map.Entry<String, Entity> templateEntry : Entity.getTemplates().entrySet()) {
			File fTemplate = new File(directory + "/entities/" + templateEntry.getKey() + ".xml");
			String xmlEntity = xmlFromEntityTemplate(templateEntry.getValue());
			Files.write(fTemplate.toPath(), xmlEntity.getBytes());
		}

		// Overwrite Components
		for (Map.Entry<String, Component> componentEntry : Component.getTemplates().entrySet()) {
			File fComponent = new File(directory + "/components/" + componentEntry.getKey() + ".xml");
			String xmlComponent = xmlFromComponent(componentEntry.getValue());
			Files.write(fComponent.toPath(), xmlComponent.getBytes());
		}

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
		for (Entity entity : scene.getEntities()) {
			xml += "\t<entity name=\"" + entity.getName() + "\" template=\"" + entity.getTemplate()
					+ "\" z-index=\"1\">\n";
			for(Component component : entity.getComponents().values())
			{
				xml += "\t\t<component template=\"" + component.getTemplate() + "\">\n";
				for(Field field : component.getFields().values())
				{
					xml += "\t\t\t<field name=\"" + field.getName() + "\">" + field.getValue() + "</field>\n";
				}
				xml +="</component>\n";
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
		xml += "<entity-template name=\"" + entity.getName() + "\">\n";
		for(Component component : entity.getComponents().values())
		{
			xml += "\t<component template=\"" + component.getName() + "\">\n";
			for(Field field : component.getFields().values())
			{
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
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		xml += "<component name=\"" + component.getName() + "\">\n";
		for(Field field : component.getFields().values())
		{
			xml += "\t<field  type=\"" + field.getType() +"\" name=\"" + field.getName() + "\">" + field.getValue() + "</field>\n";
		}
		xml += "</component>";
		return xml;	
	}
}
