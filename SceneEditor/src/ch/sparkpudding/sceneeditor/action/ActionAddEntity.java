package ch.sparkpudding.sceneeditor.action;

import javax.swing.SwingUtilities;

import ch.sparkpudding.coreengine.Scheduler.Trigger;
import ch.sparkpudding.coreengine.ecs.entity.Entity;
import ch.sparkpudding.sceneeditor.SceneEditor;

/**
 * The action to register the addition of a new entity
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba<br/>
 *         Creation Date : 20 May 2019
 *
 */
@SuppressWarnings("serial")
public class ActionAddEntity extends AbstractAction {

	private Entity newDefaultEntity;
	private Entity newLiveEntity;

	/**
	 * ctor
	 * 
	 * @param name         The name of the entity
	 * @param templateName The name of the template to create the entity
	 */
	public ActionAddEntity(String name, String templateName) {
		super("Add entity (" + name + ")");
		this.newDefaultEntity = new Entity(Entity.getTemplates().get(templateName));
		this.newDefaultEntity.setName(name);
		this.newLiveEntity = new Entity(this.newDefaultEntity);
	}

	@Override
	public boolean doAction() {
		SceneEditor.coreEngine.getScheduler().schedule(Trigger.GAME_LOOP_START, new Runnable() {

			@Override
			public void run() {
				SceneEditor.coreEngine.addEntity(newLiveEntity);
				SceneEditor.coreEngine.getCurrentScene().addDefault(newDefaultEntity);
				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						SceneEditor.createEntityList();
					}
				});
			}
		});
		return true;
	}

	@Override
	public void undoAction() {
		SceneEditor.coreEngine.getScheduler().schedule(Trigger.GAME_LOOP_START, new Runnable() {

			@Override
			public void run() {
				SceneEditor.coreEngine.deleteDefaultEntity(newDefaultEntity);
				SceneEditor.coreEngine.deleteEntity(newLiveEntity);

				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						SceneEditor.createEntityList();
					}
				});
			}
		});
	}

}
