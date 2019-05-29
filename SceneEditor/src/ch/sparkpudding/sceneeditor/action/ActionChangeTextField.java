package ch.sparkpudding.sceneeditor.action;

import javax.swing.JTextField;

import ch.sparkpudding.coreengine.ecs.component.Field;
import ch.sparkpudding.sceneeditor.SceneEditor;
import ch.sparkpudding.sceneeditor.SceneEditor.EditorState;
import ch.sparkpudding.sceneeditor.ecs.SEEntity;

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
	private SEEntity seEntity;
	private String componentName;

	/**
	 * Create an ActionChangeTextField with only the value. Doesn't update the field
	 * value if do or undo.
	 * 
	 * @param name  Specify the name of the action
	 * @param field Specify the field of the entity needed to be update
	 * @param value Specify the value to set for the field
	 */
	public ActionChangeTextField(SEEntity seEntity, Field field, String value, String componentName) {
		super("Value (" + value + ")");
		this.field = field;
		this.componentName = componentName;
		this.seEntity = seEntity;
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
	public ActionChangeTextField(SEEntity seEntity, Field field, JTextField textField, String componentName) {
		this(seEntity, field, textField.getText(), componentName);
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

		// If the game is stopped we also update the live entity
		if (SceneEditor.getGameState() == EditorState.STOP) {
			seEntity.getLiveEntity().getComponents().get(componentName).getField(field.getName())
					.setValueFromString(oldValue);
			seEntity.getDefaultEntity().getComponents().get(componentName).getField(field.getName())
					.setValueFromString(oldValue);
		}
	}

	/**
	 * Set the value for the entity
	 */
	@Override
	public boolean doAction() {
		field.setValueFromString(value);

		if (textField != null)
			textField.setText(value);

		// If the game is stopped we also update the live entity
		if (SceneEditor.getGameState() == EditorState.STOP) {
			seEntity.getLiveEntity().getComponents().get(componentName).getField(field.getName())
					.setValueFromString(value);
			// We need to do this because live values may be modified and we want to
			// replicate them into the default component as well
			// honestly I think it's bad and it will probably break something once but
			// time^tm
			seEntity.getDefaultEntity().getComponents().get(componentName).getField(field.getName())
					.setValueFromString(value);
		}

		return true;
	}

}
