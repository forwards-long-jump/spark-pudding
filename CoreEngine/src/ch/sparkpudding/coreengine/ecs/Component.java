package ch.sparkpudding.coreengine.ecs;

import java.util.HashMap;
import java.util.Map;

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

	public String getType() {
		return type;
	}
}
