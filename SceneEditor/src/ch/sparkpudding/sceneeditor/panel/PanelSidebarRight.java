package ch.sparkpudding.sceneeditor.panel;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import ch.sparkpudding.coreengine.CoreEngine;

/**
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba
 * 
 */
@SuppressWarnings("serial")
public class PanelSidebarRight extends JPanel {

	private PanelScene panelScene;
	private PanelEntityTree panelEntityTree;
	private PanelEntity panelEntity;
	private PanelComponent panelComponent;

	private BoxLayout layout;

	public PanelSidebarRight(CoreEngine ce) {
		init();
		setupLayout();
	}

	private void init() {
		layout = new BoxLayout(this, BoxLayout.Y_AXIS);

		panelScene = new PanelScene();
		panelEntityTree = new PanelEntityTree();
		panelEntity = new PanelEntity();
		panelComponent = new PanelComponent();
	}

	private void setupLayout() {
		setLayout(layout);

		add(panelScene);
		add(panelEntityTree);
		add(panelEntity);
		add(panelComponent);
	}
}
