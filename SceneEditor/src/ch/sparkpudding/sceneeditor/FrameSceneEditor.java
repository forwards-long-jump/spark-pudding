package ch.sparkpudding.sceneeditor;

import java.awt.BorderLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JSplitPane;

import ch.sparkpudding.coreengine.Scheduler.Trigger;
import ch.sparkpudding.sceneeditor.SceneEditor.EditorState;
import ch.sparkpudding.sceneeditor.menu.MenuBar;
import ch.sparkpudding.sceneeditor.panel.PanelEditor;
import ch.sparkpudding.sceneeditor.panel.PanelGame;
import ch.sparkpudding.sceneeditor.panel.PanelSidebarLeft;
import ch.sparkpudding.sceneeditor.panel.PanelSidebarRight;
import ch.sparkpudding.sceneeditor.utils.ImageStorage;

/**
 * Main frame containing the SceneEditor and a static reference to the core
 * engine
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba<br/>
 *         Creation Date : 29 April 2019
 *
 */
@SuppressWarnings("serial")
public class FrameSceneEditor extends JFrame {
	private final String TITLE = "Live Editor for LEL";
	private final int WIDTH = 1280;
	private final int HEIGHT = 720;

	private MenuBar menuBar;
	private PanelSidebarRight panelSidebarRight;
	private PanelSidebarLeft panelSidebarLeft;
	private PanelGame panelGame;
	private PanelEditor panelEditor;
	private BorderLayout borderLayout;
	private JSplitPane verticalSplitPanel;
	private JSplitPane horizontalSplitPanel;

	/**
	 * ctor
	 */
	public FrameSceneEditor() {
		init();
		setupFrame();
		setupLayout();
		addListener();

		setVisible(true);

		SceneEditor.coreEngine.getScheduler().schedule(Trigger.GAME_LOOP_START, new Runnable() {
			@Override
			public void run() {
				SceneEditor.setGameState(EditorState.STOP);
			}
		});

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
		panelEditor = new PanelEditor();
	}

	/**
	 * Setup the layout of the frame
	 */
	private void setupLayout() {
		setLayout(borderLayout);
		setJMenuBar(menuBar);

		add(panelSidebarLeft, BorderLayout.WEST);
		horizontalSplitPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panelGame, panelEditor);
		verticalSplitPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, horizontalSplitPanel, panelSidebarRight);
		add(verticalSplitPanel, BorderLayout.CENTER);
	}

	/**
	 * Add the different listener for each element of the panel
	 */
	private void addListener() {
		SceneEditor.coreEngine.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				SceneEditor.coreEngine.requestFocus();
			}
		});

		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				verticalSplitPanel
						.setDividerLocation(e.getComponent().getWidth() - PanelSidebarRight.DEFAULT_PANEL_SIZE);
				horizontalSplitPanel.setDividerLocation(e.getComponent().getHeight() - PanelEditor.DEFAULT_PANEL_HEIGHT);
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
		setIconImage(ImageStorage.ICON.getImage());
		setJMenuBar(menuBar);
		setLocationRelativeTo(null);
	}

}
