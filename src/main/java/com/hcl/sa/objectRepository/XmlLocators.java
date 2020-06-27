package com.hcl.sa.objectRepository;

public interface XmlLocators {
    String COMP_ATTR_XPATH = "//Computer/@Resource";
    String IP_ADDRESS_XPATH = "//Computer/Property[@Name ='IP Address']/text()";
    String ACTION_ID_XPATH = "//Action/ID/text()";
    String ACTION_SCRIPT_XPATH = "//ActionScript/text()";
    String COMPUTER_STATUS_XPATH = "//Computer/Status/text()";
    String ACTION_LASTMODIFIED_XPATH = "(//Name[starts-with(text(), 'Install Latest Automation Plan Engine')]/parent::Action[last()]/@LastModified)";
    String ACTION_STATUS_XPATH = "//Name[starts-with(text(), 'Install Latest Automation Plan Engine')]/parent::Action[@LastModified='date']/ID/text()";


}
