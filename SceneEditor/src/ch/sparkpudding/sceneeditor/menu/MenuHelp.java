package ch.sparkpudding.sceneeditor.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import ch.sparkpudding.sceneeditor.panel.modal.ModalAbout;

/**
 * Represent the Help menu of the SceneEditor
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba<br/>
 *         Creation Date : 30 May 2019
 *
 */
@SuppressWarnings("serial")
public class MenuHelp extends JMenu {

	private JMenuItem itemAbout;

	/**
	 * ctor
	 */
	public MenuHelp() {
		init();
		addAction();
		addKeyStroke();
		addItem();
	}

	private void init() {
		setText("?");

		itemAbout = new JMenuItem("About", KeyEvent.VK_A);
	}

	private void addAction() {
		itemAbout.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new ModalAbout();
			}
		});
	}

	private void addKeyStroke() {
		// No shortcut for about
	}

	private void addItem() {
		add(itemAbout);
	}

}
