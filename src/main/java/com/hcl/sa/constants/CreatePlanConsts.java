package com.hcl.sa.constants;

public enum CreatePlanConsts {
    SERVER_AUTOMATION("Server Automation"),MULTIPLE_FIXLETS_PLAN("Plan with Multiple Fixlets"), MULTIPLE_BASELINE_PLAN("Plan with Baselines"),
    MULTIPLE_FIXLETSANDTASKS_PLAN("Plan with Baseline having Fixlets and Tasks"),MULTIPLE_TASKS_PLAN("Plan with Multiple Tasks"),MULTIPLE_BASELINE_TASKS_PLAN("Plan with Baselines having Tasks"),
    WAS_LIBERTY_SERVER_URI(System.getenv("was_liberty_server_uri")),
    WAS_LIBERTY_API_URI("wasLibertyApi"), PLAN_ID("planID"), PLAN_EXECUTION_TEMPLATE("planExecutionTemplate"),
    EXECUTE_PLAN("executePlan"), PLAN_ACTION_ID("planActionID"), FIXLET_DETAILS("fixletDetails"), BASELINE_DETAILS("baselineDetails");

    public String text;

    CreatePlanConsts(String value) {
        text = value;
    }
}