package ch.sparkpudding.sceneeditor.panel;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import ch.sparkpudding.sceneeditor.SceneEditor;
import ch.sparkpudding.sceneeditor.SceneEditor.EDITOR_STATE;
import ch.sparkpudding.sceneeditor.ecs.SEEntity;
import ch.sparkpudding.sceneeditor.listener.GameStateEventListener;

/**
 * Contains the different parameter of an entity
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba<br/>
 *         Creation Date : 29 avr. 2019
 *
 */
@SuppressWarnings("serial")
public class PanelEntity extends JPanel {

	private PanelComponent initialPanelComponent;
	private PanelComponent livePanelComponent;
	private JTabbedPane jTabbedPane;

	private SEEntity currentEntity;

	private static final String TITLE = "Entity";

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
		this.initialPanelComponent = new PanelComponent();
		this.livePanelComponent = new PanelComponent();
		this.jTabbedPane = new JTabbedPane();
	}

	/**
	 * Setup the layout of the panel
	 */
	private void setupLayout() {
		setLayout(new BorderLayout());

		jTabbedPane.addTab("Initial", initialPanelComponent);
		jTabbedPane.addTab("Live", livePanelComponent);

		add(jTabbedPane, BorderLayout.CENTER);

		resetBorderTitle();
	}

	private void addListener() {
		SceneEditor.addGameStateEventListener(new GameStateEventListener() {

			@Override
			public void gameStateEvent(EDITOR_STATE state) {
				resetEnabledPane();
			}
		});
	}

	/**
	 * Reset the title of this panel with the name of the entity represented
	 */
	private void resetBorderTitle() {
		if (currentEntity != null) {
			setBorder(BorderFactory.createTitledBorder(TITLE + " — " + currentEntity.getDefaultEntity().getName()));
		} else {
			setBorder(BorderFactory.createTitledBorder(TITLE + " — null"));
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
			break;
		default:
			initialPanelComponent.setEnabled(false);
			livePanelComponent.setEnabled(true);
			break;

		}
	}

	/**
	 * Set the games entity represented by this panel
	 * 
	 * @param seEntity The entity represented by this panel
	 */
	public void setEntity(SEEntity seEntity) {
		currentEntity = seEntity;
		initialPanelComponent.setEntity(currentEntity.getDefaultEntity());
		livePanelComponent.setEntity(currentEntity.getLiveEntity());
		resetBorderTitle();
		resetEnabledPane();
	}

	/**
	 * Remove the component of the different entity panel
	 */
	public void removeEntity() {
		currentEntity = null;
		initialPanelComponent.removeAll();
		livePanelComponent.removeAll();
	}
}
