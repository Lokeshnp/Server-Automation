package com.hcl.sa.windows;


import com.google.gson.JsonObject;
import com.hcl.sa.constants.*;
import com.hcl.sa.objectRepository.AutomationPlanLocators;
import com.hcl.sa.utils.*;
import io.appium.java_client.windows.WindowsDriver;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AutomationPlans implements AutomationPlanLocators {
    WindowsDriver winDriver = SuperClass.getInstance().getWinAppDriver(ConsoleConsts.CONSOLE_EXE_PATH.text);
    WinAppDriverActions winActions = new WinAppDriverActions(winDriver);
    private final Logger logger = LogManager.getLogger(AutomationPlans.class);
    ApiRequests apiRequests = new ApiRequests();
    JsonParser jsonParser = new JsonParser();
    XMLParser xmlParser = new XMLParser();
    JsonObject planConsoleApiObject = jsonParser.getPlanConsoleApiObject();
    JsonObject consoleApiObject = jsonParser.getConsoleApiObject();
    ConsoleActions consoleActions = new ConsoleActions();
    CommonFunctions commonFunctions = new CommonFunctions();

    public void clickCreateBtn(){
        WebElement createBtn = winActions.winDriver.findElementByAccessibilityId(create_btn_access_id);
        winActions.waitForElementVisibilityAndClick(createBtn, TimeOutConsts.WAIT_20_SECOND.seconds);
    }

    public void enterPlanName(String planName){
        logger.debug("Creating plan by the name="+planName);
        WebElement nameField = winActions.findElementByAccessibilityId(name_field_using_accessbilityID);
        nameField.sendKeys(planName);
    }

    public void clickAddStepBtn(){
        WebElement stepsTab = winActions.findElementByName(steps_tab_using_name);
        stepsTab.click();
        WebElement addStepBtn = winActions.findElementByName(add_step_using_name);
        addStepBtn.click();
    }

    public void addStepToPlan(String fixletID,String fixletName){
        searchUsingFilter(fixletID,fixletName);
        //TODO REMOVE LATER IF BELOW CODE WORKS
//        List<WebElement> result = winActions.findElementByName(fixletName);
//        logger.debug("No of results="+result.size());
//        Actions act = new Actions(winDriver);
//        for(int i = 0 ; i < result.size() ; i ++){
//            if(result.get(i).getAttribute(ConsoleConsts.LOCALIZED_CONTROL_TYPE.text).equalsIgnoreCase("item")){
//                act.sendKeys(Keys.CONTROL).build().perform();
//                result.get(i).click();
//                winActions.hardWait(500);
//                act.keyUp(Keys.CONTROL).build().perform();
//            }
//        }
        winActions.findElementByName(fixletName).click();
        winActions.pressKeyboardKey(1, Keys.TAB);
        winActions.pressKeyboardKey(1,Keys.ENTER);
//        act.release().perform();
        logger.info("Step with name:"+fixletName+" is added to plan");
    }

    public void savePlan(){
        winActions.findElementByName(save_btn_using_name).click();
        winActions.findElementByName(ok_btn_using_name).click();
    }

    public void searchPlan(String planName){
//        List<WebElement> editFields =winActions.findElementsByXpath(edit_field_using_xpath);
//        editFields.get(0).sendKeys(planName);
        WebElement searchPlanTextBox = winActions.findElementByAccessibilityId(search_plan_text_box_access_id);
        searchPlanTextBox.sendKeys(planName);
    }

    public void getPlanID(String planName){
        winActions.hardWait(3000);
        List<WebElement> plan = winActions.findElementsByXpath("(//Table//DataItem[@Name='"+planName+"']/preceding-sibling::*)[1]");
        String planID = plan.get(0).getText();
        SuperClass.specStore.put(CreatePlanConsts.PLAN_ID,planID);
        logger.debug("planID="+planID);
    }
    public void searchUsingFilter(String fixletID,String fixlelName){
        winActions.findElementByName("+").click();
        List<WebElement> comboBox = winActions.findElementsByTagName(add_step_combobox_tagname);
        for(int i = 0 ; i < comboBox.size() ; i ++){
            String dropBoxName = comboBox.get(i).getAttribute(WinAppConsts.ATTR_VALUE.value);
            if(dropBoxName.equalsIgnoreCase(ConsoleConsts.ACTION.text.toLowerCase())) {
                comboBox.get(i).click();
                winActions.pressKeyboardKey(4,Keys.ARROW_DOWN);
                winActions.pressKeyboardKey(1,Keys.TAB);
                logger.info("Name or description filter is selected");
                break;
            }
        }
        winActions.findElementByAccessibilityId(name_filter_text_box_access_id).sendKeys(fixlelName);
        List<WebElement> editFields =winActions.findElementsByTagName(id_filter_text_box_tagName);
        editFields.get(2).sendKeys(fixletID);
        WebElement filterSearchBtn = winActions.findElementByAccessibilityId(add_step_search_btn_using_xpath);
        filterSearchBtn.click();
        logger.info("Search completed");
    }


    public String createPlan(String planName, HashMap<String,String> fixletDetails){
        logger.info("Plan creation in progress...");
        clickCreateBtn();
        enterPlanName(planName);
        for(Map.Entry<String,String> fixletDetail : fixletDetails.entrySet()){
            clickAddStepBtn();
            addStepToPlan(fixletDetail.getValue(), fixletDetail.getKey().split(".bes")[0]);
        }
        savePlan();
        searchPlan(planName);
        getPlanID(planName);
        logger.info("Plan Created");
        return planName;
    }

    public void clickDefaultSettings(){
        WebElement ele = winActions.findElementByXpath(default_settings_btn_using_name);
        winActions.waitForElementVisibility(ele,10);
        ele.click();
    }



    public void addCompToSelectedTarget(String compName){
        WebElement searchBtn = winActions.findElementByAccessibilityId(default_settings_search_btn_auto_id);
        winActions.waitForElementVisibilityAndClick(searchBtn,10);
        winActions.findElementByXpath("//Table//DataItem[contains(@Name,'"+compName+"')]").click();
        winActions.findElementByName(default_settings_move_btn_name).click();
        winActions.findElementByName(default_settings_OK_btn_name).click();
        logger.info("Selected computer= "+compName+" is added to select target window");
    }

    public void clickTakeAction(String planName){
        searchPlan(planName);
//        winActions.hardWait(2000); //sync issue
        winActions.waitForListVisibility(winActions.findElementsByName(planName),10);
        winActions.findElementByName(planName).click();
//        List<WebElement> plan = winActions.findElementsByXpath("(//DataItem[@Name='"+planName+"']/preceding-sibling::*)[1]");
//        plan.get(0).click();
        winActions.findElementByName(take_action_using_name).click();
        logger.info("User clicked on take action button");
    }

    public void verifyDefaultSettings(){
        //TODO REMOVE LATER AFTER PLAN ENGINE IS INSTALLED PROPERLY
        winActions.hardWait(5000);
        List<WebElement> computerList = winActions.findElementsByXpath("//*[normalize-space(@Name)='Computers']/following-sibling::*");
        Assert.assertEquals(ConsoleConsts.SERVER_99_COMP_NAME.text,computerList.get(0).getAttribute(WinAppConsts.ATTR_NAME.value).trim());
    }

    public Response getPlanXml(String planID) {
        String uri = jsonParser.getUriToFetchPlanXml(planConsoleApiObject);
        HashMap<String, String> params = new HashMap<>();
        params.put(CreatePlanConsts.PLAN_ID.text, planID);
        RequestSpecification requestSpecification = apiRequests.setWasLibertyURIAndBasicAuthentication().and().pathParams(params);
        Response response = apiRequests.GET(requestSpecification, uri);
        return response;
    }
    public String getApplicableComputerName(String fixletID) throws IOException, SAXException {
        String computerName = null;
        List<String> computerIDs = getApplicableComputersID_For_Combination(fixletID);
        int computerIDsSize = computerIDs.size();
        for (int i = 0; i < computerIDsSize; i++) {
            String uri = jsonParser.getUriToFetchCompProperties(consoleApiObject);
            Response response = apiRequests.GET(ConsoleConsts.COMPUTER_ID.text, computerIDs.get(i), uri);
            computerName = xmlParser.getElementOfXmlByXpath(response.asInputStream(), XMLConsts.COMPUTER_NAME.text).get(0);
        }
        logger.debug("Computer ID for the fixlet ID " + fixletID + " is " + computerName);
        SuperClass.specStore.put(XMLConsts.COMPUTER_NAME, computerName);
        return computerName;
    }
    public List<String> getApplicableComputersID(String fixletID) throws IOException, SAXException {
        List<String> compIDsList = new ArrayList<String>();
        Response response = consoleActions.getRelevantComputers(commonFunctions.commonParams(ConsoleConsts.CUSTOM.text, ConsoleConsts.POOJA.text, fixletID));
        logger.debug("Relevant Machines = " + response.asString());
        List<String> computersList = xmlParser.getElementOfXmlByXpath(response.asInputStream(), consoleActions.COMP_ATTR_XPATH);
        computersList.forEach(computer -> {
            String computerID = commonFunctions.filterData(ConsoleConsts.COMPUTER_ID_REGEX.text, computer);
            compIDsList.add(computerID.split("/")[1]);
        });
        logger.debug("Computer ID's List = " + compIDsList);
        return compIDsList;
    }

    public void executePlan(String planID, HashMap<String, String> fixletDetails) throws IOException, SAXException, TransformerException {
        Response response = getPlanXml(planID);
        logger.debug("Initiated the execute plan" + response.toString());
        XMLParser xmlParser = new XMLParser();
        Document doc = xmlParser.buildDocument(response.asInputStream());
        NodeList nodes = doc.getElementsByTagName("step");
        Assert.assertEquals("step and fixlet details length is different", fixletDetails.size(), nodes.getLength());
        int count = 0;
        for (Map.Entry<String, String> map : fixletDetails.entrySet()) {
            Node node = nodes.item(count);
            Element element = (Element) node;
            String fixletID = map.getValue();
            String computerName = getApplicableComputerName(fixletID);
            Element newElement = doc.createElement("target-set");
            Element computer = doc.createElement("computer");
            computer.setAttribute("name", computerName);
            newElement.appendChild(computer);
            element.appendChild(newElement);
            count++;
        }
        String actionBody = xmlParser.convertDocToString(doc);
        String uri = jsonParser.getUriToInitiatePlanExecutionAction(planConsoleApiObject);
        HashMap<String, String> params = new HashMap<>();
        params.put(CreatePlanConsts.PLAN_ID.text, planID);
        RequestSpecification requestSpecification = apiRequests.setWasLibertyURIAndBasicAuthentication().contentType(ContentType.JSON).and().accept(ContentType.ANY)
                .and().pathParam(CreatePlanConsts.PLAN_ID.text, params.get(CreatePlanConsts.PLAN_ID.text)).and().body(actionBody);
        Response postResponse = apiRequests.POST(requestSpecification, uri);
        String planActionID = postResponse.getBody().asString();
        SuperClass.specStore.put(CreatePlanConsts.PLAN_ACTION_ID, planActionID);
        logger.debug("Response after initiating the action: \n" + planActionID);
    }
    public String waitTillActionIsStopped(String actionID) throws IOException, SAXException {
        String status;
        Boolean isStopped;
        logger.info("Waiting till the plan action state becomes stopped");
        do {
            Response response = getPlanActionStatus(actionID);
            status = xmlParser.getElementOfXmlByXpath(response.asInputStream(), consoleActions.STATUS).get(0);
            isStopped = (status.contains("Stopped"));
        } while (!(isStopped));
        logger.debug("Action Status = " + status);
        return status;
    }

    public Response getPlanActionStatus(String actionID) {
        String uri = jsonParser.getUriToFetchActionStatus(consoleApiObject);
        Response response = apiRequests.GET(ConsoleConsts.ACTION_ID.text, actionID, uri);
        return response;
    }

    public void deletePlan(String fixletID) {
        String uri = jsonParser.getUriToDeletePlan(consoleApiObject);
        Response response = apiRequests.DELETE(ConsoleConsts.FIXLET_ID.text, fixletID, uri);
        logger.debug("Response after deleting the plan = " + response.asString());
    }
    public List<String> getApplicableComputersID_For_Combination(String fixletID) throws IOException, SAXException {
        List<String> compIDsList = new ArrayList<String>();
        Response response = consoleActions.getRelevantComputers(commonFunctions.commonParams(ConsoleConsts.CUSTOM.text, ConsoleConsts.LOKESH.text, fixletID));
        logger.debug("Relevant Machines = " + response.asString());
        List<String> computersList = xmlParser.getElementOfXmlByXpath(response.asInputStream(), consoleActions.COMP_ATTR_XPATH);
        computersList.forEach(computer -> {
            String computerID = commonFunctions.filterData(ConsoleConsts.COMPUTER_ID_REGEX.text, computer);
            compIDsList.add(computerID.split("/")[1]);
        });
        logger.debug("Computer ID's List = " + compIDsList);
        return compIDsList;
    }


}