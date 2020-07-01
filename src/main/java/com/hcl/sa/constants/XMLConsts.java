package com.hcl.sa.constants;

public enum XMLConsts {
	

    COMPUTER_TAGNAME("Computer"),INCLUDE_IN_RELEVANCE_ATTR("IncludeInRelevance"),ACTION_NAME_ATTR("ActionName"),SOURCE_SITE_URL_ATTR("SourceSiteURL"),
	SOURCE_ID_ATTR("SourceID"),SOURCE_SITE_URL("http://winpatch-bes-1:52311/cgi-bin/bfgather.exe/{siteName}"),
	BASELINE_COMPONENT_GROUP("BaselineComponentGroup"),BASELINE_COMPONENT("BaselineComponent"),ACTION_NAME_ATTR_VAL("Action1"),INCLUDE_IN_REL_ATTR_VAL("true"),
	SITE_NAME_REG_EX("{siteName}");
	
	public String text;
	
	XMLConsts(String text){
		this.text = text;
	}
	
	 XMLConsts() {
	}

}
