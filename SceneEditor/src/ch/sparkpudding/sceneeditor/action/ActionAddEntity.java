package ch.sparkpudding.sceneeditor.action;

import javax.swing.SwingUtilities;

import ch.sparkpudding.coreengine.Camera;
import ch.sparkpudding.coreengine.Scheduler.Trigger;
import ch.sparkpudding.coreengine.ecs.component.Component;
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
		
		if (this.newDefaultEntity.hasComponent("position") && this.newDefaultEntity.hasComponent("size")) {
			Camera camera = SceneEditor.coreEngine.getCamera();
			Component size = this.newDefaultEntity.getComponents().get("size");

			double width = size.getField("width").getDouble();
			double height = size.getField("height").getDouble();

			this.newDefaultEntity.getComponents().get("position").getField("x")
					.setValue((camera.getPosition().getX() + SceneEditor.coreEngine.getGameWidth() / 2 - width / 2)
							/ camera.getScaling());
			this.newDefaultEntity.getComponents().get("position").getField("y")
					.setValue((camera.getPosition().getY() + SceneEditor.coreEngine.getGameHeight() / 2 - height / 2)
							/ camera.getScaling());
		}

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
