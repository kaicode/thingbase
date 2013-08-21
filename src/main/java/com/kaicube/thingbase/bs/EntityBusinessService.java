package com.kaicube.thingbase.bs;

import java.io.ByteArrayInputStream;
import java.sql.SQLException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import com.kaicube.thingbase.util.XmlUtils;
import com.kaicube.thingbase.dao.DAO;
import com.kaicube.thingbase.dao.DAOH2Impl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class EntityBusinessService {

	private XmlUtils xmlUtils;
	private XPathFactory xPathFactory;
	private DAO dao;
	private DocumentBuilderFactory documentBuilderFactory;


	public EntityBusinessService() throws ClassNotFoundException, SQLException {
		xPathFactory = XPathFactory.newInstance();
		xmlUtils = new XmlUtils();
		documentBuilderFactory = DocumentBuilderFactory.newInstance();
		dao = new DAOH2Impl();
	}

    public String store(String documentString) throws Exception {
        Document document = xmlUtils.stringToNode(documentString);
        store(document);
        return xmlUtils.nodeToString(document);
    }

    public Document store(Document document) throws Exception {
		XPath xPath = xPathFactory.newXPath();
		NodeList persistentNodes = (NodeList) xPath.evaluate("//*[@persistent]", document, XPathConstants.NODESET);
		for (int i = 0; i < persistentNodes.getLength(); i++) {
			Element element = (Element) persistentNodes.item(i);
			String entityString = xmlUtils.nodeToString(element);

			long id = dao.store(element.getNodeName(), entityString);
			element.setAttribute("id", "" + id);
		}
        return document;
	}
	
	public String loadAsString(String entityType, long id) throws Exception {
        Document entity = load(entityType, id);
        return xmlUtils.nodeToString(entity);
    }

	public Document load(String entityType, long id) throws Exception {
        Document document = null;
		String entityString = dao.load(entityType, id);
        if (entityString != null) {
            document = documentBuilderFactory.newDocumentBuilder().parse(new ByteArrayInputStream(entityString.getBytes()));
            Element root = (Element) document.getFirstChild();
            root.setAttribute("id", "" + id);
        }
		return document;
	}

	public static void main(String[] args) throws Exception {
		EntityBusinessService bs = new EntityBusinessService();
		DocumentBuilder b = bs.documentBuilderFactory.newDocumentBuilder();
		Document document = b.parse(new ByteArrayInputStream("<customer persistent='true'><name>Kai</name></customer>".getBytes()));
		bs.store(document);
		System.out.println(bs.xmlUtils.nodeToString(document));
		
		Document load = bs.load("customer", 2);
		System.out.println(bs.xmlUtils.nodeToString(load));
	}

}
