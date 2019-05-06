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

	private static final String TITLE = "Scene Editor";
	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;

	private MenuBar menuBar;
	private PanelSidebarRight panelSidebarRight;
	private PanelSidebarLeft panelSidebarLeft;
	private PanelGame panelGame;
	private BorderLayout borderLayout;

	public static CoreEngine coreEngine;

	/**
	 * ctor
	 * 
	 * @param gameFolder the path to the folder containing the current game
	 * @throws Exception thrown if the coreEngine can't read the gameFolder
	 */
	public FrameSceneEditor(String gameFolder) throws Exception {
		FrameSceneEditor.coreEngine = new CoreEngine(gameFolder);
		FrameSceneEditor.coreEngine.togglePauseAll();

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
		FrameSceneEditor.coreEngine.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				FrameSceneEditor.coreEngine.requestFocus();
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

}
