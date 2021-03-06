package ch.sparkpudding.sceneeditor.panel.modal;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import ch.sparkpudding.sceneeditor.SceneEditor;
import ch.sparkpudding.sceneeditor.action.ActionAddSystem;

/**
 * Create a new system from the given name
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba<br/>
 *         Creation Date : 28 May 2019
 *
 */
@SuppressWarnings("serial")
public class ModalSystem extends Modal {

	private JLabel labelName;
	private JTextField textFieldName;
	private JButton buttonAdd;
	private JButton buttonCancel;

	/**
	 * ctor
	 */
	public ModalSystem() {
		super(SceneEditor.frameSceneEditor, "New system", true);
		init();
		setupLayout();
		setupListener();
		setupFrame();
	}

	/**
	 * Initialize the ui components and their values
	 */
	private void init() {
		labelName = new JLabel("Name :");
		textFieldName = new JTextField(15);

		buttonCancel = new JButton("Cancel");
		buttonAdd = new JButton("Add");
		buttonAdd.setEnabled(false);
	}

	/**
	 * Set the display of the components
	 */
	private void setupLayout() {
		mainPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

		mainPanel.add(labelName);
		mainPanel.add(textFieldName);
		mainPanel.add(buttonAdd);
		mainPanel.add(buttonCancel);
	}

	/**
	 * Set the frame size and parameters
	 */
	private void setupFrame() {
		pack();
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	/**
	 * Setup the listeners for the components
	 */
	private void setupListener() {
		textFieldName.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					dispose();
				}
				buttonAdd.setEnabled(textFieldName.getText().length() > 0);
			}
		});

		buttonAdd.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String name = textFieldName.getText();
				new ActionAddSystem(name).actionPerformed(e);
				dispose();
			}
		});

		buttonCancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		textFieldName.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				buttonAdd.doClick();
			}
		});
	}

}
