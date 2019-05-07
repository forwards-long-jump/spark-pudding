package ch.sparkpudding.sceneeditor.panel.modal;

import java.awt.BorderLayout;
import java.awt.Dimension;
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

		String[] tableHeaders = { "Name", "Type", "Default value" };
		this.tblModel = new DefaultTableModel(tableHeaders, 2);
		this.tblCompFields = new JTable(tblModel);

	}

	private void setupLayout() {
		mainPanel.setLayout(new GridBagLayout());
		mainPanel.setMinimumSize(new Dimension(3000, 300));

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.NORTHWEST;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(0, 0, 0, 20);
		mainPanel.add(lblCompName, c);

		c.gridx = 1;
		mainPanel.add(fiCompName, c);

		pnlFields.setLayout(new BorderLayout());
		pnlFields.add(tblCompFields.getTableHeader(), BorderLayout.PAGE_START);
		pnlFields.add(tblCompFields, BorderLayout.CENTER);
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		c.insets = new Insets(20, 0, 0, 0);
		mainPanel.add(pnlFields, c);

		c.insets = new Insets(20, 0, 0, 0);
		c.gridx = 1;
		c.gridy = 3;
		c.gridwidth = 1;
		mainPanel.add(btnValidate, c);

		c.gridy = 2;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.EAST;
		c.insets = new Insets(5, 0, 0, 0);
		mainPanel.add(btnAddField, c);
	}

	private void setupFrame() {
		setSize(300, 500);
	}

	private void setupListener() {
		// fields.add
		btnAddField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String[] emptyRow = { "", "", "" };
				tblModel.addRow(emptyRow);
			}
		});

		btnValidate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO : create new component and add it where it belongs
				Map<String, Field> fields = new HashMap<String, Field>();
				Component c = new Component(fiCompName.getText(), fields);
				// TODO : maybe handle better the closing of the window
				dispose();
			}
		});
	}
}
