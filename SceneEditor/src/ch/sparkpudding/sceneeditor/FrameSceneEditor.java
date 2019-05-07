package ch.sparkpudding.sceneeditor;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

import ch.sparkpudding.coreengine.CoreEngine;
import ch.sparkpudding.sceneeditor.menu.MenuBar;
import ch.sparkpudding.sceneeditor.panel.PanelGame;
import ch.sparkpudding.sceneeditor.panel.PanelSidebarLeft;
import ch.sparkpudding.sceneeditor.panel.PanelSidebarRight;
import ch.sparkpudding.sceneeditor.panel.modal.ModalComponent;

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
		
		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				System.out.println("lala");
				if(e.getKeyChar() == 'c')
				{					
					ModalComponent mc = new ModalComponent();
					mc.setVisible(true);
				}		
			}
		});
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
