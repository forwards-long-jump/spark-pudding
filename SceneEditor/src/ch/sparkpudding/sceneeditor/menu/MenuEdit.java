package ch.sparkpudding.sceneeditor.menu;

import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import ch.sparkpudding.sceneeditor.action.AbstractAction;
import ch.sparkpudding.sceneeditor.action.ActionRedo;
import ch.sparkpudding.sceneeditor.action.ActionUndo;
import ch.sparkpudding.sceneeditor.action.ActionsHistory;
import ch.sparkpudding.sceneeditor.action.HistoryEventListener;

/**
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba
 * 
 */
@SuppressWarnings("serial")
public class MenuEdit extends JMenu {

	private JMenuItem itemUndo;
	private JMenuItem itemRedo;

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
	}

	/**
	 * Add the shortcut to the different item
	 */
	private void addAction() {
		itemUndo.setAction(new ActionUndo());
		itemRedo.setAction(new ActionRedo());

		itemUndo.setEnabled(false);
		itemRedo.setEnabled(false);

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
