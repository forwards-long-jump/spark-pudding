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
import ch.sparkpudding.coreengine.ecs.entity.Entity;
import ch.sparkpudding.coreengine.utils.Pair;
import ch.sparkpudding.coreengine.utils.RunnableOneParameter;
import ch.sparkpudding.sceneeditor.SceneEditor;
import ch.sparkpudding.sceneeditor.SceneEditor.EditorState;
import ch.sparkpudding.sceneeditor.action.ActionChangeCheckBox;
import ch.sparkpudding.sceneeditor.action.ActionChangeTextField;
import ch.sparkpudding.sceneeditor.ecs.SEEntity;
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
	private SEEntity seEntity;
	private Entity entity;
	private Component component;

	private boolean enableable;

	/**
	 * ctor
	 *
	 * @param fields     Collection of all the components of an entity
	 * @param enableable Whether the component can be enabled
	 */
	public PanelFieldsEditor(SEEntity seEntity, Entity entity, Component component, boolean enableable) {
		this.fields = new ArrayList<Field>(component.getFields().values());
		this.seEntity = seEntity;
		this.fieldsInput = new ArrayList<JComponent>();
		this.onFieldsChanged = new ArrayList<RunnableOneParameter>();
		this.enableable = enableable;
		this.component = component;
		this.entity = entity;

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

			// Position and size *can* be modified while the game is editing paused so
			// we track when they are changed and update them here
			if (seEntity.getLiveEntity() == entity && component.getName().equals("position")
					&& (field.getName().equals("x") || field.getName().equals("y"))) {
				addRunnableForLiveChanges("position", field, valueField);
			} else if (seEntity.getLiveEntity() == entity && component.getName().equals("size")
					&& (field.getName().equals("width") || field.getName().equals("height"))) {
				addRunnableForLiveChanges("size", field, valueField);
			}
		}
	}

	/**
	 * Add a RunnableOneParameter to update default entity when the game is stopped
	 * but live changes are made
	 * 
	 * @param componentName name of the component to track
	 * @param field         the field to track
	 * @param inputField    input to update when the value is changed
	 */
	private void addRunnableForLiveChanges(String componentName, Field field, JComponent inputField) {
		RunnableOneParameter onComponentAdded = new RunnableOneParameter() {
			@Override
			public void run() {
				// we only handle that when the game is stopped
				if (SceneEditor.getGameState() == EditorState.STOP) {
					Entity entity = (Entity) ((Pair<?, ?>) getObject()).first();
					Component component = (Component) ((Pair<?, ?>) getObject()).second();

					if (seEntity.getLiveEntity() == entity && component.getName().equals("se-entity-transform-done")
							&& entity.hasComponent("se-selected")) {

						if (entity.hasComponent(componentName)
								&& entity.getComponents().get(componentName).getField(field.getName()) != null) {
								new ActionChangeTextField(seEntity, field, (JTextField) inputField, componentName)
									.actionPerformed(null);

						}
					}
				}
			}
		};

		SceneEditor.coreEngine.getScheduler().notify(Trigger.COMPONENT_ADDED, onComponentAdded);
		this.onFieldsChanged.add(onComponentAdded);
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
				ActionChangeTextField action = new ActionChangeTextField(seEntity, field, input, component.getName());
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
				ActionChangeTextField action = new ActionChangeTextField(seEntity, field, input, component.getName());
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
