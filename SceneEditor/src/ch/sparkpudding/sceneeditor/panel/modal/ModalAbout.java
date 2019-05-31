package ch.sparkpudding.sceneeditor.panel.modal;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ch.sparkpudding.sceneeditor.SceneEditor;
import ch.sparkpudding.sceneeditor.utils.ImageStorage;

/**
 * Modal dialog used to give information about the program
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba<br/>
 *         Creation Date : 30 May 2019
 *
 */
@SuppressWarnings("serial")
public class ModalAbout extends Modal {

	private JLabel labelIcon;

	private final String ABOUTTEXT = "<html>Developped by A. Bianchi, P. Bürki, L. Jeanneret and J. Leuba<br>during the 4th semester P2 project at HE-Arc.";

	private JPanel contentPanel;
	private JLabel labelText;
	private JButton buttonOkay;

	public ModalAbout() {
		super(SceneEditor.frameSceneEditor, "About", true);

		init();
		setupLayout();
		addListener();
		setupFrame();
	}

	private void init() {
		labelIcon = new JLabel(ImageStorage.HEARCLOGO);

		contentPanel = new JPanel();
		buttonOkay = new JButton("OK");
		labelText = new JLabel(ABOUTTEXT);
	}

	private void setupLayout() {
		mainPanel.setLayout(new BorderLayout(0, 50));
		mainPanel.add(labelIcon, BorderLayout.NORTH);

		FlowLayout contentPanelLayout = new FlowLayout();
		contentPanelLayout.setAlignment(FlowLayout.LEFT);
		contentPanelLayout.setHgap(50);
		contentPanel.setLayout(contentPanelLayout);
		contentPanel.add(labelText);
		contentPanel.add(buttonOkay);

		mainPanel.add(contentPanel, BorderLayout.CENTER);
	}

	private void setupFrame() {
		pack();
		// setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void addListener() {
		addKeyListener(new KeyAdapter() {

			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					buttonOkay.doClick();
				}
			}
		});

		buttonOkay.addKeyListener(new KeyAdapter() {

			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					buttonOkay.doClick();
				}
			}
		});

		buttonOkay.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
	}
}
