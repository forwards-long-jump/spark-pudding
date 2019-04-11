package ch.sparkpudding.sceneeditor.panel;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import ch.sparkpudding.coreengine.ecs.Component;
import ch.sparkpudding.coreengine.ecs.Field;
import ch.sparkpudding.coreengine.ecs.Field.FieldType;
import ch.sparkpudding.sceneeditor.generator.ComponentGenerator;

/**
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba<br />
 *         Creation Date : 9 avr. 2019
 * 
 *         Display the components of the selected entity
 */
@SuppressWarnings("serial")
public class PanelComponent extends JPanel {
	public PanelComponent() {
		// serve only to test the FieldGenerator
		// TODO : Delete the lines concerning fs and cs before merging with develop
		List<Field> fs = new ArrayList<Field>();
		fs.add(new Field("Nombre", FieldType.INTEGER, 11));
		fs.add(new Field("Vrai", FieldType.BOOLEAN, true));
		fs.add(new Field("Chemin", FieldType.FILE_PATH, "/test/lala"));
		fs.add(new Field("Text", FieldType.STRING, " le vol LeL 737 est partira avec un retard d'une semaine"));
		fs.add(new Field("nombre à virgule", FieldType.DOUBLE, 11.123453));

		List<Component> cs = new ArrayList<Component>();
		cs.add(new Component("lala"));
		cs.get(0).addField(fs.get(0));
		cs.get(0).addField(fs.get(1));

		cs.add(new Component("lel"));
		cs.get(1).addField(fs.get(0));
		cs.get(1).addField(fs.get(2));

		cs.add(new Component("Is this a test ?"));
		cs.get(2).addField(fs.get(0));
		cs.get(2).addField(fs.get(1));
		cs.get(2).addField(fs.get(2));
		cs.get(2).addField(fs.get(3));
		cs.get(2).addField(fs.get(4));

		add(new ComponentGenerator(cs));
	}
}
