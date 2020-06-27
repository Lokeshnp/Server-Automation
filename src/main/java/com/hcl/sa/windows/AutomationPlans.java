package com.hcl.sa.windows;

import com.hcl.sa.constants.ConsoleConsts;
import com.hcl.sa.constants.WinAppConsts;
import com.hcl.sa.objectRepository.AutomationPlanLocators;
import com.hcl.sa.utils.SuperClass;
import com.hcl.sa.utils.WinAppDriverActions;
import io.appium.java_client.windows.WindowsDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.List;

public class AutomationPlans implements AutomationPlanLocators {

    WindowsDriver winDriver = SuperClass.getInstance().getWinAppDriver(ConsoleConsts.CONSOLE_EXE_PATH.text);
    WinAppDriverActions winActions = new WinAppDriverActions(winDriver);
    private Logger logger = LogManager.getLogger(AutomationPlans.class);

    public void clickCreateBtn(){
        WebElement createBtn = winActions.findElementByXpath(create_btn_using_xpath);
        winActions.waitForElementVisibilityAndClick(createBtn, 20);
        logger.info("create button clicked");
    }

    public void enterPlanName(String planName){
        logger.debug("Creating plan by the name="+planName);
        WebElement nameField = winActions.findElementByAccessibilityId(name_field_using_accessbilityID);
        nameField.sendKeys(planName);
        logger.info("Plan name entered");
    }

    public void clickAddStepBtn(){
        WebElement stepBtn = winActions.findElementByName(steps_tab_using_name);
        stepBtn.click();
        WebElement addStepBtn = winActions.findElementByName(add_step_using_name);
        addStepBtn.click();
        logger.info("Add step button clicked");
    }

    public void addStepToPlan(String filter){
        searchUsingName(filter);
        List<WebElement> result = winActions.findElementsByXpath("//Table//DataItem[contains(@Name,'"+filter+"')]");
        logger.debug("No of results="+result.size());
        Actions act = new Actions(winDriver);
        for(int i = 0 ; i < result.size() ; i ++){
            act.sendKeys(Keys.CONTROL).build().perform();
            result.get(i).click();
            winActions.hardWait(500);
            act.keyUp(Keys.CONTROL).build().perform();
        }
        winActions.moveToWebElementAndClick(winActions.findElementByXpath(add_button_using_xpath));
        act.release().perform();
    }

    public void savePlan(){
        winActions.findElementByName(save_btn_using_name).click();
        winActions.findElementByName(ok_btn_using_name).click();
        logger.info("Plan is saved");
    }

    public void searchPlan(String planName){
        List<WebElement> editFields =winActions.findElementsByXpath(edit_field_using_xpath);
        editFields.get(0).sendKeys(planName);
    }

    public void getPlanID(String planName){
        List<WebElement> plan = winActions.findElementsByXpath("(//Table//DataItem[@Name='"+planName+"']/preceding-sibling::*)[1]");
        String planID = plan.get(0).getText();
        logger.debug("planID="+planID);
    }
    public void searchUsingName(String name){
        logger.debug("searching the step by name="+name);
        List<WebElement> comboBox = winActions.findElementsByXpath(add_step_combobox);
        for(int i = 0 ; i < comboBox.size() ; i ++){
            String dropBoxName = comboBox.get(i).getAttribute(WinAppConsts.ATTR_VALUE.value);
            if(dropBoxName.equalsIgnoreCase(WinAppConsts.ATTR_NAME.value.toLowerCase())) {
                comboBox.get(i).click();
                winActions.pressKeyboardKey(1,Keys.ARROW_DOWN);
                winActions.pressKeyboardKey(1,Keys.TAB);
                logger.info("Name or description filter is selected");
                break;
            }
        }
        List<WebElement> editFields =winActions.findElementsByXpath(edit_field_using_xpath);
        editFields.get(1).sendKeys(name);
        WebElement filterSearchBtn = winActions.findElementByAccessibilityId(add_step_search_btn_using_xpath);
        filterSearchBtn.click();
        logger.info("Search completed");
    }

    public void createPlan(String planName,String filter){
        logger.info("Plan creation in progress...");
        clickCreateBtn();
        enterPlanName(planName);
        clickAddStepBtn();
        addStepToPlan(filter);
        savePlan();
        getPlanID(planName);
        logger.info("Plan Created");
    }
}
