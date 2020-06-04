package com.hcl.sa.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hcl.sa.constants.ConsoleConsts;

public class JsonParser {
    JsonElement consoleObj = SuperClass.getInstance().getJsonParser(ConsoleConsts.CONSOLE_JSON_PATH.text).getAsJsonObject().get(ConsoleConsts.CONSOLE.text);

    public String getCustomSiteName() {
        return consoleObj.getAsJsonObject().get(ConsoleConsts.CUSTOM_SITE_NAME.text).getAsString();
    }

    public JsonObject getApiUriObject() {
        return consoleObj.getAsJsonObject().get(ConsoleConsts.API_URI.text).getAsJsonObject();
    }

    public String getUriToImportFixlet(JsonObject jsonObject) {
        return jsonObject.get(ConsoleConsts.IMPORT_FIXLET_TO_CUSTOM_SITE.text).getAsString();
    }

    public String getUriToInitiateAction(JsonObject jsonObject) {
        return jsonObject.get(ConsoleConsts.INITIATE_ACTION.text).getAsString();
    }

    public String getUriToFetchRelevantVMs(JsonObject jsonObject) {
        return jsonObject.get(ConsoleConsts.RELEVANT_VM_FOR_FIXLET.text).getAsString();
    }

    public String getUriToFetchCompIdInfo(JsonObject jsonObject) {
        return jsonObject.get(ConsoleConsts.COMPUTER_ID_INFO.text).getAsString();
    }

    public String getUriToFetchActionStatus(JsonObject jsonObject) {
        return jsonObject.get(ConsoleConsts.ACTION_STATUS.text).getAsString();
    }

    public String getUriToStopAction(JsonObject jsonObject) {
        return jsonObject.get(ConsoleConsts.STOP_ACTION.text).getAsString();
    }
}
