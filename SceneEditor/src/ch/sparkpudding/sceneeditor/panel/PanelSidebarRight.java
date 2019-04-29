package ch.sparkpudding.sceneeditor.panel;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

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

	public PanelSidebarRight() {
		init();
		setupLayout();
	}

	private void init() {
		layout = new BoxLayout(this, BoxLayout.Y_AXIS);

		panelComponent = new PanelComponent();
		panelEntity = new PanelEntity(panelComponent);
		panelEntityTree = new PanelEntityTree(panelEntity);
		panelScene = new PanelScene(panelEntityTree);
	}

	private void setupLayout() {
		setLayout(layout);

		add(panelScene);
		add(panelEntityTree);
		add(panelEntity);
		add(panelComponent);
	}
}
