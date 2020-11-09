package com.hcl.sa.steps;

import com.hcl.sa.constants.ConsoleConsts;
import com.hcl.sa.constants.CreatePlanConsts;
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
//import junit.framework.Assert;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.HashMap;

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
                pathParams(commonFunctions.commonParams(ConsoleConsts.CUSTOM.text, ConsoleConsts.SA.text));
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
        HashMap<String, String> fixletDetails = consoleActions.createBaseline(ConsoleConsts.CUSTOM.text, ConsoleConsts.SA.text, ConsoleConsts.BASELINE_FIXLETS_FOLDER.text, ConsoleConsts.SRC_SITENAME.text);
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
                pathParams(commonFunctions.commonParams(ConsoleConsts.CUSTOM.text, ConsoleConsts.SA.text));
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
        HashMap<String, String> baselineDetails = consoleActions.createBaselineHavingTasks(ConsoleConsts.CUSTOM.text, ConsoleConsts.SA.text, ConsoleConsts.BASELINE_TASKS_FOLDER.text, ConsoleConsts.SRC_SITENAME.text);
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
                pathParams(commonFunctions.commonParams(ConsoleConsts.CUSTOM.text, ConsoleConsts.SA.text));
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
        HashMap<String, String> baselineDetails = consoleActions.createBaselineHavingFixletsAndTasks(ConsoleConsts.CUSTOM.text, ConsoleConsts.SA.text, ConsoleConsts.BASELINE_FIXLETS_AND_TASKS_FOLDER.text, ConsoleConsts.SRC_SITENAME.text);
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

    @Step("Create automation plan with a combination of baseline, fixlets and tasks on following OS <table>")
    public void createMultipleCombinationPlan(Table table) throws IOException, SAXException, TransformerException, ParserConfigurationException {
        RequestSpecification requestSpecification = apiRequests.setBaseURIAndBasicAuthentication().
                contentType(ContentType.JSON).and().accept(ContentType.ANY).and().
                pathParams(commonFunctions.commonParams(ConsoleConsts.CUSTOM.text, ConsoleConsts.SA.text));
        HashMap<String, String> fixletsAndTasksDetails = consoleActions.createFixletsAndTasks(CommonFunctions.getPath(ConsoleConsts.PLAN_BASELINE_FIXLETS_TASKS_FOLDER.text), requestSpecification);
        HashMap<String, String> baselinID = consoleActions.createBaseline(ConsoleConsts.CUSTOM.text, ConsoleConsts.SA.text, ConsoleConsts.BASELINE_FIXLETS_FOLDER.text, ConsoleConsts.SRC_SITENAME.text);
        fixletsAndTasksDetails.putAll(baselinID);
        //Plan name is too long so declaring from code
        String planID = automationPlans.createPlan("Plan with combination of fixlets_tasks_baseline", fixletsAndTasksDetails);
        SuperClass.specStore.put(CreatePlanConsts.FIXLET_DETAILS, fixletsAndTasksDetails);
        logger.info("Created plan id : \n" + planID);

    }

    @Step("Then execute automation plan with a combination of baselines, fixlets and tasks on following OS <table>")
    public void executeMultipleCombinationPlan(Table table) throws Exception {
        String planID = SuperClass.specStore.get(CreatePlanConsts.PLAN_ID).toString();
        HashMap<String, String> fixletDetails = (HashMap<String, String>) SuperClass.specStore.get(CreatePlanConsts.FIXLET_DETAILS);
        automationPlans.executePlan(planID, fixletDetails);
        logger.info("Executed plan id : \n" + planID);
    }

    @Step("Create parallel automation plan on following OS <table>")
    public void createParallelPlan(Table table) throws IOException, SAXException, TransformerException {
        RequestSpecification requestSpecification = apiRequests.setBaseURIAndBasicAuthentication().
                contentType(ContentType.JSON).and().accept(ContentType.ANY).and().
                pathParams(commonFunctions.commonParams(ConsoleConsts.CUSTOM.text, ConsoleConsts.SA.text));
        HashMap<String, String> fixletDetails = consoleActions.importFixlet(CommonFunctions.getPath(ConsoleConsts.FIXLETS_FOLDER.text), requestSpecification);
        String planID = automationPlans.createParallelPlan(CreatePlanConsts.PARALLEL_PLAN.text, fixletDetails);
        logger.info("Created plan id : \n" + planID);

    }

    @Step("Then execute parallel automation plan on following OS <table>")
    public void executeParallelPlan(Table table) throws Exception {
        String planID = SuperClass.specStore.get(CreatePlanConsts.PLAN_ID).toString();
        HashMap<String, String> fixletDetails = (HashMap<String, String>) SuperClass.specStore.get(CreatePlanConsts.FIXLET_DETAILS);
        automationPlans.executePlan(planID, fixletDetails);
        logger.info("Executed plan id : \n" + planID);
    }

    @Step("User click on create button")
    public void clickCreateButton(){
        automationPlans.clickCreateBtn();
        logger.info("User clicked on create button");
    }

    @Step("Then give plan name in details tab")
    public void inputPlanName(){
        automationPlans.enterPlanName("Verifying Default Settings");
        logger.info("User entered the plan name");
    }

    @Step("And click on add step button")
    public void openStepsTab(){
        automationPlans.clickAddStepBtn();
        logger.info("User clicked on steps tab");
    }

    @Step("Then add a fixlet to the plan")
    public void addFixletToPlan(){
        automationPlans.addStepToPlan(CreatePlanConsts.FIX_UNINSTALL_PLAN_ENGINE_ID.text, CreatePlanConsts.FIX_UNINSTALL_PLAN_ENGINE.text);
        logger.info("Adding step to the plan");
    }

    @Step("Click on default settings icon")
    public void clickDefaultSettingsIcon(){
        automationPlans.clickDefaultSettings();
        logger.info("User clicked on default settings btn");
    }

    @Step("verify user is able to add computers in selected targets tab")
    public void changeDefaultSettings(){
        automationPlans.addCompToSelectedTarget(ConsoleConsts.ROOT_SERVER.text);
        logger.info("User changed the default settings of the plan");
    }

    @Step("Click on save button")
    public void clickSavePlanBtn(){
        automationPlans.savePlan();
        logger.info("User save the plan");
    }

    @Step("Select the created plan & click on take action button")
    public void clickTakeAction(){
        automationPlans.clickTakeAction("Verifying Default Settings");
    }

    @Step("Verify added target computers is displaying under selected Targets tab")
    public void verifyAddedComp(){
        automationPlans.verifyDefaultSettings();
        logger.info("Default computer is displayed under selected Target while taking action for plan");
    }
}