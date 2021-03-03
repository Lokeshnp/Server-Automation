package com.hcl.sa.utils.parser;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hcl.sa.constants.ConsoleConsts;
import com.hcl.sa.constants.CreatePlanConsts;
import com.hcl.sa.constants.VsphereConsts;
import com.hcl.sa.utils.bigfix.CommonFunctions;
import com.hcl.sa.utils.bigfix.SuperClass;

public class JsonParser {
    JsonElement consoleObj = SuperClass.getInstance().getJsonParser(CommonFunctions.getPath(ConsoleConsts.BIGFIX_API_JSON_PATH.text)).getAsJsonObject().get(ConsoleConsts.BIGFIX_API.text);
    JsonElement vmObj = SuperClass.getInstance().getJsonParser(CommonFunctions.getPath(VsphereConsts.VM_DETAILS_JSON_PATH.text)).getAsJsonObject().get(VsphereConsts.VIRTUAL_MACHINE_JSON_OBJ.text);
    public JsonObject getConsoleApiObject() {
        return consoleObj.getAsJsonObject().get(ConsoleConsts.CONSOLE_API.text).getAsJsonObject();
    }

    public JsonObject getBaselineFixletObj(){
        return  vmObj.getAsJsonObject().get(VsphereConsts.BASELINE_FIXLETS.text).getAsJsonObject();
    }

    public String getUriToDeleteAction(JsonObject jsonObject) {
        return jsonObject.get(ConsoleConsts.DELETE_ACTION.text).getAsString();
    }

    public String getUriToInitiateAction(JsonObject jsonObject) {
        return jsonObject.get(ConsoleConsts.INITIATE_ACTION.text).getAsString();
    }

    public String getUriToFetchRelevantComputers(JsonObject jsonObject) {
        return jsonObject.get(ConsoleConsts.RELEVANT_COMPUTERS.text).getAsString();
    }

    public String getUriToFetchCompProperties(JsonObject jsonObject) {
        return jsonObject.get(ConsoleConsts.COMP_PROPERTIES.text).getAsString();
    }

    public String getUriToFetchActionStatus(JsonObject jsonObject) {
        return jsonObject.get(ConsoleConsts.ACTION_STATUS.text).getAsString();
    }

    public String getUriToStopAction(JsonObject jsonObject) {
        return jsonObject.get(ConsoleConsts.STOP_ACTION.text).getAsString();
    }

    public JsonObject getSiteTypeObject() {
        return consoleObj.getAsJsonObject().get(ConsoleConsts.SITE_TYPE.text).getAsJsonObject();
    }

    public JsonObject getSiteNameObject() {
        return consoleObj.getAsJsonObject().get(ConsoleConsts.SITE_NAME.text).getAsJsonObject();
    }

    public String getUriToImportFixlet(JsonObject jsonObject) {
        return jsonObject.get(ConsoleConsts.IMPORT_FIXLET.text).getAsString();
    }

    public String getUriToCreateBaseline(JsonObject jsonObject) {
        return jsonObject.get(ConsoleConsts.CREATE_BASELINE.text).getAsString();
    }

    public String getUriToCreateTask(JsonObject jsonObject) {
        return jsonObject.get(ConsoleConsts.CREATE_TASKS.text).getAsString();
    }

    public JsonObject getSaRestPlanConsoleApiObject() {
        return consoleObj.getAsJsonObject().get(CreatePlanConsts.SA_REST_API_URI.text).getAsJsonObject();
    }

    public String getUriToFetchPlanXml(JsonObject jsonObject) {
        return jsonObject.get(CreatePlanConsts.PLAN_EXECUTION_TEMPLATE.text).getAsString();
    }

    public String getUriToFetchSaRestMasterPlanXml(JsonObject jsonObject) {
        return jsonObject.get(CreatePlanConsts.PLAN_EXECUTION_MASTER_TEMPLATE.text).getAsString();
    }

    public String getUriToFetchSaRestOperatorPlanXml(JsonObject jsonObject) {
        return jsonObject.get(CreatePlanConsts.PLAN_EXECUTION_TEMPLATE.text).getAsString();
    }


    public String getUriToFetchFixletList(JsonObject jsonObject) {
        return jsonObject.get(ConsoleConsts.FIXLET_LIST.text).getAsString();
    }

    public String getUriToDeletePlan(JsonObject jsonObject) {
        return jsonObject.get(ConsoleConsts.DELETE_PLAN.text).getAsString();
    }

    public String getUriToFetchPlanAction(JsonObject jsonObject) {
        return jsonObject.get(ConsoleConsts.PLAN_ACTION.text).getAsString();
    }

    public String getUriToCreateOperator(JsonObject jsonObject)
    {
        return jsonObject.get(ConsoleConsts.CREATE_OPERATOR.text).getAsString();
    }

    public String getUriToAssignSite(JsonObject jsonObject)
    {
        return jsonObject.get(ConsoleConsts.ASSIGN_SITE.text).getAsString();
    }

    public String getUriToDeleteOperator(JsonObject jsonObject) {
        return jsonObject.get(ConsoleConsts.DELETE_OPERATOR.text).getAsString();
    }
}

