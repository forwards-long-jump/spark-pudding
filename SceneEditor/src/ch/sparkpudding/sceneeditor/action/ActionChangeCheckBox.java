package ch.sparkpudding.sceneeditor.action;

import javax.swing.JCheckBox;

import ch.sparkpudding.coreengine.ecs.component.Field;

/**
 * The action to register the update of a CheckBox value for an entity field
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba<br/>
 *         Creation Date : 7 May 2019
 *
 */
@SuppressWarnings("serial")
public class ActionChangeCheckBox extends AbstractAction {

	private Field field;
	private boolean value;
	private boolean oldValue;
	private JCheckBox checkBox;

	/**
	 * Create an ActionChangeCheckBox with only the value. Doesn't update the field
	 * value if do or undo.
	 * 
	 * @param name  Specify the name of the action
	 * @param field Specify the field of the entity needed to be update
	 * @param value Specify the value to set for the field
	 */
	public ActionChangeCheckBox(String name, Field field, boolean value) {
		super("Value (" + value + ")");
		this.field = field;
		this.value = value;
		this.oldValue = (boolean) field.getValue();
	}

	/**
	 * Create an ActionChangeCheckBox with only the value. Update the field value if
	 * do or undo.
	 * 
	 * @param name      Specify the name of the action
	 * @param field     Specify the field of the entity needed to be update
	 * @param checkBox Specify JTextField linked to this value for update when undo
	 *                  or redo
	 */
	public ActionChangeCheckBox(String name, Field field, JCheckBox checkBox) {
		this(name, field, checkBox.isSelected());
		System.out.println(checkBox);
		this.checkBox = checkBox;
	}

	/**
	 * Reset the oldValue when the Action was created
	 */
	@Override
	public void undoAction() {
		field.setValue(oldValue);

		if (checkBox != null)
			checkBox.setSelected(oldValue);
	}

	/**
	 * Set the value for the entity
	 */
	@Override
	public boolean doAction() {
		field.setValue(value);

		if (checkBox != null)
			checkBox.setSelected(value);

		return true;
	}

}
