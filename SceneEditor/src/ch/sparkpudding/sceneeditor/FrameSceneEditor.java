package ch.sparkpudding.sceneeditor;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import ch.sparkpudding.sceneeditor.menu.MenuBar;

public class FrameSceneEditor extends JFrame {

	private static final String TITLE = "Scene Editor";
	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;

	private MenuBar menuBar;
	private BorderLayout borderLayout;

	public FrameSceneEditor() {
		init();
		setupFrame();
	}

	private void init() {
		borderLayout = new BorderLayout();
		menuBar = new MenuBar();
	}

	private void setupFrame() {
		setSize(WIDTH, HEIGHT);
		setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle(TITLE);
		setJMenuBar(menuBar);
		setLayout(borderLayout);
		setLocationRelativeTo(null);
		setVisible(true);
	}

}
