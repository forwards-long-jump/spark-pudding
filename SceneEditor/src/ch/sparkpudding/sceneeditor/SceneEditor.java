package ch.sparkpudding.sceneeditor;

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

	private static EDITOR_STATE gameState;

	static {
		gameState = EDITOR_STATE.PAUSE;

		try {
			coreEngine = new CoreEngine(Main.class.getResource("/emptygame").getPath());
		} catch (Exception e) {
			e.printStackTrace();
		}

		coreEngine.togglePauseAll();
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
			SceneEditor.coreEngine.togglePauseAll();
			break;
		case PLAY:
			SceneEditor.coreEngine.togglePauseAll();
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