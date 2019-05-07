package ch.sparkpudding.sceneeditor.action;

/**
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba<br/>
 *         Creation Date : 16 avr. 2019
 * 
 *         The action who handle the redo
 *
 */
@SuppressWarnings("serial")
public class ActionRedo extends AbstractAction {

	private static final String NAME = "Redo";

	private ActionsHistory actionsHistory;

	public ActionRedo() {
		super(NAME, false);
		this.actionsHistory = ActionsHistory.getInstance();
	}

	@Override
	public boolean doAction() {
		actionsHistory.redo();
		return true;
	}

}
