package ch.sparkpudding.sceneeditor.ecs;

import ch.sparkpudding.coreengine.ecs.entity.Entity;

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

	/**
	 * Create a SEEntityLinker and link the two entity
	 * 
	 * @param defaultEntity The entity at a start of a scene
	 * @param liveEntity    The entity live link to the default one
	 */
	public SEEntity(Entity defaultEntity, Entity liveEntity) {

		this.defaultEntity = defaultEntity;
		this.liveEntity = liveEntity;

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
}
