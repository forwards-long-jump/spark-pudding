package ch.sparkpudding.sceneeditor.panel;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ch.sparkpudding.sceneeditor.SceneEditor;
import ch.sparkpudding.sceneeditor.SceneEditor.EditorState;
import ch.sparkpudding.sceneeditor.action.ActionSetZIndex;
import ch.sparkpudding.sceneeditor.ecs.SEEntity;
import ch.sparkpudding.sceneeditor.listener.EntityEventAdapter;
import ch.sparkpudding.sceneeditor.listener.GameStateEventListener;
import ch.sparkpudding.sceneeditor.panel.modal.ModalComponent;
import ch.sparkpudding.sceneeditor.panel.modal.ModalEntityTemplate;

/**
 * Contains the different parameter of an entity
 *
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba<br/>
 *         Creation Date : 29 April 2019
 *
 */
@SuppressWarnings("serial")
public class PanelEntity extends JPanel {

	private PanelComponentsContainer initialPanelComponent;
	private PanelComponentsContainer livePanelComponent;
	private JTabbedPane jTabbedPane;
	private JButton btnAddComponent;
	private JButton btnSaveAsTemplate;
	private JPanel entitySettings;
	private JTextField entityZIndex;

	private SEEntity currentEntity;

	private static final String TITLE = "Entity";
	private static final String TITLE_LIVE = "Live entity";

	/**
	 * ctor
	 */
	public PanelEntity() {
		init();
		setupLayout();
		addListener();
	}

	/**
	 * Initialize the different element of the panel
	 */
	private void init() {
		this.entitySettings = new JPanel();
		this.initialPanelComponent = new PanelComponentsContainer();
		this.livePanelComponent = new PanelComponentsContainer();
		this.jTabbedPane = new JTabbedPane();
		this.btnAddComponent = new JButton("Add component");
		this.btnSaveAsTemplate = new JButton("Save as template");
		this.entityZIndex = new JTextField();
	}

	/**
	 * Setup the layout of the panel
	 */
	private void setupLayout() {
		setLayout(new BorderLayout());

		JPanel bottomButtons = new JPanel();
		bottomButtons.setLayout(new FlowLayout());

		entitySettings.setLayout(new BorderLayout(10, 10));
		entitySettings.add(new JLabel("Z-Index: "), BorderLayout.WEST);
		entitySettings.add(entityZIndex, BorderLayout.CENTER);

		jTabbedPane.addTab("Initial", initialPanelComponent);
		jTabbedPane.addTab("Live", livePanelComponent);

		entityZIndex.setEnabled(false);
		btnAddComponent.setEnabled(false);
		btnSaveAsTemplate.setEnabled(false);

		add(entitySettings, BorderLayout.NORTH);
		add(jTabbedPane, BorderLayout.CENTER);

		bottomButtons.add(btnAddComponent);
		bottomButtons.add(btnSaveAsTemplate);
		add(bottomButtons, BorderLayout.SOUTH);

		resetBorderTitle();
	}

