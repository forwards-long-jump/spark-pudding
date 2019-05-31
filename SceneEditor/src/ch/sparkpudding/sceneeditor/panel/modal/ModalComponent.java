package ch.sparkpudding.sceneeditor.panel.modal;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import ch.sparkpudding.coreengine.ecs.component.Component;
import ch.sparkpudding.coreengine.ecs.component.Field;
import ch.sparkpudding.coreengine.ecs.component.Field.FieldType;
import ch.sparkpudding.coreengine.ecs.entity.Entity;
import ch.sparkpudding.sceneeditor.SceneEditor;
import ch.sparkpudding.sceneeditor.action.ActionAddComponent;
import ch.sparkpudding.sceneeditor.ecs.SEEntity;
import ch.sparkpudding.sceneeditor.utils.ImageStorage;

/**
 * Create a new component for the current entity with the input in this modal
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba
 * 
 */
@SuppressWarnings("serial")
public class ModalComponent extends Modal {
	private JLabel labelComponentName;
	private JComboBox<String> cmbComponents;

	private JTextField fieldComponentName;
	private JComboBox<FieldType> comboBoxFieldType;
	private JTable tableFields;
	private DefaultTableModel tableModel;
	private JPanel panelFields;
	private JScrollPane panelFieldsScroll;

	private JButton buttonValidate;
	private JButton buttonAddField;
	private JButton buttonRemoveField;

	private Entity entity;
	private SEEntity seEntity;

	/**
	 * Constructor for the modal to create a new component
	 * 
	 * @param entity to add component to
	 */
	public ModalComponent(SEEntity seEntity, Entity entity) {
		super(SceneEditor.frameSceneEditor, "Add component", true);
		this.entity = entity;
		this.seEntity = seEntity;

		if (init()) {
			setupLayout();
			setupFieldsTable();
			setupFrame();
			addListener();

			pack();
			setLocationRelativeTo(null);
			setVisible(true);
		}
	}

