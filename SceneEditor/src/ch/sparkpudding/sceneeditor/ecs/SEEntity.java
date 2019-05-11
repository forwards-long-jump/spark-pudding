package ch.sparkpudding.sceneeditor.ecs;

import ch.sparkpudding.coreengine.Scheduler.Trigger;
import ch.sparkpudding.coreengine.ecs.entity.Entity;
import ch.sparkpudding.sceneeditor.SceneEditor;

/**
 * Allow to track a game entity from the SceneEditor and duplicate it with it
 * default values
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba<br/>
 *         Creation Date : 15 April 2019
 * 
 */
public class SEEntity {

	private Entity defaultEntity;
	private Entity liveEntity;
	private boolean selected;

	/**
	 * Create a SEEntityLinker and link the two entity
	 * 
	 * @param defaultEntity The entity at a start of a scene
	 * @param liveEntity    The entity live link to the default one
	 */
	public SEEntity(Entity defaultEntity, Entity liveEntity) {

		this.defaultEntity = defaultEntity;
		this.liveEntity = liveEntity;

		this.selected = false;
	}

	/**
	 * Getter for defaultEntity
	 * 
	 * @return The default entity linked to it
	 */
	public Entity getDefaultEntity() {
		return defaultEntity;
	}

	/**
	 * Getter for liveEntity
	 * 
	 * @return The live entity linked to it
	 */
	public Entity getLiveEntity() {
		return liveEntity;
	}

	/**
	 * Get if entity is selected or not
	 * 
	 * @return if entity is selected or not
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * Set selected state
	 * 
	 * @param selected the state to set
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
		if (selected) {
			SceneEditor.coreEngine.getScheduler().schedule(Trigger.AFTER_UPDATE, new Runnable() {
				@Override
				public void run() {
					if (getLiveEntity().add("se-selected")) {
						SceneEditor.coreEngine.notifySystemsOfNewComponent(getLiveEntity(), "se-selected");
					}
				}
			});
		} else {
			SceneEditor.coreEngine.getScheduler().schedule(Trigger.AFTER_UPDATE, new Runnable() {
				@Override
				public void run() {
					SceneEditor.coreEngine.removeComponent(getLiveEntity(), "se-selected");
				}
			});
		}
	}
}
