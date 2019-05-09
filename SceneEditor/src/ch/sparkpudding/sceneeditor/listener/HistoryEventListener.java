package ch.sparkpudding.sceneeditor.listener;

import java.util.EventListener;

/**
 * Interface to create HistoryEventListener
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba<br/>
 *         Creation Date : 9 mai 2019
 *
 */
public interface HistoryEventListener extends EventListener {

	/**
	 * Event call when the history change
	 * 
	 * @param stackPointer Position of the stack of the history
	 * @param stackSize    Size of the stack of the history
	 */
	public void historyEvent(int stackPointer, int stackSize);

}
