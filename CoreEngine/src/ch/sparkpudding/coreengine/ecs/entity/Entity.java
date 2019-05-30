package ch.sparkpudding.coreengine.ecs.entity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ch.sparkpudding.coreengine.Lel;
import ch.sparkpudding.coreengine.Scheduler.Trigger;
import ch.sparkpudding.coreengine.api.MetaEntity;
import ch.sparkpudding.coreengine.ecs.component.Component;
import ch.sparkpudding.coreengine.utils.Pair;

/**
 * Part of the ECS design pattern, described by the components it contains.
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba
 * 
 */
public class Entity implements Iterable<Entry<String, Component>> {

	private static Map<String, Entity> templates;
	static {
		templates = new HashMap<String, Entity>();
	}

	private Map<String, Component> components;
	private String name;
	private String template;
	private int zIndex;

	private LuaTable luaEntity;

	/**
	 * Default constructor
	 */
	public Entity() {
		this("", "", 0, new HashMap<String, Component>());
	}

	/**
	 * Constructor
	 * 
	 * @param name   Name of the entity
	 * @param zIndex z index, larger numbers imply foreground
	 */
	public Entity(String name, String template, int zIndex, HashMap<String, Component> components) {
		this.name = name;
		this.template = template;
		this.setZIndex(zIndex);
		this.components = components;

		createLuaEntity();
	}

