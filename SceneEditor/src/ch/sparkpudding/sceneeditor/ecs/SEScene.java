package ch.sparkpudding.sceneeditor.ecs;

import java.util.ArrayList;
import java.util.List;

import ch.sparkpudding.coreengine.ecs.entity.Entity;
import ch.sparkpudding.coreengine.ecs.entity.Scene;

/**
 * Allow to track a game scene from the SceneEditor and duplicate it with it
 * default values
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba<br/>
 *         Creation Date : 7 mai 2019
 *
 */
public class SEScene {

	private Scene liveScene;
	private List<SEEntity> seEntities;

	public SEScene(Scene liveScene) {
		this.liveScene = liveScene;
		this.seEntities = new ArrayList<SEEntity>();
		
		populateScene();
	}
	
	public void populateScene() {
		List<Entity> defaultEntities = liveScene.getDefaultEntities();
		List<Entity> liveEntities = liveScene.getEntities();
		for (int i = 0; i < defaultEntities.size(); i++) {
			seEntities.add(new SEEntity(defaultEntities.get(i), liveEntities.get(i)));
		}
	}
	
	public List<SEEntity> getSEEntities()
	{
		return this.seEntities;
	}
	
	public Scene getLiveScene()
	{
		return this.liveScene;
	}

}
