package org.example;

import com.hcl.sa.constants.ConsoleConsts;
import com.hcl.sa.utils.CommonFunctions;
import com.hcl.sa.utils.ConsoleActions;
import com.thoughtworks.gauge.Step;
import io.restassured.response.Response;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class CreatePlan_Steps {
    ConsoleActions consoleActions = new ConsoleActions();

    @Step("Uninstall automation plan engine")
    public void uninstallPlanEngine() throws InterruptedException, IOException, SAXException, ParserConfigurationException {
        consoleActions.uninstallAutomationPlanEngine(ConsoleConsts.UNINSTALL_PE_FIXLET_ID.text);
    }

    @Step("Plan engine should be up and running")
    public void installPlanEngine() throws InterruptedException, IOException, SAXException, ParserConfigurationException {
        consoleActions.installAutomationPlanEngine(ConsoleConsts.INSTALL_PE_FIXLET_ID.text);
    }

    @Step("Delete plan action")
    public void deletePlanAction() {
        consoleActions.deleteAction("843"); //TODO : Get the action id when we execute the plan, and delete that id (Remove hardcoding)
    }
}