	/**
	 * Copy constructor
	 * 
	 * @param entity
	 */
	public Entity(Entity entity) {
		this(entity.name, entity.template, entity.zIndex, new HashMap<String, Component>());

		// copy components
		this.components = new HashMap<String, Component>();
		for (Component component : entity.getComponents().values()) {
			this.components.put(component.getName(), new Component(component));
		}

		createLuaEntity();
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
		this.template = this.name;

		String zindex = entityElement.getAttribute("z-index");
		if (zindex.length() > 0) {
			this.setZIndex(Integer.parseInt(zindex));
		} else {
			this.setZIndex(0);
		}

		this.components = new HashMap<String, Component>();
		NodeList componentsXML = entityElement.getChildNodes();
		for (int i = 0; i < componentsXML.getLength(); i++) {
			Node node = componentsXML.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element componentElement = (Element) componentsXML.item(i);
				this.addComponent(new Component(componentElement));
			}
		}
	}

	/**
	 * Creates an entity from a template, and adds changes described in the XML
	 * element
	 * 
	 * @param element A properly formated XML element describing the entity
	 */
	public Entity(Element element) {
		this(templates.getOrDefault(element.getAttribute("template"), new Entity()));
		this.name = element.getAttribute("name");
		this.template = element.getAttribute("template");
		this.zIndex = Integer.parseInt(element.getAttribute("z-index"));

		NodeList components = element.getChildNodes();
		for (int i = 0; i < components.getLength(); i++) {
			Node node = components.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element componentElement = (Element) components.item(i);
				if (componentElement.hasAttribute("delete")) {
					this.removeComponent(componentElement.getAttribute("template"));
				} else {
					// Either edit or add this component
					// Since this.remove does nothing on inexistent names,
					// we can just remove and add anew
					this.removeComponent(componentElement.getAttribute("template"));
					this.addComponent(new Component(componentElement));
				}
			}
		}

		createLuaEntity();
	}

	/**
	 * Instantiate the Lua entity
	 */
	private void createLuaEntity() {
		this.luaEntity = coerceToLua();
	}

	/**
	 * Adds a component to the entity
	 * 
	 * @param c Component to be added
	 */
	public void addComponent(Component c) {
		components.put(c.getName(), c);

		Lel.coreEngine.getScheduler().trigger(Trigger.COMPONENT_ADDED, new Pair<Entity, Component>(this, c));

		createLuaEntity();
	}

	/**
	 * Adds a component from the templates to the entity. This method is intended to
	 * be called by the Lua systems, and as such needs to update the Lua entity
	 * 
	 * @param componentName the name of the component
	 * @return true if the component was added
	 */
	public boolean addComponent(String componentName) {
		Component component = Component.getTemplates().get(componentName);
		if (component != null && !components.containsKey(componentName)) {
			addComponent(component);

			// update luaEntity
			createLuaEntity();
			Lel.coreEngine.getScheduler().trigger(Trigger.COMPONENT_ADDED,
					new Pair<Entity, Component>(this, component));
			return true;
		}
		return false;
	}

	/**
	 * Removes a component from the entity
	 * 
	 * @param name Name of the component to be removed
	 * @return true if the component was removed
	 */
	public boolean removeComponent(String name) {
		if (components.containsKey(name)) {
			components.remove(name);

			// update luaEntity
			createLuaEntity();
			return true;
		}
		return false;
	}

	/**
	 * Gets the name of the entity
	 * 
	 * @return Name of the entity
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the z index
	 * 
	 * @return z index
	 */
	public int getZIndex() {
		return zIndex;
	}

	/**
	 * Attach/detach specified component to entity and use entity template if
	 * specifable
	 * 
	 * @param componentName name of component to attach/detach
	 * @param attach        boolean if component should be attached or detach
	 */
	public void setComponentAttached(String componentName, boolean attached) {
		components.get(componentName).setAttached(attached, true);
		if (attached) {
			if (templates.get(template).getComponents().get(componentName) != null) {
				Component nc = new Component(templates.get(template).getComponents().get(componentName));
				components.put(componentName, nc);
			}
		}
	}

	/**
	 * Sets the z index
	 * 
	 * @param zIndex
	 */
	public void setZIndex(int zIndex) {
		this.zIndex = zIndex;
	}

	/**
	 * Sets the name
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets components
	 * 
	 * @return components
	 */
	public Map<String, Component> getComponents() {
		return components;
	}

	/**
	 * Gets iterator on the components
	 */
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

	/**
	 * Add an entity to the template list
	 * 
	 * @param template
	 */
	public static void addTemplate(Entity template) {
		Entity newTemplate = new Entity(template);

		// Force template to have all their components attached (without reseting from
		// comp template)
		for (Entry<String, Component> componentEntry : newTemplate.getComponents().entrySet()) {
			componentEntry.getValue().setAttached(true, false);
		}

		templates.put(template.getTemplate(), newTemplate);
	}

	/**
	 * Get entity template
	 * 
	 * @return string name of the template
	 */
	public String getTemplate() {
		return template;
	}

	/**
	 * Set the template used by this entity (has no effects on fields until reload)
	 * 
	 * @param template to set
	 */
	public void setTemplate(String template) {
		this.template = template;
	}

	/**
	 * Return true if entity has all specified components
	 * 
	 * @param componentNames
	 * @return true if entity has all specified components
	 */
	public boolean hasComponents(List<String> componentNames) {
		return components.keySet().containsAll(componentNames);
	}

	/**
	 * Convert this entity to a Luatable in the form of entity.component.field
	 * 
	 * @return Luatable in the form of entity.component.field
	 */
	private LuaTable coerceToLua() {
		LuaTable entityLua = new LuaTable();
		for (Component component : this.getComponents().values()) {
			// entity.component
			entityLua.set(component.getName(), component.coerceToLua());
		}
		entityLua.set("_meta", CoerceJavaToLua.coerce(new MetaEntity(this)));
		return entityLua;
	}

	/**
	 * Gets the Lua entity
	 * 
	 * @return Lua entity
	 */
	public LuaTable getLuaEntity() {
		return luaEntity;
	}

	/**
	 * Return true if the entity has the specified component
	 * 
	 * @param componentName to look for
	 * @return true if the entity has the specified component
	 */
	public boolean hasComponent(String componentName) {
		return components.keySet().contains(componentName);
	}
}
