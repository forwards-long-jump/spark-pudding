package ch.sparkpudding.sceneeditor.panel;

import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class PanelSidebarLeft extends JPanel {
	JButton btnPlay;
	JButton btnReset;
	
	BoxLayout layout;
	
	public PanelSidebarLeft() {
		btnPlay = new JButton("Play");
		btnReset = new JButton("Reset");
		
		layout = new BoxLayout(this, BoxLayout.Y_AXIS);
		
		setLayout(layout);
		
		add(btnPlay);
		add(btnReset);
	}
}
