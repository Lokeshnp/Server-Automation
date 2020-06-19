package com.hcl.sa.constants;

public enum ConsoleConsts {
    BIGFIX_SERVER_URI(System.getenv("bigFix_server_uri")),CONSOLE("bigfixApi"), CUSTOM_SITE_NAME("customSiteName"), PARAM_NAME_CUSTOM_SITE("customSiteName"), CONSOLE_API_JSON_PATH(System.getenv("console_api_json_path")),
    COMPUTER("Computer"), SUCCESSFUL("successful"),RESTART("restart"),FAILED("failed"), INSTALL_PE_FIXLET_ID(System.getenv("install_plan_eng_fixlet_id")), UNINSTALL_PE_FIXLET_ID(System.getenv("uninstall_plan_eng_fixlet_id")),
    SITE_NAME(System.getenv("site_name")), SITE_TYPE(System.getenv("site_type")),

    API_URI("consoleApi"), RELEVANT_COMPUTERS("relevantComputers"),
    FIXLET_ID("fixletID"),COMP_PROPERTIES("compProperties"),COMPUTER_ID("computerID"), INITIATE_ACTION("initiateAction"),
    ACTION_STATUS("actionStatus"),STOP_ACTION("stopAction"),ACTION_ID("actionID"), COMPUTER_ID_REGEX("computer/\\d+"), DELETE_ACTION("deleteAction");

    public String text;

    ConsoleConsts(String value) {
        text = value;
    }

    ConsoleConsts() {
    }
}
