package ch.sparkpudding.coreengine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Schedule tasks to be run at set trigger points
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba
 * 
 */
public class TaskScheduler {
	public enum Trigger{
		BEFORE_UPDATE, AFTER_UPDATE
	};
	
	private Map<Trigger, List<Runnable>> tasks;
	
	/**
	 * 
	 */
	public TaskScheduler() {
		tasks = new HashMap<Trigger, List<Runnable>>();
		for (Trigger trig : Trigger.values()) {
			tasks.put(trig, new ArrayList<Runnable>());
		}
	}
	
	public void trigger(Trigger trig) {
		for (Runnable task : tasks.get(trig)) {
			task.run();
		}
		tasks.get(trig).clear();
	}
	
	public void schedule(Trigger trigger, Runnable runnable) {
		tasks.get(trigger).add(runnable);
	}
}
