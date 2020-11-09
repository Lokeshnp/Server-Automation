package com.hcl.sa.steps;

import com.google.gson.JsonObject;
import com.hcl.sa.constants.ConsoleConsts;
import com.hcl.sa.constants.CreatePlanConsts;
import com.hcl.sa.constants.XMLConsts;
import com.hcl.sa.utils.api.ApiRequests;
import com.hcl.sa.utils.bigfix.CommonFunctions;
import com.hcl.sa.utils.bigfix.ConsoleActions;
import com.hcl.sa.utils.bigfix.SuperClass;
import com.hcl.sa.utils.parser.JsonParser;
import com.hcl.sa.windows.AutomationPlans;
import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.Table;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class Sa_Rest_Steps {
    protected ConsoleActions consoleActions = new ConsoleActions();
    private Logger logger = LogManager.getLogger(Create_Plan_Steps.class);
    protected AutomationPlans automationPlans = new AutomationPlans();
    ApiRequests apiRequests = new ApiRequests();
    CommonFunctions commonFunctions = new CommonFunctions();
    JsonParser jsonParser = new JsonParser();
    public JsonObject saRestConsoleApiObject = jsonParser.getSaRestPlanConsoleApiObject();
    Response response;

    @Step("User creates a master site plan having external site fixlets/tasks <table>")
    public void createPlanWithExternalFixlets(Table table) throws IOException, SAXException {
        HashMap<String, String> fixletDetails = new HashMap<>();
        fixletDetails.put(CreatePlanConsts.EX_FIX_INCORRECT_CLOCK_TIME.text, CreatePlanConsts.EX_FIX_INCORRECT_CLOCK_TIME_ID.text);
        fixletDetails.put(CreatePlanConsts.EX_FIX_Restart_BES_Clients.text, CreatePlanConsts.EX_FIX_Restart_BES_Clients_ID.text);
        String planID = automationPlans.createPlan(CreatePlanConsts.PLAN_WITH_EXTERNAL_SITE_FIXLETS.text, fixletDetails);
        logger.info("Created plan id : \n" + planID);
    }

    @Step("And checks whether sa rest GET api works for external site")
    public void verifyPlanWithExternalSiteFixlet() throws IOException, SAXException {
        String planID = SuperClass.specStore.get(CreatePlanConsts.PLAN_ID).toString();
        response = automationPlans.getPlanXml(planID);
        response.then().statusCode(Integer.parseInt(ConsoleConsts.HTTP_SC_OK.text));
        logger.info("Generated response is : \n" + response);
    }

    @Step("And gets response as http 200 status code for external site")
    public void statusCodeValidation() throws IOException, SAXException {
        Integer statusCode = response.statusCode();
        Assert.assertEquals(Integer.parseInt(ConsoleConsts.HTTP_SC_OK.text), statusCode.intValue());
        logger.info("Status code is verified successfully");
    }

    @Step("And response body as plan defintion xml template of external site")
    public void responseXmlValidation() {
        String responseString = response.asString();
        Assert.assertTrue(responseString.contains(XMLConsts.EXECUTE_PLAN.text));
        Assert.assertTrue(responseString.contains(XMLConsts.SA_REST.text));
        logger.info("Response string is " + responseString);
    }

    @Step("When User sends SA-REST Get Api Request for the existing external site automation plan")
    public void saRestGetApiRequestForExistExtrnlPlan() throws IOException, SAXException {
        List<String> plans = consoleActions.getPlansList(commonFunctions.commonParams(ConsoleConsts.EXTERNAL.text, ConsoleConsts.SERVER_AUTOMATION.text));
        for (int i = 0; i < plans.size(); i++) {
            RequestSpecification requestSpecification = apiRequests.setSaRestURIAndBasicAuthentication().contentType(ContentType.JSON).and().accept(ContentType.ANY).and().
                    and().pathParams(commonFunctions.saRestCommonParams(ConsoleConsts.EXTERNAL.text, ConsoleConsts.SERVER_AUTOMATION.text, plans.get(i)));
            Response response = automationPlans.getPlanXml(requestSpecification);
            response.then().statusCode(Integer.parseInt(ConsoleConsts.HTTP_SC_OK.text));
            logger.info("Generated response is : \n" + response);
        }
    }

    @Step("And response body as plan defintion xml template of existing external site")
    public void responseXmlDefValidation() throws IOException, SAXException {
        List<String> plans = consoleActions.getPlansList(commonFunctions.commonParams(ConsoleConsts.EXTERNAL.text, ConsoleConsts.SERVER_AUTOMATION.text));
        for (int i = 0; i < plans.size(); i++) {
            RequestSpecification requestSpecification = apiRequests.setSaRestURIAndBasicAuthentication().contentType(ContentType.JSON).and().accept(ContentType.ANY).and().
                    and().pathParams(commonFunctions.saRestCommonParams(ConsoleConsts.EXTERNAL.text, ConsoleConsts.SERVER_AUTOMATION.text, plans.get(i)));
            Response response = automationPlans.getPlanXml(requestSpecification);
            Integer statusCode = response.statusCode();
            Assert.assertEquals(Integer.parseInt(ConsoleConsts.HTTP_SC_OK.text), statusCode.intValue());
            String responseString = response.asString();
            Assert.assertTrue(responseString.contains(XMLConsts.EXECUTE_PLAN.text));
            Assert.assertTrue(responseString.contains(XMLConsts.SA_REST.text));
            logger.info("Response string is " + responseString);
        }
    }

    @Step("When User sends SA-REST Plan Action Api Request without authentication parameters")
    public void verifyPlanActionWithoutAuthenatication() throws IOException, SAXException {
        response = apiRequests.invalidSaAuthentication().contentType(ContentType.JSON).and().accept(ContentType.ANY).and().
                and().pathParams(CreatePlanConsts.PLAN_ACTION_ID.text, CreatePlanConsts.AUTOMATION_PLAN_ID).when().get(jsonParser.getUriToFetchPlanAction(saRestConsoleApiObject));
        logger.info("Generated response is : \n" + response);
    }

    @Step("And gets response as http 401 status code for without authentication")
    public void verifyPlanActionWithoutAuth() throws IOException, SAXException {
        Integer statusCode = response.statusCode();
        Assert.assertEquals(Integer.parseInt(ConsoleConsts.HTTP_SC_UNAUTH.text), statusCode.intValue());
        logger.info("status code is " + statusCode);
    }

    @Step("And user should validate the error message in response body of without authentication")
    public void responseBodyValidation() {
        String responseString = response.asString();
        Assert.assertTrue(responseString.contains("HTTP 401: Unauthorized"));
        logger.info("Response string is " + responseString);
    }

    @Step("User creates a master site plan")
    public void createPlan() throws IOException, SAXException {
        RequestSpecification requestSpecification = apiRequests.setBaseURIAndBasicAuthentication().
                contentType(ContentType.JSON).and().accept(ContentType.ANY).and().
                pathParams(commonFunctions.commonParams(ConsoleConsts.CUSTOM.text, ConsoleConsts.SA.text));
        HashMap<String, String> fixletDetails = consoleActions.importFixlet(CommonFunctions.getPath(ConsoleConsts.FIXLETS_FOLDER.text), requestSpecification);
        String planID = automationPlans.createPlan(CreatePlanConsts.MASTER_PLAN.text, fixletDetails);
        logger.info("Created plan id : \n" + planID);
    }

    @Step("When User sends SA-REST execute Plan Api Request without authentication parameters")
    public void verifyExecutePlanWithoutAuthenatication() throws Exception {
        String planID = SuperClass.specStore.get(CreatePlanConsts.PLAN_ID).toString();
        HashMap<String, String> fixletDetails = (HashMap<String, String>) SuperClass.specStore.get(CreatePlanConsts.FIXLET_DETAILS);
        HashMap<String, String> params = new HashMap<>();
        Response responseXML = automationPlans.getPlanXml(planID);
        logger.debug("Initiated the execute plan" + responseXML.toString());
        String actionBody = automationPlans.modifyPlanDefXmlTemplate(fixletDetails, responseXML);
        String uri = jsonParser.getUriToFetchSaRestMasterPlanXml(saRestConsoleApiObject);
        params.put(CreatePlanConsts.PLAN_ID.text, planID);
        response = apiRequests.invalidSaAuthentication().contentType(ContentType.JSON).and().accept(ContentType.ANY).and().
                and().pathParams(CreatePlanConsts.PLAN_ID.text, params.get(CreatePlanConsts.PLAN_ID.text)).and().body(actionBody).when().post(uri);
        logger.info("Generated response is : \n" + response);

    }

    @Step("Then in response user should get http 401 status code for execute plan")
    public void verifyExeutePlanActionWithoutAuthentication() throws IOException, SAXException {
        Integer statusCode = response.statusCode();
        Assert.assertEquals(Integer.parseInt(ConsoleConsts.HTTP_SC_UNAUTH.text), statusCode.intValue());
        logger.info("status code is " + statusCode);
    }

    @Step("And user should validate the error message in response body of execute plan")
    public void responseBodyValidations() {
        String responseString = response.asString();
        Assert.assertTrue(responseString.contains("HTTP 401: Unauthorized"));
        logger.info("Response string is " + responseString);
    }

    @Step("When User sends SA-REST Get Api Request without authentication parameters")
    public void verifyPlanWithoutAuthenatication() throws IOException, SAXException {
        response = apiRequests.invalidSaAuthentication().contentType(ContentType.JSON).and().accept(ContentType.ANY).and().
                and().pathParams(CreatePlanConsts.PLAN_ID.text, CreatePlanConsts.AUTOMATION_PLAN_ID).when().get(jsonParser.getUriToFetchSaRestMasterPlanXml(saRestConsoleApiObject));
        logger.info("Generated response is : \n" + response);
    }

    @Step("Then in response user should get http 401 status code")
    public void verifyGetPlanActionWithoutAuthentication() throws IOException, SAXException {
        Integer statusCode = response.statusCode();
        Assert.assertEquals(Integer.parseInt(ConsoleConsts.HTTP_SC_UNAUTH.text), statusCode.intValue());
        logger.info("status code is " + statusCode);
    }

    @Step("And user should validate the error message in response body")
    public void responseValidation() {
        String responseString = response.asString();
        Assert.assertTrue(responseString.contains("HTTP 401: Unauthorized"));
        logger.info("Response string is " + responseString);

    }

    @Step("When User sends SA-REST Get Api Request with invalid authentication parameters")
    public void verifyWithInvalidAuth() throws IOException, SAXException {
        response = apiRequests.setSaRestURIAndInvalidAuthentication().contentType(ContentType.JSON).and().accept(ContentType.ANY).and().and().
                pathParams(CreatePlanConsts.PLAN_ID.text, CreatePlanConsts.AUTOMATION_PLAN_ID).when().get(jsonParser.getUriToFetchSaRestMasterPlanXml(saRestConsoleApiObject));
        logger.info("Generated response is : \n" + response);
    }

    @Step("Then in response user should get http 401 status code for invalid authentication")
    public void verifyWithInvalidAuthentication() throws IOException, SAXException {
        Integer statusCode = response.statusCode();
        Assert.assertEquals(Integer.parseInt(ConsoleConsts.HTTP_SC_UNAUTH.text), statusCode.intValue());
        logger.info("status code is " + statusCode);
    }

    @Step("And user should validate the error message in response body of invalid authentication")
    public void invalidResponseBodyValidation() {
        String responseString = response.asString();
        Assert.assertTrue(responseString.contains("HTTP 401: Unauthorized"));
        logger.info("Response string is " + responseString);

    }

    @Step("Create automation plan with multiple fixlet on following OS <table>")
    public void createMasterSitePlan(Table table) throws IOException, SAXException {

        RequestSpecification requestSpecification = apiRequests.setBaseURIAndBasicAuthentication().
                contentType(ContentType.JSON).and().accept(ContentType.ANY).and().
                pathParams(commonFunctions.commonParams(ConsoleConsts.CUSTOM.text, ConsoleConsts.SA.text));
        HashMap<String, String> fixletDetails = consoleActions.importFixlet(CommonFunctions.getPath(ConsoleConsts.FIXLETS_FOLDER.text), requestSpecification);
        String planID = automationPlans.createPlan(CreatePlanConsts.MASTER_SITE_PLAN.text, fixletDetails);
        logger.info("Created plan id : \n" + planID);
    }

    @Step("Then execute automation plan with multiple fixlet on following OS <table>")
    public void executeMasterSitePlan(Table table) throws Exception {
        String planID = SuperClass.specStore.get(CreatePlanConsts.PLAN_ID).toString();
        HashMap<String, String> fixletDetails = (HashMap<String, String>) SuperClass.specStore.get(CreatePlanConsts.FIXLET_DETAILS);
        automationPlans.executePlan(planID, fixletDetails);
        logger.info("Executed plan id : \n" + planID);
    }

    @Step("And checks whether SA-REST Plan Action api works for existing plan")
    public void verifyPlanActionID() throws IOException, SAXException {
        String planActionID = SuperClass.specStore.get(CreatePlanConsts.PLAN_ACTION_ID).toString();
        response = consoleActions.getStepActionStatus(planActionID);
        response.then().statusCode(Integer.parseInt(ConsoleConsts.HTTP_SC_OK.text));
        logger.info("Generated response is : \n" + response);
    }

    @Step("Then in response user should get http 200 status code for existing plan")
    public void responseStatus() throws IOException, SAXException {
        Integer statusCode = response.statusCode();
        Assert.assertEquals(Integer.parseInt(ConsoleConsts.HTTP_SC_OK.text), statusCode.intValue());
        logger.info("status code is " + statusCode);
    }

    @Step("And response body as plan defintion xml template of existing plan")
    public void responsePlanActionXMLValidation() {
        String responseString = response.asString();
        Assert.assertTrue(responseString.contains(XMLConsts.SA_REST.text));
        Assert.assertTrue(responseString.contains(XMLConsts.STATUS_SET.text));
        logger.info("Response string is " + responseString);
    }

    @Step("When User sends SA-REST Plan Action Api Request with invalid authentication parameter")
    public void verifyWithInvalidActionAuthentication() throws IOException, SAXException {
        response = apiRequests.setSaRestURIAndInvalidAuthentication().contentType(ContentType.JSON).and().accept(ContentType.ANY).and().
                and().pathParams(CreatePlanConsts.PLAN_ACTION_ID.text, CreatePlanConsts.AUTOMATION_PLAN_ID).when().get(jsonParser.getUriToFetchPlanAction(saRestConsoleApiObject));
        logger.info("Generated response is : \n" + response);
    }

    @Step("Then in response user should get http 401 status code for invalid authentication parameter")
    public void verifyResponseStatus() throws IOException, SAXException {
        Integer statusCode = response.statusCode();
        Assert.assertEquals(Integer.parseInt(ConsoleConsts.HTTP_SC_UNAUTH.text), statusCode.intValue());
        logger.info("status code is " + statusCode);
    }

    @Step("And user should validate the error message in response body of invalid authentication parameter")
    public void actionResponseBodyValidation() {
        String responseString = response.asString();
        Assert.assertTrue(responseString.contains("HTTP 401: Unauthorized"));
        logger.info("Response string is " + responseString);
    }

    @Step("User creates a automation plan having os deployment and bare metal imagning fixlets/tasks <table>")
    public void createPlanWithOsDeploymentFixlets(Table table) throws IOException, SAXException, TransformerException, ParserConfigurationException {
        RequestSpecification requestSpecification = apiRequests.setBaseURIAndBasicAuthentication().
                contentType(ContentType.JSON).and().accept(ContentType.ANY).and().
                pathParams(commonFunctions.commonParams(ConsoleConsts.CUSTOM.text, ConsoleConsts.SA.text));
        HashMap<String, String> fixletDetails = new HashMap<>();
        fixletDetails.put(CreatePlanConsts.OS_DEPLOY_FIX_UPDATE_SERV_WHITELIST.text, CreatePlanConsts.OS_DEPLOY_FIX_UPDATE_SERV_WHITELIST_ID.text);
        fixletDetails.put(CreatePlanConsts.OS_DEPLOY_FIX_MS_FRAMEWORK.text, CreatePlanConsts.OS_DEPLOY_FIX_MS_FRAMEWORK_ID.text);
        String planID = automationPlans.createPlan(CreatePlanConsts.PLAN_WITH_OS_DEPLOYMENT_SITE_FIXLETS.text, fixletDetails);
        logger.info("Created plan id : \n" + planID);

    }

    @Step("And checks whether sa rest GET api works for os deployment site")
    public void verifyPlanWithOsDeploymentSiteFixlet() throws IOException, SAXException, InterruptedException {
        String PlanID = SuperClass.specStore.get(CreatePlanConsts.PLAN_ID).toString();
        response = apiRequests.setSaRestURIAndBasicAuthentication().contentType(ContentType.JSON).and().accept(ContentType.ANY).and().
                and().pathParams(CreatePlanConsts.PLAN_ID.text, PlanID).when().with().get(jsonParser.getUriToFetchSaRestMasterPlanXml(saRestConsoleApiObject));
        logger.info("Generated response is : \n" + response);
    }

    @Step("And gets response as http 200 status code for os deployment site")
    public void statusCodeVerification() throws IOException, SAXException {
        Integer statusCode = response.statusCode();
        Assert.assertEquals(Integer.parseInt(ConsoleConsts.HTTP_SC_OK.text), statusCode.intValue());
        logger.info("status code is " + statusCode);
    }

    @Step("And response body as plan defintion xml template of os deployment site")
    public void responseXmlValidations() {
        String responseString = response.asString();
        Assert.assertTrue(responseString.contains(XMLConsts.EXECUTE_PLAN.text));
        Assert.assertTrue(responseString.contains(XMLConsts.SA_REST.text));
        logger.info("Response string is " + responseString);
    }

    @Step("User creates a custom site plan")
    public void createPlanWithCustomSite() throws IOException, SAXException {
        RequestSpecification requestSpecification = apiRequests.setBaseURIAndBasicAuthentication().
                contentType(ContentType.JSON).and().accept(ContentType.ANY).and().
                pathParams(commonFunctions.commonParams(ConsoleConsts.CUSTOM.text, ConsoleConsts.SA.text));
        HashMap<String, String> fixletDetails = consoleActions.importFixlet(CommonFunctions.getPath(ConsoleConsts.FIXLETS_FOLDER.text), requestSpecification);
        String planID = automationPlans.createPlan(CreatePlanConsts.CUSTOM_SITE_PLAN.text, fixletDetails, ConsoleConsts.SA.text);
        logger.info("Created plan id : \n" + planID);
    }

    @Step("And checks whether sa rest GET api works for custom site")
    public void verifyPlanWithCustomSite() throws IOException, SAXException {
        String planID = SuperClass.specStore.get(CreatePlanConsts.PLAN_ID).toString();
        RequestSpecification requestSpecification = apiRequests.setSaRestURIAndBasicAuthentication().contentType(ContentType.JSON).and().accept(ContentType.ANY).and().
                and().pathParams(commonFunctions.saRestCommonParams(ConsoleConsts.CUSTOM.text, ConsoleConsts.SA.text, planID));
        response = automationPlans.getPlanXml(requestSpecification);
        response.then().statusCode(Integer.parseInt(ConsoleConsts.HTTP_SC_OK.text));
        logger.info("Generated response is : \n" + response);
    }

    @Step("Then in response user should get http 200 status code for custom site")
    public void responseStatusCodeForCustomSitePlan() throws IOException, SAXException {
        Integer statusCode = response.statusCode();
        Assert.assertEquals(Integer.parseInt(ConsoleConsts.HTTP_SC_OK.text), statusCode.intValue());
        logger.info("status code is " + statusCode);
    }

    @Step("And response body as plan defintion xml template of custom site")
    public void responseValidationForCustomSitePlan() {
        String responseString = response.asString();
        Assert.assertTrue(responseString.contains(XMLConsts.EXECUTE_PLAN.text));
        Assert.assertTrue(responseString.contains(XMLConsts.SA_REST.text));
        logger.info("Response string is " + responseString);
    }

    @Step("User creates master site plan by using parameterised fixlet")
    public void createSecureParameterPlan() throws Exception {
        RequestSpecification requestSpecification = apiRequests.setBaseURIAndBasicAuthentication().
                contentType(ContentType.JSON).and().accept(ContentType.ANY).and().
                pathParams(commonFunctions.commonParams(ConsoleConsts.CUSTOM.text, ConsoleConsts.SA.text));
        HashMap<String, String> fixletDetails = consoleActions.importFixlet(CommonFunctions.getPath(ConsoleConsts.SECURE_PARAMETER_FIXLET_FOLDER_PATH.text), requestSpecification);
        String planID = automationPlans.createPlan(CreatePlanConsts.SECURE_PARAMETER_PLAN.text, fixletDetails);
        logger.info("Created plan id : \n" + planID);

    }

    @Step("And execute sa rest get api by adding the plan id in path parameters")
    public void verifySecureParameterPlan() throws IOException, SAXException {
        String planID = SuperClass.specStore.get(CreatePlanConsts.PLAN_ID).toString();
        Response response = automationPlans.getPlanXml(planID);
        response.then().statusCode(Integer.parseInt(ConsoleConsts.HTTP_SC_OK.text));
        logger.info("Generated response is : \n" + response);
    }

    @Step("Verify API is returning HTTP 200 status code & Plan definition XML template")
    public void getPlanAPIReponseValidation() {
        String planID = SuperClass.specStore.get(CreatePlanConsts.PLAN_ID).toString();
        Response response = automationPlans.getPlanXml(planID);
        response.then().statusCode(Integer.parseInt(ConsoleConsts.HTTP_SC_OK.text));
        Integer statusCode = response.statusCode();
        Assert.assertEquals(Integer.parseInt(ConsoleConsts.HTTP_SC_OK.text), statusCode.intValue());
        String responseString = response.asString();
        Assert.assertTrue(responseString.contains(XMLConsts.EXECUTE_PLAN.text));
        Assert.assertTrue(responseString.contains(XMLConsts.SA_REST.text));
        Assert.assertTrue(responseString.contains(XMLConsts.PARAMETER_SET.text));
        Assert.assertTrue(responseString.contains(XMLConsts.TARGET_SET.text));
        logger.info("Response string is " + responseString);
    }

    @Step("And add regular parameter, secure parameter in parameter-set tag of plan definition XML")
    public void setParameter() throws Exception {
        String planID = SuperClass.specStore.get(CreatePlanConsts.PLAN_ID).toString();
        Response response = automationPlans.getPlanXml(planID);
        HashMap<String, String> fixletDetails = (HashMap<String, String>) SuperClass.specStore.get(CreatePlanConsts.FIXLET_DETAILS);
        String actionBody = automationPlans.modifySecureParameterPlanDefXmlTemp(fixletDetails, response);
        SuperClass.specStore.put(CreatePlanConsts.ACTION_BODY, actionBody);
    }

    @Step("Then execute the plan by adding xml template as request body in sa-rest post api")
    public void executePlan() throws Exception {
        String planID = SuperClass.specStore.get(CreatePlanConsts.PLAN_ID).toString();
        HashMap<String, String> fixletDetails = (HashMap<String, String>) SuperClass.specStore.get(CreatePlanConsts.FIXLET_DETAILS);
        automationPlans.exectueSaRestPlan(planID, fixletDetails);
        logger.info("Executed plan id : \n" + planID);

    }

    @Step("Verify API is returning HTTP 200 status code")
    public void verifyExecuteAPIResponse() throws Exception {
        String statusCode = SuperClass.specStore.get(CreatePlanConsts.STATUS_CODE).toString();
        Assert.assertEquals(statusCode, ConsoleConsts.HTTP_SC_OK.text);
        logger.info("status code is verified sucessfully");
    }

    @Step("Verify plan action status and wait untill it got executed sucessfully")
    public void verifyPlanActionStatus() throws Exception {
        String actionID = SuperClass.specStore.get(ConsoleConsts.ACTION_ID).toString();
        String actionStatus = automationPlans.waitTillActionIsStoppedForSaRestPlan(actionID);
        logger.debug("verify plan action status: \n" + actionStatus);
    }


}
