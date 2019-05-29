package ch.sparkpudding.coreengine;

import java.util.HashMap;
import java.util.Map;

import ch.sparkpudding.coreengine.ecs.component.Field;

// TODO: Implement this class in an API, use a singleton and fix it
/**
 * Owns a map of persistent data to be used at the user's discretion in systems.
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba
 * 
 */
public class Register {

	private Map<String, Field> fields;
	// TODO: eventually add maps for entities and component

	public Register() {
		this.fields = new HashMap<String, Field>();
	}

	public Field getField(String name) {
		if (fields.containsKey(name)) {
			return fields.get(name);
		}
		return null;
	}

	public void addField(String name, Field field) {
		fields.put(name, field);
	}

	public void removeField(String name) {
		fields.remove(name);
	}
}
