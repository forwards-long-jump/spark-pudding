package ch.sparkpudding.sceneeditor.action;

import ch.sparkpudding.coreengine.Scheduler.Trigger;
import ch.sparkpudding.coreengine.ecs.entity.Scene;
import ch.sparkpudding.sceneeditor.SceneEditor;

/**
 * The action to register the removal of a scene
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba<br/>
 *         Creation Date : 16 May 2019
 *
 */
@SuppressWarnings("serial")
public class ActionRemoveScene extends AbstractAction {

	private Scene scene;

	/**
	 * ctr
	 * 
	 * @param scene scene to remove
	 */
	public ActionRemoveScene(String name) {
		super("Remove scene " + name);
		this.scene = SceneEditor.seScenes.get(name).getLiveScene();
	}

	/**
	 * Remove scene from the game's scenes
	 */
	@Override
	public boolean doAction() {
		// We disallow deleting the entry scene
		if (scene.getName().equals("main")) {
			return false;
		}
		SceneEditor.coreEngine.getScheduler().schedule(Trigger.GAME_LOOP_START, new Runnable() {

			@Override
			public void run() {
				SceneEditor.coreEngine.setScene("main");
				SceneEditor.coreEngine.deleteScene(scene.getName());
			}
		});
		return true;
	}

	/**
	 * Re-adds the scene to the game's scenes
	 */
	@Override
	public void undoAction() {
		SceneEditor.coreEngine.getScheduler().schedule(Trigger.GAME_LOOP_START, new Runnable() {

			@Override
			public void run() {
				SceneEditor.coreEngine.addScene(scene.getName(), scene);
				SceneEditor.coreEngine.setCurrentScene(scene);
			}
		});
	}

}
