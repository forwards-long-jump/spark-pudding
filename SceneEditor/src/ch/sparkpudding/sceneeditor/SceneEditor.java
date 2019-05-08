package ch.sparkpudding.sceneeditor;

import ch.sparkpudding.coreengine.Camera;
import ch.sparkpudding.coreengine.CoreEngine;

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
		gameState = EDITOR_STATE.PAUSE;

		try {
			coreEngine = new CoreEngine(Main.class.getResource("/emptygame").getPath(),
					Main.class.getResource("/leleditor").getPath());
		} catch (Exception e) {
			e.printStackTrace();
		}

		coreEngine.setEditingPause(true);
		camera = new Camera();
		gameCamera = coreEngine.getCamera();
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
			gameCamera = SceneEditor.coreEngine.getCamera();
			SceneEditor.coreEngine.setEditingPause(true);
			// TODO: Use this below for a smoother effect
			// camera.setScaling(gameCamera.getScaling());
			// camera.setPosition(gameCamera.getPosition().getX(),
			// gameCamera.getPosition().getY());
			SceneEditor.coreEngine.getCurrentScene().setCamera(camera);
			break;
		case PLAY:
			SceneEditor.coreEngine.getCurrentScene().setCamera(gameCamera);
			SceneEditor.coreEngine.setEditingPause(false);
			break;
		case STOP:
			SceneEditor.coreEngine.scheduleResetCurrentScene(true);
			break;
		default:
			break;

		}
		frameSceneEditor.getPanelSidebarLeft().notifyStateChange(state);
	}
}
