package ch.sparkpudding.sceneeditor.panel;

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
		addListener();
	}

	private void init() {
		fieldList = new ArrayList<ComponentGenerator>();
	}

	private void setupLayout() {
		// TODO Auto-generated method stub

	}

	private void addListener() {
		// TODO Auto-generated method stub

	}

	public void setEntity(Entity entity) {
		fieldList.clear();
		removeAll();
		add(new ComponentGenerator(entity.getComponents().values()));
		revalidate();
	}
}
