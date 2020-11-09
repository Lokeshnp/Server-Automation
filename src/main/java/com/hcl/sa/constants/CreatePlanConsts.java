package com.hcl.sa.constants;

public enum CreatePlanConsts {
    SERVER_AUTOMATION("Server Automation"), MULTIPLE_FIXLETS_PLAN("Plan with Multiple Fixlets"), MULTIPLE_BASELINE_PLAN("Plan with Baselines"),
    MULTIPLE_FIXLETS_AND_TASKS_PLAN("Plan with Baseline having Fixlets and Tasks"), MULTIPLE_TASKS_PLAN("Plan with Multiple Tasks"), MULTIPLE_BASELINE_TASKS_PLAN("Plan with Baselines having Tasks"),
    CUSTOM_SITE_PLAN("Custom Site Plan"), SA_REST_SERVER_URI(System.getenv("sa_rest_server_uri")), FIX_UNINSTALL_PLAN_ENGINE("Uninstall Automation Plan Engine"), FIX_UNINSTALL_PLAN_ENGINE_ID("3"),
    PLAN_ID("planID"), PLAN_EXECUTION_TEMPLATE("planExecutionTemplate"), PLAN_EXECUTION_MASTER_TEMPLATE("executeMasterSitePlan"), AUTOMATION_PLAN_ID("560"),
    PLAN_ACTION_ID("planActionID"), FIXLET_DETAILS("fixletDetails"), EX_FIX_INCORRECT_CLOCK_TIME("BES Clients Have Incorrect Clock Time"), EX_FIX_INCORRECT_CLOCK_TIME_ID("15"), EX_FIX_Restart_BES_Clients("Restart BES Clients"), EX_FIX_Restart_BES_Clients_ID("75"),
    OS_DEPLOY_FIX_UPDATE_SERV_WHITELIST("Update Server Whitelist for OS Deployment"), OS_DEPLOY_FIX_UPDATE_SERV_WHITELIST_ID("36"), OS_DEPLOY_FIX_MS_FRAMEWORK("Deploy Microsoft .NET Framework"), OS_DEPLOY_FIX_MS_FRAMEWORK_ID("41"), BASELINE_DETAILS("baselineDetails"), PARALLEL_PLAN("Parallel Plan"), SA_REST_API_URI("saRestApi"),
    PLAN_WITH_OS_DEPLOYMENT_SITE_FIXLETS("Plan with OS Deployment Site Fixlets"), PLAN_WITH_EXTERNAL_SITE_FIXLETS("Plan with external site fixlets"), MASTER_PLAN("Master Plan"), MASTER_SITE_PLAN("Master Site Plan"),
    SECURE_PARAMETER_PLAN("Secure Parameter Plan"), ACTION_BODY("actionBody"), SOCKET_TIME_OUT("http.socket.timeout"), CONNECTION_TIME_OUT("http.connection-manager.timeout"),
    FIRST_NAME(System.getenv("firstName")), LAST_NAME(System.getenv("lastName")), PASSWORD(System.getenv("password")), STATUS_CODE("statusCode"), PARAMETER("parameter"), NAME("name"), FIRSTNAME("firstName"), LASTNAME("lastName");

    public String text;

    CreatePlanConsts(String value) {
        text = value;
    }
}