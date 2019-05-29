package ch.sparkpudding.sceneeditor.action;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.SwingUtilities;

import ch.sparkpudding.coreengine.Scheduler.Trigger;
import ch.sparkpudding.sceneeditor.SceneEditor;

/**
 * The action to register the removal of a system
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba<br/>
 *         Creation Date : 28 May 2019
 *
 */
@SuppressWarnings("serial")
public class ActionRemoveSystem extends AbstractAction {

	private String name;
	private byte[] fileContent;

	/**
	 * ctor
	 * 
	 * @param name The name of the system
	 * @throws IOException throws an exception if the system can't be backuped
	 */
	public ActionRemoveSystem(String name) throws IOException {
		super("Remove system (" + name + ")");
		this.name = name;
		this.fileContent = Files.readAllBytes(Paths.get(SceneEditor.coreEngine.getGameFolder() + "/systems/" + name));
	}

	@Override
	public boolean doAction() {
		SceneEditor.coreEngine.getScheduler().schedule(Trigger.GAME_LOOP_START, new Runnable() {

			@Override
			public void run() {
				Path newFilePath = Paths.get(SceneEditor.coreEngine.getGameFolder() + "/systems/" + name);
				try {
					Files.delete(newFilePath);
					SceneEditor.coreEngine.reloadSystemsFromDisk();

					SwingUtilities.invokeLater(new Runnable() {

						@Override
						public void run() {
							SceneEditor.fireSystemListChanged();
						}
					});

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		return true;
	}

	@Override
	public void undoAction() {
		SceneEditor.coreEngine.getScheduler().schedule(Trigger.GAME_LOOP_START, new Runnable() {

			@Override
			public void run() {
				Path newFilePath = Paths.get(SceneEditor.coreEngine.getGameFolder() + "/systems/" + name);
				try {
					Files.write(newFilePath, fileContent);
					SceneEditor.coreEngine.reloadSystemsFromDisk();

					SwingUtilities.invokeLater(new Runnable() {

						@Override
						public void run() {
							SceneEditor.fireSystemListChanged();
						}
					});

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

}
