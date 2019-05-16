package ch.sparkpudding.sceneeditor.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Action;

/**
 * Basic class for action in the scene editor
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba<br/>
 *         Creation Date : 8 April 2019
 *
 */
@SuppressWarnings("serial")
public abstract class AbstractAction extends javax.swing.AbstractAction implements ActionListener {

	private boolean isTracked;

	/**
	 * Creates an Action with the specified name.
	 *
	 * @param name the name for the action; a value of {@code null} is ignored
	 */
	public AbstractAction(String name) {
		super(name);
		this.isTracked = true;
	}

	/**
	 * Creates an Action with the specified name and specify if the action is
	 * tracked
	 * 
	 * @param name      the name for the action; a value of {@code null} is ignored
	 * @param isTracked if the action is tracked by the history
	 */
	public AbstractAction(String name, boolean isTracked) {
		this(name);
		this.isTracked = isTracked;
	}

	/**
	 * Specify the action to perform
	 * 
	 * @return true if the operation is successfull
	 */
	public abstract boolean doAction();

	/**
	 * Specify how to undo the doAction() method
	 */
	public void undoAction() {
		throw new UnsupportedOperationException("Please override this method");
	}

	/**
	 * Get the name of the Action
	 * 
	 * @return The name of the action
	 */
	public String getName() {
		return (String) this.getValue(Action.NAME);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		doAction();

		if (isTracked)
			ActionsHistory.getInstance().insertAction(this);
	}

}
