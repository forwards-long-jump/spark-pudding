package ch.sparkpudding.coreengine.api;

import ch.sparkpudding.coreengine.Lel;
import ch.sparkpudding.coreengine.ecs.entity.Entity;

/**
 * API which serves to give meta access to entities from lua systems.
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba
 * 
 */
public class MetaEntity {
	private Entity entity;

	/**
	 * ctor
	 * 
	 * @param entity
	 */
	public MetaEntity(Entity entity) {
		this.entity = entity;
	}

	/**
	 * Sets the caller entity to be deleted at the end of the update
	 */
	public void delete() {
		Lel.coreEngine.deleteEntityAfterUpdate(entity);
	}
}
