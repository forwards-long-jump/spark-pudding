package ch.sparkpudding.sceneeditor.panel.modal;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;

/**
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba
 * 
 */
@SuppressWarnings("serial")
public class ModalComponent extends Modal {
	JLabel compNameLabel;
	JTextField compNameField;

	JTable compFieldTable;
	JPanel fields;

	JButton compButton;

	public ModalComponent() {
		init();
		setupLayout();
		setupFrame();

		this.compNameLabel = new JLabel("Name :");
		this.compNameField = new JTextField(20);
		this.fields = new JPanel();
		this.compButton = new JButton("OK");
	}

	private void init() {
		this.compNameLabel = new JLabel("Name :");
		this.compNameField = new JTextField(20);
		this.fields = new JPanel();
		this.compButton = new JButton("OK");

		String[] tableHeaders = { "Name", "Type", "Default value" };
		Object[][] data = { { "Toto", "STRING", "LEL" } };
		this.compFieldTable = new JTable(data, tableHeaders);

	}

	private void setupLayout() {
		mainPanel.setLayout(new GridBagLayout());
		mainPanel.setMinimumSize(new Dimension(3000,300));
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.NORTHWEST;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(0, 0, 0, 20);
		mainPanel.add(compNameLabel, c);
		
		c.gridx = 1;
		mainPanel.add(compNameField, c);
		
		fields.setLayout(new BorderLayout());
		fields.add(compFieldTable.getTableHeader(), BorderLayout.PAGE_START);
		fields.add(compFieldTable, BorderLayout.CENTER);
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		c.insets = new Insets(20, 0, 0, 0);
		mainPanel.add(fields, c);
		
		c.insets = new Insets(20,0,0,0);
		c.gridx = 1;
		c.gridy = 2;
		c.gridwidth = 1;
		mainPanel.add(compButton, c);
	}

	private void setupFrame() {
		// TODO Auto-generated method stub

	}

}
