package com.hcl.sa.constants;

public enum CreatePlanConsts {
    SERVER_AUTOMATION("Server Automation"),MULTIPLE_FIXLETS_PLAN("Plan with Multiple Fixlets"),
    MULTIPLE_BASELINE_PLAN("Plan with Baselines"),
    WASLIBERTY_SERVER_URI(System.getenv("wasliberty_server_uri")),
    WASLIBERTY_API_URI("wasLibertyApi"), PLAN_ID("planID"), PLAN_EXECUTION_TEMPLATE("planExecutionTemplate"),
    EXECUTE_PLAN("executePlan"), PLAN_ACTION_ID("planActionID"), FIXLET_DETAILS("fixletDetails");

    public String text;

    CreatePlanConsts(String value) {
        text = value;
    }
}