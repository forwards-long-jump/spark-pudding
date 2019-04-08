package ch.sparkpudding.coreengine.ecs;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Represents settings (key values pairs) that can be attached to an entity
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba
 * 
 */
public class Component {

	private String name;
	private Map<String, Field> fields;

	/**
	 * Create an empty component
	 * @param name A unique name per component
	 */
	public Component(String name) {
		this.name = name;
		this.fields = new HashMap<String, Field>();
	}

	/**
	 * Create a component from a parsed XML Document and populate its fields
	 * @param document A properly formated Document to get fields from
	 */
	public Component(Document document) {
		this.fields = new HashMap<String, Field>();
		this.name = document.getDocumentElement().getAttribute("name");
				
		NodeList fields = document.getDocumentElement().getChildNodes();
		for (int i = 0; i < fields.getLength(); i++) {
			Node node = fields.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element field = (Element) fields.item(i);
				this.fields.put(field.getAttribute("name"),
						new Field(field.getAttribute("name"), field.getAttribute("type"), field.getTextContent()));
			}
		}
	}

	/**
	 * Add a field to this component
	 * @param field Field to add, name must not already exists
	 */
	public void addField(Field field) {
		fields.put(field.getName(), field);
	}
	
	/**
	 * Fields getter
	 * @return Map<String, Field> containing all fields
	 */
	public Map<String, Field> getFields() {
		return fields;
	}

	/**
	 * Name getter
	 * @return name of the component
	 */
	public String getType() {
		return name;
	}
}
