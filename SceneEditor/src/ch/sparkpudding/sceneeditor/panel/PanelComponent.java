package ch.sparkpudding.sceneeditor.panel;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import ch.sparkpudding.coreengine.ecs.entity.Entity;
import ch.sparkpudding.sceneeditor.generator.ComponentGenerator;

/**
 * Display the components of the selected entity
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba<br />
 *         Creation Date : 9 avr. 2019
 * 
 */
@SuppressWarnings("serial")
public class PanelComponent extends JPanel {

	private List<ComponentGenerator> fieldList;

	/**
	 * ctor
	 */
	public PanelComponent() {
		init();
		setupLayout();
	}

	/**
	 * Initialize the different element of the panel
	 */
	private void init() {
		fieldList = new ArrayList<ComponentGenerator>();
	}

	/**
	 * Setup the layout of the panel
	 */
	private void setupLayout() {
		setLayout(new BorderLayout());
	}

	/**
	 * Set the games entity represented by this panel
	 * 
	 * @param entity The entity represented by this panel
	 */
	public void setEntity(Entity entity) {
		fieldList.clear();
		removeAll();
		add(new ComponentGenerator(entity.getComponents().values()), BorderLayout.CENTER);
		revalidate();
	}
}
