package ch.sparkpudding.sceneeditor.action;

import javax.swing.SwingUtilities;

import ch.sparkpudding.coreengine.Scheduler.Trigger;
import ch.sparkpudding.sceneeditor.SceneEditor;
import ch.sparkpudding.sceneeditor.ecs.SEEntity;

/**
 * Action to set a zIndex
 *
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba<br />
 *         Creation Date : 29 May 2019
 *
 */
@SuppressWarnings("serial")
public class ActionSetZIndex extends AbstractAction {
	int zIndex;
	int previousZIndex;
	SEEntity entity;

	/**
	 * ctor
	 *
	 * @param entity to set the zIndex
	 * @param zIndex to set
	 */
	public ActionSetZIndex(SEEntity entity, int zIndex) {
		super("Change z-index of " + entity.getLiveEntity().getName() + " to " + zIndex);

		this.entity = entity;
		this.previousZIndex = entity.getLiveEntity().getZIndex();
		this.zIndex = zIndex;
	}

	/**
	 * Change z-index of entity to specified value
	 */
	@Override
	public boolean doAction() {
		SceneEditor.coreEngine.getScheduler().schedule(Trigger.GAME_LOOP_START, new Runnable() {

			@Override
			public void run() {
				entity.getLiveEntity().setZIndex(zIndex);
				entity.getDefaultEntity().setZIndex(zIndex);

				SceneEditor.coreEngine.notifyZIndexChange();

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
	 * Change zIndex back to its previous value
	 */
	@Override
	public void undoAction() {
		SceneEditor.coreEngine.getScheduler().schedule(Trigger.GAME_LOOP_START, new Runnable() {

			@Override
			public void run() {
				entity.getLiveEntity().setZIndex(previousZIndex);
				entity.getDefaultEntity().setZIndex(previousZIndex);

				SceneEditor.coreEngine.notifyZIndexChange();

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
