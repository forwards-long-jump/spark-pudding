package ch.sparkpudding.sceneeditor.generator;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;

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
import ch.sparkpudding.sceneeditor.action.ActionDeleteComponent;
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

	/**
	 * ctor
	 * 
	 * @param components Collection of all the components of an entity
	 */
	public ComponentGenerator(Entity entity) {
		this.components = entity.getComponents().values();

		init();
		createComponents(entity);
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
	private void createComponents(Entity entity) {
		removeAll();
		for (Component component : components) {
			if (!component.getName().startsWith("se-")) {
				setupComponentsLayout(entity, component);
			}
		}
		revalidate();
	}

	/**
	 * Create the representation of a component
	 * 
	 * @param component The component to consider
	 */
	private void setupComponentsLayout(Entity entity, Component component) {
		Box titleBar = new Box(BoxLayout.X_AXIS);
		JLabel titleComp = new JLabel(component.getName());
		titleComp.setFont(titleComp.getFont().deriveFont(Font.BOLD));

		titleBar.add(Box.createHorizontalStrut(PanelSidebarRight.BASIC_ELEMENT_MARGIN));
		titleBar.add(titleComp);
		titleBar.add(Box.createHorizontalGlue());
		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				new ActionDeleteComponent("delete component " + component.getName(), entity, component).actionPerformed(arg0);
			}
		});

		titleBar.add(btnDelete);
		titleBar.add(new JButton("Detach"));
		this.contentPanel.add(titleBar);
		this.contentPanel.add(new FieldGenerator(new ArrayList<Field>(component.getFields().values())));
		this.contentPanel.add(new JSeparator());
	}
}
