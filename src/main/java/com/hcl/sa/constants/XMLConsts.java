package com.hcl.sa.constants;

public enum XMLConsts {

	COMPUTER_TAGNAME("Computer"),INCLUDE_IN_RELEVANCE_ATTR("IncludeInRelevance"),ACTION_NAME_ATTR("ActionName"),SOURCE_SITE_URL_ATTR("SourceSiteURL"),
	SOURCE_ID_ATTR("SourceID"),SOURCE_SITE_URL("http://"+System.getenv("bigfix_server_name")+":52311/cgi-bin/bfgather.exe/{siteName}"),
	BASELINE_COMPONENT_GROUP("BaselineComponentGroup"),BASELINE_COMPONENT("BaselineComponent"),ACTION_NAME_ATTR_VAL("Action1"),INCLUDE_IN_REL_ATTR_VAL("true"),
	SITE_NAME_REG_EX("{siteName}"), STEP("step"), TARGET_SET("target-set"), COMPUTER("computer"), NAME("name"),

	
	VERSION_ATTR("version"),OFFERING("offering"),BASE_OFFERING("baseOffering"),

	COMP_ATTR_XPATH("//Computer/@Resource"),
	IP_ADDRESS_XPATH("//Computer/Property[@Name ='IP Address']/text()"), ACTION_ID_XPATH("//Action/ID/text()"),
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
