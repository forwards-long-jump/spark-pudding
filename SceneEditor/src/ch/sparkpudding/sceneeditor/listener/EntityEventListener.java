package ch.sparkpudding.sceneeditor.listener;

import java.util.EventListener;
import java.util.Map;

import ch.sparkpudding.sceneeditor.ecs.SEEntity;
import ch.sparkpudding.sceneeditor.ecs.SEScene;

/**
 * Interface to create EntityEventListener
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba<br/>
 *         Creation Date : 13 May 2019
 *
 */
public interface EntityEventListener extends EventListener {

	/**
	 * Event call when the selected entity change
	 * 
	 * @param state The current selected entity
	 */
	public void changeSelectedEntity(SEEntity entity);

	/**
	 * Event call when the selected entity change
	 * 
	 * @param state The current selected entity
	 */
	public void entityListChanged(Map<String, SEScene> seScenes);

}
