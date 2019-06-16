package ch.sparkpudding.sceneeditor;

import java.util.Locale;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import ch.sparkpudding.sceneeditor.panel.modal.ModalStart;

/**
 *
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba
 *
 */
public class Main {

	public static void main(String[] args) {

		// Set the default locale to english because everything is in english
		Locale.setDefault(Locale.ENGLISH);

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}
		new ModalStart();
	}

}
