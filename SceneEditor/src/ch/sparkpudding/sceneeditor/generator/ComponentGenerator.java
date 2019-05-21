package ch.sparkpudding.sceneeditor.generator;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;

import ch.sparkpudding.coreengine.ecs.component.Component;
import ch.sparkpudding.coreengine.ecs.component.Field;
import ch.sparkpudding.coreengine.ecs.entity.Entity;
import ch.sparkpudding.sceneeditor.action.ActionAttach;
import ch.sparkpudding.sceneeditor.action.ActionDeleteComponent;
import ch.sparkpudding.sceneeditor.action.ActionDetach;
import ch.sparkpudding.sceneeditor.action.ActionSetComponent;
import ch.sparkpudding.sceneeditor.ecs.SEEntity;
import ch.sparkpudding.sceneeditor.panel.PanelSidebarRight;

/**
 * Generate the interface for the components passed in arguments. Since it
 * inherits JComponent, it can be used as one.
 *
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba<br/>
 *         Creation Date : 8 April 2019
 *
 */
@SuppressWarnings("serial")
public class ComponentGenerator extends JPanel {

	private JPanel contentPanel;
	private JScrollPane jScrollPane;
	private Collection<Component> components;
	private List<FieldGenerator> fieldGenerators;

	private SEEntity seEntity;
	private Entity entity;

	/**
	 * ctor
	 *
	 * @param components Collection of all the components of an entity
	 */
	public ComponentGenerator(SEEntity seEntity, Entity entity) {
		this.components = entity.getComponents().values();
		this.fieldGenerators = new ArrayList<FieldGenerator>();

		this.seEntity = seEntity;
		this.entity = entity;

		init();
		createComponents();
		setupLayout();
	}

	/**
	 * Initialize the different element of the panel
	 */
	private void init() {
		this.contentPanel = new JPanel();
		this.contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

		this.jScrollPane = new JScrollPane(contentPanel);
		this.jScrollPane.getVerticalScrollBar().setUnitIncrement(16);
		this.jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		this.jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	}

	/**
	 * Get scroll position
	 * 
	 * @return scroll position
	 */
	public int getScrollPosition() {
		return this.jScrollPane.getVerticalScrollBar().getValue();
	}

	/**
	 * Set scroll position
	 * 
	 * @param scroll position
	 */
	public void setScrollPosition(int value) {
		this.jScrollPane.getVerticalScrollBar().setValue(value);
	}

	/**
	 * Setup the layout of the panel
	 */
	private void setupLayout() {
		setLayout(new BorderLayout());
		add(this.jScrollPane, BorderLayout.CENTER);
	}

	/**
	 * Create and recreate all the representation of the components stored in
	 * <code>this.components</code>
	 */
	private void createComponents() {
		removeAll();
		List<Component> componentsToSort = new ArrayList<Component>();
		for (Component component : components) {
			if (!component.getName().startsWith("se-")) {
				componentsToSort.add(component);
			}
		}

		componentsToSort.sort(new Comparator<Component>() {
			@Override
			public int compare(Component arg0, Component arg1) {
				if (arg0.getName().equals("position") || arg0.getName().equals("size")) {
					return -1;
				}
				if (arg1.getName().equals("position") || arg1.getName().equals("size")) {
					return 1;
				}
				return arg0.getName().compareTo(arg1.getName());
			}
		});

		for (Component component : componentsToSort) {
			setupComponentsLayout(component);
		}

		revalidate();
	}

	/**
	 * Create the representation of a component
	 *
	 * @param component The component to consider
	 */
	private void setupComponentsLayout(Component component) {
		Box titleBar = new Box(BoxLayout.X_AXIS);
		JLabel titleComp = new JLabel(component.getName());
		JButton btnDelete = new JButton("Delete");
		JButton btnDetachOrCopy;
		boolean isLive = (seEntity.getLiveEntity() == entity);
		if (isLive) {
			btnDetachOrCopy = new JButton("Copy to default");
			btnDetachOrCopy.addActionListener(
					new ActionSetComponent("Set " + component.getName() + " fields as initial for " + entity.getName(),
							seEntity.getDefaultEntity(), component));
		} else {
			if (component.isAttached()) {
				btnDetachOrCopy = new JButton("Detach");
				btnDetachOrCopy.addActionListener(new ActionDetach(component));
			} else {
				btnDetachOrCopy = new JButton("Attach");
				btnDetachOrCopy.addActionListener(new ActionAttach(component));
			}
		}

		titleComp.setFont(titleComp.getFont().deriveFont(Font.BOLD));

		titleBar.add(Box.createHorizontalStrut(PanelSidebarRight.BASIC_ELEMENT_MARGIN));
		titleBar.add(titleComp);
		titleBar.add(Box.createHorizontalGlue());
		btnDelete.addActionListener(
				new ActionDeleteComponent("delete component " + component.getName(), entity, component));

		titleBar.add(btnDelete);
		titleBar.add(btnDetachOrCopy);
		this.contentPanel.add(titleBar);
		FieldGenerator field = new FieldGenerator(new ArrayList<Field>(component.getFields().values()), isLive || !component.isAttached());
		this.fieldGenerators.add(field);
		this.contentPanel.add(field);
		this.contentPanel.add(new JSeparator());
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		for (FieldGenerator fieldGenerator : fieldGenerators) {
			fieldGenerator.setEnabled(enabled);
		}
		super.setEnabled(enabled);
	}
}
