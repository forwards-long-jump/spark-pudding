package ch.sparkpudding.sceneeditor.menu;

import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

/**
 * Represent the MenuFile of the SceneEditor
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba<br/>
 *         Creation Date : 9 mai 2019
 *
 */
@SuppressWarnings("serial")
public class MenuFile extends JMenu {

	private JMenuItem itemNew;
	private JMenuItem itemSave;
	private JMenuItem itemExit;

	/**
	 * ctor
	 */
	public MenuFile() {
		init();
		addAction();
		addKeyStroke();
		addItem();
	}

	/**
	 * Set basic attributes and create item
	 */
	private void init() {
		setText("File");

		itemNew = new JMenuItem("New", KeyEvent.VK_N);
		itemSave = new JMenuItem("Save", KeyEvent.VK_S);
		itemExit = new JMenuItem("Exit", KeyEvent.VK_X);
	}

	/**
	 * Add the shortcut to the different item
	 */
	private void addAction() {
		// TODO: Implement method
	}

	/**
	 * Add the shortcut to the different item
	 */
	private void addKeyStroke() {
		itemNew.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.ALT_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));
		itemSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
	}

	/**
	 * Add the item to the menu
	 */
	private void addItem() {
		add(itemNew);
		add(itemSave);
		addSeparator();
		add(itemExit);
	}

}
