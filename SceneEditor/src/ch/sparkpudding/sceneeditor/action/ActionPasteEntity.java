package ch.sparkpudding.sceneeditor.action;

import java.awt.geom.Point2D;

import javax.swing.SwingUtilities;

import ch.sparkpudding.coreengine.Scheduler.Trigger;
import ch.sparkpudding.coreengine.ecs.component.Component;
import ch.sparkpudding.coreengine.ecs.entity.Entity;
import ch.sparkpudding.sceneeditor.SceneEditor;
import ch.sparkpudding.sceneeditor.ecs.SEEntity;

/**
 * The action to register the pasting of a new entity
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba<br/>
 *         Creation Date : 20 May 2019
 *
 */
@SuppressWarnings("serial")
public class ActionPasteEntity extends AbstractAction {

	private Entity newDefaultEntity;
	private Entity newLiveEntity;

	/**
	 * ctor
	 * 
	 * @param entity The entity to paste
	 */
	public ActionPasteEntity(SEEntity entity) {
		super("Paste entity (" + entity.getLiveEntity().getName() + ")");
		this.newDefaultEntity = new Entity(entity.getLiveEntity());
		this.newDefaultEntity.removeComponent("se-selected");

		if (this.newDefaultEntity.hasComponent("position") && this.newDefaultEntity.hasComponent("size")) {
			Component size = this.newDefaultEntity.getComponents().get("size");

			double width = size.getField("width").getDouble();
			double height = size.getField("height").getDouble();
			Point2D worldMousePosition = SceneEditor.coreEngine
					.panelPositionToWorld(SceneEditor.coreEngine.getInput().getMousePosition());

			this.newDefaultEntity.getComponents().get("position").getField("x").setValue(worldMousePosition.getX() - width / 2);
			this.newDefaultEntity.getComponents().get("position").getField("y").setValue(worldMousePosition.getY() - height / 2);
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
						SceneEditor.coreEngine.getScheduler().schedule(Trigger.GAME_LOOP_START, new Runnable() {

							@Override
							public void run() {
								for (SEEntity seEntity : SceneEditor.currentScene.getSEEntities()) {
									if (seEntity.getLiveEntity() == newLiveEntity) {
										seEntity.setSelected(true);
										break;
									}
								}
							}

						});
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
