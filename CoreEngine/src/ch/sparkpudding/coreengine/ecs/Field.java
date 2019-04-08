package ch.sparkpudding.coreengine.ecs;

/**
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba
 * 
 */
public class Field {

	enum FieldType {
		INTEGER, DOUBLE, STRING, FILE_PATH, BOOLEAN
	}

	private FieldType type;
	private String name;
	private Object value;

	/**
	 * Create field using actual types
	 * @param name Name of the field
	 * @param type Type of the data
	 * @param value Data
	 */
	public Field(String name, FieldType type, Object value) {
		this.name = name;
		this.type = type;
		this.setValue(value);
	}

	/**
	 * Constructor using Strings only
	 * @param name Name of the field
	 * @param type Type of the data
	 * @param value Data
	 */
	public Field(String name, String type, String value) {
		this.name = name;
		this.type = FieldType.valueOf(type);
		switch (this.type) {
		case BOOLEAN:
			this.setValue(value == "true" ? true : false);
			break;
		case DOUBLE:
			this.setValue(Double.parseDouble(value));
			break;
		case FILE_PATH:
		case STRING:
			this.setValue(value);
			break;
		case INTEGER:
			this.setValue(Integer.parseInt(value));
			break;
		default:
			java.lang.System.err.println();
			break;
		}
	}

	/**
	 * Get field name
	 * @return Field name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get field value
	 * @return Field value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Sets field value
	 * @param value New value
	 */
	public void setValue(Object value) {
		this.value = value;
	}

}
