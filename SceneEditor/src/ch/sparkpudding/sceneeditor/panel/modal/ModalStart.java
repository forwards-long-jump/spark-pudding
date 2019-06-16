package ch.sparkpudding.sceneeditor.panel.modal;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
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

/**
 * Starting modal for the scene editor
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba<br/>
 *         Creation Date : 30 May 2019
 *
 */
@SuppressWarnings("serial")
public class ModalStart extends Modal {

	private String gamePath;
	private boolean gameStarted;
	private JButton btnOpen;
	private JButton btnNewEmpty;
	private JButton btnNewBasic;

	/**
	 * Constructor
	 */
	public ModalStart() {
		super(null, "Create or open game", false);
		init();
		setupLayout();
		setupListener();
		setupFrame();
	}

	/**
	 * Initialize the ui components and their values
	 */
	private void init() {
		gameStarted = false;
		btnOpen = new JButton("Open");
		btnNewEmpty = new JButton("New empty game");
		btnNewBasic = new JButton("New basic game");
	}

	/**
	 * Set the display of the components
	 */
	private void setupLayout() {
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(btnOpen, BorderLayout.NORTH);
		mainPanel.add(btnNewEmpty, BorderLayout.CENTER);
		mainPanel.add(btnNewBasic, BorderLayout.SOUTH);
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
		btnOpen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();

				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fc.setCurrentDirectory(new File(System.getProperty("user.dir")));
				int returnVal = fc.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					gamePath = fc.getSelectedFile().getAbsolutePath();
					openGame();
				} else {
					return;
				}
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

					openGame();
				} else {
					return;
				}
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

					openGame();
				} else {
					return;
				}
			}
		});

		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				if (!gameStarted) {
					System.exit(0);
				}
			}
		});
	}

	/**
	 * Start the Scene editor with the game path
	 * 
	 * @throws URISyntaxException when the path is incorrect
	 * @throws Exception          If the CoreEngine can't start the game
	 */
	private void openGame() {
		try {
			SceneEditor.coreEngine = new CoreEngine(gamePath,
					Paths.get(Main.class.getResource("/leleditor").toURI()).toString());
		} catch (URISyntaxException e) {
			JOptionPane.showMessageDialog(mainPanel, "Invalid path", "Error during editor startup",
					JOptionPane.ERROR_MESSAGE);
			return;
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(mainPanel, "No games found at this location.", "Error during editor startup",
					JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(mainPanel,
					"Cannot open game. Error: \n\n" + e.getMessage() + "\n\nCheck the console for more details.",
					"Error during editor startup", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			return;
		}
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

		gameStarted = true;
		dispose();
	}
}
