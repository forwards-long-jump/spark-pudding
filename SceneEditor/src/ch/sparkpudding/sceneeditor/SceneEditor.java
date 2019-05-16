package ch.sparkpudding.sceneeditor;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import javax.swing.SwingUtilities;
import javax.swing.event.EventListenerList;

import ch.sparkpudding.coreengine.Camera;
import ch.sparkpudding.coreengine.Camera.Mode;
import ch.sparkpudding.coreengine.CoreEngine;
import ch.sparkpudding.coreengine.Scheduler.Trigger;
import ch.sparkpudding.coreengine.ecs.entity.Scene;
import ch.sparkpudding.sceneeditor.ecs.SEEntity;
import ch.sparkpudding.sceneeditor.ecs.SEScene;
import ch.sparkpudding.sceneeditor.listener.EntityEventListener;
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

	private static EventListenerList listenerList;

	private static Camera camera;
	private static Camera gameCamera;

	private static EditorState gameState;

	static {
		listenerList = new EventListenerList();
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
					coreEngine.setBlackBarsColor(new Color(0, 0, 0, 127));
					if (!coreEngine.isEditingPause()) {
						coreEngine.setEditingPause(true);
						swapToSceneEditorCamera();
					}
				}
			});

			break;
		case PLAY:
			coreEngine.getScheduler().schedule(Trigger.BEFORE_UPDATE, new Runnable() {
				@Override
				public void run() {
					coreEngine.setBlackBarsColor(new Color(0, 0, 0));
					if (coreEngine.isEditingPause()) {
						coreEngine.setEditingPause(false);
						swapToGameCamera();
					}
				}
			});
			break;
		case STOP:
			coreEngine.getScheduler().schedule(Trigger.AFTER_UPDATE, new Runnable() {
				@Override
				public void run() {
					coreEngine.setBlackBarsColor(new Color(0, 0, 0, 127));
					coreEngine.resetCurrentScene();
					createEntityList();
					if (!coreEngine.isEditingPause()) {
						coreEngine.setEditingPause(true);
						swapToSceneEditorCamera();
					}
				}
			});
			break;
		default:
			break;

		}
		
		SwingUtilities.invokeLater(new Runnable() {		
			@Override
			public void run() {
				fireGameStateChanged();
			}
		});
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

		SwingUtilities.invokeLater(new Runnable() {		
			@Override
			public void run() {
				fireEntityListChanged();
			}
		});
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
	public static void setSelectedEntity(SEEntity entity) {
		if (entity == selectedEntity) {
			return;
		}

		if (selectedEntity != null) {
			selectedEntity.setSelected(false);
		}

		selectedEntity = entity;
		entity.setSelected(true);

		fireSelectedEntityChanged();
	}

	/**
	 * Set current scene
	 *
	 * @param newScene the new current scene
	 */
	public static void setCurrentScene(SEScene newScene) {
		currentScene = newScene;
	}

	/**
	 * Add a listener for the event of the state of the SceneEditor
	 *
	 * @param evtListener the listener
	 */
	public static void addGameStateEventListener(GameStateEventListener evtListener) {
		listenerList.add(GameStateEventListener.class, evtListener);
	}

	/**
	 * Remove a listener for the event of the state of the SceneEditor
	 *
	 * @param evtListener the listener to remove
	 * @return {@code true} if the event exist
	 */
	public static void removeGameStateEventListener(GameStateEventListener evtListener) {
		listenerList.remove(GameStateEventListener.class, evtListener);
	}

	/**
	 * Allow to fire the gameState event of the listeners
	 */
	private static void fireGameStateChanged() {
		for (GameStateEventListener listener : listenerList.getListeners(GameStateEventListener.class)) {
			listener.gameStateChanged(gameState);
		}
	}

	/**
	 * Add an entity listener
	 *
	 * @param evtListener the listener
	 */
	public static void addEntityEventListener(EntityEventListener evtListener) {
		listenerList.add(EntityEventListener.class, evtListener);
	}

	/**
	 * Remove an entity listener
	 *
	 * @param evtListener the listener to remove
	 * @return {@code true} if the event exist
	 */
	public static void removeEntityEventListener(EntityEventListener evtListener) {
		listenerList.remove(EntityEventListener.class, evtListener);
	}

	/**
	 * Allow to fire an event when the selected entity change
	 */
	public static void fireSelectedEntityChanged() {
		for (EntityEventListener listener : listenerList.getListeners(EntityEventListener.class)) {
			listener.changeSelectedEntity(selectedEntity);
		}
	}
	
	/**
	 * Allow to fire an event when the selected entity change
	 */
	public static void fireEntityListChanged() {
		for (EntityEventListener listener : listenerList.getListeners(EntityEventListener.class)) {
			listener.entityListChanged(seScenes);
		}
	}
}
