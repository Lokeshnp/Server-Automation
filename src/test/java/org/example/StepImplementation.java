package org.example;

import com.hcl.sa.constants.ConsoleConsts;
import com.hcl.sa.utils.ApiUtils;
import com.thoughtworks.gauge.Step;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class StepImplementation {
    @Step("Uninstall automation plan engine")
    public void uninstallPlanEngine() throws InterruptedException, IOException, SAXException, ParserConfigurationException {
       System.out.println("Uninstall test....");
       ApiUtils util = new ApiUtils();
       util.uninstallAutomationPlanEngine(ConsoleConsts.UNINSTALL_PE_FIXLET_ID.text, ConsoleConsts.BIGFIX_SERVER_URI.text);
    }

    @Step("Plan engine should be up and running")
    public void gotoGetStartedPage() throws InterruptedException, IOException, SAXException, ParserConfigurationException {
       System.out.println("Gauge framework test...");
       ApiUtils util = new ApiUtils();
       util.installAutomationPlanEngine(ConsoleConsts.INSTALL_PE_FIXLET_ID.text, ConsoleConsts.BIGFIX_SERVER_URI.text);

       /* Verify plan engine is running
       Commonfunctions comnfunc = new Commonfunctions();
       String s =comnfunc.verifyFixletIsRunning("1", "553176257"); */
    }
}