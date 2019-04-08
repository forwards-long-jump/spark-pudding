package ch.sparkpudding.sceneeditor.menu;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import java.awt.event.KeyEvent;

public class MenuEdit extends JMenu {
	
	private JMenuItem itemUndo;
	private JMenuItem itemRedo;
	
	public MenuEdit()
	{
		init();
		addItem();
	}

	private void init() {
		setText("Edit");
		
		itemUndo = new JMenuItem("Undo", KeyEvent.VK_1);
		itemRedo = new JMenuItem("Redo");
	}

	private void addItem() {
		add(itemUndo);
		add(itemRedo);
	}
}
