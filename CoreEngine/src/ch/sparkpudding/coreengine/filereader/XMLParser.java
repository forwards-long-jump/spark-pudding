package ch.sparkpudding.coreengine.filereader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Tool for handling XML files.
 * 
 * @author Alexandre Bianchi, Pierre Bürki, Loïck Jeanneret, John Leuba
 * 
 */
public class XMLParser {

	/**
	 * Parse XML from file
	 * 
	 * @param xmlFile
	 * @return XML Document
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	static public Document parse(File xmlFile) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(xmlFile);
		doc.getDocumentElement().normalize();
		return doc;
	}
	
	/**
	 * Parse XML from stream
	 * 
	 * @param xmlStream
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	static public Document parse(InputStream xmlStream) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(xmlStream);
		doc.getDocumentElement().normalize();
		return doc;
	}
}
