package ch.sparkpudding.sceneeditor.action;

/**
 * The action who handle the undo
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba<br/>
 *         Creation Date : 16 avr. 2019
 *
 */
@SuppressWarnings("serial")
public class ActionUndo extends AbstractAction {

	private static final String NAME = "Undo";

	private ActionsHistory actionsHistory;
	private AbstractAction rollback;

	/**
	 * Create a new action ActionUndo and prevent to store it in the ActionHistory
	 */
	public ActionUndo() {
		super(NAME, false);
		this.actionsHistory = ActionsHistory.getInstance();
		this.rollback = null;
	}

	/**
	 * Create a new action ActionUndo with a Action to rollback to. Also prevent to
	 * store it in the ActionHistory
	 * 
	 * @param rollback The Action to rollback to
	 */
	public ActionUndo(AbstractAction rollback) {
		super(NAME + " - " + rollback.getName(), false);
		this.actionsHistory = ActionsHistory.getInstance();
		this.rollback = rollback;
	}

	@Override
	public boolean doAction() {
		if (rollback == null) {
			actionsHistory.undo();
		} else {
			AbstractAction action;
			do {
				action = actionsHistory.undo();
			} while (action != rollback);
		}

		return true;
	}

}
