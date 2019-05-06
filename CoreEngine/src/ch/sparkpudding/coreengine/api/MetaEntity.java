package ch.sparkpudding.coreengine.api;

import org.luaj.vm2.LuaTable;

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

	/**
	 * Sets the caller entity to have its component deleted at the end of the update
	 * Returns the LuaEntity of the calling entity,
	 * because Lua syntax for calling this is
	 * 
	 * <pre>
	 * entity = entity._meta:deleteComponent("componentName")
	 * </pre>
	 * @param componentName
	 */
	public LuaTable deleteComponent(String componentName) {
		Lel.coreEngine.removeComponentAfterUpdate(entity, componentName);
		return entity.getLuaEntity();
	}

	/**
	 * Adds the given component to the entity right away. Note that this operation
	 * must not be delayed at the end of the update because Lua writers may expect
	 * it to be added right away.
	 * Returns the LuaEntity of the calling entity,
	 * because Lua syntax for calling this is
	 * 
	 * <pre>
	 * entity = entity._meta:addComponent("componentName")
	 * </pre>
	 * 
	 * @param componentName
	 * @return LuaTable
	 */
	public LuaTable addComponent(String componentName) {
		entity.add(componentName);
		Lel.coreEngine.notifySystemsOfNewComponent(entity, componentName);
		return entity.getLuaEntity();
	}
}
