package ch.sparkpudding.sceneeditor.panel.modal;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
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

/**
 * Create a new component for the current entity with the input in this modal
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba
 * 
 */
@SuppressWarnings("serial")
public class ModalComponent extends Modal {
	JLabel lblCompName;
	JComboBox<String> cmbComponents;

	JTextField fiCompName;
	JComboBox<FieldType> cmbFieldType;
	JTable tblFields;
	DefaultTableModel tblModel;
	JPanel pnlFields;
	JScrollPane pnlFieldsScroll;

	JButton btnValidate;
	JButton btnAddField;
	JButton btnRemoveField;

	Entity entity;

	/**
	 * Constructor for the modal to create a new component
	 * 
	 * @param entity to add component to
	 * 
	 * @param parent Component to block while the modal is active
	 * @param title  Title of the modal
	 * @param modal  True if the parent should be blocked while the modal is active
	 */
	public ModalComponent(Entity entity) {
		super(SceneEditor.frameSceneEditor, "Add component", true);
		init();
		setupLayout();
		setupFieldsTable();
		setupFrame();
		setupListener();

		this.entity = entity;
		
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	/**
	 * Initialize the ui components and their values
	 */
	private void init() {
		this.lblCompName = new JLabel("Name :");
		this.fiCompName = new JTextField(20);
		this.pnlFields = new JPanel();
		this.pnlFieldsScroll = new JScrollPane();
		this.btnValidate = new JButton("OK");
		this.btnAddField = new JButton("+");
		this.btnRemoveField = new JButton("-");
		this.cmbFieldType = new JComboBox<FieldType>();
		this.cmbComponents = new JComboBox<String>();

		// Find all existing components
		for (Entry<String, Component> component : Component.getTemplates().entrySet()) {
			if (!component.getValue().getName().startsWith("se-")) {
				cmbComponents.addItem(component.getValue().getName());
			}
		}

		cmbFieldType.addItem(FieldType.BOOLEAN);
		cmbFieldType.addItem(FieldType.DOUBLE);
		cmbFieldType.addItem(FieldType.FILE_PATH);
		cmbFieldType.addItem(FieldType.INTEGER);
		cmbFieldType.addItem(FieldType.STRING);

		String[] tableHeaders = { "Name", "Type", "Default value" };

		this.tblModel = new DefaultTableModel(tableHeaders, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				if ((column == 0 || column == 1) && entity == null) {
					return false;
				}
				return true;
			}
		};

		this.tblFields = new JTable(tblModel);

		TableColumn cm = tblFields.getColumnModel().getColumn(1);
		cm.setCellEditor(new DefaultCellEditor(cmbFieldType));
	}

	/**
	 * Set the fields table to be scrollable with header always visible
	 */
	private void setupFieldsTable() {
		pnlFields.add(tblFields.getTableHeader(), BorderLayout.PAGE_START);
		pnlFieldsScroll.add(tblFields);
		pnlFieldsScroll.setViewportView(tblFields);

		pnlFieldsScroll.setPreferredSize(new Dimension(250, 150));

		pnlFields.add(pnlFieldsScroll, BorderLayout.CENTER);
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
		mainPanel.add(lblCompName, c);

		c.gridwidth = 1;
		c.gridx = 1;

		// No entity => panel edition
		if (entity != null) {
			mainPanel.add(fiCompName, c);
		} else {
			mainPanel.add(cmbComponents);

			btnAddField.setEnabled(false);
			btnRemoveField.setEnabled(false);
		}

		pnlFields.setLayout(new BorderLayout());
		pnlFields.add(tblFields.getTableHeader(), BorderLayout.PAGE_START);
		pnlFields.add(tblFields, BorderLayout.CENTER);

		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 3;
		c.insets = new Insets(20, 0, 0, 0);
		mainPanel.add(pnlFields, c);

		c.gridy = 2;
		c.gridx = 1;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.EAST;
		c.insets = new Insets(5, 0, 0, 0);
		mainPanel.add(btnRemoveField, c);

		c.gridx = 2;
		mainPanel.add(btnAddField, c);

		c.insets = new Insets(20, 0, 0, 0);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 3;
		mainPanel.add(btnValidate, c);
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
	private void setupListener() {

		// Remove the selected row of the field table or the last row if none is
		// selected
		btnRemoveField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = tblFields.getSelectedRow();
				if (selectedRow == -1) {
					tblModel.removeRow(tblModel.getRowCount() - 1);
				}
				tblModel.removeRow(selectedRow);
				pack();
			}
		});

		// Add a new row in the field table
		btnAddField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String[] emptyRow = { "", "", "" };
				tblModel.addRow(emptyRow);
				pack();
			}
		});

		// Create a component from user input, add it to the current entity the close
		// the modal
		btnValidate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String compName;
				if (entity == null) {
					compName = fiCompName.getText();
				} else {
					compName = (String)cmbComponents.getSelectedItem();
				}
				if (!compName.equals("")) {
					Map<String, Field> fields = new HashMap<String, Field>();

					for (int i = 0; i < tblFields.getRowCount(); i++) {
						String fieldName = (String) tblFields.getModel().getValueAt(i, 0);
						String fieldType = (String)tblFields.getModel().getValueAt(i, 1);
						String fieldValue = (String) tblFields.getModel().getValueAt(i, 2);

						if (fieldName != "" && fieldType != null) {
							fields.put(fieldName, new Field(fieldName, fieldType, fieldValue));
						}
					}

					// This *should* be the same as "From template" when adding it to an entity
					Component component = new Component(compName, fields);

					if (entity == null) {
						Component.addTemplate(component);
					} else {
						(new ActionAddComponent("Add component (" + component.getName() + ")", entity, component)).actionPerformed(null);
					}

					dispose();
				} else {
					fiCompName.setBackground(Color.RED);
				}
			}
		});

		this.cmbComponents.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				tblModel.setRowCount(0);
				// Find all existing components
				for (Entry<String, Component> component : Component.getTemplates().entrySet()) {
					if (component.getValue().getName().equals(cmbComponents.getSelectedItem())) {
						cmbComponents.addItem(component.getValue().getName());
						for (Entry<String, Field> field : component.getValue().getFields().entrySet()) {
							String[] row = { field.getValue().getName(), field.getValue().getType().toString(),
									field.getValue().getValue().toString() };
							tblModel.addRow(row);
						}
						break;
					}
				}

				pack();
			}
		});

		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {

			}
		});
	}
}
