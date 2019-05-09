package ch.sparkpudding.sceneeditor;

import ch.sparkpudding.coreengine.Camera;
import ch.sparkpudding.coreengine.Camera.Mode;
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
					swapToSceneEditorCamera();
				}
			});

			break;
		case PLAY:
			coreEngine.getScheduler().schedule(Trigger.BEFORE_UPDATE, new Runnable() {
				@Override
				public void run() {
					SceneEditor.coreEngine.setEditingPause(false);
					swapToGameCamera();
				}
			});
			break;
		case STOP:
			coreEngine.getScheduler().schedule(Trigger.AFTER_UPDATE, new Runnable() {
				@Override
				public void run() {
					SceneEditor.coreEngine.resetCurrentScene();
					SceneEditor.coreEngine.setEditingPause(true);
					swapToSceneEditorCamera();
				}
			});

			break;
		default:
			break;

		}
		frameSceneEditor.getPanelSidebarLeft().notifyStateChange(state);
	}

	/**
	 * Swap game camera to use the one from the editor
	 */
	private static void swapToSceneEditorCamera() {
		gameCamera = SceneEditor.coreEngine.getCamera();

		// SMOOOOTH MC GROOOVE
		camera.setScaling(gameCamera.getScaling());
		camera.setTargetScaling(gameCamera.getScaling() * 0.9f);
		camera.setSmoothScaleSpeedCoeff(0.1f);
		camera.setScalingPoint(gameCamera.getScalingPoint());
		camera.setPosition(gameCamera.getPosition().getX(), gameCamera.getPosition().getY());
		camera.setTargetToPosition();
		
		SceneEditor.coreEngine.getCurrentScene().setCamera(camera);
	}
	
	/**
	 * Swap editor camera to use the one from the game
	 */
	private static void swapToGameCamera() {
		// We allow ourselves to touch the player camera if it's set to reset itself
		if(gameCamera.getTranslateMode() != Mode.INSTANT || gameCamera.getTranslateMode() != Mode.NO_FOLLOW) {
			gameCamera.setPosition(camera.getPosition().getX(), camera.getPosition().getY());
			gameCamera.setScaling(camera.getScaling());
		}
		SceneEditor.coreEngine.getCurrentScene().setCamera(gameCamera);
	}
}
