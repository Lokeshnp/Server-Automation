package com.hcl.sa.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hcl.sa.constants.ConsoleConsts;

public class JsonParser {
    JsonElement consoleObj = SuperClass.getInstance().getJsonParser(ConsoleConsts.CONSOLE_API_JSON_PATH.text).getAsJsonObject().get(ConsoleConsts.CONSOLE.text);

    public String getCustomSiteName() {
        return consoleObj.getAsJsonObject().get(ConsoleConsts.CUSTOM_SITE_NAME.text).getAsString();
    }

    public JsonObject getConsoleApiObject() {
        return consoleObj.getAsJsonObject().get(ConsoleConsts.API_URI.text).getAsJsonObject();
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

    public String getSiteName(JsonObject jsonObject) {
        return jsonObject.get(ConsoleConsts.SITE_NAME.text).getAsString();
    }
}
