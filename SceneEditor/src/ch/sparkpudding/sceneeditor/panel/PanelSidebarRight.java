package ch.sparkpudding.sceneeditor.panel;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

/**
 * The panel which contains all the property of the game (scenes, entities,
 * components)
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba<br/>
 *         Creation Date : 29 avr. 2019
 *
 */
@SuppressWarnings("serial")
public class PanelSidebarRight extends JPanel {

	private PanelScene panelScene;
	private PanelEntityTree panelEntityTree;
	private PanelEntity panelEntity;

	public static final int BASIC_ELEMENT_WIDTH = 275;
	public static final int BASIC_ELEMENT_HEIGHT = 275;
	public static final int BASIC_ELEMENT_MARGIN = 5;

	private BoxLayout layout;

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
		layout = new BoxLayout(this, BoxLayout.Y_AXIS);

		panelEntity = new PanelEntity();
		panelEntityTree = new PanelEntityTree(panelEntity);
		panelScene = new PanelScene(panelEntityTree);
	}

	/**
	 * Setup the layout of the panel
	 */
	private void setupLayout() {
		setLayout(layout);

		add(panelScene);
		add(panelEntityTree);
		add(panelEntity);

		setBorder(BorderFactory.createEtchedBorder());
	}
}
