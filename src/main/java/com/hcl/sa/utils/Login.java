package com.hcl.sa.utils;

import com.hcl.sa.constants.ConsoleConsts;
import com.hcl.sa.constants.TimeOutConsts;
import com.hcl.sa.objectRepository.ConsoleLoginLocators;
import io.appium.java_client.windows.WindowsDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;


public class Login {

    WindowsDriver winDriver = SuperClass.getInstance().getWinAppDriver(ConsoleConsts.CONSOLE_EXE_PATH.text);
    WinAppDriverActions winActions = new WinAppDriverActions(winDriver);
    Logger logger = LogManager.getLogger(Login.class);

    public void enterServerIP(String serverIP) {
        logger.debug("Entering server ip=" + serverIP);
        WebElement serverNameField = winActions.findElementByXpath(ConsoleLoginLocators.server_ip_edit_xpath);
        winActions.waitForElementVisibilityAndClick(serverNameField, TimeOutConsts.WAIT_20_SECOND.seconds);
        serverNameField.clear();
        serverNameField.sendKeys(serverIP);
    }

    public void enterUsername(String username) {
        logger.debug("entering username=" + username);
        WebElement userName = winActions.findElementByXpath(ConsoleLoginLocators.username_xpath);
        winActions.waitForElementVisibilityAndClick(userName, TimeOutConsts.WAIT_20_SECOND.seconds);
        userName.clear();
        userName.sendKeys(username);
    }

    public void enterPwd(String password) {
        logger.debug("entering password=" + password);
        WebElement passwordField = winActions.findElementByXpath(ConsoleLoginLocators.password_xpath);
        passwordField.click();
        passwordField.clear();
        passwordField.sendKeys(password);
    }

    public void clickOkBtn() {
        winActions.findElementByXpath(ConsoleLoginLocators.ok_btn_xpath).click();
        logger.info("CLicked on ok button");
        winActions.waitForNoOfWindowsToBe(1, TimeOutConsts.WAIT_60_SECOND.seconds);
        winDriver.switchTo().window(winActions.getChildWindowsHandle().iterator().next());
    }

    public void consoleLogin(Credentials credentials) {
        enterServerIP(credentials.getServerIP());
        enterUsername(credentials.getUsername());
        enterPwd(credentials.getPassword());
        clickOkBtn();
        logger.info("User is logged into Big fix console");
    }

    public void closeBigFixConsole() {
        winActions.findElementByName(ConsoleLoginLocators.close_btn_name).click();
        logger.info("Big Fix Console is closed");
    }

}
