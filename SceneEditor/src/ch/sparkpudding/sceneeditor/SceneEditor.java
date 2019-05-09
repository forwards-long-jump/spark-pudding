package ch.sparkpudding.sceneeditor;

import java.util.ArrayList;
import java.util.List;
import ch.sparkpudding.coreengine.CoreEngine;
import ch.sparkpudding.sceneeditor.listener.GameStateEventListener;

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

	private static List<GameStateEventListener> eventListeners;
	private static EDITOR_STATE gameState;

	static {
		gameState = EDITOR_STATE.PAUSE;

		try {
			coreEngine = new CoreEngine(Main.class.getResource("/emptygame").getPath());
		} catch (Exception e) {
			e.printStackTrace();
		}

		eventListeners = new ArrayList<GameStateEventListener>();
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
		fireGameStateEvent();
	}
	/**
	 * Add a listener for the event of the state of the SceneEditor
	 * 
	 * @param evtListener the listener
	 */
	public static void addGameStateEventListener(GameStateEventListener evtListener) {
		eventListeners.add(evtListener);
	}

	/**
	 * Remove a listener for the event of the state of the SceneEditor
	 * 
	 * @param evtListener the listener to remove
	 * @return {@code true} if the event exist
	 */
	public static boolean removeGameStateEventListener(GameStateEventListener evtListener) {
		return eventListeners.remove(evtListener);
	}

	/**
	 * Remove all the event of the SceneEditor
	 */
	public static void removeAllGameStateEventListener() {
		eventListeners.clear();
	}

	/**
	 * Allow to fire the gameState event of the listeners
	 */
	private static void fireGameStateEvent() {
		for (GameStateEventListener gameStateEventListener : eventListeners) {
			gameStateEventListener.gameStateEvent(gameState);
		}
	}
}