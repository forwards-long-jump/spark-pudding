package ch.sparkpudding.sceneeditor.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import ch.sparkpudding.sceneeditor.SceneEditor;
import ch.sparkpudding.sceneeditor.filewriter.LelWriter;

/**
 * Represent the MenuFile of the SceneEditor
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba<br/>
 *         Creation Date : 9 May 2019
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
		
		// TODO: This action is not working at this moment and have been disabled in the meantime
		itemNew.setEnabled(false);
		itemExit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
	}

	/**
	 * Add the shortcut to the different item
	 */
	private void addAction() {
		itemSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				LelWriter lel = new LelWriter();
				try {
					lel.save(SceneEditor.coreEngine, SceneEditor.gamePath + "/");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		itemNew.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				LelWriter lel = new LelWriter();
				String newGamePath;

				JFileChooser folderChooser = new JFileChooser();
				folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

				int returnVal = folderChooser.showSaveDialog(SceneEditor.frameSceneEditor);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					newGamePath = folderChooser.getSelectedFile().getAbsolutePath();
				} else {
					return;
				}

				lel.create(newGamePath + "/", true); // Not fully implemented yet. best not use it
			}
		});
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
