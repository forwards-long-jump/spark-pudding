package ch.sparkpudding.sceneeditor.panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;

import ch.sparkpudding.coreengine.Scheduler.Trigger;
import ch.sparkpudding.coreengine.ecs.component.Component;
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
public class PanelFieldsEditor extends JComponent {

	private Collection<Field> fields;
	private List<RunnableOneParameter> onFieldsChanged;
	private List<JComponent> fieldsInput;
	private Component component;

	private boolean enableable;

	/**
	 * ctor
	 *
	 * @param fields     Collection of all the components of an entity
	 * @param enableable Whether the component can be enabled
	 */
	public PanelFieldsEditor(Component component, boolean enableable) {
		this.fields = new ArrayList<Field>(component.getFields().values());
		this.fieldsInput = new ArrayList<JComponent>();
		this.onFieldsChanged = new ArrayList<RunnableOneParameter>();
		this.enableable = enableable;
		this.component = component;

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
		List<Field> sortedFields = new ArrayList<Field>(fields);
		sortedFields.sort(new Comparator<Field>() {
			@Override
			public int compare(Field arg0, Field arg1) {
				return arg0.getName().compareTo(arg1.getName());
			}
		});

		for (Field field : sortedFields) {
			JLabel labelField = new JLabel(field.getName());
			add(labelField);
			JComponent valueField = createValueField(field, labelField);
			add(valueField);
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
			addTextFieldListener((JTextField) input, field);
			break;
		case DOUBLE:
			input = new JFormattedTextField(NumberFormat.getInstance());
			((JFormattedTextField) input).setValue(field.getDouble());
			addTextFieldListener((JTextField) input, field);
			break;
		default: // Permits to avoid double-initialization of input.
		case STRING:
			input = new JTextField();
			((JTextField) input).setText(field.getValue().toString());
			addTextFieldListener((JTextField) input, field);
			break;
		case FILE_PATH:
			input = new JFormattedTextField();
			((JFormattedTextField) input).setValue(field.getValue());
			addTextFieldListener((JTextField) input, field);
			break;
		case BOOLEAN:
			input = new JCheckBox("", (boolean) field.getValue());
			addCheckBoxListener((JCheckBox) input, field);
			break;
		}
		labelField.setLabelFor(input);
		input.setEnabled(enableable);

		fieldsInput.add(input);
		return input;
	}

	/**
	 * Create the listener for a textField
	 *
	 * @param input the input which contains the new value
	 * @param field the field represented by this input
	 */
	private void addTextFieldListener(JTextField input, Field field) {
		RunnableOneParameter onFieldChange = new RunnableOneParameter() {
			@Override
			public void run() {
				// TODO: Handle thread synchronization correctly instead of this try catch
				try {
					if (!field.getValue().toString().equals(input.getText()) && !input.hasFocus()) {
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								input.setText(field.getValue().toString());
							}
						});
					}
				} catch (Exception e) {
					System.err.println("Known issue");
					e.printStackTrace();
				}
			}
		};

		onFieldsChanged.add(onFieldChange);
		SceneEditor.coreEngine.getScheduler().notify(Trigger.GAME_LOOP_START, onFieldChange);

		input.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ActionChangeTextField action = new ActionChangeTextField(field, input, component.getName());
				action.actionPerformed(e);
			}
		});

		input.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						input.selectAll();
					}
				});
			}

			@Override
			public void focusLost(FocusEvent e) {
				ActionChangeTextField action = new ActionChangeTextField(field, input, component.getName());
				action.actionPerformed(null);
			}
		});
	}

	/**
	 * Add the listener for a checkBox
	 *
	 * @param input the input which contains the new value
	 * @param field the field represented by this input
	 */
	private void addCheckBoxListener(JCheckBox input, Field field) {
		RunnableOneParameter onFieldChange = new RunnableOneParameter() {
			@Override
			public void run() {
				// TODO: Handle thread synchronization correctly instead of this try catch
				try {
					if (input.isSelected() != (boolean) field.getValue() && !input.hasFocus()) {

						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								input.setSelected((boolean) field.getValue());
							}
						});
					}
				} catch (Exception e) {
					System.err.println("Known issue.");
					e.printStackTrace();
				}
			}
		};

		onFieldsChanged.add(onFieldChange);
		SceneEditor.coreEngine.getScheduler().notify(Trigger.GAME_LOOP_START, onFieldChange);

		input.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ActionChangeCheckBox action = new ActionChangeCheckBox("", field, input);
				action.actionPerformed(e);
			}
		});
	}

	/**
	 * Override setEnabled in order to prevent attached components to be changed
	 */
	@Override
	public void setEnabled(boolean enabled) {
		if (enableable) {
			for (JComponent comp : fieldsInput) {
				comp.setEnabled(enabled);
			}
		}
		super.setEnabled(enabled);
	}
}
