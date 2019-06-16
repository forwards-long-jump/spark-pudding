package ch.sparkpudding.sceneeditor.ecs;

import java.util.ArrayList;
import java.util.List;

import ch.sparkpudding.coreengine.ecs.entity.Entity;
import ch.sparkpudding.coreengine.ecs.entity.Scene;
import ch.sparkpudding.sceneeditor.SceneEditor;
import ch.sparkpudding.sceneeditor.SceneEditor.EditorState;

/**
 * Allow to track a game scene from the SceneEditor and duplicate it with it
 * default values
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba<br/>
 *         Creation Date : 7 May 2019
 *
 */
public class SEScene {

	private Scene liveScene;
	private List<SEEntity> seEntities;

	/**
	 * Create a SEScene and populate the list of entities (default and live) as
	 * SEEntity
	 * 
	 * @param liveScene the Scene represented by this SEScene
	 */
	public SEScene(Scene liveScene) {
		this.liveScene = liveScene;
		this.seEntities = new ArrayList<SEEntity>();

		populateSEEntities();
	}

	/**
	 * Populate the list of entities (default and live) as SEEntity
	 */
	public void populateSEEntities() {
		List<Entity> defaultEntities = liveScene.getDefaultEntities();
		List<Entity> liveEntities = liveScene.getEntities();

		if (SceneEditor.getGameState() != EditorState.STOP) {
			for (int i = 0; i < Math.min(defaultEntities.size(), liveEntities.size()); i++) {
				seEntities.add(new SEEntity(defaultEntities.get(i), liveEntities.get(i)));
			}
		} else {
			for (int i = 0; i < defaultEntities.size(); i++) {
				seEntities.add(new SEEntity(defaultEntities.get(i), defaultEntities.get(i)));
			}
		}
	}

	/**
	 * Get all the seEntities contains in a SEScene
	 * 
	 * @return all the SEEntities
	 */
	public List<SEEntity> getSEEntities() {
		return this.seEntities;
	}

	/**
	 * Get the live Scene linked to this SEScene
	 * 
	 * @return the live Scene
	 */
	public Scene getLiveScene() {
		return this.liveScene;
	}

}
