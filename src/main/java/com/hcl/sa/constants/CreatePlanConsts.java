package com.hcl.sa.constants;

public enum CreatePlanConsts {
    SERVER_AUTOMATION("Server Automation"), MULTIPLE_FIXLETS_PLAN("Plan with Multiple Fixlets"), MULTIPLE_BASELINE_PLAN("Plan with Baselines"),
    MULTIPLE_FIXLETS_AND_TASKS_PLAN("Plan with Baseline having Fixlets and Tasks"), MULTIPLE_TASKS_PLAN("Plan with Multiple Tasks"), MULTIPLE_BASELINE_TASKS_PLAN("Plan with Baselines having Tasks"),
    CUSTOM_SITE_PLAN("Custom Site Plan"), WAS_LIBERTY_SERVER_URI(System.getenv("was_liberty_server_uri")), SA_REST_SERVER_URI(System.getenv("sa_rest_server_uri")),
    WAS_LIBERTY_API_URI("wasLibertyApi"), PLAN_ID("planID"), PLAN_EXECUTION_TEMPLATE("planExecutionTemplate"), PLAN_EXECUTION_MASTER_TEMPLATE("executeMasterSitePlan"),
    EXECUTE_PLAN("executePlan"), PLAN_ACTION_ID("planActionID"), FIXLET_DETAILS("fixletDetails"), EXTERNAL_FIXLET1("BES Clients Have Incorrect Clock Time"), EXTERNAL_FIXLET_ID1("15"),EXTERNAL_FIXLET2("Restart BES Clients"), EXTERNAL_FIXLET_ID2("75"),
    OS_DEPLOY_FIXLET1("Update Server Whitelist for OS Deployment"), OS_DEPLOY_FIXLET_ID1("36"),OS_DEPLOY_FIXLET2("Deploy Microsoft .NET Framework"), OS_DEPLOY_FIXLET_ID2("41"), BASELINE_DETAILS("baselineDetails"), PARALLEL_PLAN("Parallel Plan"), SA_REST_API_URI("saRestApi"), Response("response"),
    PLAN_WITH_OS_DEPLOYMENT_SITE_FIXLETS("Plan with OS Deployment Site Fixlets"), PLAN_WITH_EXTERNAL_SITE_FIXLETS("Plan with external site fixlets"), MASTER_PLAN("Master Plan"), MASTER_SITE_PLAN("Master Site Plan"),
    SECURE_PARAMETER_PLAN("Secure Parameter Plan"),ACTION_BODY("actionBody"), SOCKET_TIME_OUT("http.socket.timeout"), CONNECTION_TIME_OUT("http.connection-manager.timeout"),
    FIRST_NAME(System.getenv("firstName")),LAST_NAME(System.getenv("lastName")),PASSWORD(System.getenv("password")),STATUS_CODE("statusCode");



    public String text;

    CreatePlanConsts(String value) {
        text = value;
    }
}