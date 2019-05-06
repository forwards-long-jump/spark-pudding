package ch.sparkpudding.sceneeditor.generator;

import java.awt.Color;
import java.text.NumberFormat;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import ch.sparkpudding.coreengine.ecs.component.Field;
import ch.sparkpudding.sceneeditor.utils.SpringUtilities;

/**
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba<br/>
 *         Creation Date : 8 avr. 2019
 *
 *         Generate the interface for the fields passed in arguments. Since it
 *         inherits JComponent, it can be used as one.
 * 
 */
@SuppressWarnings("serial")
public class FieldGenerator extends JComponent {

	public FieldGenerator(List<Field> fields) {
		setLayout(new SpringLayout());

		for (Field field : fields) {
			JLabel labelField = new JLabel(field.getName());
			add(labelField);
			add(createValueField(field, labelField));
		}
		
		setBackground(Color.RED);

		SpringUtilities.makeGrid(this, fields.size(), 2, 5, 5, 5, 5);
	}

	/**
	 * Generate the right JComponent and it's parameters following the type of the
	 * field.
	 * 
	 * @param field
	 * @return
	 */
	private JComponent createValueField(Field field, JLabel labelField) {
		JComponent input;

		switch (field.getType()) {
		case INTEGER:
			NumberFormat integerFormatter = NumberFormat.getIntegerInstance();
			integerFormatter.setGroupingUsed(false);
			input = new JFormattedTextField(integerFormatter);
			((JFormattedTextField) input).setValue(field.getValue());
			break;
		case DOUBLE:
			input = new JFormattedTextField(NumberFormat.getInstance());
			((JFormattedTextField) input).setValue(field.getValue());
			break;
		default: // Permits to avoid double-initialization of input.
		case STRING:
			input = new JTextField();
			((JTextField) input).setText(field.getValue().toString());
			break;
		case FILE_PATH:
			input = new JFormattedTextField();
			((JFormattedTextField) input).setValue(field.getValue());
			break;
		case BOOLEAN:
			input = new JCheckBox("", (boolean) field.getValue());
			break;
		}
		labelField.setLabelFor(input);
		return input;
	}
}
