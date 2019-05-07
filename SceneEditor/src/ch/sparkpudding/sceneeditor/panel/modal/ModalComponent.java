package ch.sparkpudding.sceneeditor.panel.modal;

import javax.swing.JLabel;
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
	
	public ModalComponent() {
		super();
		this.compNameLabel = new JLabel("Name :");
		this.compNameField = new JTextField(20);
		String[] tableHeaders = {"Name", "Type", "Default value"};
		this.compFieldTable = new JTable(null, tableHeaders);
	}
	
}
