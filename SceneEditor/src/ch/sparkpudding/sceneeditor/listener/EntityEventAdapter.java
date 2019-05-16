package ch.sparkpudding.sceneeditor.listener;

import java.util.Map;

import ch.sparkpudding.sceneeditor.ecs.SEEntity;
import ch.sparkpudding.sceneeditor.ecs.SEScene;

/**
 * Adapter for the entity event listener
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba<br/>
 *         Creation Date : 16 May 2019
 *
 */
public abstract class EntityEventAdapter implements EntityEventListener {

	@Override
	public void changeSelectedEntity(SEEntity entity) {
	}

	@Override
	public void entityListChanged(Map<String, SEScene> seScenes) {
	}

}
