package ch.sparkpudding.coreengine.ecs;

public class Field {

	public enum FieldType {
		INTEGER, DOUBLE, STRING, FILE_PATH, BOOLEAN
	}

	private FieldType type;
	private String name;
	private Object value;

	public Field(FieldType type, Object value) {
		this.type = type;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public FieldType getType() {
		return type;
	}

	public Object getValue() {
		return value;
	}

}
