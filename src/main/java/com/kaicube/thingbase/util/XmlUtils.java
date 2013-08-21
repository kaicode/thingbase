package com.kaicube.thingbase.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class XmlUtils {

    private final DocumentBuilderFactory documentBuilderFactory;
    private TransformerFactory transformerFactory;

	public XmlUtils() {
		transformerFactory = TransformerFactory.newInstance();
        documentBuilderFactory = DocumentBuilderFactory.newInstance();
	}
	
	public String nodeToString(Node node) throws TransformerConfigurationException, TransformerException {
		StringWriter stringWriter = new StringWriter();
		transformerFactory.newTransformer().transform(new DOMSource(node), new StreamResult(stringWriter));
		return stringWriter.toString();
	}

    public Document stringToNode(String document) throws ParserConfigurationException, IOException, SAXException {
        return documentBuilderFactory.newDocumentBuilder().parse(new ByteArrayInputStream(document.getBytes()));
    }
}
