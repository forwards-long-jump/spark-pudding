package ch.sparkpudding.coreengine.api;

import ch.sparkpudding.coreengine.Lel;
import ch.sparkpudding.coreengine.ecs.entity.Entity;

public class MetaEntity {
	private Entity entity;

	public MetaEntity(Entity entity) {
		this.entity = entity;
	}

	public void delete() {
		Lel.coreEngine.deleteEntityAfterUpdate(entity);
	}
}
