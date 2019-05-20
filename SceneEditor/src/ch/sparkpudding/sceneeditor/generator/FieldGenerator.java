package ch.sparkpudding.sceneeditor.generator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;

import ch.sparkpudding.coreengine.Scheduler.Trigger;
import ch.sparkpudding.coreengine.ecs.component.Field;
import ch.sparkpudding.coreengine.utils.RunnableOneParameter;
import ch.sparkpudding.sceneeditor.SceneEditor;
import ch.sparkpudding.sceneeditor.action.ActionChangeCheckBox;
import ch.sparkpudding.sceneeditor.action.ActionChangeTextField;
import ch.sparkpudding.sceneeditor.utils.SpringUtilities;

/**
 * Generate the interface for the fields passed in arguments. Since it inherits
 * JComponent, it can be used as one.
 *
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba<br/>
 *         Creation Date : 8 April 2019
 *
 */
@SuppressWarnings("serial")
public class FieldGenerator extends JComponent {

	private Collection<Field> fields;
	private List<RunnableOneParameter> onFieldsChanged;

	/**
	 * ctor
	 *
	 * @param fields Collection of all the components of an entity
	 */
	public FieldGenerator(Collection<Field> fields) {
		this.fields = fields;
		this.onFieldsChanged = new ArrayList<RunnableOneParameter>();

		createFields();
		setupLayout();
	}

	/**
	 * Setup the layout of the panel
	 */
	private void setupLayout() {
		setLayout(new SpringLayout());
		if (fields.size() > 0) {
			SpringUtilities.makeGrid(this, fields.size(), 2, 5, 5, 5, 5);
		}
	}

	/**
	 * Create and recreate all the representation of the fields stored in
	 * <code>this.fields</code>
	 */
	private void createFields() {
		for (Field field : fields) {
			JLabel labelField = new JLabel(field.getName());
			add(labelField);
			add(createValueField(field, labelField));
		}
	}

	@Override
	public void removeNotify() {
		for (RunnableOneParameter onFieldChanged : onFieldsChanged) {
			SceneEditor.coreEngine.getScheduler().removeNotify(Trigger.GAME_LOOP_START, onFieldChanged);
		}
		super.removeNotify();
	}

	/**
	 * Generate the right JComponent and it's parameters following the type of the
	 * field.
	 *
	 * @param field The field to consider
	 * @return The input generated
	 */
	private JComponent createValueField(Field field, JLabel labelField) {
		JComponent input;

		switch (field.getType()) {
		case INTEGER:
			NumberFormat integerFormatter = NumberFormat.getIntegerInstance();
			integerFormatter.setGroupingUsed(false);
			input = new JFormattedTextField(integerFormatter);
			((JFormattedTextField) input).setValue(field.getInt());
			createTextFieldListener((JTextField) input, field);
			break;
		case DOUBLE:
			input = new JFormattedTextField(NumberFormat.getInstance());
			((JFormattedTextField) input).setValue(field.getDouble());
			createTextFieldListener((JTextField) input, field);
			break;
		default: // Permits to avoid double-initialization of input.
		case STRING:
			input = new JTextField();
			((JTextField) input).setText(field.getValue().toString());
			createTextFieldListener((JTextField) input, field);
			break;
		case FILE_PATH:
			input = new JFormattedTextField();
			((JFormattedTextField) input).setValue(field.getValue());
			createTextFieldListener((JTextField) input, field);
			break;
		case BOOLEAN:
			input = new JCheckBox("", (boolean) field.getValue());
			createCheckBoxListener((JCheckBox) input, field);
			break;
		}
		labelField.setLabelFor(input);
		return input;
	}

	/**
	 * Create the listener for a textField
	 *
	 * @param input the input which contains the new value
	 * @param field the field represented by this input
	 */
	private void createTextFieldListener(JTextField input, Field field) {
		RunnableOneParameter onFieldChange = new RunnableOneParameter() {
			@Override
			public void run() {
				if (!field.getValue().toString().equals(input.getText()) && !input.hasFocus()) {
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							input.setText(field.getValue().toString());
						}
					});
				}
			}
		};

		onFieldsChanged.add(onFieldChange);
		SceneEditor.coreEngine.getScheduler().notify(Trigger.GAME_LOOP_START, onFieldChange);

		input.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ActionChangeTextField action = new ActionChangeTextField("", field, input);
				action.actionPerformed(e);
			}
		});

		input.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				input.selectAll();
			}

			@Override
			public void focusLost(FocusEvent e) {
				ActionChangeTextField action = new ActionChangeTextField("", field, input);
				action.actionPerformed(null);
			}
		});
	}

	/**
	 * Create the listener for a checkBox
	 *
	 * @param input the input which contains the new value
	 * @param field the field represented by this input
	 */
	private void createCheckBoxListener(JCheckBox input, Field field) {
		RunnableOneParameter onFieldChange = new RunnableOneParameter() {
			@Override
			public void run() {
				if (input.isSelected() != (boolean) field.getValue() && !input.hasFocus()) {

					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							input.setSelected((boolean) field.getValue());
						}
					});
				}
			}
		};

		onFieldsChanged.add(onFieldChange);
		SceneEditor.coreEngine.getScheduler().schedule(Trigger.GAME_LOOP_START, onFieldChange);

		input.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ActionChangeCheckBox action = new ActionChangeCheckBox("", field, input);
				action.actionPerformed(e);
			}
		});
	}
}
