package ch.sparkpudding.sceneeditor.menu;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * 
 * @author Alexandre Bianchi
 *
 */
public class MenuEdit extends JMenu {

	private JMenuItem itemUndo;
	private JMenuItem itemRedo;

	public MenuEdit() {
		init();
		addKeyStroke();
		addItem();
	}

	/**
	 * Set basic attributes and create item
	 */
	private void init() {
		setText("Edit");

		itemUndo = new JMenuItem("Undo", KeyEvent.VK_1);
		itemRedo = new JMenuItem("Redo");
	}

	/**
	 * Add the shortcut to the different item
	 */
	private void addKeyStroke() {
		itemUndo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK));
		itemRedo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_DOWN_MASK));
	}

	/**
	 * Add the item to the menu
	 */
	private void addItem() {
		add(itemUndo);
		add(itemRedo);
	}
}
