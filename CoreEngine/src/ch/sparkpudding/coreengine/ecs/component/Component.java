package ch.sparkpudding.coreengine.ecs.component;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Represents settings (key-value pairs) that can be attached to an entity
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba
 * 
 */
public class Component implements Iterable<Entry<String, Field>> {

	private static Map<String, Component> templates;
	static {
		templates = new HashMap<String, Component>();
	}

	private String name;
	private String template;
	private Map<String, Field> fields;

	/**
	 * Create an empty component
	 * 
	 * @param name     : A unique name per component
	 * @param template : The component template
	 */
	public Component(String name, String template) {
		this.name = name;
		this.template = template;
		this.fields = new HashMap<String, Field>();
	}

	/**
	 * 
	 * @param name   : A unique name per component
	 * @param fields : The components fields
	 */
	public Component(String name, Map<String, Field> fields) {
		this.name = name;
		this.fields = fields;
	}

	/**
	 * Copy constructor
	 * 
	 * @param component : The component to copy
	 */
	public Component(Component component) {
		this.name = component.name;
		this.template = component.template;
		this.fields = new HashMap<String, Field>();
		for (Field field : component.fields.values()) {
			addField(new Field(field));
		}
	}

	/**
	 * Create a component from a parsed XML Document and populate its fields.
	 * 
	 * Note that if a document is to describe a component, then this component must
	 * be a template.
	 * 
	 * @param document A properly formated Document to get fields from
	 */
	public Component(Document document) {
		this.fields = new HashMap<String, Field>();
		this.name = document.getDocumentElement().getAttribute("name");
		this.template = null;

		NodeList fields = document.getDocumentElement().getChildNodes();
		for (int i = 0; i < fields.getLength(); i++) {
			Node node = fields.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element fieldElement = (Element) fields.item(i);
				this.fields.put(fieldElement.getAttribute("name"), new Field(fieldElement.getAttribute("name"),
						fieldElement.getAttribute("type"), fieldElement.getTextContent()));
			}
		}
	}

	/**
	 * Create a component from a template, and adds changes described in the XML
	 * element
	 * 
	 * @param element A properly formatted XML element describing the component
	 */
	public Component(Element element) {
		this(templates.get(element.getAttribute("template")));
		this.template = element.getAttribute("template");
		NodeList fields = element.getChildNodes();
		for (int i = 0; i < fields.getLength(); i++) {
			Node node = fields.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element fieldElement = (Element) fields.item(i);
				this.fields.get(fieldElement.getAttribute("name")).setValueFromString(fieldElement.getTextContent());
			}
		}
	}

	/**
	 * Add a field to this component
	 * 
	 * @param field Field to add, name must not already exists
	 */
	public void addField(Field field) {
		fields.put(field.getName(), field);
	}

	public Field getField(String name) {
		return fields.get(name);
	}

	/**
	 * Fields getter
	 * 
	 * @return Map<String, Field> containing all fields
	 */
	public Map<String, Field> getFields() {
		return fields;
	}

	@Override
	public Iterator<Entry<String, Field>> iterator() {
		return fields.entrySet().iterator();
	}

	/**
	 * Name getter
	 * 
	 * @return name of the component
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get component templates
	 * 
	 * @return Associative array name => component
	 */
	public static Map<String, Component> getTemplates() {
		return templates;
	}

	/**
	 * Add component template
	 * 
	 * @param template Component template to add
	 */
	public static void addTemplate(Component template) {
		templates.put(template.getName(), template);
	}

	public String getTemplate() {
		return template;
	}
}
