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
public class Component {

	private String name;
	private Map<String, Field> fields;

	public Component(String name) {
		this.name = name;
		this.fields = new HashMap<String, Field>();
	}

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

	public void addField(Field field) {
		fields.put(field.getName(), field);
	}
	
	public Map<String, Field> getFields() {
		return fields;
	}

	public String getType() {
		return name;
	}
}
