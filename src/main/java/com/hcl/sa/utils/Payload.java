package com.hcl.sa.utils;

import com.hcl.sa.constants.ConsoleConsts;
import com.hcl.sa.constants.CreatePlanConsts;
import com.hcl.sa.constants.XMLConsts;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.transform.TransformerException;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;


public class Payload {

    XMLParser xmlParser = new XMLParser();

    Logger logger = LogManager.getLogger(Payload.class);

    // For custom site: Prefix will be CustomSite_[Name of site]
    public HashMap<String, HashMap<String, String>> getAttributeForBaselinePayload(String sourceSiteName) {
        HashMap<String, String> fixletID = (HashMap<String, String>) SuperClass.specStore.get(CreatePlanConsts.FIXLET_DETAILS);
        HashMap<String, String> attr = new HashMap<>();
        attr.put(XMLConsts.SOURCE_SITE_URL_ATTR.name(), XMLConsts.SOURCE_SITE_URL.text.replace(XMLConsts.SITE_NAME_REG_EX.text, sourceSiteName));
        HashMap<String, HashMap<String, String>> baselineComponentAttr = new HashMap<>();
        for (Map.Entry<String, String> sourceID : fixletID.entrySet()) {
            baselineComponentAttr.put(sourceID.getValue(), attr);
        }
        logger.debug("Baseline Attributes=" + baselineComponentAttr);
        return baselineComponentAttr;
    }

    // For custom site: Prefix will be CustomSite_[Name of site]. It can very as per site name. Consider Console for exact name
    public String createBaseline(FileInputStream fis, String sourceSiteName) throws TransformerException {
        Document doc = xmlParser.buildDocument(fis);
        NodeList nodes = doc.getElementsByTagName(XMLConsts.BASELINE_COMPONENT_GROUP.text);
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            Element element = (Element) node;
            for (Map.Entry<String, HashMap<String, String>> baselineComp : getAttributeForBaselinePayload(sourceSiteName).entrySet()) {
                Element newElement = doc.createElement(XMLConsts.BASELINE_COMPONENT.text);
                newElement.setAttribute(XMLConsts.INCLUDE_IN_RELEVANCE_ATTR.text, XMLConsts.INCLUDE_IN_REL_ATTR_VAL.text);
                newElement.setAttribute(XMLConsts.SOURCE_SITE_URL_ATTR.text, baselineComp.getValue().get(XMLConsts.SOURCE_SITE_URL_ATTR.name()));
                newElement.setAttribute(XMLConsts.ACTION_NAME_ATTR.text, XMLConsts.ACTION_NAME_ATTR_VAL.text);
                newElement.setAttribute(XMLConsts.SOURCE_ID_ATTR.text, baselineComp.getKey());
                element.appendChild(newElement);
            }
        }
        return xmlParser.convertDocToString(doc);
    }

    public String createAction(FileInputStream fis, HashMap<String, String> params, String computerID) throws TransformerException {
        Document doc = xmlParser.buildDocument(fis);
        doc.getElementsByTagName("Sitename").item(0).setTextContent(params.get(ConsoleConsts.SITE_NAME.text));
        doc.getElementsByTagName("FixletID").item(0).setTextContent(params.get(ConsoleConsts.FIXLET_ID.text));
        doc.getElementsByTagName("ComputerID").item(0).setTextContent(computerID);
        return xmlParser.convertDocToString(doc);
    }
}