	/**
	 * Initialize the ui components and their values
	 * 
	 * @return true if everything went well
	 */
	private boolean init() {
		this.labelComponentName = new JLabel("Name :");
		this.fieldComponentName = new JTextField(20);
		this.panelFields = new JPanel();
		this.panelFieldsScroll = new JScrollPane();
		this.buttonValidate = new JButton("OK");
		this.comboBoxFieldType = new JComboBox<FieldType>();
		this.cmbComponents = new JComboBox<String>();
		
		this.buttonAddField = new JButton(ImageStorage.PLUS);
		buttonAddField.setBorderPainted(false);
		buttonAddField.setContentAreaFilled(false);
		this.buttonRemoveField = new JButton(ImageStorage.TRASH);
		buttonRemoveField.setBorderPainted(false);
		buttonRemoveField.setContentAreaFilled(false);

		comboBoxFieldType.addItem(FieldType.BOOLEAN);
		comboBoxFieldType.addItem(FieldType.DOUBLE);
		comboBoxFieldType.addItem(FieldType.FILE_PATH);
		comboBoxFieldType.addItem(FieldType.INTEGER);
		comboBoxFieldType.addItem(FieldType.STRING);

		String[] tableHeaders = { "Name", "Type", "Default value" };

		this.tableModel = new DefaultTableModel(tableHeaders, 3) {
			@Override
			public boolean isCellEditable(int row, int column) {
				if ((column == 0 || column == 1) && entity != null) {
					return false;
				}
				return true;
			}
		};

		this.tableFields = new JTable(tableModel);

		TableColumn cm = tableFields.getColumnModel().getColumn(1);
		cm.setCellEditor(new DefaultCellEditor(comboBoxFieldType));

		ActionMap am = this.tableFields.getActionMap();
		am.put("selectPreviousColumnCell", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
				manager.focusPreviousComponent();
			}
		});

		am.put("selectNextColumnCell", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
				manager.focusNextComponent();
			}
		});

		// Find all existing components
		for (Entry<String, Component> component : Component.getTemplates().entrySet()) {
			if (!component.getValue().getName().startsWith("se-")) {
				if (entity == null || !entity.hasComponent(component.getValue().getName())) {
					cmbComponents.addItem(component.getValue().getName());
				}
			}
		}

		if (cmbComponents.getItemCount() == 0) {
			JOptionPane.showMessageDialog(this, "This entity already has all existing components.");
			dispose();
			return false;
		}

		if (entity != null) {
			// Display table every time, could be optimised but it means handling which one
			// is selected
			cmbComponents.setSelectedIndex(0);
			displayFieldsForComponent(cmbComponents.getItemAt(0));
		}

		return true;
	}

	/**
	 * Set the fields table to be scrollable with header always visible
	 */
	private void setupFieldsTable() {
		panelFields.add(tableFields.getTableHeader(), BorderLayout.PAGE_START);
		panelFieldsScroll.add(tableFields);
		panelFieldsScroll.setViewportView(tableFields);

		panelFieldsScroll.setPreferredSize(new Dimension(250, 150));

		panelFields.add(panelFieldsScroll, BorderLayout.CENTER);
	}

	/**
	 * Set how the components display using a GridBagLayout
	 */
	private void setupLayout() {

		mainPanel.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.CENTER;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(0, 0, 0, 20);
		mainPanel.add(labelComponentName, c);

		c.gridwidth = 1;
		c.gridx = 1;

		// No entity => panel edition
		if (entity == null) {
			mainPanel.add(fieldComponentName, c);
			panelFields.setLayout(new BorderLayout());
			panelFields.add(tableFields.getTableHeader(), BorderLayout.PAGE_START);
			panelFields.add(tableFields, BorderLayout.CENTER);
			c.gridx = 0;
			c.gridy = 1;
			c.gridwidth = 3;
			c.insets = new Insets(20, 0, 0, 0);
			mainPanel.add(panelFields, c);
			
			c.gridy = 2;
			c.gridx = 1;
			c.gridwidth = 1;
			c.fill = GridBagConstraints.NONE;
			c.anchor = GridBagConstraints.EAST;
			c.insets = new Insets(5, 0, 0, 0);
			mainPanel.add(buttonRemoveField, c);
			
			c.gridx = 2;
			mainPanel.add(buttonAddField, c);
		} else {
			mainPanel.add(cmbComponents);

			buttonAddField.setEnabled(false);
			buttonRemoveField.setEnabled(false);
		}



		c.insets = new Insets(20, 0, 0, 0);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 3;
		mainPanel.add(buttonValidate, c);
	}

	/**
	 * Set the frame size and parameters
	 */
	private void setupFrame() {
		setSize(300, 500);
		setResizable(false);
	}

	/**
	 * Setup the listeners for the components and the frame
	 */
	private void addListener() {

		// Remove the selected row of the field table or the last row if none is
		// selected
		buttonRemoveField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = tableFields.getSelectedRow();
				if (tableModel.getRowCount() > 0) {
					if (selectedRow == -1) {
						tableModel.removeRow(tableModel.getRowCount() - 1);
					} else {
						tableModel.removeRow(selectedRow);
					}
				}
				pack();
			}
		});

		// Add a new row in the field table
		buttonAddField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String[] emptyRow = { "", "", "" };
				tableModel.addRow(emptyRow);
				pack();
			}
		});

		// Create a component from user input, add it to the current entity the close
		// the modal
		buttonValidate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String compName;
				if (entity == null) {
					compName = fieldComponentName.getText();
				} else {
					compName = (String) cmbComponents.getSelectedItem();
				}
				if (!compName.equals("")) {
					Map<String, Field> fields = new HashMap<String, Field>();

					for (int i = 0; i < tableFields.getRowCount(); i++) {
						try {
							String fieldName = (String) tableFields.getModel().getValueAt(i, 0);
							String fieldType;

							if (tableFields.getModel().getValueAt(i, 1) instanceof FieldType) {
								fieldType = ((FieldType) tableFields.getModel().getValueAt(i, 1)).name();
							} else {
								fieldType = (String) tableFields.getModel().getValueAt(i, 1);
							}

							String fieldValue = (String) tableFields.getModel().getValueAt(i, 2);

							if (fieldType != null && fieldName != null) {
								if (fieldValue == null) {
									if (fieldType.equals("STRING") || fieldType.equals("FILE_PATH")) {
										fieldValue = "";
									} else {
										fieldValue = "0";
									}
								}

								fields.put(fieldName, new Field(fieldName, fieldType, fieldValue));
							}
						} catch (Exception exception) {
							JOptionPane.showMessageDialog(ModalComponent.this, "Please check your inputs.");
							return;
						}
					}

					// This *should* be the same as "From template" when adding it to an entity
					Component component = new Component(compName, fields);

					if (entity == null) {
						Component.addTemplate(component);
					} else {
						(new ActionAddComponent(seEntity, entity, component)).actionPerformed(null);
					}

					dispose();
				} else {
					fieldComponentName.setBackground(Color.RED);
				}
			}
		});

		this.cmbComponents.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				displayFieldsForComponent(cmbComponents.getSelectedItem().toString());
			}
		});

		this.cmbComponents.addKeyListener(new KeyAdapter() {

			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					buttonValidate.doClick();
				}
			}
		});
	}

	/**
	 * Update the table to display fields from selected component
	 */
	private void displayFieldsForComponent(String componentName) {
		tableModel.setRowCount(0);
		// Find all existing components
		for (Entry<String, Component> component : Component.getTemplates().entrySet()) {
			if (component.getValue().getName().equals(componentName)) {
				for (Entry<String, Field> field : component.getValue().getFields().entrySet()) {
					String[] row = { field.getValue().getName(), field.getValue().getType().toString(),
							field.getValue().getValue().toString() };
					tableModel.addRow(row);
				}
				break;
			}
		}

		pack();
	}
}
