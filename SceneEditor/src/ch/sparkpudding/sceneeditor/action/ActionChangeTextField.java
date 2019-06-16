package ch.sparkpudding.sceneeditor.action;

import javax.swing.JTextField;

import ch.sparkpudding.coreengine.ecs.component.Field;

/**
 * The action to register the update of a textField value for an entity field
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba<br/>
 *         Creation Date : 7 May 2019
 *
 */
@SuppressWarnings("serial")
public class ActionChangeTextField extends AbstractAction {

	private Field field;
	private String value;
	private String oldValue;
	private JTextField textField;

	/**
	 * Create an ActionChangeTextField with only the value. Doesn't update the field
	 * value if do or undo.
	 * 
	 * @param name  Specify the name of the action
	 * @param field Specify the field of the entity needed to be update
	 * @param value Specify the value to set for the field
	 */
	public ActionChangeTextField(Field field, String value, String componentName) {
		super("Value (" + value + ")");
		this.field = field;
		this.value = value;
		this.oldValue = field.getValue().toString();
	}

	/**
	 * Create an ActionChangeTextField with only the value. Update the field value
	 * if do or undo.
	 * 
	 * @param name      Specify the name of the action
	 * @param field     Specify the field of the entity needed to be update
	 * @param textField Specify JTextField linked to this value for update when undo
	 *                  or redo
	 */
	public ActionChangeTextField(Field field, JTextField textField, String componentName) {
		this(field, textField.getText(), componentName);
		this.textField = textField;
	}

	/**
	 * Reset the oldValue when the Action was created
	 */
	@Override
	public void undoAction() {
		field.setValueFromString(oldValue);

		if (textField != null) {
			textField.setText(oldValue);
		}
	}

	/**
	 * Set the value for the entity
	 */
	@Override
	public boolean doAction() {
		if (this.oldValue.equals(value)) {
			return false;
		}

		field.setValueFromString(value);

		if (textField != null) {
			textField.setText(value);
		}
		return true;
	}

}
