package com.hcl.sa.windows;

import com.google.gson.JsonObject;
import com.hcl.sa.constants.ConsoleConsts;
import com.hcl.sa.constants.CreatePlanConsts;
import com.hcl.sa.constants.WinAppConsts;
import com.hcl.sa.constants.XMLConsts;
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
import org.openqa.selenium.interactions.Actions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AutomationPlans implements AutomationPlanLocators {

    WindowsDriver winDriver = SuperClass.getInstance().getWinAppDriver(ConsoleConsts.CONSOLE_EXE_PATH.text);
    WinAppDriverActions winActions = new WinAppDriverActions(winDriver);
    private Logger logger = LogManager.getLogger(AutomationPlans.class);
    ApiRequests apiRequests = new ApiRequests();
    JsonParser jsonParser = new JsonParser();
    XMLParser xmlParser = new XMLParser();
    JsonObject planConsoleApiObject = jsonParser.getPlanConsoleApiObject();
    JsonObject consoleApiObject = jsonParser.getConsoleApiObject();
    ConsoleActions consoleActions = new ConsoleActions();
    CommonFunctions commonFunctions = new CommonFunctions();

    public void clickCreateBtn() {
        WebElement createBtn = winActions.findElementByXpath(create_btn_using_xpath);
        winActions.waitForElementVisibilityAndClick(createBtn, 20);
        logger.info("create button clicked");
    }

    public void enterPlanName(String planName) {
        logger.debug("Creating plan by the name=" + planName);
        WebElement nameField = winActions.findElementByAccessibilityId(name_field_using_accessbilityID);
        nameField.sendKeys(planName);
        logger.info("Plan name entered");
    }

    public void clickAddStepBtn() {
        WebElement stepBtn = winActions.findElementByName(steps_tab_using_name);
        stepBtn.click();
        WebElement addStepBtn = winActions.findElementByName(add_step_using_name);
        addStepBtn.click();
        logger.info("Add step button clicked");
    }

    public void addStepToPlan(String filter, String stepName) {
        searchUsingFilter(filter, stepName);
        List<WebElement> result = winActions.findElementsByXpath("//Table//DataItem[contains(@Name,'" + stepName + "')]");
        logger.debug("No of results=" + result.size());
        Actions act = new Actions(winDriver);
        for (int i = 0; i < result.size(); i++) {
            act.sendKeys(Keys.CONTROL).build().perform();
            result.get(i).click();
            winActions.hardWait(500);
            act.keyUp(Keys.CONTROL).build().perform();
        }
        winActions.moveToWebElementAndClick(winActions.findElementByXpath(add_button_using_xpath));
        act.release().perform();
    }

    public void savePlan() {
        winActions.findElementByName(save_btn_using_name).click();
        winActions.findElementByName(ok_btn_using_name).click();
        logger.info("Plan is saved");
    }

    public void searchPlan(String planName) {
        List<WebElement> editFields = winActions.findElementsByXpath(edit_field_using_xpath);
        editFields.get(0).clear();
        editFields.get(0).sendKeys(planName);
    }

    public String getPlanID(String planName) {
        searchPlan(planName);
        winActions.hardWait(6000); //sync issue
        List<WebElement> plan = winActions.findElementsByXpath("(//Table//DataItem[@Name='" + planName + "']/preceding-sibling::*)[1]");
        String planID = plan.get(0).getText();
        logger.debug("planID=" + planID);
        return planID;
    }

    public void searchUsingFilter(String fixletName, String fixletID) {
        winActions.findElementByName("+").click();
        List<WebElement> comboBox = winActions.findElementsByXpath(add_step_combobox);
        for (int i = 0; i < comboBox.size(); i++) {
            String dropBoxName = comboBox.get(i).getAttribute(WinAppConsts.ATTR_VALUE.value);
            if (dropBoxName.equalsIgnoreCase(ConsoleConsts.ACTION.text.toLowerCase())) {
                comboBox.get(i).click();
                winActions.pressKeyboardKey(4, Keys.ARROW_DOWN);
                winActions.pressKeyboardKey(1, Keys.TAB);
                logger.info("Name or description filter is selected");
                break;
            }
        }
        List<WebElement> editFields = winActions.findElementsByXpath(edit_field_using_xpath);
        editFields.get(1).sendKeys(fixletName);
        editFields.get(2).sendKeys(fixletID);
        WebElement filterSearchBtn = winActions.findElementByAccessibilityId(add_step_search_btn_using_xpath);
        filterSearchBtn.click();
        logger.info("Search completed");
    }

    public String createPlan(String planName, HashMap<String, String> fixletDetails) {
        logger.info("Plan creation in progress...");
        clickCreateBtn();
        enterPlanName(planName);
        for (Map.Entry<String, String> fixletDetail : fixletDetails.entrySet()) {
            clickAddStepBtn();
            String fixletName = fixletDetail.getKey().split(".bes")[0].trim();
            addStepToPlan(fixletName, fixletDetail.getValue());
        }
        savePlan();
        String planActionId = getPlanID(planName);
        logger.info("Plan Created");
        return planActionId;
    }

    public Response getPlanXml(String planID) {
        String uri = jsonParser.getUriToFetchPlanXml(planConsoleApiObject);
        HashMap<String, String> params = new HashMap<>();
        params.put(CreatePlanConsts.PLAN_ID.text, planID);
        RequestSpecification requestSpecification = apiRequests.setWasLibertyURIAndBasicAuthentication().and().pathParams(params);
        Response response = apiRequests.GET(requestSpecification, uri);
        return response;
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

    public String getApplicableComputerName(String fixletID) throws IOException, SAXException {
        String computerName = null;
        List<String> computerIDs = getApplicableComputersID(fixletID);
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

    public String waitTillActionIsStopped(String actionID) throws IOException, SAXException {
        String status;
        Boolean isStopped;
        logger.info("Waiting till the plan action state becomes stopped");
        do {
            Response response = getPlanActionStatus(actionID);
            status = xmlParser.getElementOfXmlByXpath(response.asInputStream(), consoleActions.STATUS).get(0);
            isStopped = (status.toString().contains("Stopped"));
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
}