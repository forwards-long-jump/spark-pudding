package ch.sparkpudding.sceneeditor.action;

import javax.swing.SwingUtilities;

import ch.sparkpudding.coreengine.Scheduler.Trigger;
import ch.sparkpudding.coreengine.ecs.component.Component;
import ch.sparkpudding.coreengine.ecs.entity.Entity;
import ch.sparkpudding.sceneeditor.SceneEditor;

/**
 * Action to add a component
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba<br />
 *         Creation Date : 16 May 2019
 * 
 */
@SuppressWarnings("serial")
public class ActionAddComponent extends AbstractAction {
	Component component;
	Entity entity;

	/**
	 * ctor
	 * 
	 * @param name      of the action
	 * @param entity    affected by the component deletion
	 * @param component to delete
	 */
	public ActionAddComponent(String name, Entity entity, Component component) {
		super(name);
		this.component = component;
		this.entity = entity;
	}

	/**
	 * Add the component to the entity and notify CE
	 */
	@Override
	public boolean doAction() {
		SceneEditor.coreEngine.getScheduler().schedule(Trigger.GAME_LOOP_START, new Runnable() {

			@Override
			public void run() {
				entity.add(component);
				SceneEditor.coreEngine.notifySystemsOfNewComponent(entity, component);
				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						SceneEditor.fireSelectedEntityChanged();
					}
				});
			}
		});
		return true;
	}

	/**
	 * Remove the component from the entity and notify CE
	 */
	@Override
	public void undoAction() {
		SceneEditor.coreEngine.getScheduler().schedule(Trigger.GAME_LOOP_START, new Runnable() {

			@Override
			public void run() {
				SceneEditor.coreEngine.removeComponent(entity, component.getName());
				SwingUtilities.invokeLater(new Runnable() {
					
					@Override
					public void run() {						
						SceneEditor.fireSelectedEntityChanged();
					}
				});
			}
		});
	}
}