	/**
	 * Add the different listener for each element of the panel
	 */
	private void addListener() {
		SceneEditor.addGameStateEventListener(new GameStateEventListener() {

			@Override
			public void gameStateChanged(EditorState state) {
				resetEnabledPane();
			}
		});

		SceneEditor.addEntityEventListener(new EntityEventAdapter() {

			@Override
			public void changeSelectedEntity(SEEntity entity) {
				setEntity(entity);
			}
		});

		btnAddComponent.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				switch (jTabbedPane.getSelectedIndex()) {
				case 0:
					new ModalComponent(SceneEditor.selectedEntity, SceneEditor.selectedEntity.getDefaultEntity());
					break;
				case 1:
					new ModalComponent(SceneEditor.selectedEntity, SceneEditor.selectedEntity.getLiveEntity());
					break;
				default:
					System.err.println("Invalid selected panel");
					break;
				}
			}
		});

		btnSaveAsTemplate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				switch (jTabbedPane.getSelectedIndex()) {
				case 0:
					new ModalEntityTemplate(SceneEditor.selectedEntity.getDefaultEntity());
					break;
				case 1:
					new ModalEntityTemplate(SceneEditor.selectedEntity.getLiveEntity());
					break;
				default:
					System.err.println("Invalid selected panel");
					break;
				}
			}
		});

		entityZIndex.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						entityZIndex.selectAll();
					}
				});
			}

			public void focusLost(FocusEvent arg0) {
				try {
					int zIndex = Integer.parseInt(entityZIndex.getText());
					new ActionSetZIndex(SceneEditor.selectedEntity, zIndex).actionPerformed(null);
				} catch (Exception e) {
					entityZIndex.setText(SceneEditor.selectedEntity.getLiveEntity().getZIndex() + "");
				}
			}
		});

		entityZIndex.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					int zIndex = Integer.parseInt(entityZIndex.getText());
					new ActionSetZIndex(SceneEditor.selectedEntity, zIndex).actionPerformed(null);
				} catch (Exception e) {
					entityZIndex.setText(SceneEditor.selectedEntity.getLiveEntity().getZIndex() + "");
				}
			}
		});

		jTabbedPane.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				boolean enabled = true;
				switch (jTabbedPane.getSelectedIndex()) {
				case 0:
					enabled = SceneEditor.selectedEntity != null && SceneEditor.getGameState() == EditorState.STOP;
					break;
				case 1:
					enabled = SceneEditor.selectedEntity != null && SceneEditor.getGameState() != EditorState.STOP;
					break;
				default:
					System.err.println("Invalid selected panel");
					break;
				}
				btnAddComponent.setEnabled(enabled);
				btnSaveAsTemplate.setEnabled(enabled);
			}
		});
	}

	/**
	 * Reset the title of this panel with the name of the entity represented
	 */
	private void resetBorderTitle() {
		if (currentEntity != null) {
			if (currentEntity.getDefaultEntity() != null) {
				setBorder(BorderFactory.createTitledBorder(TITLE + " — " + currentEntity.getDefaultEntity().getName()
						+ " (" + currentEntity.getDefaultEntity().getTemplate() + ")"));
			} else {
				setBorder(BorderFactory.createTitledBorder(TITLE_LIVE + " — " + currentEntity.getLiveEntity().getName()
						+ " (" + currentEntity.getLiveEntity().getTemplate() + ")"));
			}
		} else {
			setBorder(BorderFactory.createTitledBorder(TITLE + " — (none)"));
		}
	}

	/**
	 * Allow to set the good pane in regards of the gameState
	 */
	private void resetEnabledPane() {
		switch (SceneEditor.getGameState()) {
		case STOP:
			initialPanelComponent.setEnabled(true);
			livePanelComponent.setEnabled(false);
			jTabbedPane.setEnabledAt(1, false);
			jTabbedPane.setSelectedIndex(0);
			break;
		case ERROR:
			// Keep previous state in case of error
			break;
		default:
			initialPanelComponent.setEnabled(false);
			livePanelComponent.setEnabled(true);
			jTabbedPane.setEnabledAt(1, true);
			jTabbedPane.setSelectedIndex(1);
			break;
		}

		entityZIndex.setEnabled(SceneEditor.selectedEntity != null);
		btnAddComponent.setEnabled(SceneEditor.selectedEntity != null);
		btnSaveAsTemplate.setEnabled(SceneEditor.selectedEntity != null);
	}

	/**
	 * Set the games entity represented by this panel
	 *
	 * @param seEntity The entity represented by this panel
	 */
	private void setEntity(SEEntity seEntity) {
		if (seEntity == null) {
			clearTabbedPanes();
			return;
		}
		currentEntity = seEntity;
		// Is there a default entity ?
		if (seEntity.getDefaultEntity() != null) {
			initialPanelComponent.setEntity(seEntity, currentEntity.getDefaultEntity());
			this.jTabbedPane.setEnabledAt(0, true);
		} else {
			this.jTabbedPane.setEnabledAt(0, false);
		}

		entityZIndex.setText(seEntity.getLiveEntity().getZIndex() + "");
		livePanelComponent.setEntity(seEntity, currentEntity.getLiveEntity());

		resetBorderTitle();
		resetEnabledPane();
	}

	/**
	 * Remove the component of the different entity panel
	 */
	public void clearTabbedPanes() {
		currentEntity = null;
		setBorder(BorderFactory.createTitledBorder(TITLE + " — null"));
		initialPanelComponent.removeAll();
		initialPanelComponent.invalidate();
		initialPanelComponent.repaint();
		livePanelComponent.removeAll();
		livePanelComponent.invalidate();
		livePanelComponent.repaint();
		entityZIndex.setEnabled(false);
		btnAddComponent.setEnabled(false);
		btnSaveAsTemplate.setEnabled(false);
	}
}
