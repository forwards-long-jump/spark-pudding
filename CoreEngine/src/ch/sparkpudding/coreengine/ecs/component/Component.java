package ch.sparkpudding.coreengine.ecs.component;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ch.sparkpudding.coreengine.utils.Lua;

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
	private String templateName;
	private Map<String, Field> fields;

	/**
	 * Create an empty component with a reference on it's template
	 * 
	 * @param name     : A unique name per component
	 * @param template : The component template
	 */
	public Component(String name, String template) {
		this.name = name;
		this.templateName = template;
		this.fields = new HashMap<String, Field>();
	}

	/**
	 * Template component constructor
	 * 
	 * @param name   : A unique name per component
	 * @param fields : The components fields
	 */
	public Component(String name, Map<String, Field> fields) {
		this.name = name;
		this.fields = fields;
		this.templateName = null;
	}

	/**
	 * Copy constructor
	 * 
	 * @param component : The component to copy
	 */
	public Component(Component component) {
		this.name = component.name;
		this.templateName = component.templateName;
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
		this.templateName = null;

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
		this.templateName = element.getAttribute("template");
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

	/**
	 * Return the template name of this component
	 * 
	 * @return
	 */
	public String getTemplateName() {
		return templateName;
	}

	/**
	 * Convert this entity to a Luatable in the form of component.field
	 * 
	 * @return Luatable in the form of component.field
	 */
	public LuaValue coerceToLua() {
		LuaTable componentLua = new LuaTable();
		LuaValue metatableSetterMethod = Lua.getMetatableSetterMethod();

		for (Field field : getFields().values()) {
			// entity.component.field
			LuaValue fieldLua = CoerceJavaToLua.coerce(field);
			componentLua.set("_" + field.getName(), fieldLua);
		}

		metatableSetterMethod.call(componentLua);
		return componentLua;	
	}
}
