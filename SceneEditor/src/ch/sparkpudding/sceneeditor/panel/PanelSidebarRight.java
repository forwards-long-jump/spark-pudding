package ch.sparkpudding.sceneeditor.panel;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

/**
 * The panel which contains all the property of the game (scenes, entities,
 * components)
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba<br/>
 *         Creation Date : 29 April 2019
 *
 */
@SuppressWarnings("serial")
public class PanelSidebarRight extends JPanel {

	private PanelScene panelScene;
	private PanelEntityTree panelEntityTree;
	private PanelEntity panelEntity;

	public static final int DEFAULT_PANEL_SIZE = 500;
	public static final int BASIC_ELEMENT_WIDTH = 275;
	public static final int BASIC_ELEMENT_HEIGHT = 275;
	public static final int BASIC_ELEMENT_MARGIN = 5;

	private BorderLayout layout;

	/**
	 * ctor
	 */
	public PanelSidebarRight() {
		init();
		setupLayout();
	}

	/**
	 * Initialize the different element of the panel
	 */
	private void init() {
		layout = new BorderLayout();
		
		panelEntity = new PanelEntity();
		panelEntityTree = new PanelEntityTree(panelEntity);
		panelScene = new PanelScene(panelEntityTree);
	}

	/**
	 * Setup the layout of the panel
	 */
	private void setupLayout() {
		setLayout(layout);

		add(panelScene, BorderLayout.NORTH);
		JSplitPane sp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panelEntityTree, panelEntity);
	
		add(sp, BorderLayout.CENTER);
		setBorder(BorderFactory.createEtchedBorder());
	}

	/**
	 * Populate this panel
	 */
	public void populatePanel() {
		panelScene.populatePanel();
	}

	/**
	 * Get panelEntity
	 * 
	 * @return panelEntity
	 */
	public PanelEntity getPanelEntity() {
		return panelEntity;
	}
}
