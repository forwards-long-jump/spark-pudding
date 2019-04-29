package ch.sparkpudding.sceneeditor.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.JPanel;

import ch.sparkpudding.sceneeditor.FrameSceneEditor;

/**
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba
 * 
 */
@SuppressWarnings("serial")
public class PanelGame extends JPanel {

	public PanelGame() {
		init();
		setupLayout();
	}

	private void init() {
		FrameSceneEditor.ce.setSize(1280, 720);
		FrameSceneEditor.ce.setPreferredSize(new Dimension(1280, 720));
	}

	private void setupLayout() {
		Box vbox = Box.createVerticalBox();
		Box hbox = Box.createHorizontalBox();

		hbox.add(Box.createHorizontalGlue());
		hbox.add(FrameSceneEditor.ce);
		hbox.add(Box.createHorizontalGlue());

		vbox.add(Box.createVerticalGlue());
		vbox.add(hbox);
		vbox.add(Box.createVerticalGlue());

		setLayout(new BorderLayout());
		add(vbox, BorderLayout.CENTER);

		setBackground(Color.GREEN);
	}
}
