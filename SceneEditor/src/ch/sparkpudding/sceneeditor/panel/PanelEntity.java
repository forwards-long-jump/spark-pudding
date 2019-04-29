package ch.sparkpudding.sceneeditor.panel;

import javax.swing.JPanel;

/**
 * Contains the different parameter of an entity
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba<br/>
 *         Creation Date : 29 avr. 2019
 *
 */
@SuppressWarnings("serial")
public class PanelEntity extends JPanel {

	private PanelComponent panelComponent;

	/**
	 * ctor
	 */
	public PanelEntity() {
		this.panelComponent = new PanelComponent();
	}
}
