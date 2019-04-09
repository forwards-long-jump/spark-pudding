package ch.sparkpudding.sceneeditor.generator;

import java.awt.FlowLayout;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JTextField;

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

	//List<FieldGenerator> fields;
	
	public ComponentGenerator(List<Component> components) {
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		//fields = new ArrayList<FieldGenerator>();
		
		// TODO : change all the reference to type to references to name when the merge request 8 will be done.
		for (Component component : components) {
			Box titleBar = new Box(BoxLayout.X_AXIS);
			titleBar.add(new JLabel(component.getType()));
			titleBar.add(new JButton("Delete"));
			titleBar.add(new JButton("Detache"));
			add(titleBar);
			add(new FieldGenerator(new ArrayList<Field>(component.getFields().values())));
		}
	}
}
