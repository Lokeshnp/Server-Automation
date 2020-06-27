package com.hcl.sa.steps;

import com.hcl.sa.constants.ConsoleConsts;
import com.hcl.sa.constants.CreatePlanConsts;
import com.hcl.sa.utils.ConsoleActions;
import com.hcl.sa.windows.AutomationPlans;
import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.Table;
import com.thoughtworks.gauge.TableRow;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

public class Create_Plan_Steps {

    protected ConsoleActions consoleActions = new ConsoleActions();
    private final Logger logger = LogManager.getLogger(AutomationPlans.class);
    protected AutomationPlans automationPlans = new AutomationPlans();

    @Step("User should be able to execute Uninstall Automation Plan Engine Fixlet on the server Machine")
    public void executeUninstallPeFixlet() throws ParserConfigurationException, SAXException, IOException {
        consoleActions.takeAction(ConsoleConsts.UNINSTALL_PE_FIXLET_ID.text);
        logger.info("Automation Plan engine is uninstalled");
    }


    @Step("Create automation plan with multiple fixlets on following OS <table>")
    public void createPlanWithMultipleFixlets(Table table) {
        //TODO LATER THIS FILTER NAME WILL BE PASSED ON THE BASIS OF FIXLETS NAME UNDER TEST DATA
        automationPlans.createPlan(CreatePlanConsts.MULTIPLE_FIXLETS_PLAN.text, "Google Chrome 80.0");
    }
}
