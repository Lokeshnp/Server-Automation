package com.hcl.sa.constants;

public enum ConsoleConsts {
    BIGFIX_SERVER_URI(System.getenv("bigFix_server_uri")),CONSOLE("bigfixApi"), CUSTOM_SITE_NAME("customSiteName"), PARAM_NAME_CUSTOM_SITE("customSiteName"), CONSOLE_JSON_PATH(System.getenv("console_json_path")), EXPORTED_FIXLET_FOLDER_PATH(System.getenv("exported_fixlet_folder_path")),
    SITE_TYPE("siteType"), COMPUTER("Computer"), SUCCESSFUL("successful"),RESTART("restart"),FAILED("failed"), INSTALL_PE_FIXLET_ID(System.getenv("install_plan_eng_fixlet_id")), UNINSTALL_PE_FIXLET_ID(System.getenv("uninstall_plan_eng_fixlet_id")),

    API_URI("consoleApi"), IMPORT_FIXLET_TO_CUSTOM_SITE("importFixletToCustomSite"), SITES_LIST("sitesList"),RELEVANT_VM_FOR_FIXLET("relevantVMForFixlet"),
    FIXLET_ID("fixletID"),COMPUTER_ID_INFO("computerIDInfo"),COMPUTER_ID("computerID"), INITIATE_ACTION("initiateAction"),
    ACTION_STATUS("actionStatus"),STOP_ACTION("stopAction"),ACTION_ID("actionID"), COMPUTER_ID_REGEX("computer/\\d+"), IS_CUSTOM_SITE_PRESENT;

    public String text;


    ConsoleConsts(String value) {
        text = value;
    }

    ConsoleConsts() {
    }
}
