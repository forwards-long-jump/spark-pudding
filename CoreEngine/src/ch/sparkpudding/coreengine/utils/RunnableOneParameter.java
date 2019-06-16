package ch.sparkpudding.coreengine.utils;

/**
 * Runnable with one parameter
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba<br/>
 *         Creation Date : 16 May 2019
 *
 */
public class RunnableOneParameter implements Runnable {
	private Object object;

	/**
	 * Set object to be given to the run method
	 * 
	 * @param object to set
	 */
	public void setObject(Object object) {
		this.object = object;
	}

	/**
	 * Get the object set previously
	 * 
	 * @return object set previously
	 */
	public Object getObject() {
		return this.object;
	}

	@Override
	public void run() {

	}

}