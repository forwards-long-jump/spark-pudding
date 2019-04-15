package ch.sparkpudding.sceneeditor.ecs;

import ch.sparkpudding.coreengine.ecs.Component;

/**
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba<br/>
 *         Creation Date : 15 avr. 2019
 * 
 *         Allow to track a game component from the SceneEditor and add the
 *         specific attributes
 *
 */
public class SEComponent {

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
	}
}
