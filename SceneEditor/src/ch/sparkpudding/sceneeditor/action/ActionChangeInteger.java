package ch.sparkpudding.sceneeditor.action;

import javax.swing.JTextField;

import ch.sparkpudding.coreengine.ecs.component.Field;

/**
 * The action to register the update of an integer value for an entity field
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba<br/>
 *         Creation Date : 7 May 2019
 *
 */
@SuppressWarnings("serial")
public class ActionChangeInteger extends AbstractAction {

	private Field field;
	private String value;
	private String oldValue;
	private JTextField textField;

	/**
	 * Create an ActionChangeInteger with only the value. Doesn't update the field
	 * value if do or undo.
	 * 
	 * @param name  Specify the name of the action
	 * @param field Specify the field of the entity needed to be update
	 * @param value Specify the value to set for the field
	 */
	public ActionChangeInteger(String name, Field field, String value) {
		super("Value (" + value + ")");
		this.field = field;
		this.value = value;
		this.oldValue = field.getValue().toString();
	}

	/**
	 * Create an ActionChangeInteger with only the value. Update the field value if
	 * do or undo.
	 * 
	 * @param name      Specify the name of the action
	 * @param field     Specify the field of the entity needed to be update
	 * @param textField Specify JTextField linked to this value for update when undo
	 *                  or redo
	 */
	public ActionChangeInteger(String name, Field field, JTextField textField) {
		this(name, field, textField.getText());
		this.textField = textField;
	}

	/**
	 * Reset the oldValue when the Action was created
	 */
	@Override
	public void undoAction() {
		field.setValueFromString(oldValue);

		if (textField != null)
			textField.setText(oldValue);
	}

	/**
	 * Set the value for the entity
	 */
	@Override
	public boolean doAction() {
		field.setValueFromString(value);

		if (textField != null)
			textField.setText(value);

		return true;
	}

}
