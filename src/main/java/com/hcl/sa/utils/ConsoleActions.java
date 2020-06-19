package com.hcl.sa.utils;

import com.google.gson.JsonObject;
import com.hcl.sa.constants.ConsoleConsts;
import com.hcl.sa.constants.XMLConsts;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ConsoleActions {
    private Logger logger = LogManager.getLogger(ConsoleActions.class);
    ApiRequests apiRequests = new ApiRequests();
    Commonfunctions commonFunctions = new Commonfunctions();
    XMLParser xmlParser = new XMLParser();
    JsonParser jsonParser = new JsonParser();

    String customSiteName = jsonParser.getCustomSiteName();

    JsonObject consoleApiObject = jsonParser.getConsoleApiObject();

    public Response getRelevantComputers(String fixletID) {
        String uri = jsonParser.getUriToFetchRelevantComputers(consoleApiObject);
        HashMap<String, String> params = new HashMap<>();
        params.put(ConsoleConsts.PARAM_NAME_CUSTOM_SITE.text, customSiteName);
        params.put(ConsoleConsts.FIXLET_ID.text, fixletID);
        Response response = apiRequests.GET(params, uri);
        return response;
    }

    public List<String> getApplicableComputersID(String fixletID) throws IOException, SAXException {
        List<String> compIDsList = new ArrayList<String>();
        Response response = getRelevantComputers(fixletID);
        logger.debug("Relevant Machines = " + response.asString());
        List<String> computersList = xmlParser.getElementOfXmlByXpath(response.asInputStream(), XMLConsts.COMPUTER_ATTRIBUTE_XPATH.text);
        computersList.forEach(computer -> {
            String computerID = commonFunctions.filterData(ConsoleConsts.COMPUTER_ID_REGEX.text, computer);
            compIDsList.add(computerID.split("/")[1]);
        });
        logger.debug("Computer ID's List = " + compIDsList);
        return compIDsList;
    }

    public Integer waitUntilApplicableCompsVisible(String fixletID, int timeout) throws IOException, SAXException, ParserConfigurationException {
        int computerTagCount;
        long startTime = System.currentTimeMillis();
        boolean isPending = true;
            do {
                Response relevantMachines = getRelevantComputers(fixletID);
                computerTagCount = xmlParser.getElementsByTagName(relevantMachines.asInputStream(), XMLConsts.COMPUTER_TAGNAME.text).getLength();
                isPending =(System.currentTimeMillis()-startTime)<timeout ;
                if (isPending == false) {
                    logger.info("Applicable computers are zero for this fixlet ");
                    break;
                }
            } while (computerTagCount == 0);
            return computerTagCount;
    }

    public String getApplicableComputerID(String fixletID, String targetVmIpAddress) throws IOException, SAXException {
        String ipAddress = null;
        String computerID = null;
        List<String> computerIDs = getApplicableComputersID(fixletID);
        int computerIDsSize = computerIDs.size();
        for (int i = 0; i < computerIDsSize; i++) {
            String uri = jsonParser.getUriToFetchCompProperties(consoleApiObject);
            Response response = apiRequests.GET(ConsoleConsts.COMPUTER_ID.text, computerIDs.get(i), uri);
            ipAddress = xmlParser.getElementOfXmlByXpath(response.asInputStream(), XMLConsts.IP_ADDRESS_XPATH.text).get(0);

            if (ipAddress.equals(targetVmIpAddress)) {
                computerID = computerIDs.get(i);
                break;
            } else {
                getApplicableComputerID(fixletID, targetVmIpAddress);
            }
        }
        logger.debug("Fixlet with ID " + fixletID + " becomes relevant to a machine with IP Address " + ipAddress);
        logger.debug("Computer ID for the fixlet ID " + fixletID + " is " + computerID);
        SuperClass.specStore.put(ConsoleConsts.COMPUTER_ID, computerID);
        return computerID;
    }

    public Response initiateAction(String fixletID, String computerID) {
        //TODO, need to change the below method of passing string to create a new xml file
        String actionBody = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
                "<BES xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"BES.xsd\">\r\n" +
                " <SourcedFixletAction>\r\n" +
                "   <SourceFixlet>\r\n" +
                "     <Sitename>" + customSiteName + "</Sitename>\r\n" +
                "     <FixletID>" + fixletID + "</FixletID>\r\n" +
                "     <Action>Action1</Action>\r\n" +
                "   </SourceFixlet>\r\n" +
                "   <Target>\r\n" +
                "     <ComputerID>" + computerID + "</ComputerID>\r\n" +
                "   </Target>\r\n" +
                "  <Parameter Name=\"_BESClient_EMsg_Detail\">1000</Parameter>\r\n" +
                " </SourcedFixletAction>\r\n" +
                "</BES>";
        Response response = apiRequests.POST(actionBody, jsonParser.getUriToInitiateAction(consoleApiObject));
        logger.debug("Response after initiating the action: \n" + response.asString());
        return response;
    }

    public String getActionID(InputStream inputStream) throws IOException, SAXException {
        String actionID = xmlParser.getElementOfXmlByXpath(inputStream, XMLConsts.ACTION_ID_XPATH.text).get(0);
        logger.debug("Action ID " + actionID);
        return actionID;
    }

    //TODO : move to particular class
    public void uninstallAutomationPlanEngine(String fixletID, String targetVmIpAddress) throws IOException, SAXException, ParserConfigurationException, InterruptedException {
        String actionID = takeAction(fixletID, targetVmIpAddress);
        String actionStatus  = SuperClass.specStore.get(ConsoleConsts.ACTION_STATUS).toString();
        verifyActionStatus(fixletID, actionID, actionStatus, targetVmIpAddress);
    }

    //TODO : move to particular class
    public void installAutomationPlanEngine(String fixletID, String targetVmIpAddress) throws IOException, SAXException, ParserConfigurationException, InterruptedException {
        String actionID = takeAction(fixletID, targetVmIpAddress);
        String actionStatus  = SuperClass.specStore.get(ConsoleConsts.ACTION_STATUS).toString();
        verifyActionStatus(fixletID, actionID, actionStatus, targetVmIpAddress);
    }

    public String takeAction(String fixletID, String targetVmIpAddress) throws IOException, SAXException, ParserConfigurationException, InterruptedException {
        Response response = null;
        targetVmIpAddress = commonFunctions.getIpAddress(targetVmIpAddress);
        String computerID = getApplicableComputerID(fixletID, targetVmIpAddress);
        Integer computerTagCount = waitUntilApplicableCompsVisible(fixletID, 10000);
        String actionID = null;
        if (computerTagCount == 1) {
            response = initiateAction(fixletID, computerID);
            actionID = getActionID(response.asInputStream());
            waitTillXmlResponseReceived(actionID);
            String actionStatus = waitTillActionIsFixed(actionID);
            SuperClass.specStore.put(ConsoleConsts.ACTION_STATUS, actionStatus);
        }
        return actionID;
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
            status = xmlParser.getElementOfXmlByXpath(response.asInputStream(), XMLConsts.COMPUTER_STATUS_XPATH.text).get(0);
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
}