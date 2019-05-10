package ch.sparkpudding.sceneeditor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.sparkpudding.coreengine.Camera;
import ch.sparkpudding.coreengine.Camera.Mode;
import ch.sparkpudding.coreengine.CoreEngine;
import ch.sparkpudding.coreengine.Scheduler.Trigger;
import ch.sparkpudding.coreengine.ecs.entity.Scene;
import ch.sparkpudding.sceneeditor.ecs.SEScene;
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
	public static Map<String, SEScene> seScenes;

	private static List<GameStateEventListener> eventListeners;

	private static Runnable callbackSyncListEntity;

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

		eventListeners = new ArrayList<GameStateEventListener>();

		seScenes = new HashMap<String, SEScene>();
		callbackSyncListEntity = createSyncListEntity();

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
		fireGameStateEvent();
	}

	/**
	 * Create the callback to register all entity of the coreEngine
	 * 
	 * @return the callback
	 */
	private static Runnable createSyncListEntity() {
		return new Runnable() {
			@Override
			public void run() {
				Map<String, Scene> scenes = coreEngine.getScenes();
				for (Scene scene : scenes.values()) {
					seScenes.put(scene.getName(), new SEScene(scene));
				}

				frameSceneEditor.populateSidebarRight();
			}
		};
	}

	/**
	 * Workaround to delay the first populating of a new FrameSceneEditor
	 */
	public static void firstPopulateNewProject() {
		coreEngine.getScheduler().schedule(Trigger.GAME_LOOP_START, callbackSyncListEntity);
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
		if (gameCamera.getTranslateMode() != Mode.INSTANT || gameCamera.getTranslateMode() != Mode.NO_FOLLOW) {
			gameCamera.setPosition(camera.getPosition().getX(), camera.getPosition().getY());
			gameCamera.setScaling(camera.getScaling());
		}
		SceneEditor.coreEngine.getCurrentScene().setCamera(gameCamera);
	}
}
