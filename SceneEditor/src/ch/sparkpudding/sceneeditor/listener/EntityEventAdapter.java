package ch.sparkpudding.sceneeditor.listener;

import java.util.Map;

import ch.sparkpudding.sceneeditor.ecs.SEEntity;
import ch.sparkpudding.sceneeditor.ecs.SEScene;

public abstract class EntityEventAdapter implements EntityEventListener {

	@Override
	public void changeSelectedEntity(SEEntity entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void entityListChanged(Map<String, SEScene> seScenes) {
		// TODO Auto-generated method stub

	}

}
