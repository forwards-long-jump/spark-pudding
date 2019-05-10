package ch.sparkpudding.sceneeditor.listener;

import java.util.EventListener;

import ch.sparkpudding.sceneeditor.SceneEditor;

/**
 * Interface to create GameStateEventListener
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba<br/>
 *         Creation Date : 9 May 2019
 *
 */
public interface GameStateEventListener extends EventListener {

	/**
	 * Event call when the state of the game change
	 * 
	 * @param state The current state of the game
	 */
	public void gameStateEvent(SceneEditor.EDITOR_STATE state);

}
