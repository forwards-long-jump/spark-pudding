package ch.sparkpudding.sceneeditor.panel.modal;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import ch.sparkpudding.coreengine.ecs.component.Component;
import ch.sparkpudding.coreengine.ecs.component.Field;

/**
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba
 * 
 */
@SuppressWarnings("serial")
public class ModalComponent extends Modal {
	JLabel lblCompName;
	JTextField fiCompName;

	JTable tblCompFields;
	DefaultTableModel tblModel;
	JPanel pnlFields;

	JButton btnValidate;
	JButton btnAddField;
	JButton btnRemoveField;

	public ModalComponent() {
		init();
		setupLayout();
		setupFrame();
		setupListener();
	}

	private void init() {
		this.lblCompName = new JLabel("Name :");
		this.fiCompName = new JTextField(20);
		this.pnlFields = new JPanel();
		this.btnValidate = new JButton("OK");
		this.btnAddField = new JButton("+");
		this.btnRemoveField = new JButton("-");

		String[] tableHeaders = { "Name", "Type", "Default value" };
		this.tblModel = new DefaultTableModel(tableHeaders, 2);
		this.tblCompFields = new JTable(tblModel);

	}

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
		mainPanel.add(fiCompName, c);

		pnlFields.setLayout(new BorderLayout());
		pnlFields.add(tblCompFields.getTableHeader(), BorderLayout.PAGE_START);
		pnlFields.add(tblCompFields, BorderLayout.CENTER);

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

	private void setupFrame() {
		setSize(300, 500);
	}

	private void setupListener() {
		// fields.add
		btnRemoveField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tblModel.removeRow(tblModel.getRowCount() - 1);
				pack();
			}
		});

		btnAddField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String[] emptyRow = { "", "", "" };
				tblModel.addRow(emptyRow);
				pack();
			}
		});

		btnValidate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				Map<String, Field> fields = new HashMap<String, Field>();

				for (int i = 0; i < tblCompFields.getRowCount(); i++) {
					String fieldName = (String) tblCompFields.getModel().getValueAt(i, 0);
					String fieldType = (String) tblCompFields.getModel().getValueAt(i, 1);
					String fieldValue = (String) tblCompFields.getModel().getValueAt(i, 2);

					if (fieldName != "" && fieldType != "") {
						fields.put(fieldName, new Field(fieldName, fieldType, fieldValue));
					}
				}

				Component component = new Component(fiCompName.getText(), fields);
				// TODO : pass this component to the current entity

				// TODO : maybe handle better the closing of the window
				dispose();
			}
		});
	}
}
