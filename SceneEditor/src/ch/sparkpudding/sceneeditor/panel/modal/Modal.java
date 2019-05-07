package ch.sparkpudding.sceneeditor.panel.modal;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba
 * 
 */
@SuppressWarnings("serial")
public class Modal extends JFrame {

	JPanel mainPanel;

	public Modal() {
		super();
		this.setSize(300, 500);
		this.setLayout(new BorderLayout());
		
		this.mainPanel = new JPanel();
		this.mainPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
		this.add(mainPanel, BorderLayout.NORTH);
	}

}
