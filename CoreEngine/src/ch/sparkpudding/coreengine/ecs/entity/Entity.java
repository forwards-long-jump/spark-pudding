package ch.sparkpudding.coreengine.ecs.entity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ch.sparkpudding.coreengine.ecs.component.Component;

/**
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba
 * 
 *         Part of the ECS design pattern, described by the components it
 *         contains.
 * 
 */
public class Entity implements Iterable<Entry<String, Component>> {

	private static Map<String, Entity> templates;
	static {
		templates = new HashMap<String, Entity>();
	}

	private Map<String, Component> components;
	private String name, template;
	private int zIndex;

	/**
	 * Default constructor
	 */
	public Entity() {
		this("", "", 0);
	}

	/**
	 * Constructor
	 * 
	 * @param name   Name of the entity
	 * @param zIndex z index, larger numbers imply foreground
	 */
	public Entity(String name, String template, int zIndex) {
		this.name = name;
		this.template = template;
		this.setZIndex(zIndex);
		this.components = new HashMap<String, Component>();
	}

	/**
	 * Copy constructor
	 * 
	 * @param entity
	 */
	public Entity(Entity entity) {
		this.name = entity.name;
		this.template = entity.template;
		this.zIndex = entity.zIndex;
		this.components = new HashMap<String, Component>();
		for (Component component : entity.getComponents().values()) {
			this.add(new Component(component));
		}
	}

	/**
	 * Create an entity from a parsed XML Document and populate its components.
	 * 
	 * Note that entities described in documents must be templates.
	 * 
	 * @param document A properly formated Document to get components from
	 */
	public Entity(Document document) {
		Element entityElement = document.getDocumentElement();

		this.name = entityElement.getAttribute("name");
		
		this.template = entityElement.getAttribute("template");

		String zindex = entityElement.getAttribute("z-index");
		if (zindex.length() > 0) {
			this.setZIndex(Integer.parseInt(zindex));
		} else {
			this.setZIndex(0);
		}

		this.components = new HashMap<String, Component>();
		NodeList components = entityElement.getChildNodes();
		for (int i = 0; i < components.getLength(); i++) {
			Node node = components.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element componentElement = (Element) components.item(i);
				this.add(new Component(componentElement));
			}
		}
	}

	/**
	 * Creates an entity from a template, and adds changes described in the XML
	 * element
	 * 
	 * @param element A properly formated XML element describing the entitiy
	 */
	public Entity(Element element) {
		this(templates.get(element.getAttribute("template")));
		this.name = element.getAttribute("name");
		this.zIndex = Integer.parseInt(element.getAttribute("z-index"));

		NodeList components = element.getChildNodes();
		for (int i = 0; i < components.getLength(); i++) {
			Node node = components.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element componentElement = (Element) components.item(i);
				if (componentElement.hasAttribute("delete")) {
					this.remove(componentElement.getAttribute("template"));
				} else {
					// Either edit or add this component
					// Since this.remove does nothing on inexistent names,
					// we can just remove and add anew
					this.remove(componentElement.getAttribute("template"));
					this.add(new Component(componentElement));
				}
			}
		}
	}

	/**
	 * Adds a component to the entity
	 * 
	 * @param c Component to be added
	 */
	public void add(Component c) {
		components.put(c.getName(), c);
	}

	/**
	 * Removes a component to the entity
	 * 
	 * @param name Name of the component to be removed
	 */
	public void remove(String name) {
		components.remove(name);
	}

	/**
	 * Gets the name of the entity
	 * 
	 * @return Name of the entity
	 */
	public String getName() {
		return name;
	}

	public int getZIndex() {
		return zIndex;
	}

	public void setZIndex(int zIndex) {
		this.zIndex = zIndex;
	}

	public Map<String, Component> getComponents() {
		return components;
	}

	@Override
	public Iterator<Entry<String, Component>> iterator() {
		return components.entrySet().iterator();
	}

	/**
	 * Get entity templates
	 * 
	 * @return Associative array name => entity
	 */
	public static Map<String, Entity> getTemplates() {
		return templates;
	}

	/**
	 * Set entity templates
	 * 
	 * @param templates
	 */
	public static void setTemplates(Map<String, Entity> templates) {
		Entity.templates = templates;
	}

	public static void addTemplate(Entity template) {
		templates.put(template.getName(), template);
	}

	public boolean hasComponents(List<String> componentNames) {
		return components.keySet().containsAll(componentNames);
	}

	public String getTemplate() {
		return template;
	}
}
