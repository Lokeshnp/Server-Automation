package com.hcl.sa.utils;

import com.google.gson.JsonObject;
import com.hcl.sa.constants.ConsoleConsts;
import com.hcl.sa.constants.CreatePlanConsts;
import com.hcl.sa.constants.TimeOutConsts;
import com.hcl.sa.constants.XMLConsts;
import com.hcl.sa.objectRepository.XmlLocators;
import com.hcl.sa.windows.AutomationPlans;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ConsoleActions implements XmlLocators {
    private final Logger logger = LogManager.getLogger(ConsoleActions.class);
    ApiRequests apiRequests = new ApiRequests();
    CommonFunctions commonFunctions = new CommonFunctions();
    XMLParser xmlParser = new XMLParser();
    JsonParser jsonParser = new JsonParser();
    Payload payload = new Payload();
    String siteName = jsonParser.getSiteNameObject().get(ConsoleConsts.SERVER_AUTOMATION.text).getAsString();
    JsonObject consoleApiObject = jsonParser.getConsoleApiObject();

    public Response getApplicableComputersApiResponse(HashMap<String, String> params) {
        String uri = jsonParser.getUriToFetchRelevantComputers(consoleApiObject);
        RequestSpecification requestSpecification = apiRequests.setBaseURIAndBasicAuthentication().and().pathParams(params);
        Response response = apiRequests.GET(requestSpecification, uri);
        return response;
    }

    public List<String> getApplicableComputersID(HashMap<String, String> params) throws IOException, SAXException {
        List<String> compIDsList = new ArrayList<String>();
        Response response = getApplicableComputersApiResponse(params);
        logger.debug("Relevant Machines = " + response.asString());
        List<String> computersList = xmlParser.getElementOfXmlByXpath(response.asInputStream(), COMP_ATTR_XPATH);
        computersList.forEach(computer -> {
            String computerID = commonFunctions.filterData(ConsoleConsts.COMPUTER_ID_REGEX.text, computer);
            compIDsList.add(computerID.split("/")[1]);
        });
        logger.debug("Computer ID's List = " + compIDsList);
        return compIDsList;
    }

    public Integer waitUntilApplicableCompsVisible(HashMap<String, String> params) throws IOException, SAXException, ParserConfigurationException {
        int computerTagCount;
        long startTime = System.currentTimeMillis();
        boolean waitUntillApplicableCompsDisp = true;
        do {
            Response relevantMachines = getApplicableComputersApiResponse(params);
            computerTagCount = xmlParser.getElementsByTagName(relevantMachines.asInputStream(), XMLConsts.COMPUTER_TAGNAME.text).getLength();
            waitUntillApplicableCompsDisp = (System.currentTimeMillis() - startTime) < commonFunctions.convertToMilliSeconds(TimeOutConsts.WAIT_60_SECOND.seconds);
            if (!waitUntillApplicableCompsDisp) {
                logger.info("Applicable computers are zero for this fixlet ");
                break;
            }
        } while (computerTagCount == 0);
        return computerTagCount;
    }

    public String getApplicableComputerID(HashMap<String, String> params, String targetVmIpAddress) throws IOException, SAXException {
        String ipAddress = null;
        String computerID = null;
        List<String> computerIDs = getApplicableComputersID(params);
        int computerIDsSize = computerIDs.size();
        for (int i = 0; i < computerIDsSize; i++) {
            String uri = jsonParser.getUriToFetchCompProperties(consoleApiObject);
            Response response = apiRequests.GET(ConsoleConsts.COMPUTER_ID.text, computerIDs.get(i), uri);
            ipAddress = xmlParser.getElementOfXmlByXpath(response.asInputStream(), IP_ADDRESS_XPATH).get(0);

            if (ipAddress.equals(targetVmIpAddress)) {
                computerID = computerIDs.get(i);
                break;
            } else {
                getApplicableComputerID(params, targetVmIpAddress);
            }
        }
        logger.debug("Fixlet with ID " + params.get(ConsoleConsts.FIXLET_ID.text) + " becomes relevant to a machine with IP Address " + ipAddress);
        logger.debug("Computer ID for the fixlet ID " + params.get(ConsoleConsts.FIXLET_ID.text) + " is " + computerID);
        SuperClass.specStore.put(ConsoleConsts.COMPUTER_ID, computerID);
        return computerID;
    }

    public Response getTakeActionApiResponse(String actionBody) {
        Response response = apiRequests.POST(actionBody, jsonParser.getUriToInitiateAction(consoleApiObject));
        logger.debug("Response after initiating the action: \n" + response.asString());
        return response;
    }

    public String getActionID(InputStream inputStream) throws IOException, SAXException {
        String actionID = xmlParser.getElementOfXmlByXpath(inputStream, ACTION_ID_XPATH).get(0);
        logger.debug("Action ID " + actionID);
        return actionID;
    }

    public void takeActionOnRootServer(HashMap<String, String> params) throws IOException, SAXException, ParserConfigurationException, TransformerException {
        Response response = null;
        String targetVmIpAddress = commonFunctions.getIpAddress(ConsoleConsts.BIGFIX_SERVER_URI.text);
        String computerID = getApplicableComputerID(params, targetVmIpAddress);
        Integer computerTagCount = waitUntilApplicableCompsVisible(params);
        String actionID = null;
        if (computerTagCount == 1) {
            String initiateActionPayloadPath = CommonFunctions.getPath(ConsoleConsts.TAKE_ACTION_PAYLOAD_PATH.text);
            String body = payload.createAction(new FileInputStream(initiateActionPayloadPath), params, computerID);
            response = getTakeActionApiResponse(body);
            actionID = getActionID(response.asInputStream());
            waitTillXmlResponseReceived(actionID);
            String actionStatus = waitTillActionIsFixed(actionID);
            verifyActionStatus(params.get(ConsoleConsts.FIXLET_ID.text), actionID, actionStatus, targetVmIpAddress);
        }
    }

    public Response getActionStatus(String actionID) {
        String uri = jsonParser.getUriToFetchActionStatus(consoleApiObject);
        Response response = apiRequests.GET(ConsoleConsts.ACTION_ID.text, actionID, uri);
        return response;
    }

    public void stopAction(String actionID) {
        String uri = jsonParser.getUriToStopAction(consoleApiObject);
        Response response = apiRequests.POST(ConsoleConsts.ACTION_ID.text, actionID, uri);
        logger.debug("Response after stopping the action = " + response.asString());
    }

    public void waitTillXmlResponseReceived(String actionID) throws IOException, SAXException, ParserConfigurationException {
        int computerTagIndex;
        logger.info("Waiting till fixlet gets reported to a machine");
        do {
            Response response = getActionStatus(actionID);
            computerTagIndex = xmlParser.getElementsByTagName(response.asInputStream(), ConsoleConsts.COMPUTER.text).getLength();
        } while (computerTagIndex == 0);
        logger.info("Fixlet got reported to a target machine");
    }

    public String waitTillActionIsFixed(String actionID) throws IOException, SAXException {
        String status;
        Boolean isSuccessful;
        Boolean isRestart;
        Boolean isFailed;
        logger.info("Waiting till the fixlet status becomes fixed or failed or restart");
        do {
            Response response = getActionStatus(actionID);
            status = xmlParser.getElementOfXmlByXpath(response.asInputStream(), COMPUTER_STATUS_XPATH).get(0);
            isSuccessful = (status.toString().contains("successful"));
            isRestart = (status.toString().contains("restart"));
            isFailed = (status.toString().contains("failed"));
        } while (!(isSuccessful || isRestart || isFailed));
        logger.debug("Action Status = " + status);
        return status;
    }

    public void verifyActionStatus(String fixletID, String actionID, String actionStatus, String targetVmIpAddress) {
        if (actionStatus.toString().contains(ConsoleConsts.SUCCESSFUL.text)) {
            stopAction(actionID);
            logger.debug("Stopped an Action for the fixlet ID " + fixletID + " with action ID " + actionID);
        }
        if (actionStatus.toString().contains(ConsoleConsts.RESTART.text)) {
            logger.debug("Restart the target machine " + targetVmIpAddress + " for the fixlet ID " + fixletID + " with action ID " + actionID);
        }
        if (actionStatus.toString().contains(ConsoleConsts.FAILED.text)) {
            logger.debug("Fixlet ID " + fixletID + " with action ID " + actionID + " got failed");
        }
    }

    public void deleteAction(String actionID) {
        String uri = jsonParser.getUriToDeleteAction(consoleApiObject);
        Response response = apiRequests.DELETE(ConsoleConsts.ACTION_ID.text, actionID, uri);
        logger.debug("Response after deleting the action = " + response.asString());
    }

    public HashMap<String, String> importFixlet(String folderPath, RequestSpecification requestSpecification) throws IOException, SAXException {
        HashMap<String, String> fixletDetails = new HashMap<>();
        File[] files = new File(folderPath).listFiles();
        for (File file : files) {
            if (file.isFile()) {
                String fileName = file.getName();
                String apiRequestBody = commonFunctions.readTextFile(folderPath + fileName).toString();
                String uri = jsonParser.getUriToImportFixlet(jsonParser.getConsoleApiObject());
                Response response = apiRequests.POST(requestSpecification, uri, apiRequestBody);
                logger.debug("API request Response after importing the fixlet into custom site =/n" + response.asString());
                logger.info("Fixlet got imported to the site " + siteName);
                fixletDetails.put(fileName, xmlParser.getElementOfXmlByXpath(response.asInputStream(), XmlLocators.FIXLET_ID_XPATH).get(0));
            }
        }
        logger.debug("Imported Fixlet details=" + fixletDetails);
        SuperClass.specStore.put(CreatePlanConsts.FIXLET_DETAILS, fixletDetails);
        return fixletDetails;
    }

    public HashMap<String, String> createTask(String folderPath, RequestSpecification requestSpecification) throws IOException, SAXException {
        HashMap<String, String> taskID = new HashMap<>();
        File[] files = new File(folderPath).listFiles();
        for (File file : files) {
            if (file.isFile()) {
                String fileName = file.getName();
                String apiRequestBody = commonFunctions.readTextFile(folderPath + fileName).toString();
                String uri = jsonParser.getUriToCreateTask(jsonParser.getConsoleApiObject());
                Response response = apiRequests.POST(requestSpecification, uri, apiRequestBody);
                logger.debug("API request Response after creating the task into custom site =/n" + response.asString());
                logger.info("Tasks got imported to the site " + siteName);
                taskID.put(fileName, xmlParser.getElementOfXmlByXpath(response.asInputStream(), XmlLocators.TASK_ID_XPATH).get(0));
            }
        }
        logger.debug("Imported task details=" + taskID);
        SuperClass.specStore.put(CreatePlanConsts.FIXLET_DETAILS, taskID);
        return taskID;
    }

    public HashMap<String, String> createBaseline(String siteType, String siteName, String filePath, String srcSiteName) throws IOException, SAXException, TransformerException {
        HashMap<String, String> baselineDetails = new HashMap<>();
         filePath = CommonFunctions.getPath(ConsoleConsts.BASELINE_FIXLETS_FOLDER.text);
        String baselinePayloadPath = CommonFunctions.getPath(ConsoleConsts.CREATE_BASELINE_PAYLOAD_PATH.text);
        srcSiteName = ConsoleConsts.CUSTOM_SITE.text + "_" + siteName;
        logger.debug("Baseline Folder path=" + filePath);
        RequestSpecification requestSpecification = apiRequests.setBaseURIAndBasicAuthentication().
                contentType(ContentType.JSON).and().accept(ContentType.ANY).and().
                pathParams(commonFunctions.commonParams(siteType, siteName));
        importFixlet(CommonFunctions.getPath(filePath), requestSpecification);
        //TODO : Source site url computer name should be dynamic
        String body = payload.createBaseline(new FileInputStream(baselinePayloadPath), srcSiteName);
        logger.debug("API  Request body for sending API=" + body);
        String uri = jsonParser.getUriToCreateBaseline(jsonParser.getConsoleApiObject());
        Response response = apiRequests.POST(requestSpecification, uri, body);
        logger.debug("Create baseline API response=" + response.asString());
        baselineDetails.put("Custom Baseline", xmlParser.getElementOfXmlByXpath(response.asInputStream(), XmlLocators.BASELINE_ID_XPATH).get(0));
        SuperClass.specStore.put(CreatePlanConsts.BASELINE_DETAILS, baselineDetails);
        return baselineDetails;
    }
   //TODO : Later try to convert a reusable code into method
    public HashMap<String, String> createBaselineHavingTasks(String siteType, String siteName, String filePath, String srcSiteName) throws IOException, SAXException, TransformerException {
        HashMap<String, String> baselineDetails = new HashMap<>();
        filePath = CommonFunctions.getPath(ConsoleConsts.BASELINE_TASKS_FOLDER.text);
        String baselinePayloadPath = CommonFunctions.getPath(ConsoleConsts.CREATE_BASELINE_PAYLOAD_PATH.text);
        srcSiteName = ConsoleConsts.CUSTOM_SITE.text + "_" + siteName;
        logger.debug("Baseline Folder path=" + filePath);
        RequestSpecification requestSpecification = apiRequests.setBaseURIAndBasicAuthentication().
                contentType(ContentType.JSON).and().accept(ContentType.ANY).and().
                pathParams(commonFunctions.commonParams(siteType, siteName));
        createTask(CommonFunctions.getPath(filePath), requestSpecification);
        //TODO : Source site url computer name should be dynamic
        String body = payload.createBaseline(new FileInputStream(baselinePayloadPath), srcSiteName);
        logger.debug("API  Request body for sending API=" + body);
        String uri = jsonParser.getUriToCreateBaseline(jsonParser.getConsoleApiObject());
        Response response = apiRequests.POST(requestSpecification, uri, body);
        logger.debug("Create baseline with Task API response=" + response.asString());
        baselineDetails.put("Custom Baseline", xmlParser.getElementOfXmlByXpath(response.asInputStream(), XmlLocators.BASELINE_ID_XPATH).get(0));
        SuperClass.specStore.put(CreatePlanConsts.BASELINE_DETAILS, baselineDetails);
        return baselineDetails;
    }

    public HashMap<String, String> createFixletsAndTasks(String folderPath, RequestSpecification requestSpecification) throws IOException, SAXException, ParserConfigurationException {
        HashMap<String, String> FixletAndTaskID = new HashMap<>();
        File[] files = new File(folderPath).listFiles();
        for (File file : files) {
            if (file.isFile()) {
                String fileName = file.getName();
                String apiRequestBody = commonFunctions.readTextFile(folderPath + fileName).toString();
                InputStream inputData = new ByteArrayInputStream(apiRequestBody.getBytes());
                NodeList elements = xmlParser.getNodeList(inputData, XmlLocators.NODE_LIST_XPATH);
                String type = null;
                for (int i = 0; i < elements.getLength(); ++i) {
                    type = elements.item(i).getNodeName();
                }
                if (type == ConsoleConsts.FIXLET.text) {
                    String uri = jsonParser.getUriToImportFixlet(jsonParser.getConsoleApiObject());
                    Response response = apiRequests.POST(requestSpecification, uri, apiRequestBody);
                    logger.debug("API request Response after importing the fixlet into custom site =/n" + response.asString());
                    FixletAndTaskID.put(fileName, xmlParser.getElementOfXmlByXpath(response.asInputStream(), XmlLocators.FIXLET_ID_XPATH).get(0));
                } else if (type == ConsoleConsts.TASK.text) {
                    String uri = jsonParser.getUriToCreateTask(jsonParser.getConsoleApiObject());
                    Response response = apiRequests.POST(requestSpecification, uri, apiRequestBody);
                    logger.debug("API request Response after importing the fixlet into custom site =/n" + response.asString());
                    FixletAndTaskID.put(fileName, xmlParser.getElementOfXmlByXpath(response.asInputStream(), XmlLocators.TASK_ID_XPATH).get(0));
                }
            }
        }
        logger.info("Fixlet got imported to the site " + siteName);
        logger.debug("Imported Fixlet and Task details=" + FixletAndTaskID);
        SuperClass.specStore.put(CreatePlanConsts.FIXLET_DETAILS, FixletAndTaskID);
        return FixletAndTaskID;
    }

    public HashMap<String, String> createBaselineHavingFixletsAndTasks(String siteType, String siteName) throws IOException, SAXException, TransformerException, ParserConfigurationException {
        HashMap<String, String> baselineDetails = new HashMap<>();
        String baselineFixletsAndTasksFolderPath = CommonFunctions.getPath(ConsoleConsts.BASELINE_FIXLETS_AND_TASKS_FOLDER.text);
        String baselinePayloadPath = CommonFunctions.getPath(ConsoleConsts.CREATE_BASELINE_PAYLOAD_PATH.text);
        String sourceSiteName = ConsoleConsts.CUSTOM_SITE.text + "_" + siteName;
        logger.debug("Baseline Fixlets and Tasks Folder path=" + baselineFixletsAndTasksFolderPath);
        RequestSpecification requestSpecification = apiRequests.setBaseURIAndBasicAuthentication().
                contentType(ContentType.JSON).and().accept(ContentType.ANY).and().
                pathParams(commonFunctions.commonParams(siteType, siteName));
        createFixletsAndTasks(CommonFunctions.getPath(baselineFixletsAndTasksFolderPath), requestSpecification);
        //TODO : Source site url computer name should be dynamic
        String body = payload.createBaseline(new FileInputStream(baselinePayloadPath), sourceSiteName);
        logger.debug("API  Request body for sending API=" + body);
        String uri = jsonParser.getUriToCreateBaseline(jsonParser.getConsoleApiObject());
        Response response = apiRequests.POST(requestSpecification, uri, body);
        logger.debug("Create baseline with Task API response=" + response.asString());
        baselineDetails.put("Custom Baseline", xmlParser.getElementOfXmlByXpath(response.asInputStream(), XmlLocators.BASELINE_ID_XPATH).get(0));
        SuperClass.specStore.put(CreatePlanConsts.BASELINE_DETAILS, baselineDetails);
        return baselineDetails;
    }

    public List<String> getPlansList(HashMap<String, String> params) throws IOException, SAXException {
        List<String> planList = new ArrayList<String>();
        String uri = jsonParser.getUriToFetchFixletLIist(consoleApiObject);
        RequestSpecification requestSpecification = apiRequests.setBaseURIAndBasicAuthentication().and().pathParams(params);
        Response response = apiRequests.GET(requestSpecification, uri);
        planList = xmlParser.getElementOfXmlByXpath(response.asInputStream(), PLAN_NAME_XPATH);
        return planList;
    }

}
