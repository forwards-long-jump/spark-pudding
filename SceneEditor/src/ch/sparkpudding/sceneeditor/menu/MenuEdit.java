package ch.sparkpudding.sceneeditor.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import ch.sparkpudding.sceneeditor.action.AbstractAction;
import ch.sparkpudding.sceneeditor.action.ActionRedo;
import ch.sparkpudding.sceneeditor.action.ActionUndo;
import ch.sparkpudding.sceneeditor.action.ActionsHistory;
import ch.sparkpudding.sceneeditor.listener.HistoryEventListener;
import ch.sparkpudding.sceneeditor.panel.modal.ModalComponent;

/**
 * Represent the MenuEdit of the SceneEditor
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba<br/>
 *         Creation Date : 9 May 2019
 *
 */
@SuppressWarnings("serial")
public class MenuEdit extends JMenu {

	private JMenuItem itemUndo;
	private JMenuItem itemRedo;
	private JMenuItem itemCreateComponent;

	/**
	 * ctor
	 */
	public MenuEdit() {
		init();
		addAction();
		addKeyStroke();
		addItem();
	}

	/**
	 * Set basic attributes and create item
	 */
	private void init() {
		setText("Edit");

		itemUndo = new JMenuItem("Undo");
		itemRedo = new JMenuItem("Redo");
		itemCreateComponent = new JMenuItem("Create component");
	}

	/**
	 * Add the shortcut to the different item
	 */
	private void addAction() {
		itemUndo.setAction(new ActionUndo());
		itemRedo.setAction(new ActionRedo());

		itemUndo.setEnabled(false);
		itemRedo.setEnabled(false);

		itemCreateComponent.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				new ModalComponent(null);
			}
		});

		ActionsHistory.getInstance().addHistoryEventListener(new HistoryEventListener() {
			@Override
			public void historyEvent(int stackPointer, int stackSize) {
				if (stackPointer >= 0) {
					itemUndo.setEnabled(true);
				} else {
					itemUndo.setEnabled(false);
				}

				if (stackPointer < stackSize - 1) {
					itemRedo.setEnabled(true);
				} else {
					itemRedo.setEnabled(false);
				}

				updateActionsHistory(stackPointer, stackSize);
			}
		});
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
		addSeparator();
		add(itemCreateComponent);
		addSeparator();
	}

	/**
	 * Rebuild and show the ActionsHistory
	 * 
	 * @param stackPointer The point position of the stack
	 * @param stackSize    The size of the stack
	 */
	private void updateActionsHistory(int stackPointer, int stackSize) {
		removeAll();
		addItem();

		for (int i = stackSize - 1; i >= stackSize - 5 && i >= 0; i--) {
			AbstractAction action = ActionsHistory.getInstance().getActionAt(i);
			JMenuItem jMenuItem = new JMenuItem(new ActionUndo(action));

			if (i > stackPointer) {
				jMenuItem.setEnabled(false);
			}

			add(jMenuItem);
		}

		if (stackSize > 5) {
			// TODO Allow to see all the history
			add("...");
		}
	}
}
