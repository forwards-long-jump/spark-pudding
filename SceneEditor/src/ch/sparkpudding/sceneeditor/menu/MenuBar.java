package ch.sparkpudding.sceneeditor.menu;

import javax.swing.JMenuBar;

/**
 * Represent the MenuBar of the SceneEditor
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba<br/>
 *         Creation Date : 9 May 2019
 *
 */
@SuppressWarnings("serial")
public class MenuBar extends JMenuBar {

	private MenuFile menuFile;
	private MenuEdit menuEdit;
	private MenuCamera menuCamera;

	/**
	 * ctor
	 */
	public MenuBar() {
		init();
		addMenu();
	}

	/**
	 * Initialize the different element of the panel
	 */
	private void init() {
		menuFile = new MenuFile();
		menuEdit = new MenuEdit();
		menuCamera = new MenuCamera();
	}

	/**
	 * Add the different menu to the menu bar
	 */
	private void addMenu() {
		add(menuFile);
		add(menuEdit);
		add(menuCamera);
	}
}
