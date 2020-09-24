package com.hcl.sa.objectRepository;

public interface AutomationPlanLocators {
    String create_btn_using_xpath = "//*[@AutomationId='planDetailCreateButton']/Text[contains(@Name,\"Create\")]";
    String name_field_using_accessbilityID = "planDetailNameVal";
    String steps_tab_using_name = "Steps";
    String add_step_using_name = "Add Step";
    String add_button_using_xpath = "//*[normalize-space(@Name)='Add']";
    String save_btn_using_name = "Save";
    String ok_btn_using_name = "OK";
    String edit_field_using_xpath = "//*[@LocalizedControlType='edit']";
    String add_step_combobox = "//*[@LocalizedControlType='combo box']";
    String add_step_search_btn_using_xpath = "filterSearchButton";
    String create_btn_access_id = "planDetailCreateButton";
    String search_plan_text_box_access_id = "dijit_form_TextBox_0";
    String add_step_combobox_tagname = "ComboBox";
    String name_filter_text_box_access_id = "filterWidgetTextbox_2";
    String id_filter_text_box_tagName = "Edit";
    String parallel_radio_btn_using_name = "Parallel";
    String fixlet_task_using_name = "Fixlet/Task";
    String summary_using_xpath = "//Text[@Name='Summary']/parent::*/following-sibling::*";
    //Sequence order is not changing so 101 is passed in Name locator
    String select_check_box_using_xpath = "//*[@Name='101']/preceding-sibling::*/CheckBox";
}