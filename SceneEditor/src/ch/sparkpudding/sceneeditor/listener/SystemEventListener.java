package ch.sparkpudding.sceneeditor.listener;

import java.util.EventListener;

/**
 * Interface to create SystemEventListener
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba<br/>
 *         Creation Date : 28 May 2019
 *
 */
public interface SystemEventListener extends EventListener {

	/**
	 * Event call when the system list change
	 */
	public void systemListChanged();

}
