package ch.sparkpudding.sceneeditor.action;

import javax.swing.SwingUtilities;

import ch.sparkpudding.coreengine.Scheduler.Trigger;
import ch.sparkpudding.sceneeditor.SceneEditor;

/**
 * Action to rename a scene
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba<br />
 *         Creation Date : 16 May 2019
 * 
 */
@SuppressWarnings("serial")
public class ActionRenameScene extends AbstractAction {

	private String oldName;
	private String newName;

	/**
	 * ctor
	 * 
	 * @param oldName name of the scene to rename
	 * @param newName new name to give to the scene
	 */
	public ActionRenameScene(String oldName, String newName) {
		super("Rename scene " + oldName + " to " + newName);
		this.oldName = oldName;
		this.newName = newName;
	}

	/**
	 * Set the name of the scene to newName
	 */
	@Override
	public boolean doAction() {
		// We disallow getting rid of the entry scene
		if (oldName.equals("main")) {
			return false;
		}
		SceneEditor.coreEngine.getScheduler().schedule(Trigger.GAME_LOOP_START, new Runnable() {

			@Override
			public void run() {
				SceneEditor.coreEngine.renameScene(oldName, newName);
				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						SceneEditor.fireEntityListChanged();
					}
				});
			}
		});
		return true;
	}

	/**
	 * Reverts the name of the scene to oldName
	 */
	@Override
	public void undoAction() {
		SceneEditor.coreEngine.getScheduler().schedule(Trigger.GAME_LOOP_START, new Runnable() {

			@Override
			public void run() {
				SceneEditor.coreEngine.renameScene(newName, oldName);
				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						SceneEditor.fireEntityListChanged();
					}
				});
			}
		});
	}
}
