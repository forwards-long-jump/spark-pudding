package ch.sparkpudding.sceneeditor.panel.modal;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import ch.sparkpudding.coreengine.CoreEngine;
import ch.sparkpudding.coreengine.Scheduler.Trigger;
import ch.sparkpudding.coreengine.utils.RunnableOneParameter;
import ch.sparkpudding.sceneeditor.FrameSceneEditor;
import ch.sparkpudding.sceneeditor.Main;
import ch.sparkpudding.sceneeditor.SceneEditor;
import ch.sparkpudding.sceneeditor.SceneEditor.EditorState;
import ch.sparkpudding.sceneeditor.filewriter.LelWriter;

@SuppressWarnings("serial")
public class ModalStart extends Modal {

	String gamePath;
	private JButton btnOpen;
	private JButton btnNewEmpty;
	private JButton btnNewBasic;

	public ModalStart() {
		super(null, "Create or open game", false);
		init();
		setupLayout();
		setupListener();
		setupFrame();
	}

	private void init() {
		btnOpen = new JButton("Open");
		btnNewEmpty = new JButton("New empty game");
		btnNewBasic = new JButton("New basic game");
	}

	private void setupLayout() {
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(btnOpen, BorderLayout.NORTH);
		mainPanel.add(btnNewEmpty,  BorderLayout.CENTER);
		mainPanel.add(btnNewBasic,  BorderLayout.SOUTH);
	}

	private void setupFrame() {
		pack();
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void setupListener() {
		btnOpen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();

				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fc.setCurrentDirectory(new File(System.getProperty("user.dir")));
				int returnVal = fc.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					gamePath = fc.getSelectedFile().getAbsolutePath();
					try {
						openGame();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				} else
					return;
			}
		});

		btnNewBasic.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();

				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fc.setCurrentDirectory(new File(System.getProperty("user.dir")));
				int returnVal = fc.showSaveDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					gamePath = fc.getSelectedFile().getAbsolutePath();
					LelWriter lel = new LelWriter();
					lel.create(gamePath + '/', false); 
					try {
						openGame();
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(mainPanel, "Invalid path", "Error during editor startup", JOptionPane.ERROR_MESSAGE);
					}
				} else
					return;
			}
		});
		btnNewEmpty.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();

				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fc.setCurrentDirectory(new File(System.getProperty("user.dir")));
				int returnVal = fc.showSaveDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					gamePath = fc.getSelectedFile().getAbsolutePath();
					LelWriter lel = new LelWriter();
					lel.create(gamePath + '/', true); 
					try {
						openGame();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				} else
					return;
			}
		});
	}

	private void openGame() throws URISyntaxException, Exception {
		SceneEditor.coreEngine = new CoreEngine(gamePath,
				Paths.get(Main.class.getResource("/leleditor").toURI()).toString());
		SceneEditor.gamePath = gamePath;

		// EDITING_STATE_CHANGED is called in GAME_LOOP_START so no need to add another
		// scheduling
		// adding a new scheduling would break the camera
		SceneEditor.coreEngine.getScheduler().notify(Trigger.EDITING_STATE_CHANGED, new RunnableOneParameter() {
			@Override
			public void run() {
				if (SceneEditor.coreEngine.isInError()) {
					SceneEditor.setGameState(EditorState.ERROR);
				} else if ((boolean) getObject()) {
					SceneEditor.setGameState(EditorState.PAUSE);
				} else {
					SceneEditor.setGameState(EditorState.PLAY);
				}
			}
		});

		SceneEditor.frameSceneEditor = new FrameSceneEditor();
	}

	public String getPath() {
		return gamePath;
	}

}
