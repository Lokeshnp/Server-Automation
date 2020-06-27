package com.hcl.sa.constants;

public enum ConsoleConsts {
    BIGFIX_SERVER_URI(System.getenv("bigFix_server_uri")), BIGFIX_API("bigfixApi"), BIGFIX_API_JSON_PATH(System.getenv("bigfix_api_json_path")),
    COMPUTER("Computer"), SUCCESSFUL("successful"), RESTART("restart"), FAILED("failed"), INSTALL_PE_FIXLET_ID(System.getenv("install_plan_eng_fixlet_id")),
    UNINSTALL_PE_FIXLET_ID(System.getenv("uninstall_plan_eng_fixlet_id")),

    CONSOLE_API("consoleApi"), RELEVANT_COMPUTERS("relevantComputers"),
    FIXLET_ID("fixletID"), COMP_PROPERTIES("compProperties"), COMPUTER_ID("computerID"), INITIATE_ACTION("initiateAction"),
    ACTION_STATUS("actionStatus"), STOP_ACTION("stopAction"), ACTION_ID("actionID"), COMPUTER_ID_REGEX("computer/\\d+"), DELETE_ACTION("deleteAction"),
    SITE_TYPE("siteType"), SITE_NAME("siteName"), EXTERNAL("external"), SERVER_AUTOMATION("serverAutomation"), CONSOLE_EXE_PATH(System.getenv("console_exe_path"));

    public String text;

    ConsoleConsts(String value) {
        text = value;
    }

}
