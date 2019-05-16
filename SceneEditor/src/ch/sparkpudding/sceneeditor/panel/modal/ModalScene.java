package ch.sparkpudding.sceneeditor.panel.modal;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import ch.sparkpudding.sceneeditor.action.ActionAddScene;

/**
 * Create a new scene from the given name
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba
 * 
 */
@SuppressWarnings("serial")
public class ModalScene extends Modal {
	
	private JLabel lblName;
	private JTextField fiName;
	private JButton btnAdd;

	/**
	 * ctor
	 * 
	 * @param parent Component to block while the modal is active
	 */
	public ModalScene(JFrame parent) {
		super(parent, "New scene", true);
		init();
		setupLayout();
		setupListener();
 	}
	
	/**
	 * Initialize the ui components and their values
	 */
	private void init() {
		lblName = new JLabel("Name :");
		fiName = new JTextField(15);
		btnAdd = new JButton("Add");
		btnAdd.setEnabled(false);
	}
	
	/**
	 * Set the display of the components
	 */
	private void setupLayout() {
		mainPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		mainPanel.add(lblName);
		mainPanel.add(fiName);
		mainPanel.add(btnAdd);
	}

	/**
	 * Set the frame size and parameters
	 */
	private void setupFrame() {
		setSize(400, 100);
		setResizable(false);
		setVisible(true);
	}	
	
	/**
	 * Setup the listeners for the components
	 */
	private void setupListener() {
		fiName.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyReleased(KeyEvent e) {
				btnAdd.setEnabled(fiName.getText().length() > 0);
			}
		});
		
		btnAdd.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new ActionAddScene(fiName.getText()).actionPerformed(e);
				dispose();
			}
		});
				
		fiName.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				btnAdd.doClick();
			}
		});
	}

}
