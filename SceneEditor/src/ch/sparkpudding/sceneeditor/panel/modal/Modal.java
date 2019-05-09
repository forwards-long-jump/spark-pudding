package ch.sparkpudding.sceneeditor.panel.modal;

import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Base modal for all the project's modals
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba
 * 
 */
@SuppressWarnings("serial")
public abstract class Modal extends JDialog {

	JPanel mainPanel;

	/**
	 * Base constructor for the project's modal, prepare the modal to have the right
	 * behavior
	 * 
	 * @param parent Component to block while the modal is active
	 * @param title  Title of the modal
	 * @param modal  True if the parent should be blocked while the modal is active
	 */
	public Modal(JFrame parent, String title, Boolean modal) {
		super(parent, title, modal);
		this.setLayout(new FlowLayout());

		this.mainPanel = new JPanel();
		this.mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		this.add(mainPanel);
	}

}
