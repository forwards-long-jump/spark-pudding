package ch.sparkpudding.sceneeditor.panel;

import javax.swing.JPanel;

/**
 * Show the different entity of a Scene as a list
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba<br/>
 *         Creation Date : 29 avr. 2019
 *
 */
@SuppressWarnings("serial")
public class PanelEntityTree extends JPanel {

	private PanelEntity panelEntity;

	/**
	 * ctor
	 * 
	 * @param panelEntity the panel where to show the informations of a selected
	 *                    entity
	 */
	public PanelEntityTree(PanelEntity panelEntity) {
		this.panelEntity = panelEntity;
	}

}
