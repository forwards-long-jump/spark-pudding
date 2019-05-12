package ch.sparkpudding.sceneeditor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.sparkpudding.coreengine.Camera;
import ch.sparkpudding.coreengine.Camera.Mode;
import ch.sparkpudding.coreengine.CoreEngine;
import ch.sparkpudding.coreengine.Scheduler.Trigger;
import ch.sparkpudding.coreengine.ecs.entity.Entity;
import ch.sparkpudding.coreengine.ecs.entity.Scene;
import ch.sparkpudding.sceneeditor.ecs.SEEntity;
import ch.sparkpudding.sceneeditor.ecs.SEScene;
import ch.sparkpudding.sceneeditor.listener.GameStateEventListener;

/**
 * The heart of the SceneEditor, emerged after a 40 min fight
 *
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba
 *
 */
public class SceneEditor {
	public enum EditorState {
		PLAY, PAUSE, STOP;
	}

	public static FrameSceneEditor frameSceneEditor;
	public static CoreEngine coreEngine;
	public static Map<String, SEScene> seScenes;
	public static SEScene currentScene;
	public static SEEntity selectedEntity;

	private static List<GameStateEventListener> eventListeners;

	private static Camera camera;
	private static Camera gameCamera;

	private static EditorState gameState;

	static {
		try {
			coreEngine = new CoreEngine(Main.class.getResource("/emptygame").getPath(),
					Main.class.getResource("/leleditor").getPath());
		} catch (Exception e) {
			e.printStackTrace();
		}

		coreEngine.getScheduler().notify(Trigger.COMPONENT_ADDED, new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < coreEngine.getCurrentScene().getEntities().size(); i++) {
					Entity entity = coreEngine.getCurrentScene().getEntities().get(i);
					if (entity.hasComponent("se-selected")) {
						for (SEEntity seEntity : currentScene.getSEEntities()) {
							if (seEntity.getLiveEntity() == entity) {
								SceneEditor.frameSceneEditor.getPanelSidebarRight().getPanelEntityTree()
										.selectSEEntity(seEntity);
								return;
							}
						}
					}
				}
			}
		});

		eventListeners = new ArrayList<GameStateEventListener>();

		seScenes = new HashMap<String, SEScene>();

		camera = new Camera();

	}

	/**
	 * Get current editor state
	 *
	 * @return current editor state
	 */
	public static EditorState getGameState() {
		return gameState;
	}

	/**
	 * Change editor state
	 */
	public static void setGameState(EditorState state) {
		gameState = state;
		switch (state) {
		case PAUSE:
			coreEngine.getScheduler().schedule(Trigger.AFTER_UPDATE, new Runnable() {
				@Override
				public void run() {
					if (!SceneEditor.coreEngine.isEditingPause()) {
						SceneEditor.coreEngine.setEditingPause(true);
						swapToSceneEditorCamera();
					}
				}
			});

			break;
		case PLAY:
			coreEngine.getScheduler().schedule(Trigger.BEFORE_UPDATE, new Runnable() {
				@Override
				public void run() {
					if (SceneEditor.coreEngine.isEditingPause()) {
						SceneEditor.coreEngine.setEditingPause(false);
						swapToGameCamera();
					}
				}
			});
			break;
		case STOP:
			coreEngine.getScheduler().schedule(Trigger.AFTER_UPDATE, new Runnable() {
				@Override
				public void run() {
					SceneEditor.coreEngine.resetCurrentScene();
					createEntityList();
					if (!SceneEditor.coreEngine.isEditingPause()) {
						SceneEditor.coreEngine.setEditingPause(true);
						swapToSceneEditorCamera();
					}
				}
			});
			break;
		default:
			break;

		}
		fireGameStateEvent();
	}

	/**
	 * Create the entity list for all scenes and update the display must be called
	 * in sync in the core engine
	 */
	private static void createEntityList() {
		Map<String, Scene> scenes = coreEngine.getScenes();
		seScenes.clear();
		for (Scene scene : scenes.values()) {
			SEScene seScene = new SEScene(scene);
			seScenes.put(scene.getName(), seScene);

			if (coreEngine.getCurrentScene() == scene) {
				currentScene = seScene;
			}
		}

		frameSceneEditor.populateSidebarRight();
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

	/**
	 * Select an entity in game
	 *
	 * @param entity to set as selected
	 */
	public static void selectEntity(SEEntity entity) {
		if (entity == selectedEntity) {
			return;
		}

		if (selectedEntity != null) {
			selectedEntity.setSelected(false);
		}

		selectedEntity = entity;
		entity.setSelected(true);
		SceneEditor.frameSceneEditor.getPanelSidebarRight().getPanelEntity().setEntity(entity);
	}

	/**
	 * Set current scene
	 *
	 * @param newScene the new current scene
	 */
	public static void setCurrentScene(SEScene newScene) {
		currentScene = newScene;
	}
}
