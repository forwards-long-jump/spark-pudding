package ch.sparkpudding.sceneeditor.generator;

import java.awt.GridLayout;
import java.text.NumberFormat;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JTextField;

import ch.sparkpudding.coreengine.ecs.Field;

/**
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba<br/>
 *         Creation Date : 8 avr. 2019
 *
 */
@SuppressWarnings("serial")
public class FieldGenerator extends JComponent {

	List<JLabel> names;
	List<JComponent> values;

	public FieldGenerator(List<Field> fields) {
		setLayout(new GridLayout(0, 2));

		for (Field field : fields) {
			add(new JLabel(field.getName()));
			add(createValueField(field));
		}
	}

	private JComponent createValueField(Field field) {
		JComponent input;

		switch (field.getType()) {
		case INTEGER:
			NumberFormat integerFormatter = NumberFormat.getIntegerInstance();
			integerFormatter.setGroupingUsed(false);
			input = new JFormattedTextField(integerFormatter);
			((JFormattedTextField) input).setValue(field.getValue());
			break;
		case DOUBLE:
			// NumberFormat doubleFormatter = NumberFormat.get
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
			input = new JCheckBox("", (boolean)field.getValue());
			break;
		}
		return input;
	}

}
