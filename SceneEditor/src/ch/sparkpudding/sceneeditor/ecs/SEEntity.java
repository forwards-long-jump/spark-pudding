package ch.sparkpudding.sceneeditor.ecs;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import ch.sparkpudding.coreengine.ecs.Component;
import ch.sparkpudding.coreengine.ecs.Entity;

/**
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba<br/>
 *         Creation Date : 15 avr. 2019
 * 
 *         Allow to track a game entity from the SceneEditor and add the
 *         specific attributes
 * 
 */
public class SEEntity implements Iterable<Entry<String, SEComponent>> {

	private Entity gameEntity;
	private Map<String, SEComponent> components;

	/**
	 * Ctor create a SEEntity and it components as SEComponent
	 * 
	 * @param gameEntity the entity link to this SceneEditor Entity
	 */
	public SEEntity(Entity gameEntity) {

		this.gameEntity = gameEntity;

		for (Entry<String, Component> entry : this.gameEntity) {
			components.put(entry.getKey(), new SEComponent(entry.getValue()));
		}

	}

	/**
	 * Getter for gameEntity
	 * 
	 * @return the gameEntity attached to this
	 */
	public Entity getGameEntity() {
		return gameEntity;
	}

	/**
	 * Getter for components
	 * 
	 * @return the components map of this entity
	 */
	public Map<String, SEComponent> getComponents() {
		return components;
	}

	@Override
	public Iterator<Entry<String, SEComponent>> iterator() {
		return components.entrySet().iterator();
	}
}
