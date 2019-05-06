package ch.sparkpudding.sceneeditor.panel;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import ch.sparkpudding.coreengine.ecs.entity.Entity;

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

	private Entity currentEntity;

	private static final String TITLE = "Entity";

	/**
	 * ctor
	 */
	public PanelEntity() {
		init();
		setupLayout();
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

	/**
	 * Reset the title of this panel with the name of the entity represented
	 */
	private void resetBorderTitle() {
		if (currentEntity != null) {
			setBorder(BorderFactory.createTitledBorder(TITLE + " — " + currentEntity.getName()));
		} else {
			setBorder(BorderFactory.createTitledBorder(TITLE + " — null"));
		}
	}

	/**
	 * Set the games entity represented by this panel
	 * 
	 * @param entity The entity represented by this panel
	 */
	public void setEntity(Entity entity) {
		currentEntity = entity;
		initialPanelComponent.setEntity(currentEntity);
		livePanelComponent.setEntity(currentEntity);
		resetBorderTitle();
	}
}
