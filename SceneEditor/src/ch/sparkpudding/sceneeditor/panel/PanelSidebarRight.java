package ch.sparkpudding.sceneeditor.panel;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

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
