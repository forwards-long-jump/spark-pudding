package ch.sparkpudding.sceneeditor.panel.modal;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;

/**
 * Base modal for all the project's modals
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba
 * 
 */
@SuppressWarnings("serial")
public abstract class Modal extends JDialog {

	JPanel mainPanel;

	private static final KeyStroke escapeStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
	public static final String dispatchWindowClosingActionMapKey = "com.spodding.tackline.dispatch:WINDOW_CLOSING";

	/**
	 * Base constructor for the project's modal, prepare the modal to have the right
	 * behavior
	 * 
	 * @param parent Component to block while the modal is active
	 * @param title  Title of the modal
	 * @param modal  True if the parent should be blocked while the modal is active
	 */
	public Modal(JFrame parent, String title, Boolean modal) {
		super(parent, title, modal);
		this.setLayout(new FlowLayout());

		this.mainPanel = new JPanel();
		this.mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		this.add(mainPanel);

		installEscapeCloseOperation(this);
	}

	/**
	 * Allow to press escape to close the modal
	 * Source: https://stackoverflow.com/questions/642925/swing-how-do-i-close-a-dialog-when-the-esc-key-is-pressed
	 * 
	 * @param dialog
	 */
	public static void installEscapeCloseOperation(final JDialog dialog) {
		Action dispatchClosing = new AbstractAction() {
			public void actionPerformed(ActionEvent event) {
				dialog.dispatchEvent(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING));
			}
		};
		JRootPane root = dialog.getRootPane();
		root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeStroke, dispatchWindowClosingActionMapKey);
		root.getActionMap().put(dispatchWindowClosingActionMapKey, dispatchClosing);
	}

}
