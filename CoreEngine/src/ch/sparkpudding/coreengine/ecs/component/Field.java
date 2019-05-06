package ch.sparkpudding.coreengine.ecs.component;

/**
 * Piece of data to be contained by a component, described by its name, the type
 * of its value, and the value itself.
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba
 * 
 */
public class Field {

	public enum FieldType {
		INTEGER, DOUBLE, STRING, FILE_PATH, BOOLEAN
	}

	private FieldType type;
	private String name;
	private Object value;

	/**
	 * Create field using actual types
	 * 
	 * @param name  Name of the field
	 * @param type  Type of the data
	 * @param value Data
	 */
	public Field(String name, FieldType type, Object value) {
		this.name = name;
		this.type = type;
		switch (type) {
		case BOOLEAN:
		case INTEGER:
		case DOUBLE:
			this.value = value;
			break;
		case FILE_PATH:
		case STRING:
			this.value = new String(value.toString());
			break;
		default:
			break;
		}
	}

	/**
	 * Copy constructor
	 * 
	 * @param field
	 */
	public Field(Field field) {
		this(new String(field.name), field.type, new Object());
		switch (type) {
		case BOOLEAN:
		case INTEGER:
		case DOUBLE:
			this.value = field.value;
			break;
		case FILE_PATH:
		case STRING:
			this.value = new String(field.value.toString());
			break;
		default:
			break;
		}
	}

	/**
	 * Constructor using Strings only
	 * 
	 * @param name  Name of the field
	 * @param type  Type of the data
	 * @param value Data
	 */
	public Field(String name, String type, String value) {
		this.name = name;
		this.type = FieldType.valueOf(type);
		this.setValueFromString(value);
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return new Field(this);
	}

	/**
	 * Get field name
	 * 
	 * @return Field name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get field type
	 * 
	 * @return Field type
	 */
	public FieldType getType() {
		return type;
	}

	/**
	 * Get field value
	 * 
	 * @return Field value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Sets field value
	 * 
	 * @param value New value
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * Sets field value, casting from String into actual value type
	 * 
	 * @param value New value
	 */
	public void setValueFromString(String value) {
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

}
