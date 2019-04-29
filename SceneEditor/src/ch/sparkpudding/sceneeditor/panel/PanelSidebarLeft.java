package ch.sparkpudding.sceneeditor.panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import ch.sparkpudding.sceneeditor.FrameSceneEditor;

/**
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba
 * 
 */
@SuppressWarnings("serial")
public class PanelSidebarLeft extends JPanel {

	private JButton btnPausePlay;
	private JButton btnReset;

	private BoxLayout layout;

	private final String PAUSE_TEXT = "Pause";
	private final String PLAY_TEXT = "Play";

	public PanelSidebarLeft() {
		init();
		setupLayout();
		addListener();
	}

	private void init() {
		layout = new BoxLayout(this, BoxLayout.Y_AXIS);

		btnPausePlay = new JButton(PLAY_TEXT);
		btnReset = new JButton("Reset");
	}

	private void setupLayout() {
		setLayout(layout);

		add(btnPausePlay);
		add(btnReset);
	}

	private void addListener() {
		btnPausePlay.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FrameSceneEditor.ce.togglePauseAll();
				toggleTextPausePlay();
			}
		});
	}

	private void toggleTextPausePlay() {
		if (btnPausePlay.getText() == PLAY_TEXT)
			btnPausePlay.setText(PAUSE_TEXT);
		else
			btnPausePlay.setText(PLAY_TEXT);
	}

}
