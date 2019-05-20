package ch.sparkpudding.sceneeditor.action;

import javax.swing.SwingUtilities;

import ch.sparkpudding.coreengine.Lel;
import ch.sparkpudding.coreengine.Scheduler.Trigger;
import ch.sparkpudding.coreengine.ecs.component.Component;
import ch.sparkpudding.coreengine.ecs.entity.Entity;
import ch.sparkpudding.sceneeditor.SceneEditor;

/**
 * Action to set a component
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba<br />
 *         Creation Date : 16 May 2019
 * 
 */
@SuppressWarnings("serial")
public class ActionSetComponent extends AbstractAction {
	Component component;
	Component previousComponent;
	Entity entity;

	/**
	 * ctor
	 * 
	 * @param name      of the action
	 * @param entity    to set the component
	 * @param component to set
	 */
	public ActionSetComponent(String name, Entity entity, Component component) {
		super(name);
		this.component = component;
		this.entity = entity;
	}

	/**
	 * Give the component to the entity - CE is not notified
	 */
	@Override
	public boolean doAction() {
		SceneEditor.coreEngine.getScheduler().schedule(Trigger.GAME_LOOP_START, new Runnable() {

			@Override
			public void run() {
				previousComponent = entity.getComponents().get(component.getName());
				entity.remove(component.getName());
				entity.add(new Component(component));
			}
		});
		return true;
	}

	/**
	 * Remove the component from the entity ang give it its  old version if any - CE is not notified
	 */
	@Override
	public void undoAction() {
		SceneEditor.coreEngine.getScheduler().schedule(Trigger.GAME_LOOP_START, new Runnable() {

			@Override
			public void run() {
				Lel.coreEngine.removeComponent(entity, component.getName());
				if (previousComponent != null) {
					entity.add(previousComponent);
					// This below *may* be useful but at the moment it will just spawn a lifeless
					// entity and we don't want to spawn defaults entities
					// Lel.coreEngine.notifySystemsOfNewComponent(entity, component);
				}
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
