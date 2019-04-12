package ch.sparkpudding.sceneeditor.generator;

import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;

import ch.sparkpudding.coreengine.ecs.Component;
import ch.sparkpudding.coreengine.ecs.Field;

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
public class ComponentGenerator extends JComponent {

	public ComponentGenerator(List<Component> components) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		for (Component component : components) {
			Box titleBar = new Box(BoxLayout.X_AXIS);
			titleBar.add(new JLabel(component.getName()));
			titleBar.add(new JButton("Delete"));
			titleBar.add(new JButton("Detache"));
			add(titleBar);
			add(new FieldGenerator(new ArrayList<Field>(component.getFields().values())));
		}
	}
}
