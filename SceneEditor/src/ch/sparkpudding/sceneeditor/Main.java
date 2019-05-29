package ch.sparkpudding.sceneeditor;

import java.nio.file.Paths;
import java.util.Locale;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import ch.sparkpudding.coreengine.CoreEngine;
import ch.sparkpudding.coreengine.Scheduler.Trigger;
import ch.sparkpudding.coreengine.utils.RunnableOneParameter;
import ch.sparkpudding.sceneeditor.SceneEditor.EditorState;

/**
 *
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba
 *
 */
public class Main {

	public static void main(String[] args) {

		// Set the default locale to english because everything is in english
		Locale.setDefault(Locale.ENGLISH);

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedLookAndFeelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			SceneEditor.coreEngine = new CoreEngine(Paths.get(Main.class.getResource("/emptygame").toURI()).toString(),
					Paths.get(Main.class.getResource("/leleditor").toURI()).toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		// EDITING_STATE_CHANGED is called in GAME_LOOP_START so no need to add another
		// scheduling
		// adding a new scheduling would break the camera
		SceneEditor.coreEngine.getScheduler().notify(Trigger.EDITING_STATE_CHANGED, new RunnableOneParameter() {
			@Override
			public void run() {
				if (SceneEditor.coreEngine.isInError()) {
					SceneEditor.setGameState(EditorState.ERROR);
				} else if ((boolean) getObject()) {
					SceneEditor.setGameState(EditorState.PAUSE);
				} else {
					SceneEditor.setGameState(EditorState.PLAY);
				}
			}
		});

		SceneEditor.frameSceneEditor = new FrameSceneEditor();
	}

}
