package ch.sparkpudding.sceneeditor;

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
			SceneEditor.coreEngine = new CoreEngine(Main.class.getResource("/emptygame").getPath(),
					Main.class.getResource("/leleditor").getPath());
		} catch (Exception e) {
			e.printStackTrace();
		}

		SceneEditor.coreEngine.getScheduler().notify(Trigger.EDITING_STATE_CHANGED, new RunnableOneParameter() {
			@Override
			public void run() {
				if ((boolean) getObject()) {
					// TODO: Handle error in a more nice way (though this could be fixed if changing
					// initial is done instantly)
					if (SceneEditor.getGameState() != EditorState.STOP) {
						SceneEditor.setGameState(EditorState.PAUSE);
					}
				} else {
					SceneEditor.setGameState(EditorState.PLAY);
				}
			}
		});

		SceneEditor.frameSceneEditor = new FrameSceneEditor();
	}

}
