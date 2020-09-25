package com.hcl.sa.windows;

import com.google.gson.JsonObject;
import com.hcl.sa.constants.*;
import com.hcl.sa.objectRepository.AutomationPlanLocators;
import com.hcl.sa.utils.api.ApiRequests;
import com.hcl.sa.utils.bigfix.CommonFunctions;
import com.hcl.sa.utils.bigfix.ConsoleActions;
import com.hcl.sa.utils.bigfix.FixletDetails;
import com.hcl.sa.utils.bigfix.SuperClass;
import com.hcl.sa.utils.parser.JsonParser;
import com.hcl.sa.utils.parser.XMLParser;
import com.hcl.sa.utils.winappdriver.WinAppDriverActions;
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

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
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
    public JsonObject saRestConsoleApiObject = jsonParser.getSaRestPlanConsoleApiObject();
    JsonObject consoleApiObject = jsonParser.getConsoleApiObject();
    CommonFunctions commonFunctions = new CommonFunctions();
    ConsoleActions consoleActions=new ConsoleActions();


    public void clickCreateBtn() {
        WebElement createBtn = winActions.winDriver.findElementByAccessibilityId(create_btn_access_id);
        int seconds = (int)commonFunctions.convertToMilliSeconds(TimeOutConsts.WAIT_3_SEC.seconds);
        winActions.hardWait(seconds);
        winActions.waitForElementVisibilityAndClick(createBtn, TimeOutConsts.WAIT_10_SECONDS.seconds);
    }

    public void enterPlanName(String planName) {
        logger.debug("Creating plan by the name=" + planName);
        WebElement nameField = winActions.findElementByAccessibilityId(name_field_using_accessbilityID);
        nameField.sendKeys(planName);
        logger.info("Plan name entered");
    }

    public void selectSiteName(String siteName) {
        WebElement comboBox = winActions.findElementByName(ConsoleConsts.MASTER_ACTION_SITE.text);
        comboBox.click();
        winActions.findElementByName(siteName).click();
        logger.info("selected site name clicked");
    }

    public void clickAddStepBtn() {
        WebElement stepBtn = winActions.findElementByName(steps_tab_using_name);
        stepBtn.click();
        WebElement addStepBtn = winActions.findElementByName(add_step_using_name);
        addStepBtn.click();
        logger.info("Add step button clicked");
    }

    public void addStepToPlan(String fixletID, String fixletName) {
        searchUsingFilter(fixletID, fixletName);
        winActions.findElementByName(fixletName).click();
        winActions.pressKeyboardKey(1, Keys.TAB);
        winActions.pressKeyboardKey(1, Keys.ENTER);
        logger.info("Step with name:" + fixletName + " is added to plan");
    }


    public void savePlan() {
        winActions.findElementByName(save_btn_using_name).click();
        winActions.findElementByName(ok_btn_using_name).click();
        logger.info("Plan is saved");
    }

    public void searchPlan(String planName) {
        WebElement searchPlanTextBox = winActions.findElementByAccessibilityId(search_plan_text_box_access_id);
        searchPlanTextBox.clear();
        searchPlanTextBox.sendKeys(planName);
        winActions.findElementByName(planName).click();
    }

    public String getPlanID(String planName) {
        searchPlan(planName);
        winActions.hardWait(6000); //sync issue
        List<WebElement> plan = winActions.findElementsByXpath("(//Table//DataItem[@Name='" + planName + "']/preceding-sibling::*)[1]");
        String planID = plan.get(0).getText();
        logger.debug("planID=" + planID);
        return planID;
    }

    public void searchUsingFilter(String fixletID, String fixlelName) {
        winActions.findElementByName("+").click();
        List<WebElement> comboBox = winActions.findElementsByTagName(add_step_combobox_tagname);
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
        winActions.findElementByAccessibilityId(name_filter_text_box_access_id).sendKeys(fixlelName);
        List<WebElement> editFields = winActions.findElementsByTagName(id_filter_text_box_tagName);
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
            addStepToPlan(fixletDetail.getValue(), fixletName);
        }
        savePlan();
        String planID = getPlanID(planName);
        SuperClass.specStore.put(CreatePlanConsts.PLAN_ID, planID);
        logger.info("Plan Created");
        return planID;
    }

    public String createPlan(String planName, HashMap<String, String> fixletDetails, String siteName) {
        logger.info("Plan creation in progress...");
        clickCreateBtn();
        enterPlanName(planName);
        selectSiteName(siteName);
        for (Map.Entry<String, String> fixletDetail : fixletDetails.entrySet()) {
            clickAddStepBtn();
            String fixletName = fixletDetail.getKey().split(".bes")[0].trim();
            addStepToPlan(fixletDetail.getValue(), fixletName);
        }
        savePlan();
        String planID = getPlanID(planName);
        SuperClass.specStore.put(CreatePlanConsts.PLAN_ID, planID);
        logger.info("Plan Created");
        return planID;
    }

    public Response getPlanXml(String planID) {
        String uri = jsonParser.getUriToFetchPlanXml(planConsoleApiObject);
        HashMap<String, String> params = new HashMap<>();
        params.put(CreatePlanConsts.PLAN_ID.text, planID);
        RequestSpecification requestSpecification = apiRequests.setWasLibertyURIAndBasicAuthentication().and().pathParams(params);
        Response response = apiRequests.GET(requestSpecification, uri);
        return response;
    }

    public Response getPlanXml(RequestSpecification requestSpecification) {
        String uri = jsonParser.getUriToFetchPlanXml(saRestConsoleApiObject);
        System.out.println("Uri="+uri);
        Response response = apiRequests.GET(requestSpecification, uri);
        return response;
    }

    public Response getPlanXml(RequestSpecification requestSpecification,String uri) {
        Response response = apiRequests.GET(requestSpecification, uri);
        return response;
    }

    public void executePlan(String planID, HashMap<String, String> fixletDetails) throws Exception {
        HashMap<String, String> params = new HashMap<>();
        Response response = getPlanXml(planID);
        logger.debug("Initiated the execute plan" + response.toString());
        String actionBody= modifyPlanDefXmlTemplate(fixletDetails, response);
        String uri = jsonParser.getUriToTakeActionOnPlan(planConsoleApiObject);
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
            computerName = xmlParser.getElementOfXmlByXpath(response.asInputStream(), consoleActions.COMPUTER_NAME).get(0);
        }
        logger.debug("Computer ID for the fixlet ID " + fixletID + " is " + computerName);
        SuperClass.specStore.put(consoleActions.COMPUTER_NAME, computerName);
        return computerName;
    }

    public Integer waitUntilApplicableCompsVisible(String fixletID) throws Exception {
        int computerTagCount;
        long startTime = System.currentTimeMillis();
        boolean waitUntillApplicableCompsDisp = true;
        do {
            Response relevantMachines = consoleActions.getApplicableComputersApiResponse(commonFunctions.commonParams(ConsoleConsts.CUSTOM.text, ConsoleConsts.POOJA.text, fixletID));
            computerTagCount = xmlParser.getElementsByTagName(relevantMachines.asInputStream(), XMLConsts.COMPUTER_TAGNAME.text).getLength();
            waitUntillApplicableCompsDisp = (System.currentTimeMillis() - startTime) < commonFunctions.convertToMilliSeconds(TimeOutConsts.WAIT_60_SECOND.seconds);
            if (!waitUntillApplicableCompsDisp) {
                logger.info("Applicable computers are zero for this fixlet ");
                throw new Exception("appicable computers are not visisble");
            }
        } while (computerTagCount == 0);
        return computerTagCount;
    }

    public List<String> getApplicableComputersID(String fixletID) throws IOException, SAXException {
        List<String> compIDsList = new ArrayList<String>();
        Response response = consoleActions.getApplicableComputersApiResponse(commonFunctions.commonParams(ConsoleConsts.CUSTOM.text, ConsoleConsts.POOJA.text, fixletID));
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
            Response response = consoleActions.getActionStatus(actionID);
            status = xmlParser.getElementOfXmlByXpath(response.asInputStream(), consoleActions.STATUS).get(0);
            isStopped = (status.toString().contains("Stopped"));
        } while (!(isStopped));
        logger.debug("Action Status = " + status);
        return status;
    }

    public void deletePlan(String fixletID) {
        String uri = jsonParser.getUriToDeletePlan(consoleApiObject);
        Response response = apiRequests.DELETE(ConsoleConsts.FIXLET_ID.text, fixletID, uri);
        logger.debug("Response after deleting the plan = " + response.asString());
    }

    public String modifyPlanDefXmlTemplate(HashMap<String, String> fixletDetails, Response response) throws Exception {
        XMLParser xmlParser = new XMLParser();
        Document doc = xmlParser.buildDocument(response.asInputStream());
        NodeList nodes = doc.getElementsByTagName(XMLConsts.STEP.text);
        Assert.assertEquals("step and fixlet details length is different", fixletDetails.size(), nodes.getLength());
        int count = 0;
        for (Map.Entry<String, String> map : fixletDetails.entrySet()) {
            Node node = nodes.item(count);
            Element element = (Element) node;
            String fixletID = map.getValue();
            Integer computerTagCount = waitUntilApplicableCompsVisible(fixletID);
            String computerName = null;
            if (computerTagCount == 1) {
                //TODO : Later try to use dynamic method from ConsoleActions
                computerName = getApplicableComputerName(fixletID);
            }
            Element targetElement = doc.createElement(XMLConsts.TARGET_SET.text);
            Element computer = doc.createElement(XMLConsts.COMPUTER.text);
            computer.setAttribute(XMLConsts.NAME.text, computerName);
            targetElement.appendChild(computer);
            element.appendChild(targetElement);
            count++;
        }
        String actionBody = xmlParser.convertDocToString(doc);
        return actionBody;
    }

    public FixletDetails getPeInstallFixletDetails(String fixletID) {
        return new FixletDetails.Builder().setSiteName(ConsoleConsts.SITE_NAME.text)
                .setSiteType(ConsoleConsts.SITE_TYPE.text)
                .setFixletID(fixletID)
                .build();

    }

}