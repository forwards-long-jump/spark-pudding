package ch.sparkpudding.coreengine.ecs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
/**
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba<br/>
 *         Creation Date : 8 avr. 2019
 *
 * Contains the game components
 * 
 */
public class Component {

	private String type;
	private Map<String, Field> fields;

	public Component(String type) {
		this.type = type;
		this.fields = new HashMap<String, Field>();
	}

	public void addField(Field field) {
		fields.put(field.getName(), field);
	}
	
	public Field getField(String name) {
		return fields.get(name);
	}

	public String getType() {
		return type;
	}

	public Map<String, Field> getFields() {
		return fields;
	}
}
