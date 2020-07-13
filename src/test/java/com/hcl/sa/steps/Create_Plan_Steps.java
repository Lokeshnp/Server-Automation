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
import com.thoughtworks.gauge.TableRow;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class Create_Plan_Steps {

    protected ConsoleActions consoleActions = new ConsoleActions();
    private final Logger logger = LogManager.getLogger(AutomationPlans.class);
    protected AutomationPlans automationPlans = new AutomationPlans();
    ApiRequests apiRequests = new ApiRequests();
    CommonFunctions commonFunctions = new CommonFunctions();

    @Step("User should be able to execute Uninstall Automation Plan Engine Fixlet on the server Machine")
    public void executeUninstallPeFixlet() throws ParserConfigurationException, SAXException, IOException {
        consoleActions.takeAction(ConsoleConsts.UNINSTALL_PE_FIXLET_ID.text);
        logger.info("Automation Plan engine is uninstalled");
    }

    @Step("Create automation plan with multiple fixlets on following OS")
    public void createPlanWithMultipleFixlets() throws IOException, SAXException, TransformerException {
        //TODO LATER THIS FILTER NAME WILL BE PASSED ON THE BASIS OF FIXLETS NAME UNDER TEST DATA
        RequestSpecification requestSpecification = apiRequests.setBaseURIAndBasicAuthentication().
                contentType(ContentType.JSON).and().accept(ContentType.ANY).and().
                pathParams(commonFunctions.commonParams(ConsoleConsts.CUSTOM.text, ConsoleConsts.POOJA.text));
        HashMap<String, String> fixletDetails = consoleActions.importFixlet("C:\\IntegrateCode\\sa-dashboard-automation\\src\\test\\resources\\PlanFixlets\\", requestSpecification);
        String planID = automationPlans.createPlan(CreatePlanConsts.MULTIPLE_FIXLETS_PLAN.text, fixletDetails);
        SuperClass.specStore.put(CreatePlanConsts.PLAN_ID, planID);
        SuperClass.specStore.put(CreatePlanConsts.FIXLET_DETAILS, fixletDetails);
        logger.info("Created plan id : \n" + planID);
    }

    @Step("Then execute automation plan with multiple fixlets on following OS")
    public void executePlanWithID() throws IOException, SAXException, TransformerException {
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

    @Step("Create automation plan with baseline having multiple fixlets on following OS")
    public void createPlanWithMultipleBaselines() throws IOException, SAXException, TransformerException {
        //TODO LATER THIS FILTER NAME WILL BE PASSED ON THE BASIS OF FIXLETS NAME UNDER TEST DATA
        String baselinID = consoleActions.createBaseline(ConsoleConsts.CUSTOM.text, ConsoleConsts.POOJA.text);
        HashMap<String, String> fixletDetails = new HashMap<>();
        fixletDetails.put("Custom Baseline", baselinID);
        String planID = automationPlans.createPlan(CreatePlanConsts.MULTIPLE_BASELINE_PLAN.text, fixletDetails);
        SuperClass.specStore.put(CreatePlanConsts.PLAN_ID, planID);
        SuperClass.specStore.put(CreatePlanConsts.FIXLET_DETAILS, fixletDetails);
    }

    @Step("Then execute automation plan with baseline having multiple fixlets on following OS")
    public void executePlanWithMultipleBaselines() throws TransformerException, SAXException, IOException {
        String planID = SuperClass.specStore.get(CreatePlanConsts.PLAN_ID).toString();
        HashMap<String, String> fixletDetails = (HashMap<String, String>) SuperClass.specStore.get(CreatePlanConsts.FIXLET_DETAILS);
        automationPlans.executePlan(planID, fixletDetails);
        logger.info("Executed plan id : \n" + planID);
    }
}