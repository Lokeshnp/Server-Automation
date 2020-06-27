package com.hcl.sa.objectRepository;

public interface AutomationPlanLocators {
      String create_btn_using_xpath = "//*[@AutomationId='planDetailCreateButton']/Text[@Name='Create']";
      String name_field_using_accessbilityID = "planDetailNameVal";
      String steps_tab_using_name= "Steps";
      String add_step_using_name = "Add Step";
      String add_button_using_xpath = "//*[normalize-space(@Name)='Add']";
      String save_btn_using_name = "Save";
      String ok_btn_using_name = "OK";
      String edit_field_using_xpath = "//*[@LocalizedControlType='edit']";
      String add_step_combobox = "//*[@LocalizedControlType='combo box']";
      String add_step_search_btn_using_xpath = "filterSearchButton";

}
