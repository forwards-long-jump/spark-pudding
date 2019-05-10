package ch.sparkpudding.sceneeditor.action;

/**
 * The action who handle the redo
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba<br/>
 *         Creation Date : 16 April 2019
 *
 */
@SuppressWarnings("serial")
public class ActionRedo extends AbstractAction {

	private static final String NAME = "Redo";

	private ActionsHistory actionsHistory;
	private AbstractAction rollback;

	/**
	 * Create a new action ActionRedo and prevent it storage in the ActionHistory
	 */
	public ActionRedo() {
		super(NAME, false);
		this.actionsHistory = ActionsHistory.getInstance();
		this.rollback = null;
	}

	/**
	 * Create a new action ActionRedo with a specified index (number of redo action)
	 * and prevent it storage in the ActionHistory
	 * 
	 * @param rollback The Action to rollbackReverse to
	 */
	public ActionRedo(AbstractAction rollback) {
		super(NAME, false);
		this.actionsHistory = ActionsHistory.getInstance();
		this.rollback = rollback;
	}

	@Override
	public boolean doAction() {
		if (rollback == null) {
			actionsHistory.redo();
		} else {
			AbstractAction action;
			do {
				action = actionsHistory.redo();
			} while (action != rollback);
		}

		return true;
	}

}
