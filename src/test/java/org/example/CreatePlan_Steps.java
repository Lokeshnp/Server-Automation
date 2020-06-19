package org.example;

import com.hcl.sa.constants.ConsoleConsts;
import com.hcl.sa.utils.Commonfunctions;
import com.hcl.sa.utils.ConsoleActions;
import com.thoughtworks.gauge.Step;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class CreatePlan_Steps {
    @Step("Uninstall automation plan engine")
    public void uninstallPlanEngine() throws InterruptedException, IOException, SAXException, ParserConfigurationException {
       ConsoleActions consoleActions = new ConsoleActions();
       consoleActions.uninstallAutomationPlanEngine(ConsoleConsts.UNINSTALL_PE_FIXLET_ID.text, ConsoleConsts.BIGFIX_SERVER_URI.text);
    }

    @Step("Plan engine should be up and running")
    public void installPlanEngine() throws InterruptedException, IOException, SAXException, ParserConfigurationException {
       ConsoleActions consoleActions = new ConsoleActions();
       consoleActions.installAutomationPlanEngine(ConsoleConsts.INSTALL_PE_FIXLET_ID.text, ConsoleConsts.BIGFIX_SERVER_URI.text);
       /* Verify plan engine is running
       Commonfunctions comnfunc = new Commonfunctions();
       String s =comnfunc.verifyFixletIsRunning("1", "553176257");*/
    }

    @Step("Delete plan action")
    public void deletePlanAction() {
        ConsoleActions consoleActions = new ConsoleActions();
        consoleActions.deleteAction("796"); //TODO : Get the action id when we execute the plan, and delete that id (Remove hardcoding)
    }
}