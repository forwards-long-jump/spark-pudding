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

	public SEComponent(Component gameComponent) {
		this.gameComponent = gameComponent;
	}
}
