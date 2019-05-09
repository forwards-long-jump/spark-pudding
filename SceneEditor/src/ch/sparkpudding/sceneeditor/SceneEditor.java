package ch.sparkpudding.sceneeditor;

import ch.sparkpudding.coreengine.Camera;
import ch.sparkpudding.coreengine.CoreEngine;
import ch.sparkpudding.coreengine.Scheduler.Trigger;

/**
 * The heart of the SceneEditor, emerged after a 40 min fight
 *
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba
 *
 */
public class SceneEditor {
	public enum EDITOR_STATE {
		PLAY, PAUSE, STOP;
	}

	public static FrameSceneEditor frameSceneEditor;
	public static CoreEngine coreEngine;

	private static Camera camera;
	private static Camera gameCamera;

	private static EDITOR_STATE gameState;

	static {
		gameState = EDITOR_STATE.STOP;

		try {
			coreEngine = new CoreEngine(Main.class.getResource("/emptygame").getPath(),
					Main.class.getResource("/leleditor").getPath());
		} catch (Exception e) {
			e.printStackTrace();
		}

		coreEngine.getScheduler().schedule(Trigger.BEFORE_UPDATE, new Runnable() {
			@Override
			public void run() {
				setGameState(EDITOR_STATE.STOP);
			}
		});

		camera = new Camera();
	}

	/**
	 * Get current editor state
	 *
	 * @return current editor state
	 */
	public static EDITOR_STATE getGameState() {
		return gameState;
	}

	/**
	 * Change editor state
	 */
	public static void setGameState(EDITOR_STATE state) {
		gameState = state;
		switch (state) {
		case PAUSE:
			coreEngine.getScheduler().schedule(Trigger.AFTER_UPDATE, new Runnable() {
				@Override
				public void run() {
					SceneEditor.coreEngine.setEditingPause(true);
					gameCamera = SceneEditor.coreEngine.getCamera();
					SceneEditor.coreEngine.getCurrentScene().setCamera(camera);
				}
			});
			
			
			// TODO: Use this below for a smoother effect
			// camera.setScaling(gameCamera.getScaling());
			// camera.setPosition(gameCamera.getPosition().getX(),
			// gameCamera.getPosition().getY());
			
			break;
		case PLAY:
			coreEngine.getScheduler().schedule(Trigger.BEFORE_UPDATE, new Runnable() {
				@Override
				public void run() {
					SceneEditor.coreEngine.setEditingPause(false);
					SceneEditor.coreEngine.getCurrentScene().setCamera(gameCamera);
				}
			});
			break;
		case STOP:
			coreEngine.getScheduler().schedule(Trigger.AFTER_UPDATE, new Runnable() {
				@Override
				public void run() {
					SceneEditor.coreEngine.resetCurrentScene();
					SceneEditor.coreEngine.setEditingPause(true);
					gameCamera = SceneEditor.coreEngine.getCamera();
					SceneEditor.coreEngine.getCurrentScene().setCamera(camera);
				}
			});
			
			break;
		default:
			break;

		}
		frameSceneEditor.getPanelSidebarLeft().notifyStateChange(state);
	}
}
