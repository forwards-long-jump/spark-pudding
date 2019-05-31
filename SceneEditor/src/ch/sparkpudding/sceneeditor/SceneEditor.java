package ch.sparkpudding.sceneeditor;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.SwingUtilities;
import javax.swing.event.EventListenerList;

import ch.sparkpudding.coreengine.Camera;
import ch.sparkpudding.coreengine.Camera.Mode;
import ch.sparkpudding.coreengine.CoreEngine;
import ch.sparkpudding.coreengine.ecs.entity.Scene;
import ch.sparkpudding.sceneeditor.ecs.SEEntity;
import ch.sparkpudding.sceneeditor.ecs.SEScene;
import ch.sparkpudding.sceneeditor.listener.EntityEventListener;
import ch.sparkpudding.sceneeditor.listener.GameStateEventListener;
import ch.sparkpudding.sceneeditor.listener.SystemEventListener;

/**
 * The heart of the SceneEditor, emerged after a 40 min fight
 *
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba
 *
 */
public class SceneEditor {
	public enum EditorState {
		PLAY, PAUSE, STOP, ERROR;
	}

	public static FrameSceneEditor frameSceneEditor;
	public static CoreEngine coreEngine;
	public static String gamePath;
	public static Map<String, SEScene> seScenes;
	public static SEScene currentScene;
	public static SEEntity selectedEntity;
	public static SEEntity clipboardEntity;

	private static EventListenerList listenerList;

	private static Camera camera;
	private static Camera gameCamera;

	private static EditorState gameState;
	private static EditorState previousState;

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
	 * Clear core engine error and restore previous state
	 */
	public static void clearError() {
		if (coreEngine.isInError()) {

			coreEngine.clearError();
			if (previousState != null) {
				setGameState(previousState);
			} else {
				setGameState(EditorState.PAUSE);
			}
		}
	}

	/**
	 * Change editor state This MUST be called in sync with something done in
	 * GAME_LOOP_START
	 */
	public static void setGameState(EditorState state) {
		if (gameState != EditorState.ERROR) {
			previousState = gameState;
		}

		gameState = state;
		switch (state) {
		case PAUSE:
			swapToSceneEditorCamera();
			coreEngine.setEditingPause(true, true);
			break;
		case PLAY:
			if (previousState == EditorState.STOP) {
				coreEngine.resetCurrentScene(false);
				createEntityList();
			}

			swapToGameCamera();
			coreEngine.setEditingPause(false, true);
			break;
		case STOP:
			swapToSceneEditorCamera();
			coreEngine.resetCurrentScene(true);

			createEntityList();
			coreEngine.setEditingPause(true, true);

			if (coreEngine.isInError()) {
				setGameState(EditorState.ERROR);
			}

			break;
		case ERROR:
			swapToSceneEditorCamera();
			createEntityList();
			coreEngine.setEditingPause(true, true);
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
	public static void createEntityList() {
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
		// No change if we are already paused
		if (coreEngine.isEditingPause()) {
			return;
		}

		// Change black bar color to indicate scene camera
		coreEngine.setBlackBarsColor(new Color(0, 0, 0, 127));

		gameCamera = coreEngine.getCamera();

		// SMOOOOTH MC GROOOVE
		camera.setTranslateMode(Mode.NO_FOLLOW);
		camera.setPosition(gameCamera.getPosition().getX(), gameCamera.getPosition().getY());
		camera.setTargetToPosition();
		camera.setScalingPoint(new Point2D.Double(coreEngine.getGameWidth() / 2, coreEngine.getGameHeight() / 2));
		camera.setSmoothScaleSpeedCoeff(0.1f);
		camera.setScaling(gameCamera.getScaling());
		camera.setTargetScaling(gameCamera.getScaling() * 0.9f);

		coreEngine.getCurrentScene().setCamera(camera);
	}

	/**
	 * Swap editor camera to use the one from the game
	 */
	private static void swapToGameCamera() {
		// Something went wrong with the game too early and we don't have the game
		// camera yet
		if (gameCamera == null) {
			return;
		}

		// No change if we are already playing
		if (!coreEngine.isEditingPause()) {
			return;
		}

		// Restore black bar for game camera
		coreEngine.setBlackBarsColor(new Color(0, 0, 0));

		// We allow ourselves to touch the player camera if it's set to reset itself
		if (gameCamera.getTranslateMode() != Mode.INSTANT || gameCamera.getTranslateMode() != Mode.NO_FOLLOW) {
			gameCamera.setScaling(camera.getScaling());
			gameCamera.setPosition(camera.getPosition().getX(), camera.getPosition().getY());
		}
		SceneEditor.coreEngine.getCurrentScene().setCamera(gameCamera);
	}

	/**
	 * Select an entity in game
	 *
	 * @param entity to set as selected
	 */
	public static void setSelectedEntity(SEEntity entity) {
		if (selectedEntity != null) {
			if (entity.getLiveEntity() == selectedEntity.getLiveEntity()) {
				return;
			}

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
	 * Set current scene by name
	 * 
	 * @param newSceneName name of the new current scene
	 */
	public static void setCurrentScene(String newSceneName) {
		currentScene = seScenes.get(newSceneName);
	}

	/**
	 * Loads and unloads scenes which differ from those of the Core Engine
	 * 
	 * To be called whenever the scenes list of Core Engine changes
	 */
	public static void updateSeSceneList() {
		// Add missing scenes
		for (Scene scene : coreEngine.getScenes().values()) {
			if (!seScenes.containsKey(scene.getName())) {
				seScenes.put(scene.getName(), new SEScene(scene));
			}
		}

		// Remove excedent scenes
		Iterator<String> it = seScenes.keySet().iterator();
		while (it.hasNext()) {
			if (!coreEngine.getScenes().containsKey(it.next())) {
				it.remove();
			}
		}
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
	 * Allow to fire an event when the entity list change
	 */
	public static void fireEntityListChanged() {
		for (EntityEventListener listener : listenerList.getListeners(EntityEventListener.class)) {
			listener.entityListChanged(seScenes);
		}
	}

	/**
	 * Add a system listener
	 *
	 * @param evtListener the listener
	 */
	public static void addSystemEventListener(SystemEventListener evtListener) {
		listenerList.add(SystemEventListener.class, evtListener);
	}

	/**
	 * Remove a system listener
	 *
	 * @param evtListener the listener to remove
	 * @return {@code true} if the event exist
	 */
	public static void removeSystemEventListener(SystemEventListener evtListener) {
		listenerList.remove(SystemEventListener.class, evtListener);
	}

	/**
	 * Allow to fire an event when the system list change
	 */
	public static void fireSystemListChanged() {
		for (SystemEventListener listener : listenerList.getListeners(SystemEventListener.class)) {
			listener.systemListChanged();
		}
	}

	/**
	 * Get the editor camera
	 * 
	 * @return editing camera
	 */
	public static Camera getEditingCamera() {
		return camera;
	}

	/**
	 * Get game camera
	 * 
	 * @return game camera
	 */
	public static Camera getGameCamera() {
		return gameCamera;
	}
}
