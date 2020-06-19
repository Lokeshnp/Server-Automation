package com.hcl.sa.utils;

import com.google.gson.JsonObject;
import com.hcl.sa.constants.XMLConsts;
import io.restassured.response.Response;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.xml.sax.SAXException;

public class Commonfunctions  {
    private Logger logger = LogManager.getLogger(ConsoleActions.class);
    JsonParser jsonParser = new JsonParser();
    XMLParser xmlParser = new XMLParser();
    String customSiteName = jsonParser.getCustomSiteName();
    ApiRequests apiRequests = new ApiRequests();
    JsonObject apiUriObject = jsonParser.getConsoleApiObject();

    public String getLastModifiedDate(Response response) throws IOException, SAXException {
       List<String> lastModifiedDate = xmlParser.getElementOfXmlByXpath(response.asInputStream(), XMLConsts.ACTION_LASTMODIFIED_XPATH.text);
       String lastFilteredDate = lastModifiedDate.get(lastModifiedDate.size()-1);
       logger.debug("lastFilteredDate " + lastFilteredDate);
       return lastFilteredDate;
    }

    public String getActionIDForFixlet(Response response, String lastModifiedDate)throws IOException, SAXException {
        String actionId =  xmlParser.getElementOfXmlByXpath(response.asInputStream(), "//Name[starts-with(text(), 'Install Latest Automation Plan Engine')]/parent::Action[@LastModified='"+lastModifiedDate+"']/ID/text()").get(0);
        logger.debug("Action ID " + actionId);
        return actionId;
    }

    public String verifyFixletIsRunning(String fixletID, String computerID) throws IOException, SAXException {
       Response response = initiateAction(fixletID, computerID);
       String lastModifiedDate = getLastModifiedDate(response);
       String actionId = getActionIDForFixlet(response, lastModifiedDate);
       //String actionStatus = getActionStatusAndVerify(actionId);
       logger.debug("Response after initiating the action: \n" + actionId);
       return actionId;
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

    public Matcher getRegExMatcher(String regEx, String source) {
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(source);
        return matcher;
    }

    public String getIpAddress(String bigfixIpAddress) {
        String ipAddress = bigfixIpAddress.split("/")[2].split(":")[0];
        return ipAddress;
    }
}