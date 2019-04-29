package ch.sparkpudding.sceneeditor.panel;

import javax.swing.JPanel;

/**
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba
 * 
 */
@SuppressWarnings("serial")
public class PanelEntity extends JPanel {

	private PanelComponent panelComponent;

	public PanelEntity(PanelComponent panelComponent) {
		this.panelComponent = panelComponent;
	}
}
