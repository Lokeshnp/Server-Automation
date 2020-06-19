package com.hcl.sa.constants;

public enum XMLConsts {
	
	VERSION_ATTR("version"),OFFERING("offering"),BASE_OFFERING("baseOffering"),

	COMPUTER_ATTRIBUTE_XPATH("//Computer/@Resource"),
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
