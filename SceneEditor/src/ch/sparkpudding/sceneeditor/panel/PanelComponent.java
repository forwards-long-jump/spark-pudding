package ch.sparkpudding.sceneeditor.panel;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import ch.sparkpudding.coreengine.ecs.Field;
import ch.sparkpudding.coreengine.ecs.Field.FieldType;
import ch.sparkpudding.sceneeditor.generator.FieldGenerator;

/**
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba
 * 
 */
@SuppressWarnings("serial")
public class PanelComponent extends JPanel {
	public PanelComponent() {
		// serve only to test the FieldGenerator
		// TODO : Delete  the line concerning fs when not needed anymore
		List<Field> fs = new ArrayList<Field>();
		fs.add(new Field(FieldType.INTEGER, 11));
		fs.add(new Field(FieldType.BOOLEAN, true));
		fs.add(new Field(FieldType.FILE_PATH, "/test/lala"));
		fs.add(new Field(FieldType.STRING, " le vol LeL 737 est partira avec un retard d'une semaine"));
		fs.add(new Field(FieldType.DOUBLE, 11.123453));
		
		add(new FieldGenerator(fs));		
	}
}
