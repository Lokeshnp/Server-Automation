package com.hcl.sa.constants;

public enum XMLConsts {
	
	NAME_ATTR("name"),INFORMATION_TAG("information"),VERSION_ATTR("version"),PROPERTY_TAG("property"),APPLICABLE_OFFERINGS("applicable.offerings"),
	VALUE_ATTR("value"),OFFERING("offering"),BASE_OFFERING("baseOffering"),

	FIXLET_ID_XPATH("//Fixlet/ID/text()"), CUSTOM_SITE_NAME_XPATH("//CustomSite/Name/text()"), COMPUTER_ATTRIBUTE_XPATH("//Computer/@Resource"),
	IP_ADDRESS_XPATH("//Computer/Property[@Name ='IP Address']/text()"),COMPUTER_TAGNAME("Computer"), ACTION_ID_XPATH("//Action/ID/text()"),
	COMPUTER_STATUS_XPATH("//Computer/Status/text()"), ACTION_SCRIPT_XPATH("//ActionScript/text()"),
	ACTION_LASTMODIFIED_XPATH("(//Name[starts-with(text(), 'Install Latest Automation Plan Engine')]/parent::Action[last()]/@LastModified)"),
	ACTION_STATUS_XPATH("//Name[starts-with(text(), 'Install Latest Automation Plan Engine')]/parent::Action[@LastModified='date']/ID/text()");
	
	public String text;
	
	XMLConsts(String text){
		this.text = text;
	}
	
	 XMLConsts() {
	}

}
