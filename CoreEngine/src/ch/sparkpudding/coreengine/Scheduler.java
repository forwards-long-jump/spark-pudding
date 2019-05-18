package ch.sparkpudding.coreengine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import ch.sparkpudding.coreengine.utils.RunnableOneParameter;

/**
 * Schedule tasks to be run at set trigger points
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba
 * 
 */
public class Scheduler {
	public enum Trigger {
		BEFORE_UPDATE, GAME_LOOP_START, COMPONENT_ADDED, EDITING_STATE_CHANGED, FIELD_VALUE_CHANGED;
	};

	private Map<Trigger, List<Runnable>> tasks;
	private Map<Trigger, List<Runnable>> notifications;
	private Semaphore semaphore;

	/**
	 * ctor
	 */
	public Scheduler() {
		tasks = new HashMap<Trigger, List<Runnable>>();
		semaphore = new Semaphore(1);

		for (Trigger trig : Trigger.values()) {
			tasks.put(trig, new ArrayList<Runnable>());
		}
		notifications = new HashMap<Trigger, List<Runnable>>();
		for (Trigger trig : Trigger.values()) {
			notifications.put(trig, new ArrayList<Runnable>());
		}
	}

	/**
	 * Runs all tasks and notifications associated with the given trigger
	 * 
	 * @param trigger method type to run
	 * @throws InterruptedException
	 */
	public void trigger(Trigger trigger) {
		this.trigger(trigger, null);
	}

	/**
	 * Runs all tasks and notifications associated with the given trigger
	 * 
	 * @param trigger method type to run
	 * @param object  to give to the triggerred object
	 * @throws InterruptedException
	 */
	public void trigger(Trigger trigger, Object object) {
		// First, copy all planned tasks
		List<Runnable> taskCopy = new ArrayList<Runnable>();
		try {
			semaphore.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		for (Runnable task : tasks.get(trigger)) {
			if (object != null && task instanceof RunnableOneParameter) {
				((RunnableOneParameter) task).setObject(object);
			}
			taskCopy.add(task);
		}
		tasks.get(trigger).clear();
		semaphore.release();

		// Execute all planned tasks
		for (Runnable task : taskCopy) {
			task.run();
		}

		// TODO: Copy notifications if it's necessary
		for (Runnable notif : notifications.get(trigger)) {
			if (object != null && notif instanceof RunnableOneParameter) {
				((RunnableOneParameter) notif).setObject(object);
			}
			notif.run();
		}
	}

	/**
	 * Add a task to be ran the next time the given trigger is activated
	 * 
	 * @param trigger  trigger that will run the task
	 * @param runnable task to run
	 * @throws InterruptedException
	 */
	public void schedule(Trigger trigger, Runnable runnable) {
		try {
			semaphore.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		tasks.get(trigger).add(runnable);
		semaphore.release();
	}

	/**
	 * Add a notification to be ran the next time the given trigger is activated
	 * 
	 * @param trigger  trigger that will run the notification
	 * @param runnable notification to run
	 */
	public void notify(Trigger trigger, Runnable runnable) {
		try {
			semaphore.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		notifications.get(trigger).add(runnable);
		semaphore.release();
	}
	
	/**
	 * Remove specified runnabled
	 * 
	 * @param runnable
	 */
	public void removeNotify(Trigger trigger, Runnable runnable) {
		notifications.get(trigger).remove(runnable);
	}
}
