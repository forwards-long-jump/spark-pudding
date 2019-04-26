package ch.sparkpudding.sceneeditor;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;

import ch.sparkpudding.coreengine.CoreEngine;
import ch.sparkpudding.sceneeditor.menu.MenuBar;
import ch.sparkpudding.sceneeditor.panel.PanelGame;
import ch.sparkpudding.sceneeditor.panel.PanelSidebarLeft;
import ch.sparkpudding.sceneeditor.panel.PanelSidebarRight;

/**
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba
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
	
	private CoreEngine ce;

	public FrameSceneEditor(String gameFolder) throws Exception {
		init(gameFolder);
		setupLayout();
		setupFrame();
	}

	private void init(String gameFolder) throws Exception {
		borderLayout = new BorderLayout();

		menuBar = new MenuBar();

		ce = new CoreEngine(gameFolder);
		ce.togglePauseAll();
		
		panelSidebarRight = new PanelSidebarRight(ce);
		panelSidebarLeft = new PanelSidebarLeft(ce);
		panelGame = new PanelGame(ce);
	}

	private void setupLayout() {
		setLayout(borderLayout);
		setJMenuBar(menuBar);

		add(panelSidebarRight, BorderLayout.EAST);
		add(panelSidebarLeft, BorderLayout.WEST);
		add(panelGame, BorderLayout.CENTER);

		// FIXME Remove color
		panelSidebarLeft.setBackground(Color.BLUE);
		panelSidebarRight.setBackground(Color.RED);
	}

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
