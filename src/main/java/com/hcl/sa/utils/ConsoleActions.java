package com.hcl.sa.utils;

import com.google.gson.JsonObject;
import com.hcl.sa.constants.ConsoleConsts;
import com.hcl.sa.constants.TimeOutConsts;
import com.hcl.sa.constants.XMLConsts;
import com.hcl.sa.objectRepository.XmlLocators;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
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

    public Response getRelevantComputers(HashMap<String, String> params) {
        String uri = jsonParser.getUriToFetchRelevantComputers(consoleApiObject);
        RequestSpecification requestSpecification = apiRequests.setBaseURIAndBasicAuthentication().and().pathParams(params);
        Response response = apiRequests.GET(requestSpecification, uri);
        return response;
    }

    public List<String> getApplicableComputersID(String fixletID) throws IOException, SAXException {
        List<String> compIDsList = new ArrayList<String>();
        Response response = getRelevantComputers(commonFunctions.commonParams(fixletID));
        logger.debug("Relevant Machines = " + response.asString());
        List<String> computersList = xmlParser.getElementOfXmlByXpath(response.asInputStream(), COMP_ATTR_XPATH);
        computersList.forEach(computer -> {
            String computerID = commonFunctions.filterData(ConsoleConsts.COMPUTER_ID_REGEX.text, computer);
            compIDsList.add(computerID.split("/")[1]);
        });
        logger.debug("Computer ID's List = " + compIDsList);
        return compIDsList;
    }

    public Integer waitUntilApplicableCompsVisible(String fixletID) throws IOException, SAXException, ParserConfigurationException {
        int computerTagCount;
        long startTime = System.currentTimeMillis();
        boolean waitUntillApplicableCompsDisp = true;
        do {
            Response relevantMachines = getRelevantComputers(commonFunctions.commonParams(fixletID));
            computerTagCount = xmlParser.getElementsByTagName(relevantMachines.asInputStream(), XMLConsts.COMPUTER_TAGNAME.text).getLength();
            waitUntillApplicableCompsDisp = (System.currentTimeMillis() - startTime) < commonFunctions.convertToMilliSeconds(TimeOutConsts.WAIT_60_SECOND.seconds);
            if (!waitUntillApplicableCompsDisp) {
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
            ipAddress = xmlParser.getElementOfXmlByXpath(response.asInputStream(), IP_ADDRESS_XPATH).get(0);

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
                "     <Sitename>" + siteName + "</Sitename>\r\n" +
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
        String actionID = xmlParser.getElementOfXmlByXpath(inputStream, ACTION_ID_XPATH).get(0);
        logger.debug("Action ID " + actionID);
        return actionID;
    }

    public void takeAction(String fixletID) throws IOException, SAXException, ParserConfigurationException {
        Response response = null;
        String targetVmIpAddress = commonFunctions.getIpAddress(ConsoleConsts.BIGFIX_SERVER_URI.text);
        String computerID = getApplicableComputerID(fixletID, targetVmIpAddress);
        Integer computerTagCount = waitUntilApplicableCompsVisible(fixletID);
        String actionID = null;
        if (computerTagCount == 1) {
            response = initiateAction(fixletID, computerID);
            actionID = getActionID(response.asInputStream());
            waitTillXmlResponseReceived(actionID);
            String actionStatus = waitTillActionIsFixed(actionID);
            SuperClass.specStore.put(ConsoleConsts.ACTION_STATUS, actionStatus);
            verifyActionStatus(fixletID, actionID, actionStatus, targetVmIpAddress);
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

    public HashMap<String,String> importFixlet(String folderPath,RequestSpecification requestSpecification) throws IOException, SAXException {
        HashMap<String,String> fixletID = new HashMap<>();
        File[] files = new File(folderPath).listFiles();
        for (File file : files) {
            if (file.isFile()) {
                String fileName = file.getName();
                String apiRequestBody = commonFunctions.readTextFile(folderPath + fileName).toString();
                String uri = jsonParser.getUriToImportFixlet(jsonParser.getConsoleApiObject());
                Response response = apiRequests.POST(requestSpecification, uri,apiRequestBody);
                logger.debug("API request Response after importing the fixlet into custom site =/n" + response.asString());
                logger.info("Fixlet got imported to the site " + siteName);
                fixletID.put(fileName,xmlParser.getElementOfXmlByXpath(response.asInputStream(),XmlLocators.FIXLET_ID_XPATH).get(0));
            }
        }
        logger.debug("Imported Fixlet details="+fixletID);
        SuperClass.specStore.put(ConsoleConsts.FIXLET_ID_LIST,fixletID);
        return fixletID;
    }


    public String createBaseline(String siteType,String siteName) throws IOException, SAXException, TransformerException {
        String baselineFolderPath = CommonFunctions.getPath(ConsoleConsts.BASELINE_FIXLETS_FOLDER.text);
        String baselinePayloadPath = CommonFunctions.getPath(ConsoleConsts.CREATE_BASELINE_PAYLOAD_PATH.text);
        String sourceSiteName = ConsoleConsts.CUSTOM_SITE.text+"_"+siteName;
        logger.debug("Baseline Folder path="+baselineFolderPath);
        RequestSpecification requestSpecification = apiRequests.setBaseURIAndBasicAuthentication().
        contentType(ContentType.JSON).and().accept(ContentType.ANY).and().
        pathParams(commonFunctions.commonParams(siteType,siteName));
        importFixlet(CommonFunctions.getPath(baselineFolderPath),requestSpecification);
        String body = payload.createBaseline(new FileInputStream(baselinePayloadPath),sourceSiteName);
        logger.debug("API  Request body for sending API="+body);
        String uri  = jsonParser.getUriToCreateBaseline(jsonParser.getConsoleApiObject());
        Response response = apiRequests.POST(requestSpecification, uri,body);
        logger.debug("Create baseline API response="+response.asString());
        return xmlParser.getElementOfXmlByXpath(response.asInputStream(),XmlLocators.BASELINE_ID_XPATH).get(0);
    }

}
