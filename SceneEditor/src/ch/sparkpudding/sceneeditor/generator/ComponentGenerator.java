package ch.sparkpudding.sceneeditor.generator;

import java.awt.BorderLayout;
import java.awt.Font;
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
import ch.sparkpudding.sceneeditor.panel.PanelSidebarRight;

/**
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba<br/>
 *         Creation Date : 8 avr. 2019
 *
 *         Generate the interface for the components passed in arguments. Since
 *         it inherits JComponent, it can be used as one.
 * 
 */
@SuppressWarnings("serial")
public class ComponentGenerator extends JPanel {

	private JPanel contentPanel;
	private JScrollPane jScrollPane;

	public ComponentGenerator(Collection<Component> collection) {
		this.contentPanel = new JPanel();
		this.contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

		this.jScrollPane = new JScrollPane(contentPanel);

		setLayout(new BorderLayout());

		for (Component component : collection) {
			Box titleBar = new Box(BoxLayout.X_AXIS);
			JLabel titleComp = new JLabel(component.getName());
			titleComp.setFont(titleComp.getFont().deriveFont(Font.BOLD));

			titleBar.add(Box.createHorizontalStrut(PanelSidebarRight.BASIC_ELEMENT_MARGIN));
			titleBar.add(titleComp);
			titleBar.add(Box.createHorizontalGlue());
			titleBar.add(new JButton("Delete"));
			titleBar.add(new JButton("Detach"));
			this.contentPanel.add(titleBar);
			this.contentPanel.add(new FieldGenerator(new ArrayList<Field>(component.getFields().values())));
			this.contentPanel.add(new JSeparator());
		}

		add(jScrollPane, BorderLayout.CENTER);
	}
}
