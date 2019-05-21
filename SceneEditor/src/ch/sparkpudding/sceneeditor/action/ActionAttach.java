package ch.sparkpudding.sceneeditor.action;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import ch.sparkpudding.coreengine.ecs.component.Component;
import ch.sparkpudding.coreengine.ecs.component.Field;
import ch.sparkpudding.sceneeditor.SceneEditor;

/**
 * Attach a component back to its template, preventing manual edition in the
 * Scene Editor
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba<br/>
 *         Creation Date : 20 May 2019
 * 
 */
@SuppressWarnings("serial")
public class ActionAttach extends AbstractAction {

	private Component component;
	private Map<String, String> oldValues;

	/**
	 * ctor
	 * 
	 * @param component Component which will be attached
	 */
	public ActionAttach(Component component) {
		super("Attach component " + component.getName());
		this.component = component;
	}

	@Override
	public boolean doAction() {

		// Save the old values
		this.oldValues = new HashMap<String, String>();
		for (Field field : component.getFields().values()) {
			oldValues.put(field.getName(), field.getValue().toString());
		}

		// This already sets the template values back in the component
		component.setAttached(true);

		SceneEditor.fireSelectedEntityChanged();
		return true;
	}

	@Override
	public void undoAction() {
		component.setAttached(false);

		// Restore all fields to the previous values
		for (Entry<String, String> field : oldValues.entrySet()) {
			component.getField(field.getKey()).setValueFromString(field.getValue());
		}
		SceneEditor.fireSelectedEntityChanged();
	}

}
