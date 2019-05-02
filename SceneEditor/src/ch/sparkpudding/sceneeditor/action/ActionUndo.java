package ch.sparkpudding.sceneeditor.action;

/**
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba<br/>
 *         Creation Date : 16 avr. 2019
 * 
 *         The action who handle the undo
 *
 */
@SuppressWarnings("serial")
public class ActionUndo extends AbstractAction {

	private static final String NAME = "Undo";

	private ActionsHistory actionsHistory;

	public ActionUndo() {
		super(NAME, false);
		this.actionsHistory = ActionsHistory.getInstance();
	}

	@Override
	public boolean doAction() {
		actionsHistory.undo();
		return true;
	}

}
