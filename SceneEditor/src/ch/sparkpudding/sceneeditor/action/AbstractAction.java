package ch.sparkpudding.sceneeditor.action;

import java.awt.event.ActionEvent;

/**
 * Basic class for action in the scene editor
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba<br/>
 *         Creation Date : 8 avr. 2019
 *
 */
@SuppressWarnings("serial")
public abstract class AbstractAction extends javax.swing.AbstractAction {
	
	/**
     * Creates an Action with the specified name.
     *
     * @param name the name for the action; a
     *        value of {@code null} is ignored
     */
    public AbstractAction(String name) {
        super(name);
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

	@Override
	public void actionPerformed(ActionEvent e) {
		doAction();
	}

}
