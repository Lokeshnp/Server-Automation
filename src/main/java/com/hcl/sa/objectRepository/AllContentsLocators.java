package com.hcl.sa.objectRepository;

public interface AllContentsLocators {
   String all_content_class_name = "ModeledTreeView";
   String ext_sites_col_names_xpath = "//Header[@Name=\"Header Control\"][@AutomationId=\"Header\"]/HeaderItem";
   String external_sites_xpath = "//ListItem[starts-with(@AutomationId,\"ListViewItem-\")]/Text[starts-with(@AutomationId,\"ListViewSubItem-\")][colIndex]";
}