package com.hcl.sa.steps;

import com.hcl.sa.constants.ConsoleConsts;
import com.hcl.sa.constants.CreatePlanConsts;
import com.hcl.sa.utils.ApiRequests;
import com.hcl.sa.utils.CommonFunctions;
import com.hcl.sa.utils.ConsoleActions;
import com.hcl.sa.utils.SuperClass;
import com.hcl.sa.windows.AutomationPlans;
import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.Table;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.HashMap;

public class Create_Plan_Steps {

    protected ConsoleActions consoleActions = new ConsoleActions();
    private final Logger logger = LogManager.getLogger(AutomationPlans.class);
    protected AutomationPlans automationPlans = new AutomationPlans();
    ApiRequests apiRequests = new ApiRequests();
    CommonFunctions commonFunctions = new CommonFunctions();

    @Step("User should be able to execute Uninstall Automation Plan Engine Fixlet on the server Machine")
    public void executeUninstallPeFixlet() throws ParserConfigurationException, SAXException, IOException, TransformerException {
        consoleActions.takeAction(commonFunctions.defaultSiteDetails(ConsoleConsts.UNINSTALL_PE_FIXLET_ID.text));
        logger.info("Automation Plan engine is uninstalled");
    }

    @Step("Plan engine should be up and running")
    public void executeInstallPeFixlet() throws ParserConfigurationException, SAXException, IOException, TransformerException {
        consoleActions.takeAction(commonFunctions.defaultSiteDetails(ConsoleConsts.INSTALL_PE_FIXLET_ID.text));
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
    public void executePlanWithMultipleFixlets(Table table) throws IOException, SAXException, TransformerException, ParserConfigurationException {
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
        HashMap<String, String> fixletDetails = consoleActions.createBaseline(ConsoleConsts.CUSTOM.text, ConsoleConsts.POOJA.text);
        String planID = automationPlans.createPlan(CreatePlanConsts.MULTIPLE_BASELINE_PLAN.text, fixletDetails);
        logger.info("Created plan id : \n" + planID);
    }

    @Step("Then execute automation plan with baseline having multiple fixlets on following OS <table>")
    public void executePlanHavingBaseline(Table table) throws TransformerException, SAXException, IOException, ParserConfigurationException {
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
    public void executePlanWithMultipleTasks(Table table) throws TransformerException, SAXException, IOException, ParserConfigurationException {
        String planID = SuperClass.specStore.get(CreatePlanConsts.PLAN_ID).toString();
        HashMap<String, String> fixletDetails = (HashMap<String, String>) SuperClass.specStore.get(CreatePlanConsts.FIXLET_DETAILS);
        automationPlans.executePlan(planID, fixletDetails);
        logger.info("Executed plan id : \n" + planID);
    }

    @Step("Create automation plan with baseline having multiple tasks on following OS <table>")
    public void createPlanWithBaselineHavingTasks(Table table) throws TransformerException, SAXException, IOException {
        //TODO LATER THIS FILTER NAME WILL BE PASSED ON THE BASIS OF FIXLETS NAME UNDER TEST DATA
        HashMap<String, String> baselineDetails = consoleActions.createBaselineHavingTasks(ConsoleConsts.CUSTOM.text, ConsoleConsts.POOJA.text);
        String planID = automationPlans.createPlan(CreatePlanConsts.MULTIPLE_BASELINE_TASKS_PLAN.text, baselineDetails);
        logger.info("Created plan id : \n" + planID);
    }

    @Step("Then execute automation plan with baseline having multiple tasks on following OS <table>")
    public void executePlanWithBaselineHavingTasks(Table table) throws TransformerException, SAXException, IOException, ParserConfigurationException {
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
        String planID = automationPlans.createPlan(CreatePlanConsts.MULTIPLE_FIXLETSANDTASKS_PLAN.text, fixletDetails);
        logger.info("Created plan id : \n" + planID);
    }

    @Step("Then execute automation plan with multiple fixlets and tasks on following OS <table>")
    public void executePlanWithFixletsAndTasks(Table table) throws TransformerException, SAXException, IOException, ParserConfigurationException {
        String planID = SuperClass.specStore.get(CreatePlanConsts.PLAN_ID).toString();
        HashMap<String, String> fixletDetails = (HashMap<String, String>) SuperClass.specStore.get(CreatePlanConsts.FIXLET_DETAILS);
        automationPlans.executePlan(planID, fixletDetails);
        logger.info("Executed plan id : \n" + planID);
    }

    @Step("Create automation plan with baseline having multiple fixlets and tasks on following OS <table>")
    public void createPlanWithBaselineHavingFixletsAndTasks(Table table) throws TransformerException, SAXException, IOException, ParserConfigurationException {
        //TODO LATER THIS FILTER NAME WILL BE PASSED ON THE BASIS OF FIXLETS NAME UNDER TEST DATA
        HashMap<String, String> baselineDetails = consoleActions.createBaselineHavingFixletsAndTasks(ConsoleConsts.CUSTOM.text, ConsoleConsts.POOJA.text);
        String planID = automationPlans.createPlan(CreatePlanConsts.MULTIPLE_FIXLETSANDTASKS_PLAN.text, baselineDetails);
        logger.info("Created plan id : \n" + planID);
    }

    @Step("Then execute automation plan with baseline having fixlets and tasks on following OS <table>")
    public void executePlanWithBaselineHavingFixletsAndTasks(Table table) throws SAXException, TransformerException, ParserConfigurationException, IOException {
        String planID = SuperClass.specStore.get(CreatePlanConsts.PLAN_ID).toString();
        HashMap<String, String> fixletDetails = (HashMap<String, String>) SuperClass.specStore.get(CreatePlanConsts.BASELINE_DETAILS);
        automationPlans.executePlan(planID, fixletDetails);
        logger.info("Executed plan id : \n" + planID);
    }
}