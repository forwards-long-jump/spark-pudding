package ch.sparkpudding.coreengine.ecs;

public class Field {

	enum FieldType {
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

}
