package ch.sparkpudding.sceneeditor.panel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import ch.sparkpudding.coreengine.ecs.entity.Entity;
import ch.sparkpudding.sceneeditor.ecs.SEEntity;
import ch.sparkpudding.sceneeditor.generator.ComponentGenerator;

/**
 * Display the components of the selected entity
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba<br />
 *         Creation Date : 9 April 2019
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
	 * Get all the component contained in a container recursively source :
	 * https://stackoverflow.com/questions/19324918/how-to-disable-all-components-in-a-jpanel
	 * 
	 * @param container the container where we need to find all the child
	 * @return the complete list of the children
	 */
	private List<Component> getAllComponents(final Container container) {
		Component[] comps = container.getComponents();
		List<Component> compList = new ArrayList<Component>();
		for (Component comp : comps) {
			compList.add(comp);
			if (comp instanceof Container)
				compList.addAll(getAllComponents((Container) comp));
		}
		return compList;
	}

	/**
	 * Set the games entity represented by this panel
	 * 
	 * @param entity The entity represented by this panel
	 */
	public void setEntity(SEEntity seEntity, Entity entity) {
		fieldList.clear();
		removeAll();
		add(new ComponentGenerator(seEntity, entity), BorderLayout.CENTER);
		revalidate();
	}

	/**
	 * Override the default behavior to disable recursively all the components
	 */
	@Override
	public void setEnabled(boolean enabled) {
		List<Component> comps = getAllComponents(this);
		for (Component comp : comps) {
			comp.setEnabled(enabled);
		}
	}
}
