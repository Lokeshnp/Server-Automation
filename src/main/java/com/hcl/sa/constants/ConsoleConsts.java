package com.hcl.sa.constants;

public enum ConsoleConsts {
    BIGFIX_SERVER_URI(System.getenv("bigFix_server_uri")), BIGFIX_API("bigfixApi"), BIGFIX_API_JSON_PATH(System.getenv("bigfix_api_json_path")),
    COMPUTER("Computer"), SUCCESSFUL("successful"), RESTART("restart"), FAILED("failed"), INSTALL_PE_FIXLET_ID(System.getenv("install_plan_eng_fixlet_id")),
    UNINSTALL_PE_FIXLET_ID(System.getenv("uninstall_plan_eng_fixlet_id")),

    CONSOLE_API("consoleApi"), RELEVANT_COMPUTERS("relevantComputers"), OPERATOR("operator"), OPERATOR_USERNAME(System.getenv("operator_username")),
    FIXLET_ID("fixletID"), COMP_PROPERTIES("compProperties"), COMPUTER_ID("computerID"), INITIATE_ACTION("initiateAction"),
    ACTION_STATUS("actionStatus"), STOP_ACTION("stopAction"), ACTION_ID("actionID"), COMPUTER_ID_REGEX("computer/\\d+"), DELETE_ACTION("deleteAction"),
    SITE_TYPE("siteType"), SITE_NAME("siteName"), EXTERNAL("external"), SERVER_AUTOMATION("serverAutomation"), CONSOLE_EXE_PATH(System.getenv("console_exe_path")),
    IMPORT_FIXLET("importFixlet"), BASELINE_FIXLETS_FOLDER(System.getenv("baseline_fixlets_folder_path")), BASELINE_TASKS_FOLDER(System.getenv("baseline_tasks_folder_path")),
    CUSTOM("custom"), CREATE_BASELINE("createBaselines"), BASELINE_FIXLETS_AND_TASKS_FOLDER(System.getenv("baseline_fixlets_and_tasks_folder_path")),
    CREATE_BASELINE_PAYLOAD_PATH(System.getenv("create_baseline_payload_path")), SA("SA"), HTTP_SC_OK("200"), HTTP_SC_UNAUTH("401"), OPERATOR_NAME("operatorName"),
    FIXLET_LIST("fixletList"), ACTION("Action"), DELETE_PLAN("deletePlan"), CREATE_TASKS("createTasks"), SRC_SITENAME("CustomSite_SA"),
    FIXLETS_FOLDER(System.getenv("fixlets_folder_path")), PLAN_BASELINE_FIXLETS_TASKS_FOLDER(System.getenv("plan_baseline_fixlets_tasks_folder")), TASKS_FOLDER(System.getenv("tasks_folder_path")), FIXLETS_TASKS_FOLDER(System.getenv("fixlets_and_tasks_folder_path")),
    TAKE_ACTION_PAYLOAD_PATH(System.getenv("take_action_payload_path")), MASTER_ACTION_SITE("Master Action Site"), CREATE_OPERATOR("createOperator"), SET_SITE_PERMISSION("sitePermission"),
    FIXLET("Fixlet"), ROOT_SERVER(System.getenv("bigfix_server_name")), TASK("Task"), PLAN_ACTION("executePlanAction"), DELETE_OPERATOR("deleteOperator"),
    SECURE_PARAMETER_FIXLET_FOLDER_PATH(System.getenv("secure_parameter_fixlet_folder_path")), CREATE_OPERATOR_PAYLOAD_PATH(System.getenv("create_operator_payload_path")),
    ASSIGN_SA_SITE_PAYLOAD_PATH(System.getenv("sa_site_permission_payload_path")), ASSIGN_CUSTOM_PAYLOAD_PATH(System.getenv("custom_site_permission_payload_path")), OPERATOR_VALUE(System.getenv("operator_username"));

    public String text;

    ConsoleConsts(String value) {
        text = value;
    }

}
