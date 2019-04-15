package ch.sparkpudding.sceneeditor.ecs;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import ch.sparkpudding.coreengine.ecs.Component;
import ch.sparkpudding.coreengine.ecs.Field;

/**
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba<br/>
 *         Creation Date : 15 avr. 2019
 * 
 *         Allow to track a game component from the SceneEditor and add the
 *         specific attributes
 *
 */
public class SEComponent implements Iterable<Entry<String, Field>> {

	private Component gameComponent;
	private Map<String, Field> fields;

	/**
	 * Ctor create a SEComponent and copy it fields to keep initial values
	 * 
	 * @param gameComponent the component link to this SceneEditor Component
	 */
	public SEComponent(Component gameComponent) {

		this.gameComponent = gameComponent;

		for (Entry<String, Field> entry : this.gameComponent) {
			fields.put(entry.getKey(), new Field(entry.getValue()));
		}

	}

	/**
	 * Getter for gameComponent
	 * 
	 * @return the gameComponent attached to this
	 */
	public Component getGameComponent() {
		return gameComponent;
	}

	/**
	 * Getter for fields
	 * 
	 * @return the fields map of this component (initial values)
	 */
	public Map<String, Field> getFields() {
		return fields;
	}

	@Override
	public Iterator<Entry<String, Field>> iterator() {
		return fields.entrySet().iterator();
	}
}
