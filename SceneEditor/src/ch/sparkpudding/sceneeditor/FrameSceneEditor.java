package ch.sparkpudding.sceneeditor;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;

import ch.sparkpudding.coreengine.CoreEngine;
import ch.sparkpudding.sceneeditor.menu.MenuBar;
import ch.sparkpudding.sceneeditor.panel.PanelGame;
import ch.sparkpudding.sceneeditor.panel.PanelSidebarLeft;
import ch.sparkpudding.sceneeditor.panel.PanelSidebarRight;

/**
 * Main frame containing the SceneEditor and a static reference to the core
 * engine
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba<br/>
 *         Creation Date : 29 avr. 2019
 *
 */
@SuppressWarnings("serial")
public class FrameSceneEditor extends JFrame {
	public enum EDITOR_STATE {
		PLAY, PAUSE, STOP;
	}
	
	private final String TITLE = "Scene Editor";
	private final int WIDTH = 1280;
	private final int HEIGHT = 720;

	private MenuBar menuBar;
	private PanelSidebarRight panelSidebarRight;
	private PanelSidebarLeft panelSidebarLeft;
	private PanelGame panelGame;
	private BorderLayout borderLayout;

	private EDITOR_STATE gameState;

	/**
	 * ctor
	 * 
	 * @param gameFolder the path to the folder containing the current game
	 * @throws Exception thrown if the coreEngine can't read the gameFolder
	 */
	public FrameSceneEditor(String gameFolder) throws Exception {
		Lel.frameSceneEditor = this;
		Lel.coreEngine = new CoreEngine(gameFolder);
		Lel.coreEngine.togglePauseAll();
		
		gameState = EDITOR_STATE.PAUSE;

		init();
		setupLayout();
		setupFrame();
		addListener();
	}

	/**
	 * Initialize the different element of the frame
	 */
	private void init() {
		borderLayout = new BorderLayout();

		menuBar = new MenuBar();

		panelSidebarRight = new PanelSidebarRight();
		panelSidebarLeft = new PanelSidebarLeft();
		panelGame = new PanelGame();
	}

	/**
	 * Setup the layout of the frame
	 */
	private void setupLayout() {
		setLayout(borderLayout);
		setJMenuBar(menuBar);

		add(panelSidebarRight, BorderLayout.EAST);
		add(panelSidebarLeft, BorderLayout.WEST);
		add(panelGame, BorderLayout.CENTER);
	}

	/**
	 * Add the different listener for each element of the panel
	 */
	private void addListener() {
		Lel.coreEngine.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				Lel.coreEngine.requestFocus();
			}
		});
	}

	/**
	 * Setup the basic value of the Frame
	 */
	private void setupFrame() {
		setSize(WIDTH, HEIGHT);
		setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle(TITLE);
		setJMenuBar(menuBar);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	/**
	 * Get current editor state
	 * @return current editor state
	 */
	public EDITOR_STATE getGameState() {
		return gameState;
	}
	
	/**
	 *  Change editor state
	 */
	public void setGameState(EDITOR_STATE state) {
		gameState = state;
		switch(state) {
		case PAUSE:
			Lel.coreEngine.togglePauseAll();
			break;
		case PLAY:
			Lel.coreEngine.togglePauseAll();
			break;
		case STOP:
			Lel.coreEngine.scheduleResetCurrentScene(true);
			break;
		default:
			break;
		
		}
		panelSidebarLeft.notifyStateChange(state);
	}


}
