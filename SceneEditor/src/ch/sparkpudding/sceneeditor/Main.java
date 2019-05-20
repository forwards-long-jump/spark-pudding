package ch.sparkpudding.sceneeditor;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import ch.sparkpudding.coreengine.CoreEngine;
import ch.sparkpudding.coreengine.Scheduler.Trigger;
import ch.sparkpudding.coreengine.utils.RunnableOneParameter;
import ch.sparkpudding.sceneeditor.SceneEditor.EditorState;
import ch.sparkpudding.sceneeditor.filewriter.LelWriter;

/**
 *
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba
 *
 */
public class Main {

	@SuppressWarnings("unused")
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
		while (true) {
			if (args.length > 0) {
				gamePath = args[0];
			} else {
				JFileChooser fileChooser = new JFileChooser();
				LelWriter lel = new LelWriter();
				// TODO :When passing to .lel files, uncomment the next two lines and remove the
				// next uncommented line.
				// fc.setAcceptAllFileFilterUsed(false);
				// fc.addChoosableFileFilter(FileChooserUtils.getFilter());
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

				if (1 == 1) {
					int returnVal = fileChooser.showOpenDialog(null);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						gamePath = fileChooser.getSelectedFile().getAbsolutePath();
					} else
						return;
				} else {
					int returnVal = fileChooser.showSaveDialog(SceneEditor.frameSceneEditor);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						gamePath = fileChooser.getSelectedFile().getAbsolutePath();
					} else
						return;
					lel.create(gamePath + "/");
				}

				

			}
			try {
				SceneEditor.coreEngine = new CoreEngine(gamePath, Main.class.getResource("/leleditor").getPath());
				break;
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "The selected file is not valid");
				if (args.length > 0)
					return;
				e.printStackTrace();
			}

		}
		SceneEditor.gamePath = gamePath;

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
