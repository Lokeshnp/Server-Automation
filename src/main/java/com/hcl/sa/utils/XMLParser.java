package com.hcl.sa.utils;

import com.hcl.sa.constants.XMLConsts;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class XMLParser {

    Logger logger = LogManager.getLogger(XMLParser.class);

    public String xmlFilePath = null;

    //TODO Method can be removed later
    public Document getXMLParser() throws ParserConfigurationException, SAXException, IOException {
        logger.debug("XML File Path="+xmlFilePath);
        File file = new File(xmlFilePath);
        InputStream xmlFile = new FileInputStream(file);
        Document doc = buildDocument(xmlFile);
        return doc;
    }

    public InputStream getXMLInputStream() throws FileNotFoundException {
        logger.debug("XML File Path="+xmlFilePath);
        File file = new File(xmlFilePath);
        InputStream xmlFile = new FileInputStream(file);
        return xmlFile;
    }
    
    public Document buildDocument(InputStream inputData){
        Document doc = null;
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(inputData);
            doc.getDocumentElement().normalize();
        }
        catch (ParserConfigurationException|SAXException|IOException e) {
            e.printStackTrace();
        }
        return doc;
    }

    public XPath buildXpath() {
        XPathFactory xpathFactory = XPathFactory.newInstance();
        XPath xpath = xpathFactory.newXPath();
        return xpath;
    }

    public  NodeList getElementsByTagName(InputStream inputData, String tagName) throws ParserConfigurationException, SAXException, IOException {
        Document doc = buildDocument(inputData);
        NodeList nodeList = doc.getElementsByTagName(tagName);
        return nodeList;
    }

    public String getFixPackVersion() throws ParserConfigurationException, SAXException, IOException {
        String version = null;
        NodeList nodeList = getElementsByTagName(getXMLInputStream(), XMLConsts.OFFERING.text);
        for(int i =0 ; i < nodeList.getLength();i ++) {
            Node node = nodeList.item(i);
            if(Node.ELEMENT_NODE == node.getNodeType()) {
                Element ele =  (Element)node;
                 version = ele.getAttribute(XMLConsts.VERSION_ATTR.text);
                if(!version.isEmpty()) {
                    break;
                }
            }
        }
        return  version;
    }

    public String getFixPackLowerBound() throws ParserConfigurationException, SAXException, IOException {
        String version = null;
        NodeList nodeList = getElementsByTagName(getXMLInputStream(), XMLConsts.BASE_OFFERING.text);
        for(int i =0 ; i < nodeList.getLength();i ++) {
            Node node = nodeList.item(i);
            if(Node.ELEMENT_NODE == node.getNodeType()) {
                Element ele =  (Element)node;
                version = ele.getAttribute(XMLConsts.VERSION_ATTR.text);
                if(!version.isEmpty()) {
                    break;
                }
            }
        }
        return version;
    }

    public List<String> getElementOfXML_UsingXPath(InputStream inputData, String xpathString) throws IOException, SAXException {
        Document document = buildDocument(inputData);
        XPath xpath = buildXpath();
        List<String> element = new ArrayList<>();
        try {
            XPathExpression expr = xpath.compile(xpathString);
            NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
            for (int i = 0; i < nodes.getLength(); i++)
                element.add(nodes.item(i).getNodeValue().trim());
        }
        catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return element;
    }

}
