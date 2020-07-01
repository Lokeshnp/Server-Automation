package com.hcl.sa.windows;

import com.hcl.sa.constants.ConsoleConsts;
import com.hcl.sa.constants.CreatePlanConsts;
import com.hcl.sa.constants.WinAppConsts;
import com.hcl.sa.objectRepository.AllContentsLocators;
import com.hcl.sa.utils.SuperClass;
import com.hcl.sa.utils.WinAppDriverActions;
import io.appium.java_client.windows.WindowsDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.List;

public class AllContent {

    Logger logger = LogManager.getLogger(AllContent.class);

    WindowsDriver winDriver = SuperClass.getInstance().getWinAppDriver(ConsoleConsts.CONSOLE_EXE_PATH.text);
    WinAppDriverActions winActions = new WinAppDriverActions(winDriver);

    public void clickOnAllContentWin() {
        WebElement allContentWindow = winActions.findElementByClassName(AllContentsLocators.all_content_class_name);
        Actions actions = winActions.moveToWebElement(allContentWindow).moveByOffset(0, 80).click();
        actions.build().perform();
        actions.release();
        logger.info("Clicked on All content window");
    }

    public void navigateToExternalSiteType() {
        clickOnAllContentWin();
        winActions.pressKeyboardKey(4, Keys.ARROW_DOWN);
        winActions.pressKeyboardKey(1, Keys.ARROW_RIGHT);
        winActions.pressKeyboardKey(2, Keys.ARROW_DOWN);
        winActions.pressKeyboardKey(1, Keys.ENTER);
        logger.info("Navigated to external site type");
    }

    //TODO HERE IT IS GIVING ISSUE IF EXTRA COLUMN IS ADDED SO LATER IT NEEDS TO BE CHANGED WITH BY PRESSING CTRL KEYBOARD SHOURTCUT
    public int getServerAutomationSiteNameIndex() {
        int saIndex = 0;
        List<WebElement> externalSitesList = winActions.findElementsByXpath(AllContentsLocators.external_sites_xpath);
        logger.debug("External Sites List size=" + externalSitesList.size());
        for (int i = 0; i < externalSitesList.size(); i++) {
            String siteName = externalSitesList.get(i).getAttribute(WinAppConsts.ATTR_NAME.value);
            logger.debug("site Names=" + siteName);
            if (siteName.equalsIgnoreCase(CreatePlanConsts.SERVER_AUTOMATION.text)) {
                saIndex = i + 1;
                logger.info("User got the server automation site name index");
                break;
            }
        }
        logger.info("Sever Automation Siten name index: " + saIndex);
        return saIndex;
    }

    public void expandServerAutomationSite() {
        navigateToExternalSiteType();
        int saSiteNameIndex = getServerAutomationSiteNameIndex();
        clickOnAllContentWin();
        winActions.pressKeyboardKey(1, Keys.ARROW_RIGHT);
        winActions.pressKeyboardKey(saSiteNameIndex, Keys.ARROW_DOWN);
        winActions.pressKeyboardKey(1, Keys.ARROW_RIGHT);
        logger.info("user is able to expand server automation site");
    }

    public void expandServerAutomationDashboard() {
        winActions.pressKeyboardKey(7, Keys.ARROW_DOWN);
        winActions.pressKeyboardKey(1, Keys.ARROW_RIGHT);
        logger.info("Server automation expanded");
    }

    public void openAutomationPlanDashboard() {
        expandServerAutomationSite();
        expandServerAutomationDashboard();
        winActions.pressKeyboardKey(2, Keys.ARROW_DOWN);
        winActions.pressKeyboardKey(1, Keys.ENTER);
        logger.info("User is able to open automation plan dashboard");
    }


}
