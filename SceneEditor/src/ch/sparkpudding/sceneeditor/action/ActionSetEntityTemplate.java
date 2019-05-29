package ch.sparkpudding.sceneeditor.action;

import java.util.Map.Entry;

import ch.sparkpudding.coreengine.ecs.component.Component;
import ch.sparkpudding.coreengine.ecs.entity.Entity;
import ch.sparkpudding.sceneeditor.SceneEditor;

/**
 * Action to create or change an entity template
 *
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba<br />
 *         Creation Date : 29 May 2019
 *
 */
@SuppressWarnings("serial")
public class ActionSetEntityTemplate extends AbstractAction {
	Entity newTemplateEntity;
	Entity previousTemplateEntity;
	String previousExistingTemplateEntityName;
	String newTemplateName;

	/**
	 * ctor
	 *
	 * @param entity to set as template
	 */
	public ActionSetEntityTemplate(Entity entity, String templateName) {
		super("Change/create template " + templateName);

		this.newTemplateName = templateName;
		this.newTemplateEntity = entity;
		this.previousExistingTemplateEntityName = entity.getTemplate();
		this.previousTemplateEntity = Entity.getTemplates().get(this.newTemplateName);
	}

	/**
	 * Change z-index of entity to specified value
	 */
	@Override
	public boolean doAction() {
		this.newTemplateEntity.setTemplate(this.newTemplateName);
		Entity.addTemplate(this.newTemplateEntity);

		for (Entry<String, Component> componentEntry : this.newTemplateEntity.getComponents().entrySet()) {
			componentEntry.getValue().setAttached(true, false);
		}

		SceneEditor.fireSelectedEntityChanged();
		// TODO: Reload game or apply templates everywhere
		return true;
	}

	/**
	 * Change zIndex back to its previous value
	 */
	@Override
	public void undoAction() {
		if (previousTemplateEntity == null) {
			newTemplateEntity.setTemplate(this.previousExistingTemplateEntityName);
			Entity.getTemplates().remove(newTemplateName);
		} else {
			this.newTemplateEntity.setTemplate(this.previousTemplateEntity.getTemplate());
			Entity.addTemplate(previousTemplateEntity);
		}

		SceneEditor.fireSelectedEntityChanged();
	}
}
