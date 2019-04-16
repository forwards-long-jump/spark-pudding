package ch.sparkpudding.sceneeditor.action;

import java.util.Stack;

/**
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba<br/>
 *         Creation Date : 16 avr. 2019
 * 
 *         This class is a singleton who handle the differents actions to allow
 *         basic undo and redo management
 * 
 */
public class ActionsHistory {

	private static ActionsHistory INSTANCE = new ActionsHistory();

	private int undoRedoPointer = -1;
	private Stack<AbstractAction> actionsStack;

	private ActionsHistory() {
		this.actionsStack = new Stack<AbstractAction>();
	}

	/**
	 * Insert an action in the history
	 * 
	 * @param action the action to add
	 */
	public void insertAction(AbstractAction action) {

		deleteElementsAfterPointer();
		actionsStack.push(action);

		undoRedoPointer++;

	}

	/**
	 * Delete all the action after the pointer
	 */
	private void deleteElementsAfterPointer() {

		if (actionsStack.size() < 1)
			return;

		for (int i = actionsStack.size() - 1; i > undoRedoPointer; i--) {
			actionsStack.remove(i);
		}

	}

	/**
	 * Undo the action at the pointer without deleting it to allow redo
	 */
	public void undo() {

		AbstractAction action = actionsStack.get(undoRedoPointer);
		action.undoAction();

		undoRedoPointer--;

	}

	/**
	 * Redo the last undoed action
	 */
	public void redo() {

		if (undoRedoPointer == actionsStack.size() - 1)
			return;

		undoRedoPointer++;

		AbstractAction action = actionsStack.get(undoRedoPointer);
		action.doAction();

	}

	/**
	 * Get instance of the history for the program
	 * 
	 * @return the instance of the history
	 */
	public static ActionsHistory getInstance() {
		return INSTANCE;
	}
}
