package ch.sparkpudding.coreengine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.sparkpudding.coreengine.utils.RunnableOneParameter;

/**
 * Schedule tasks to be run at set trigger points
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba
 * 
 */
public class Scheduler {
	public enum Trigger {
		BEFORE_UPDATE, AFTER_UPDATE, GAME_LOOP_START, COMPONENT_ADDED, SCENE_CHANGED, SCENE_LIST_CHANGED;
	};

	private Map<Trigger, List<Runnable>> tasks;
	private Map<Trigger, List<Runnable>> notifications;

	/**
	 * ctor
	 */
	public Scheduler() {
		tasks = new HashMap<Trigger, List<Runnable>>();
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
	 */
	public synchronized void trigger(Trigger trigger) {
		for (Runnable task : tasks.get(trigger)) {
			task.run();
		}
		tasks.get(trigger).clear();

		for (Runnable notif : notifications.get(trigger)) {
			notif.run();
		}
	}

	/**
	 * Runs all tasks and notifications associated with the given trigger
	 * 
	 * @param trigger method type to run
	 * @param object to give to the triggerred object
	 */
	public synchronized void trigger(Trigger trigger, Object object) {
		for (Runnable task : tasks.get(trigger)) {
			if (task instanceof RunnableOneParameter) {
				((RunnableOneParameter) task).setObject(object);
			}
			task.run();
		}
		tasks.get(trigger).clear();

		for (Runnable notif : notifications.get(trigger)) {
			if (notif instanceof RunnableOneParameter) {
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
	 */
	public synchronized void schedule(Trigger trigger, Runnable runnable) {
		tasks.get(trigger).add(runnable);
	}

	/**
	 * Add a task to be ran the next time the given trigger is activated
	 * 
	 * @param trigger  trigger that will run the task
	 * @param runnable task to run
	 */
	public synchronized void schedule(Trigger trigger, RunnableOneParameter runnable) {
		tasks.get(trigger).add(runnable);
	}

	/**
	 * Add a notification to be ran the next time the given trigger is activated
	 * 
	 * @param trigger  trigger that will run the notification
	 * @param runnable notification to run
	 */
	public synchronized void notify(Trigger trigger, Runnable runnable) {
		notifications.get(trigger).add(runnable);
	}
}
