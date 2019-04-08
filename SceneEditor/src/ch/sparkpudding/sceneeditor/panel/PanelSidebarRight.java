package ch.sparkpudding.sceneeditor.panel;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class PanelSidebarRight extends JPanel {

	BoxLayout layout;

	PanelScene panelScene;
	PanelEntityTree panelEntityTree;
	PanelEntity panelEntity;
	PanelComponent panelComponent;

	public PanelSidebarRight() {

		panelScene = new PanelScene();
		panelEntityTree = new PanelEntityTree();
		panelEntity = new PanelEntity();
		panelComponent = new PanelComponent();

		layout = new BoxLayout(this, BoxLayout.Y_AXIS);
		setLayout(layout);

		add(panelScene);
		add(panelEntityTree);
		add(panelEntity);
		add(panelComponent);
	}
}
