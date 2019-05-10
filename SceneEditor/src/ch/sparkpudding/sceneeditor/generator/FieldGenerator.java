package ch.sparkpudding.sceneeditor.generator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.Collection;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import ch.sparkpudding.coreengine.ecs.component.Field;
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

	private String nameComp;

	/**
	 * ctor
	 * 
	 * @param fields Collection of all the components of an entity
	 */
	public FieldGenerator(Collection<Field> fields, String nameComp) {
		this.fields = fields;
		this.nameComp = nameComp;

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
			((JFormattedTextField) input).setValue(field.getValue());
			createTextFieldListener((JTextField) input, field);
			break;
		case DOUBLE:
			input = new JFormattedTextField(NumberFormat.getInstance());
			((JFormattedTextField) input).setValue(field.getValue());
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
		input.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ActionChangeTextField action = new ActionChangeTextField("", field, input);
				action.actionPerformed(e);
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
		input.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ActionChangeCheckBox action = new ActionChangeCheckBox("", field, input);
				action.actionPerformed(e);
			}
		});
	}
}
