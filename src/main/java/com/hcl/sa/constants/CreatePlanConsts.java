package com.hcl.sa.constants;

public enum CreatePlanConsts {
    SERVER_AUTOMATION("Server Automation"),MULTIPLE_FIXLETS_PLAN("Plan with Multiple Fixlets");

    public String text;

    CreatePlanConsts(String value) {
        text = value;
    }
}