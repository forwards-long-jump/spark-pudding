package ch.sparkpudding.sceneeditor.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import ch.sparkpudding.sceneeditor.listener.HistoryEventListener;

/**
 * This class is a singleton who handle the differents actions to allow basic
 * undo and redo management
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba<br/>
 *         Creation Date : 16 avr. 2019
 * 
 */
public class ActionsHistory {

	private static ActionsHistory INSTANCE = new ActionsHistory();

	private List<HistoryEventListener> eventListeners;

	private int stackPointer = -1;
	private Stack<AbstractAction> actionsStack;

	private ActionsHistory() {
		this.actionsStack = new Stack<AbstractAction>();
		this.eventListeners = new ArrayList<HistoryEventListener>();
	}

	/**
	 * Insert an action in the history
	 * 
	 * @param action the action to add
	 */
	public void insertAction(AbstractAction action) {

		deleteElementsAfterPointer();
		actionsStack.push(action);

		stackPointer++;

		fireHistoryEvent();
	}

	/**
	 * Delete all the action after the pointer
	 */
	private void deleteElementsAfterPointer() {

		if (actionsStack.size() < 1)
			return;

		for (int i = actionsStack.size() - 1; i > stackPointer; i--) {
			actionsStack.remove(i);
		}

	}

	/**
	 * Get an Action at a specified index of the stack
	 * 
	 * @param index The index of the Action
	 * @return The Action at the index
	 */
	public AbstractAction getActionAt(int index) {
		return actionsStack.get(index);
	}

	/**
	 * Undo the action at the pointer without deleting it to allow redo
	 * 
	 * @return the action undoed
	 */
	public AbstractAction undo() {

		AbstractAction action = actionsStack.get(stackPointer);
		action.undoAction();

		stackPointer--;

		fireHistoryEvent();

		return action;
	}

	/**
	 * Redo the last undoed action
	 * 
	 * @return The action redoed
	 */
	public AbstractAction redo() {

		if (stackPointer == actionsStack.size() - 1)
			return null;

		stackPointer++;

		AbstractAction action = actionsStack.get(stackPointer);
		action.doAction();

		fireHistoryEvent();

		return action;
	}

	/**
	 * Add a listener for the event of the History
	 * 
	 * @param evtListener the listener
	 */
	public void addHistoryEventListener(HistoryEventListener evtListener) {
		eventListeners.add(evtListener);
	}

	/**
	 * Remove a listener for the event of the History
	 * 
	 * @param evtListener the listener to remove
	 * @return {@code true} if the event exist
	 */
	public boolean removeHistoryEventListener(HistoryEventListener evtListener) {
		return eventListeners.remove(evtListener);
	}

	/**
	 * Remove all the event of the History
	 */
	public void removeAllHistoryEventListener() {
		eventListeners.clear();
	}

	/**
	 * Allow to fire the history event of the listeners
	 */
	private void fireHistoryEvent() {
		for (HistoryEventListener historyEventListener : eventListeners) {
			historyEventListener.historyEvent(stackPointer, actionsStack.size());
		}
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
