package ch.sparkpudding.sceneeditor;

import javax.swing.JFileChooser;
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
		String gamePath;
		if (args.length > 0) {
			gamePath = args[0];
		} else {
			JFileChooser fc = new JFileChooser();
			// TODO :When passing to .lel files, uncomment the next two lines and remove the
			// next uncommented line.
			// fc.setAcceptAllFileFilterUsed(false);
			// fc.addChoosableFileFilter(FileChooserUtils.getFilter());
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int returnVal = fc.showOpenDialog(null);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				gamePath = fc.getSelectedFile().getAbsolutePath();
			} else
				return;
		}

		try {
			SceneEditor.coreEngine = new CoreEngine(gamePath, Main.class.getResource("/leleditor").getPath());
		} catch (Exception e) {
			e.printStackTrace();
		}

		// EDITING_STATE_CHANGED is called in GAME_LOOP_START so no need to add another scheduling
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
