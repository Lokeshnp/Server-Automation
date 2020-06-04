package com.hcl.sa.utils;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
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
import java.util.*;


public class ApiUtils {
    private Logger logger = LogManager.getLogger(ApiUtils.class);
    API_Requests apiRequests = new API_Requests();
    Commonfunctions commonFunctions = new Commonfunctions();
    XMLParser xmlParser = new XMLParser();
    JsonParser jsonParser = new JsonParser();

    String customSiteName = jsonParser.getCustomSiteName();

    JsonObject apiUriObject = jsonParser.getApiUriObject();

    public Response getRelevantMachines(String fixletID) {
        String uri = jsonParser.getUriToFetchRelevantVMs(apiUriObject);
        HashMap<String, String> params = new HashMap<>();
        params.put(ConsoleConsts.PARAM_NAME_CUSTOM_SITE.text, customSiteName);
        params.put(ConsoleConsts.FIXLET_ID.text, fixletID);
        Response response = apiRequests.GET(params, uri);
        return response;
    }

    public List<String> getComputerIDsList(String fixletID) throws IOException, SAXException {
        Response getComputersList = getRelevantMachines(fixletID);
        logger.debug("Relevant Machines = " + getComputersList.asString());
        List<String> compIDsList = new ArrayList<String>();
        List<String> computersList = xmlParser.getElementOfXML_UsingXPath(getComputersList.asInputStream(), XMLConsts.COMPUTER_ATTRIBUTE_XPATH.text);
        computersList.forEach(computer -> {
            String computerID = commonFunctions.filterData(ConsoleConsts.COMPUTER_ID_REGEX.text, computer);
            compIDsList.add(computerID.split("/")[1]);
        });
        logger.debug("Computer ID's List = " + compIDsList);
        return compIDsList;
    }

    public Integer checkForRelevantVM(String fixletID, int timeout) throws IOException, SAXException, ParserConfigurationException, InterruptedException {
        int computerTagCount;
        long startTime = System.currentTimeMillis();
        boolean isPending = true;
            do {
                Response relevantMachines = getRelevantMachines(fixletID);
                computerTagCount = xmlParser.getElementsByTagName(relevantMachines.asInputStream(), XMLConsts.COMPUTER_TAGNAME.text).getLength();
                isPending =(System.currentTimeMillis()-startTime)<timeout ;
                if (isPending == false) {
                    logger.info("Fixlet has already installed to a target machine");
                    break;
                }
            } while (computerTagCount == 0);
            return computerTagCount;
    }

    public String getComputerID(String fixletID, String targetVmIpAddress) throws IOException, SAXException {
        List<String> computerIDs = getComputerIDsList(fixletID);
        int computerIDsSize = computerIDs.size();
        String ipAddress = null;
        String computerID = null;
        for (int i = 0; i < computerIDsSize; i++) {
            String uri = jsonParser.getUriToFetchCompIdInfo(apiUriObject);
            Response response = apiRequests.GET(ConsoleConsts.COMPUTER_ID.text, computerIDs.get(i), uri);
            ipAddress = xmlParser.getElementOfXML_UsingXPath(response.asInputStream(), XMLConsts.IP_ADDRESS_XPATH.text).get(0);

            if (ipAddress.equals(targetVmIpAddress)) {
                computerID = computerIDs.get(i);
                break;
            } else {
                getComputerID(fixletID, targetVmIpAddress);
            }
        }
        logger.debug("Fixlet with ID " + fixletID + " becomes relevant to a machine with IP Address " + ipAddress);
        logger.debug("Computer ID for the fixlet ID " + fixletID + " is " + computerID);
        SuperClass.specStore.put(ConsoleConsts.COMPUTER_ID, computerID);
        return computerID;
    }

    public Response initiateAction(String fixletID, String computerID) {
        //TODO, need to change the below method of passing string
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
        Response response = apiRequests.POST(actionBody, jsonParser.getUriToInitiateAction(apiUriObject));
        logger.debug("Response after initiating the action: \n" + response.asString());
        return response;
    }

    public String getActionID(InputStream inputStream) throws IOException, SAXException {
        String actionID = xmlParser.getElementOfXML_UsingXPath(inputStream, XMLConsts.ACTION_ID_XPATH.text).get(0);
        logger.debug("Action ID " + actionID);
        return actionID;
    }

    public void uninstallAutomationPlanEngine(String fixletID, String targetVmIpAddress) throws IOException, SAXException, ParserConfigurationException, InterruptedException {
        targetVmIpAddress = commonFunctions.filterDataForTargetVm(targetVmIpAddress);
        String  computerID = getComputerID(fixletID, targetVmIpAddress);
        Integer computerTagCount = checkForRelevantVM(fixletID, 10000);
        Response response = null;
        if (computerTagCount == 1){
            response = initiateAction(fixletID, computerID);
            String actionID = getActionID(response.asInputStream());
            waitTillXmlResponseReceived(actionID);
            String getActionStatus = waitTillActionIsFixed(actionID);
            verifyActionStatus(fixletID, actionID, getActionStatus,targetVmIpAddress);
        }
    }

    public void installAutomationPlanEngine(String fixletID, String targetVmIpAddress) throws IOException, SAXException, ParserConfigurationException, InterruptedException {
        targetVmIpAddress = commonFunctions.filterDataForTargetVm(targetVmIpAddress);
        System.out.println("install");
        String  computerID = getComputerID(fixletID, targetVmIpAddress);
        Integer computerTagCount = checkForRelevantVM(fixletID, 10000);
        Response response = null;
        if (computerTagCount == 1) {
            response = initiateAction(fixletID, computerID);
            String actionID = getActionID(response.asInputStream());
            waitTillXmlResponseReceived(actionID);
            String getActionStatus = waitTillActionIsFixed(actionID);
            verifyActionStatus(fixletID, actionID, getActionStatus, targetVmIpAddress);
         }
    }

    public Response getActionStatus(String actionID) {
        String uri = jsonParser.getUriToFetchActionStatus(apiUriObject);
        Response response = apiRequests.GET(ConsoleConsts.ACTION_ID.text, actionID, uri);
        return response;
    }

    public void stopAction(String actionID) {
        String uri = jsonParser.getUriToStopAction(apiUriObject);
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
        String getStatus;
        Boolean isSuccessful;
        Boolean isRestart;
        Boolean isFailed;
        logger.info("Waiting till the fixlet status becomes fixed or failed or restart");
        do {
            Response response = getActionStatus(actionID);
            getStatus = xmlParser.getElementOfXML_UsingXPath(response.asInputStream(), XMLConsts.COMPUTER_STATUS_XPATH.text).get(0);
            isSuccessful = (getStatus.toString().contains("successful"));
            isRestart = (getStatus.toString().contains("restart"));
            isFailed = (getStatus.toString().contains("failed"));
        } while (!(isSuccessful || isRestart || isFailed));
        logger.debug("Action Status = " + getStatus);
        return getStatus;
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
}