package ch.sparkpudding.sceneeditor;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;

import ch.sparkpudding.sceneeditor.menu.MenuBar;
import ch.sparkpudding.sceneeditor.panel.PanelGame;
import ch.sparkpudding.sceneeditor.panel.PanelSidebarLeft;
import ch.sparkpudding.sceneeditor.panel.PanelSidebarRight;

public class FrameSceneEditor extends JFrame {

	private static final String TITLE = "Scene Editor";
	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;

	private MenuBar menuBar;
	private PanelSidebarRight panelSidebarRight;
	private PanelSidebarLeft panelSidebarLeft;
	private PanelGame panelGame;
	private BorderLayout borderLayout;

	public FrameSceneEditor() {
		init();
		setupLayout();
		setupFrame();
	}

	private void init() {
		borderLayout = new BorderLayout();
		
		menuBar = new MenuBar();

		panelSidebarRight = new PanelSidebarRight();
		panelSidebarLeft = new PanelSidebarLeft();
		panelGame = new PanelGame();
	}

	private void setupLayout() {
		setLayout(borderLayout);
		setJMenuBar(menuBar);
		
		add(panelSidebarRight, BorderLayout.EAST);
		add(panelSidebarLeft, BorderLayout.WEST);
		add(panelGame, BorderLayout.CENTER);
		
		panelSidebarLeft.setBackground(Color.BLUE);
		panelSidebarRight.setBackground(Color.RED);
		panelGame.setBackground(Color.GREEN);
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
