package ch.sparkpudding.coreengine.api;

import org.luaj.vm2.LuaValue;

import ch.sparkpudding.coreengine.Lel;
import ch.sparkpudding.coreengine.Scheduler.Trigger;
import ch.sparkpudding.coreengine.ecs.component.Component;
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
		Lel.coreEngine.getScheduler().schedule(Trigger.GAME_LOOP_START, new Runnable() {

			@Override
			public void run() {
				Lel.coreEngine.deleteEntity(entity);
			}
		});
	}

	/**
	 * Sets the entity to have its specified component remove after the current
	 * update
	 * 
	 * @param componentName to delete
	 */
	public void deleteComponent(String componentName) {
		Lel.coreEngine.getScheduler().schedule(Trigger.GAME_LOOP_START, new Runnable() {

			@Override
			public void run() {
				Lel.coreEngine.removeComponent(entity, componentName);
			}
		});
	}

	/**
	 * Adds the given component to the entity, and schedules the Core Engine to
	 * update the systems after the current update
	 * 
	 * @param componentName the name of the component to add
	 * @return LuaValue the component that was added
	 */
	public LuaValue addComponent(String componentName) {
		Component component = new Component(Component.getTemplates().get(componentName));
		Lel.coreEngine.getScheduler().schedule(Trigger.GAME_LOOP_START, new Runnable() {

			@Override
			public void run() {
				if (!entity.hasComponent(componentName)) {
					entity.add(component);
					Lel.coreEngine.notifySystemsOfNewComponent(entity, component);
				}
			}
		});

		return component.coerceToLua();
	}
}
