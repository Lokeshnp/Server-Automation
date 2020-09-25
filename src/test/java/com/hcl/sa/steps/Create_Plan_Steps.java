package com.hcl.sa.steps;

import com.hcl.sa.constants.ConsoleConsts;
import com.hcl.sa.constants.CreatePlanConsts;
import com.hcl.sa.utils.api.ApiRequests;
import com.hcl.sa.utils.bigfix.CommonFunctions;
import com.hcl.sa.utils.bigfix.ConsoleActions;
import com.hcl.sa.utils.bigfix.SuperClass;
import com.hcl.sa.utils.parser.JsonParser;
import com.hcl.sa.utils.parser.XMLParser;
import com.hcl.sa.windows.AutomationPlans;
import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.Table;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
//import junit.framework.Assert;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.junit.Assert;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class Create_Plan_Steps {

    protected ConsoleActions consoleActions = new ConsoleActions();
    private Logger logger = LogManager.getLogger(Create_Plan_Steps.class);
    protected AutomationPlans automationPlans = new AutomationPlans();
    ApiRequests apiRequests = new ApiRequests();
    CommonFunctions commonFunctions = new CommonFunctions();
    JsonParser jsonParser = new JsonParser();
    Response response;

    @Step("User should be able to execute Uninstall Automation Plan Engine Fixlet on the server Machine")
    public void executeUninstallPeFixlet() throws ParserConfigurationException, SAXException, IOException, TransformerException {
        consoleActions.takeActionOnRootServer(commonFunctions.setExternalSiteParams(ConsoleConsts.UNINSTALL_PE_FIXLET_ID.text));
        logger.info("Automation Plan engine is uninstalled");
    }

    @Step("Plan engine should be up and running")
    public void executeInstallPeFixlet() throws ParserConfigurationException, SAXException, IOException, TransformerException {
        consoleActions.takeActionOnRootServer(commonFunctions.setExternalSiteParams(ConsoleConsts.INSTALL_PE_FIXLET_ID.text));
        logger.info("Automation Plan engine is uninstalled");
    }

    @Step("Create automation plan with multiple fixlets on following OS <table>")
    public void createPlanWithMultipleFixlets(Table table) throws IOException, SAXException {
        //TODO LATER THIS FILTER NAME WILL BE PASSED ON THE BASIS OF FIXLETS NAME UNDER TEST DATA
        RequestSpecification requestSpecification = apiRequests.setBaseURIAndBasicAuthentication().
                contentType(ContentType.JSON).and().accept(ContentType.ANY).and().
                pathParams(commonFunctions.commonParams(ConsoleConsts.CUSTOM.text, ConsoleConsts.POOJA.text));
        HashMap<String, String> fixletDetails = consoleActions.importFixlet(CommonFunctions.getPath(ConsoleConsts.FIXLETS_FOLDER.text), requestSpecification);
        String planID = automationPlans.createPlan(CreatePlanConsts.MULTIPLE_FIXLETS_PLAN.text, fixletDetails);
        logger.info("Created plan id : \n" + planID);
    }

    @Step("Then execute automation plan with multiple fixlets on following OS <table>")
    public void executePlanWithMultipleFixlets(Table table) throws Exception {
        String planID = SuperClass.specStore.get(CreatePlanConsts.PLAN_ID).toString();
        HashMap<String, String> fixletDetails = (HashMap<String, String>) SuperClass.specStore.get(CreatePlanConsts.FIXLET_DETAILS);
        automationPlans.executePlan(planID, fixletDetails);
        logger.info("Executed plan id : \n" + planID);
    }

    @Step("Verify action status and wait untill it got executed sucessfully")
    public void verifyActionStatus() throws IOException, SAXException {
        String planActionID = SuperClass.specStore.get(CreatePlanConsts.PLAN_ACTION_ID).toString();
        String actionStatus = automationPlans.waitTillActionIsStopped(planActionID);
        logger.debug("verify plan action status: \n" + actionStatus);
    }

    @Step("And delete the Plan after status verification")
    public void deletePlanWithID() {
        String planID = SuperClass.specStore.get(CreatePlanConsts.PLAN_ID).toString();
        automationPlans.deletePlan(planID);
        logger.info("Deleted plan id : \n" + planID);
    }

    @Step("Create automation plan with baseline having multiple fixlets on following OS <table>")
    public void createPlanWithBaselineHavingFixlets(Table table) throws IOException, SAXException, TransformerException {
        //TODO LATER THIS FILTER NAME WILL BE PASSED ON THE BASIS OF FIXLETS NAME UNDER TEST DATA
        HashMap<String, String> fixletDetails = consoleActions.createBaseline(ConsoleConsts.CUSTOM.text, ConsoleConsts.POOJA.text, ConsoleConsts.BASELINE_FIXLETS_FOLDER.text, ConsoleConsts.SRC_SITENAME.text);
        String planID = automationPlans.createPlan(CreatePlanConsts.MULTIPLE_BASELINE_PLAN.text, fixletDetails);
        logger.info("Created plan id : \n" + planID);
    }

    @Step("Then execute automation plan with baseline having multiple fixlets on following OS <table>")
    public void executePlanHavingBaseline(Table table) throws Exception {
        String planID = SuperClass.specStore.get(CreatePlanConsts.PLAN_ID).toString();
        HashMap<String, String> fixletDetails = (HashMap<String, String>) SuperClass.specStore.get(CreatePlanConsts.BASELINE_DETAILS);
        automationPlans.executePlan(planID, fixletDetails);
        logger.info("Executed plan id : \n" + planID);
    }

    @Step("Create automation plan with multiple tasks on following OS <table>")
    public void createPlanWithMultipleTask(Table table) throws IOException, SAXException {
        RequestSpecification requestSpecification = apiRequests.setBaseURIAndBasicAuthentication().
                contentType(ContentType.JSON).and().accept(ContentType.ANY).and().
                pathParams(commonFunctions.commonParams(ConsoleConsts.CUSTOM.text, ConsoleConsts.POOJA.text));
        HashMap<String, String> fixletDetails = consoleActions.createTask(CommonFunctions.getPath(ConsoleConsts.TASKS_FOLDER.text), requestSpecification);
        String planID = automationPlans.createPlan(CreatePlanConsts.MULTIPLE_TASKS_PLAN.text, fixletDetails);
        logger.info("Created plan id : \n" + planID);
    }

    @Step("Then execute automation plan with multiple tasks on following OS <table>")
    public void executePlanWithMultipleTasks(Table table) throws Exception {
        String planID = SuperClass.specStore.get(CreatePlanConsts.PLAN_ID).toString();
        HashMap<String, String> fixletDetails = (HashMap<String, String>) SuperClass.specStore.get(CreatePlanConsts.FIXLET_DETAILS);
        automationPlans.executePlan(planID, fixletDetails);
        logger.info("Executed plan id : \n" + planID);
    }

    @Step("Create automation plan with baseline having multiple tasks on following OS <table>")
    public void createPlanWithBaselineHavingTasks(Table table) throws TransformerException, SAXException, IOException {
        //TODO LATER THIS FILTER NAME WILL BE PASSED ON THE BASIS OF FIXLETS NAME UNDER TEST DATA   ConsoleConsts.BASELINE_TASKS_FOLDER.text
        HashMap<String, String> baselineDetails = consoleActions.createBaselineHavingTasks(ConsoleConsts.CUSTOM.text, ConsoleConsts.POOJA.text, ConsoleConsts.BASELINE_TASKS_FOLDER.text, ConsoleConsts.SRC_SITENAME.text);
        String planID = automationPlans.createPlan(CreatePlanConsts.MULTIPLE_BASELINE_TASKS_PLAN.text, baselineDetails);
        logger.info("Created plan id : \n" + planID);
    }

    @Step("Then execute automation plan with baseline having multiple tasks on following OS <table>")
    public void executePlanWithBaselineHavingTasks(Table table) throws Exception {
        String planID = SuperClass.specStore.get(CreatePlanConsts.PLAN_ID).toString();
        HashMap<String, String> baselineDetails = (HashMap<String, String>) SuperClass.specStore.get(CreatePlanConsts.BASELINE_DETAILS);
        automationPlans.executePlan(planID, baselineDetails);
        logger.info("Executed plan id : \n" + planID);
    }

    @Step("Create automation plan with multiple fixlets and tasks on following OS <table>")
    public void createPlanWithFixletsAndTasks(Table table) throws IOException, SAXException, ParserConfigurationException {
        RequestSpecification requestSpecification = apiRequests.setBaseURIAndBasicAuthentication().
                contentType(ContentType.JSON).and().accept(ContentType.ANY).and().
                pathParams(commonFunctions.commonParams(ConsoleConsts.CUSTOM.text, ConsoleConsts.POOJA.text));
        HashMap<String, String> fixletDetails = consoleActions.createFixletsAndTasks(CommonFunctions.getPath(ConsoleConsts.FIXLETS_TASKS_FOLDER.text), requestSpecification);
        String planID = automationPlans.createPlan(CreatePlanConsts.MULTIPLE_FIXLETS_AND_TASKS_PLAN.text, fixletDetails);
        logger.info("Created plan id : \n" + planID);
    }

    @Step("Then execute automation plan with multiple fixlets and tasks on following OS <table>")
    public void executePlanWithFixletsAndTasks(Table table) throws Exception {
        String planID = SuperClass.specStore.get(CreatePlanConsts.PLAN_ID).toString();
        HashMap<String, String> fixletDetails = (HashMap<String, String>) SuperClass.specStore.get(CreatePlanConsts.FIXLET_DETAILS);
        automationPlans.executePlan(planID, fixletDetails);
        logger.info("Executed plan id : \n" + planID);
    }

    @Step("Create automation plan with baseline having multiple fixlets and tasks on following OS <table>")
    public void createPlanWithBaselineHavingFixletsAndTasks(Table table) throws TransformerException, SAXException, IOException, ParserConfigurationException {
        //TODO LATER THIS FILTER NAME WILL BE PASSED ON THE BASIS OF FIXLETS NAME UNDER TEST DATA
        HashMap<String, String> baselineDetails = consoleActions.createBaselineHavingFixletsAndTasks(ConsoleConsts.CUSTOM.text, ConsoleConsts.POOJA.text, ConsoleConsts.BASELINE_FIXLETS_AND_TASKS_FOLDER.text, ConsoleConsts.SRC_SITENAME.text);
        String planID = automationPlans.createPlan(CreatePlanConsts.MULTIPLE_FIXLETS_AND_TASKS_PLAN.text, baselineDetails);
        logger.info("Created plan id : \n" + planID);
    }

    @Step("Then execute automation plan with baseline having fixlets and tasks on following OS <table>")
    public void executePlanWithBaselineHavingFixletsAndTasks(Table table) throws Exception {
        String planID = SuperClass.specStore.get(CreatePlanConsts.PLAN_ID).toString();
        HashMap<String, String> fixletDetails = (HashMap<String, String>) SuperClass.specStore.get(CreatePlanConsts.BASELINE_DETAILS);
        automationPlans.executePlan(planID, fixletDetails);
        logger.info("Executed plan id : \n" + planID);
    }

    @Step("When User sends SA-REST Get Api Request for the existing external site automation plan")
    public void verifyPlanWithExternalSite() throws IOException, SAXException {
        List<String> plans = consoleActions.getPlansList(commonFunctions.commonParams(ConsoleConsts.EXTERNAL.text, ConsoleConsts.SERVER_AUTOMATION.text));
        for (int i = 0; i < plans.size(); i++) {
            RequestSpecification requestSpecification = apiRequests.setSaRestURIAndBasicAuthentication().contentType(ContentType.JSON).and().accept(ContentType.ANY).and().
                    and().pathParams(commonFunctions.saRestCommonParams(ConsoleConsts.EXTERNAL.text, ConsoleConsts.SERVER_AUTOMATION.text, plans.get(i)));
            Response response = automationPlans.getPlanXml(requestSpecification);
            response.then().statusCode(200);
            logger.info("Generated response is : \n" + response);
        }
    }
    @Step("And checks whether sa rest GET api works for it")
    public void verifyPlanWithExistingExternalSiteFixlet() throws IOException, SAXException {
        String PlanID = "419";
        RequestSpecification requestSpecification = apiRequests.setSaRestURIAndBasicAuthentication().contentType(ContentType.JSON).and().accept(ContentType.ANY).and().
                and().pathParams(CreatePlanConsts.PLAN_ID.text, PlanID);
        System.out.println("requestSpecification = " + requestSpecification);
        System.out.println("*****************************");
        response = automationPlans.getPlanXml(requestSpecification, "serverautomation/plan/master/{planID}");
        System.out.println("*****************************" + response.asString());
        response.then().statusCode(200);
        logger.info("Generated response is : \n" + response);
    }

    @Step("Then in response user should get http 200 status code")
    public void InResponseUserShouldGetHttp200StatusCodeOrNot() throws IOException, SAXException {
        Response response = apiRequests.setSaRestURIAndBasicAuthentication().contentType(ContentType.JSON).and().accept(ContentType.ANY).and().
                and().pathParams(CreatePlanConsts.PLAN_ID.text, 424).when().get("serverautomation/plan/master/{planID}");
        Integer statusCode = response.statusCode();
        System.out.println("##########################" + statusCode);
        Assert.assertEquals(200, statusCode.intValue());
    }
    @Step("And response body as plan defintion xml template")
    public void XmlValidationsDefinition() {
        XMLParser xmlParser = new XMLParser();
        RequestSpecification requestSpecification = apiRequests.setSaRestURIAndBasicAuthentication().contentType(ContentType.JSON).and().accept(ContentType.ANY).and().
                and().pathParams(commonFunctions.saRestCommonParams(ConsoleConsts.CUSTOM.text, ConsoleConsts.POOJA.text, "381"));
        System.out.println("requestSpecification = " + requestSpecification);
        String responseString = response.asString();
        Assert.assertTrue(responseString.contains("execute-plan"));
        Assert.assertTrue(responseString.contains("sa-rest"));
        System.out.println("response=" + responseString);
    }



    @Step("User creates a custom site plan")
    public void createPlanWithCustomSite() throws IOException, SAXException {
        RequestSpecification requestSpecification = apiRequests.setBaseURIAndBasicAuthentication().
                contentType(ContentType.JSON).and().accept(ContentType.ANY).and().
                pathParams(commonFunctions.commonParams(ConsoleConsts.CUSTOM.text, ConsoleConsts.POOJA.text));
        HashMap<String, String> fixletDetails = consoleActions.importFixlet(CommonFunctions.getPath(ConsoleConsts.FIXLETS_FOLDER.text), requestSpecification);
        String planID = automationPlans.createPlan(CreatePlanConsts.MULTIPLE_FIXLETS_PLAN.text, fixletDetails, ConsoleConsts.POOJA.text);
        logger.info("Created plan id : \n" + planID);
    }

    @Step("And checks whether sa rest GET api works for it")
    public void verifyPlanWithCustomSite() throws IOException, SAXException {
        String planID = SuperClass.specStore.get(CreatePlanConsts.PLAN_ID).toString();
        RequestSpecification requestSpecification = apiRequests.setSaRestURIAndBasicAuthentication().contentType(ContentType.JSON).and().accept(ContentType.ANY).and().
                and().pathParams(commonFunctions.saRestCommonParams(ConsoleConsts.CUSTOM.text, ConsoleConsts.POOJA.text, planID));
        Response response = automationPlans.getPlanXml(requestSpecification);
        response.then().statusCode(200);
        logger.info("Generated response is : \n" + response);
    }

    @Step("User creates a master site plan having external site fixlets/tasks <table>")
    public void createPlanWithExternalFixlets(Table table) throws IOException, SAXException {
        HashMap<String, String> fixletDetails = new HashMap<>();
        fixletDetails.put("BES Clients Have Incorrect Clock Time","15");
        fixletDetails.put("Restart BES Clients","75");
        String planID = automationPlans.createPlan(CreatePlanConsts.EXTERNAL_SITE_FIXLET.text, fixletDetails);
        logger.info("Created plan id : \n" + planID);
    }


    @Step("And checks whether sa rest GET api works for it")
    public void verifyPlanWithExternalSiteFixlet() throws IOException, SAXException {
        String PlanID = "419";
        RequestSpecification requestSpecification = apiRequests.setSaRestURIAndBasicAuthentication().contentType(ContentType.JSON).and().accept(ContentType.ANY).and().
                and().pathParams(CreatePlanConsts.PLAN_ID.text, PlanID);
        System.out.println("requestSpecification = " + requestSpecification);
        System.out.println("*****************************");
        response = automationPlans.getPlanXml(requestSpecification, "serverautomation/plan/master/{planID}");
        System.out.println("*****************************" + response.asString());
        response.then().statusCode(200);
        logger.info("Generated response is : \n" + response);
    }

    @Step("And gets response as http 200 status code")
        public void ThenInResponseUserShouldGetHttp200StatusCodeOrNot() throws IOException, SAXException {
        Response response = apiRequests.setSaRestURIAndBasicAuthentication().contentType(ContentType.JSON).and().accept(ContentType.ANY).and().
                and().pathParams(CreatePlanConsts.PLAN_ID.text, 424).when().get("serverautomation/plan/master/{planID}");
        Integer statusCode = response.statusCode();

        System.out.println("##########################" + statusCode);
        Assert.assertEquals(200, statusCode.intValue());
    }




    @Step("And response body as plan defintion xml template")
    public void XmlValidations() {
        XMLParser xmlParser = new XMLParser();
        RequestSpecification requestSpecification = apiRequests.setSaRestURIAndBasicAuthentication().contentType(ContentType.JSON).and().accept(ContentType.ANY).and().
                and().pathParams(commonFunctions.saRestCommonParams(ConsoleConsts.CUSTOM.text, ConsoleConsts.POOJA.text, "381"));
        System.out.println("requestSpecification = " + requestSpecification);
        String responseString = response.asString();
        Assert.assertTrue(responseString.contains("execute-plan"));
        Assert.assertTrue(responseString.contains("sa-rest"));
        System.out.println("response=" + responseString);
    }
    @Step("When User sends SA-REST Get Api Request without authentication parameters")
    public void verifyPlanWithoutAuthenatication() throws IOException, SAXException {
        Response response = apiRequests.invalidSaAuthentication().contentType(ContentType.JSON).and().accept(ContentType.ANY).and().
                and().pathParams(CreatePlanConsts.PLAN_ID.text,560).when().get("serverautomation/plan/master/{planID}");
        Integer statusCode = response.statusCode();

        System.out.println("##########################" +statusCode);
        Assert.assertEquals(401, statusCode.intValue());
    }
    @Step("When User sends SA-REST Plan Action Api Request without authentication parameters")
    public void verifyPlanActionWithoutAuthenatication() throws IOException, SAXException {
        Response response = apiRequests.invalidSaAuthentication().contentType(ContentType.JSON).and().accept(ContentType.ANY).and().
                and().pathParams(CreatePlanConsts.PLAN_ID.text,560).when().get("serverautomation/planaction/{planID}");
        Integer statusCode = response.statusCode();

        System.out.println("##########################" +statusCode);
        Assert.assertEquals(401, statusCode.intValue());
    }
    @Step("And checks whether sa rest GET api works for it")
    public void verifyPlanAction() throws IOException, SAXException {

        String PlanID = "560";
        Response response = apiRequests.invalidSaAuthentication().contentType(ContentType.JSON).and().accept(ContentType.ANY).and().
                and().pathParams(CreatePlanConsts.PLAN_ID.text,PlanID).when().get("serverautomation/planaction/{planID}");
        System.out.println("*****************************");
        System.out.println("*****************************"+response.getStatusCode());
        logger.info("Generated response is : \n" + response);
    }
    @Step("Then in response user should get http 401 status code")
    public void verifyPlanActionWithoutAuthe() throws IOException, SAXException {
        Response response = apiRequests.invalidSaAuthentication().contentType(ContentType.JSON).and().accept(ContentType.ANY).and().
                and().pathParams(CreatePlanConsts.PLAN_ID.text,560).when().get("serverautomation/planaction/{planID}");
        Integer statusCode = response.statusCode();

        System.out.println("##########################" +statusCode);
        Assert.assertEquals(401, statusCode.intValue());
    }

     @Step("And user should validate the error message in response body")
    public void PlanActionXmlValidations() {
        XMLParser xmlParser = new XMLParser();
        Response response = apiRequests.invalidSaAuthentication().contentType(ContentType.JSON).and().accept(ContentType.ANY).and().
                and().pathParams(CreatePlanConsts.PLAN_ID.text,560).when().get("serverautomation/planaction/{planID}");;
        String responseString = response.asString();
        System.out.println("response=" + responseString);

    }



}