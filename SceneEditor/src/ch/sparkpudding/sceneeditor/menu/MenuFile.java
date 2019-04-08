package ch.sparkpudding.sceneeditor.menu;

import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class MenuFile extends JMenu {
	
	private JMenuItem itemNew;
	private JMenuItem itemSave;
	
	public MenuFile()
	{
		init();
		addItem();
	}

	private void init() {
		setText("File");
		
		itemNew = new JMenuItem("New", KeyEvent.VK_1);
		itemSave = new JMenuItem("Save");
	}

	private void addItem() {
		add(itemNew);
		add(itemSave);
	}

}
