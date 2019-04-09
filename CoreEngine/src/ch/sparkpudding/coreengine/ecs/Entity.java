package ch.sparkpudding.coreengine.ecs;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba
 * 
 */
public class Entity {

	private static Map<String, Entity> templates;
	static {
		setTemplates(new HashMap<String, Entity>());
	}
	
	private Map<String, Component> components;
	private String name;
	private int zIndex;
	
	/**
	 * Default constructor
	 */
	public Entity() {
		this("", 0);
	}
	
	/**
	 * Constructor
	 * @param name Name of the entity
	 * @param zIndex z index, larger numbers imply foreground
	 */
	public Entity(String name, int zIndex) {
		this.name = name;
		this.setzIndex(zIndex);
		this.components = new HashMap<String, Component>();
	}
	
	/**
	 * Create an entity from a parsed XML Document and populate its components
	 * @param document A properly formated Document to get components from
	 */
	public Entity(Document document) {
		this.components = new HashMap<String, Component>();
		Element entityElement = document.getDocumentElement();
		this.name = entityElement.getAttribute("name");
		String zindex = entityElement.getAttribute("zindex");
		if (zindex.length() > 0) {
			this.setzIndex(Integer.parseInt(zindex));
		} else {
			this.setzIndex(0);
		}
		
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
	 * Adds a component to the entity
	 * @param c Component to be added
	 */
	public void add(Component c) {
		components.put(c.getName(), c);
	}
	
	/**
	 * Removes a component to the entity
	 * @param name Name of the component to be removed 
	 */
	public void remove(String name) {
		components.remove(name);
	}
	
	/**
	 * Gets the name of the entity
	 * @return Name of the entity
	 */
	public String getName() {
		return name;
	}
	
	public int getzIndex() {
		return zIndex;
	}

	public void setzIndex(int zIndex) {
		this.zIndex = zIndex;
	}
	
	public Map<String, Component> getComponents() {
		return components;
	}
	
	
	//******** STATIC **************

	/**
	 * Get entity templates
	 * @return Associative array name => entity
	 */
	public static Map<String, Entity> getTemplates() {
		return templates;
	}

	/**
	 * Set entity templates
	 * @param templates
	 */
	public static void setTemplates(Map<String, Entity> templates) {
		Entity.templates = templates;
	}
	
	public static void addTemplate(Entity template) {
		templates.put(template.getName(), template);
	}
}
