package ch.sparkpudding.sceneeditor.panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import ch.sparkpudding.sceneeditor.FrameSceneEditor;

/**
 * The panel which contains the commands to control the state of the game
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba<br/>
 *         Creation Date : 29 avr. 2019
 *
 */
@SuppressWarnings("serial")
public class PanelSidebarLeft extends JPanel {

	private JButton btnPausePlay;
	private JButton btnReset;

	private BoxLayout layout;

	private final String PAUSE_TEXT = "Pause";
	private final String PLAY_TEXT = "Play";

	/**
	 * ctor
	 */
	public PanelSidebarLeft() {
		init();
		setupLayout();
		addListener();
	}

	/**
	 * Initialize the different element of the panel
	 */
	private void init() {
		layout = new BoxLayout(this, BoxLayout.Y_AXIS);

		btnPausePlay = new JButton(PLAY_TEXT);
		btnReset = new JButton("Reset");
	}

	/**
	 * Setup the layout of the panel
	 */
	private void setupLayout() {
		setLayout(layout);

		add(btnPausePlay);
		add(btnReset);
	}

	/**
	 * Add the different listener for each element of the panel
	 */
	private void addListener() {
		btnPausePlay.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FrameSceneEditor.coreEngine.togglePauseAll();
				toggleTextPausePlay();
			}
		});
	}

	/**
	 * Toggle the text shown on the button PlayPause
	 */
	private void toggleTextPausePlay() {
		if (btnPausePlay.getText() == PLAY_TEXT)
			btnPausePlay.setText(PAUSE_TEXT);
		else
			btnPausePlay.setText(PLAY_TEXT);
	}

}
