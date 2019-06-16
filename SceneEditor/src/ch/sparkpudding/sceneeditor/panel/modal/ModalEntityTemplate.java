package ch.sparkpudding.sceneeditor.panel.modal;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import ch.sparkpudding.coreengine.ecs.entity.Entity;
import ch.sparkpudding.sceneeditor.SceneEditor;
import ch.sparkpudding.sceneeditor.action.ActionSetEntityTemplate;

/**
 * Create or change entity templates
 *
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba
 *
 */
@SuppressWarnings("serial")
public class ModalEntityTemplate extends Modal {

	private JLabel lblName;
	private JTextField templateName;
	private JButton btnAdd;
	private Entity entity;

	/**
	 * ctor
	 */
	public ModalEntityTemplate(Entity entity) {
		super(SceneEditor.frameSceneEditor, "Save as template", true);
		this.entity = entity;
		init();
		setupLayout();
		setupListener();
		setupFrame();
	}

	/**
	 * Initialize the ui components and their values
	 */
	private void init() {
		lblName = new JLabel("Name :");
		templateName = new JTextField(15);
		templateName.setText(entity.getTemplate());
		templateName.selectAll();
		btnAdd = new JButton("Override \"" + entity.getTemplate() + "\"");
	}

	/**
	 * Set the display of the components
	 */
	private void setupLayout() {
		mainPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

		mainPanel.add(lblName);
		mainPanel.add(templateName);
		mainPanel.add(btnAdd);
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
		templateName.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {
				if (Entity.getTemplates().get(templateName.getText()) == null) {
					btnAdd.setText("Create template");
				} else {
					btnAdd.setText("Override \"" + entity.getTemplate() + "\"");
				}
				btnAdd.setEnabled(templateName.getText().length() > 0);
			}
		});

		btnAdd.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String name = templateName.getText();
				new ActionSetEntityTemplate(entity, name).actionPerformed(e);
				dispose();
			}
		});

		templateName.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				btnAdd.doClick();
			}
		});
	}

}
