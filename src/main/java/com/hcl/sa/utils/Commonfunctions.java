package com.hcl.sa.utils;

import com.google.gson.JsonObject;
import com.hcl.sa.constants.XMLConsts;
import com.hcl.sa.constants.ConsoleConsts;
import io.restassured.response.Response;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.xml.sax.SAXException;

public class Commonfunctions  {
    private Logger logger = LogManager.getLogger(ApiUtils.class);
    JsonParser jsonParser = new JsonParser();
    XMLParser xmlParser = new XMLParser();
    String customSiteName = "Server Automation Development";
    API_Requests apiRequests = new API_Requests();
    JsonObject apiUriObject = jsonParser.getApiUriObject();

    public String getLastModifiedDate(Response response) throws IOException, SAXException {
       List<String> lastModifiedDate = xmlParser.getElementOfXML_UsingXPath(response.asInputStream(), XMLConsts.ACTION_LASTMODIFIED_XPATH.text);
       List<String> modifiedDate = new ArrayList<String>();
       for(String lastDate:lastModifiedDate){
           modifiedDate.add(lastDate);
       }
       String lastFilteredDate = null;
       if (modifiedDate != null && !modifiedDate.isEmpty()) {
            lastFilteredDate = modifiedDate.get(modifiedDate.size()-1);
       }
       logger.debug("Action ID " + modifiedDate);
       return lastFilteredDate;
    }

    public String getActionIDForFixlet(Response response, String lastModifiedDate)throws IOException, SAXException {
        String actionId =  xmlParser.getElementOfXML_UsingXPath(response.asInputStream(), "//Name[starts-with(text(), 'Install Latest Automation Plan Engine')]/parent::Action[@LastModified='"+lastModifiedDate+"']/ID/text()").get(0);
        logger.debug("Action ID " + actionId);
        return actionId;
    }

    public String verifyFixletIsRunning(String fixletID, String computerID) throws IOException, SAXException {
       Response response = initiateAction(fixletID, computerID);
       String lastModifiedDate = getLastModifiedDate(response);
       String actionId = getActionIDForFixlet(response, lastModifiedDate);
       String actionStatus = getActionStatusAndVerify(actionId);
       logger.debug("Response after initiating the action: \n" + actionStatus);
       return actionStatus;
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

        Response response = apiRequests.GET(jsonParser.getUriToInitiateAction(apiUriObject));
        logger.debug("Response after initiating the action: \n" + response.asString());
        return response;
    }

    public Response getActionStatus(String actionID) {
        String uri = jsonParser.getUriToFetchActionStatus(apiUriObject);
        Response response = apiRequests.GET(ConsoleConsts.ACTION_ID.text, actionID, uri);
        return response;
    }

    public String getActionStatusAndVerify(String actionID) throws IOException, SAXException {
        String getStatus;
        Response response = getActionStatus(actionID);
        getStatus = xmlParser.getElementOfXML_UsingXPath(response.asInputStream(), XMLConsts.COMPUTER_STATUS_XPATH.text).get(0);
        logger.debug("Action Status = " + getStatus);
        return getStatus;
    }

    //generic method for cve Filter
    public String filterData(String regEx, String source) {
        logger.debug("RegEx: " + regEx + " to filter data from the \n source: " + source);
        Set<String> data = new HashSet<>();
        Matcher matcher = getRegExMatcher(regEx, source);
        while (matcher.find()) {
            data.add(matcher.group());
        }
        String filteredData = StringUtils.join(data.iterator(), ";");
        return filteredData;
    }

    //Generic method for regEx filter
    public Matcher getRegExMatcher(String regEx, String source) {
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(source);
        return matcher;
    }

    public String filterDataForTargetVm(String bigfixIpAddress) {
        String ipAddress = bigfixIpAddress.split("/")[2].split(":")[0];
        return ipAddress;
    }
}

