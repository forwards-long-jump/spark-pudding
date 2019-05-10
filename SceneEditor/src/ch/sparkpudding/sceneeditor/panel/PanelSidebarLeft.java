package ch.sparkpudding.sceneeditor.panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import ch.sparkpudding.sceneeditor.SceneEditor;
import ch.sparkpudding.sceneeditor.SceneEditor.EditorState;
import ch.sparkpudding.sceneeditor.listener.GameStateEventListener;
import ch.sparkpudding.sceneeditor.utils.ImageStorage;

/**
 * The panel which contains the commands to control the state of the game
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba<br/>
 *         Creation Date : 29 April 2019
 *
 */
@SuppressWarnings("serial")
public class PanelSidebarLeft extends JPanel {

	private JButton btnPausePlay;
	private JButton btnReset;

	private BoxLayout layout;

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

		btnPausePlay = new JButton();
		btnReset = new JButton();

		btnPausePlay.setIcon(ImageStorage.PLAY);
		btnReset.setIcon(ImageStorage.STOP_DISABLED);

		btnReset.setEnabled(false);

		btnPausePlay.setOpaque(false);
		btnPausePlay.setContentAreaFilled(false);
		btnPausePlay.setBorderPainted(false);
		btnReset.setOpaque(false);
		btnReset.setContentAreaFilled(false);
		btnReset.setBorderPainted(false);
	}

	/**
	 * Setup the layout of the panel
	 */
	private void setupLayout() {
		setLayout(layout);

		add(btnPausePlay);
		add(btnReset);

		setBorder(BorderFactory.createCompoundBorder(new EtchedBorder(), new EmptyBorder(10, 10, 10, 10)));
	}

	/**
	 * Add the different listener for each element of the panel
	 */
	private void addListener() {
		btnPausePlay.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (SceneEditor.getGameState() == EditorState.PLAY) {
					SceneEditor.setGameState(EditorState.PAUSE);
				} else {
					SceneEditor.setGameState(EditorState.PLAY);
				}
			}
		});

		btnReset.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				SceneEditor.setGameState(EditorState.STOP);
			}
		});

		SceneEditor.addGameStateEventListener(new GameStateEventListener() {

			@Override
			public void gameStateEvent(EditorState state) {
				switch (state) {
				case PAUSE:
					btnReset.setIcon(ImageStorage.STOP);
					btnPausePlay.setIcon(ImageStorage.PLAY);
					break;
				case PLAY:
					btnReset.setIcon(ImageStorage.STOP);
					btnPausePlay.setIcon(ImageStorage.PAUSE);
					btnReset.setEnabled(true);
					break;
				case STOP:
					btnReset.setIcon(ImageStorage.STOP_DISABLED);
					btnPausePlay.setIcon(ImageStorage.PLAY);
					btnReset.setEnabled(false);
					break;
				default:
					break;

				}
			}
		});
	}
}
