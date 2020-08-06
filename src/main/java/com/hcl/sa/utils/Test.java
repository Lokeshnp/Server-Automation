//package com.hcl.sa.utils;
//
//import org.jdom.JDOMException;
//import org.w3c.dom.Document;
//import org.w3c.dom.NodeList;
//import org.xml.sax.SAXException;
//
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.parsers.ParserConfigurationException;
//import javax.xml.transform.TransformerException;
//import javax.xml.xpath.*;
//import java.io.*;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//public class Test {
//
//    public StringBuilder readTextFile(String textFilepath) throws IOException {
//        File file = new File(textFilepath);
//        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
//        StringBuilder stringBuilder = new StringBuilder();
//        String textData = bufferedReader.readLine();
//        while (textData != null) {
//            stringBuilder.append(textData).append("\n");
//            textData = bufferedReader.readLine();
//        }
//        return stringBuilder;
//    }
//
//
//    public static void main(String[] args) throws JDOMException, IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, SAXException, ParserConfigurationException, TransformerException {
//      Test test = new Test();
//      test.newElementForBaseline();
////      test.modifyXml();
////      test.readTextFile()
////        System.setProperty(DOMImplementationRegistry.PROPERTY, "com.sun.org.apache.xerces.internal.dom.DOMXSImplementationSourceImpl");
////        DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
////        com.sun.org.apache.xerces.internal.impl.xs.XSImplementationImpl impl = (XSImplementationImpl) registry.getDOMImplementation("XS-Loader");
////        XSLoader schemaLoader = impl.createXSLoader(null);
////        XSModel model = schemaLoader.loadURI("src/test/resources/my.xsd");
//
//    }
//
//    public List<String> getElementOfXmlByXpath(InputStream inputData, String xpathString) throws IOException, SAXException {
//        Document document = buildDocument(inputData);
//        XPath xpath = buildXpath();
//        List<String> element = new ArrayList<>();
//        try {
//            XPathExpression expr = xpath.compile(xpathString);
//            NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
//            for (int i = 0; i < nodes.getLength(); i++)
//                element.add(nodes.item(i).getNodeValue().trim());
//        }
//        catch (XPathExpressionException e) {
//            e.printStackTrace();
//        }
//        return element;
//    }
//
//    public XPath buildXpath() {
//        XPathFactory xpathFactory = XPathFactory.newInstance();
//        XPath xpath = xpathFactory.newXPath();
//        return xpath;
//    }
//
//
//
//
//
//    public NodeList getElementsByTagName(InputStream inputData, String tagName) throws ParserConfigurationException, SAXException, IOException {
//        Document doc = buildDocument(inputData);
//        NodeList nodeList = doc.getElementsByTagName(tagName);
//        return nodeList;
//    }
//
//    public void newElementForBaseline() throws IOException, TransformerException, SAXException {
////        HashMap<String,String> fixletID = new HashMap<>();
////        fixletID.put("F1","2533");
////        fixletID.put("F2","2534");
////        HashMap<String,String> commonAttrs = new HashMap<>();
////        commonAttrs.put("IncludeInRelevance","true");
////        commonAttrs.put("ActionName","Action1");
////        commonAttrs.put("SourceSiteURL","http://winpatch-bes-1:52311/cgi-bin/bfgather.exe/CustomSite_Abhinav");
////        HashMap<String,HashMap<String,String>> newElement = new HashMap<>();
////        for(Map.Entry<String,String> entrySet:fixletID.entrySet()){
////            newElement.put(entrySet.getValue(),commonAttrs);
////        }
////        System.out.println("New Element size="+newElement.size());
////    modifyXml(newElement);
//        XMLParser xmlParser = new XMLParser();
//        FileInputStream fis = new FileInputStream("C:\\sa-dashboard-automation\\src\\test\\resources\\Payloads\\test.xml");
//        String id = xmlParser.getElementOfXmlByXpath(fis,"//ID/text()").get(0);
//        System.out.println("id="+id);
//    }
//
//    public void modifyXml(HashMap<String,HashMap<String,String>> baselineComponent) throws IOException, TransformerException, SAXException {
////        Document doc = buildDocument(fis);
////        NodeList nodes = doc.getEle("BaselineComponentGroup");
////        for (int i = 0; i < nodes.getLength(); i++) {
////            Node node = nodes.item(i);
////            Element element = (Element) node;
////                for(Map.Entry<String,HashMap<String,String>> baselineComp :baselineComponent.entrySet()){
////                    Element newElement = doc.createElement("BaselineComponent");
////                    newElement.setAttribute("IncludeInRelevance",baselineComp.getValue().get("IncludeInRelevance"));
////                    newElement.setAttribute("SourceSiteURL",baselineComp.getValue().get("SourceSiteURL"));
////                    newElement.setAttribute("ActionName",baselineComp.getValue().get("ActionName"));
////                    newElement.setAttribute("SourceID",baselineComp.getKey());
////                    element.appendChild(newElement);
////            }
////        }
////
////        DOMSource domSource = new DOMSource(doc);
////        Transformer transformer = TransformerFactory.newInstance().newTransformer();
////        StringWriter sw = new StringWriter();
////        StreamResult sr = new StreamResult(sw);
////        transformer.transform(domSource, sr);
////        System.out.println("test " + sw.toString());
//    }
//    public void getPayLoadForBaseline() throws JDOMException, IOException, SAXException, ParserConfigurationException {
////        FileInputStream fis = new FileInputStream("C:/sa-dashboard-automation/src/test/resources/createBaseline.xml");
////        NodeList nodes = getElementsByTagName(fis, "BaselineComponentGroup");
////        System.out.println("list="+list.getLength());
////        System.out.println(list.item(0).getTextContent());
////        System.out.println("size="+getElementOfXmlByXpath(fis,"//BaselineComponentCollection/BaselineComponentGroup/BaselineComponent/@IncludeInRelevance").size());
//
//    }
//
//
//    public Document buildDocument(InputStream inputData){
//        Document doc = null;
//        try {
//            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//            doc = dBuilder.parse(inputData);
//            doc.getDocumentElement().normalize();
//        }
//        catch (ParserConfigurationException |SAXException|IOException e) {
//            e.printStackTrace();
//        }
//        return doc;
//    }
//
//
//}
