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
	 * Copy constructor
	 * 
	 * @param field
	 */
	public Field(Field field) {
		this.type = field.type;
		this.name = field.name;
		// NOTE: This only works because all used objects are immutable
		this.value = field.value;
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
	 * Get field value. Note that the Lua execution may have turned an int into a
	 * double, so we must check for compatibility with the type.
	 * 
	 * @return Field value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Try to convert INTEGER or DOUBLE to integer
	 * 
	 * @return A int value
	 */
	public int getInt() {
		if (value instanceof Double) {
			return ((Double) value).intValue();
		}
		return (int) value;
	}

	/**
	 * Try to convert INTEGER or DOUBLE to double
	 * 
	 * @return A double value
	 */
	public double getDouble() {
		if (value instanceof Integer) {
			return (double) (Integer) value;
		}
		return (double) value;
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
			try {
				this.setValue(Integer.parseInt(value));
			} catch (Exception e) {
				try {
					this.setValue(Double.parseDouble(value));
				} catch (Exception e1) {
					// don't change the value
				}
			}
			break;
		default:
			java.lang.System.err.println("Could not set field value from string");
			break;
		}
	}

}
