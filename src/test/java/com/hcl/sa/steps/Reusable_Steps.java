package com.hcl.sa.steps;

import com.hcl.sa.utils.bigfix.Login;
import com.hcl.sa.windows.AllContent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Reusable_Steps {


    protected Login login = new Login();
    protected AllContent allContent = new AllContent();
    private final Logger logger = LogManager.getLogger(Reusable_Steps.class);
//    @Step("Login to Bigfix console")
//    public void consoleLogin() {
//        login.consoleLogin(Credentials.CONSOLE);
//        logger.info("Login to Console is successful");
//    }
//
//    @Step("Navigate to Automation Plan Dashboard")
//    public void openAutomationPlanDashboard() {
//        allContent.openAutomationPlanDashboard();
//        logger.info("User is able to open automation plan dashboard");
//    }
}