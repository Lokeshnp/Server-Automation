package com.hcl.sa.hooks;

import com.hcl.sa.constants.ConsoleConsts;
import com.hcl.sa.utils.api.ApiRequests;
import com.hcl.sa.utils.bigfix.CommonFunctions;
import com.hcl.sa.utils.bigfix.ConsoleActions;
import com.hcl.sa.utils.bigfix.Credentials;
import com.hcl.sa.utils.bigfix.Login;
import com.hcl.sa.utils.parser.JsonParser;
import com.hcl.sa.utils.parser.XMLParser;
import com.hcl.sa.windows.AllContent;
import com.thoughtworks.gauge.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class Before {
    private final Logger logger = LogManager.getLogger(Before.class);
    protected Login login = new Login();
    protected AllContent allContent = new AllContent();
    ConsoleActions consoleActions = new ConsoleActions();
    CommonFunctions commonFunctions = new CommonFunctions();
    JsonParser jsonParser = new JsonParser();
    XMLParser xmlParser = new XMLParser();
    ApiRequests apiRequests = new ApiRequests();
    Response response;

        @BeforeSpec()
        public void consoleLogin(ExecutionContext executionContext) throws Exception {
            if(executionContext.getCurrentSpecification().getName().contains("opertor"))
            {
                String createOperatorPayloadPath = CommonFunctions.getPath(ConsoleConsts.CREATE_OPERATOR_PAYLOAD_PATH.text);
                String uri = jsonParser.getUriToCreateOperator(jsonParser.getConsoleApiObject());
                logger.info("create operator uri is: "+uri);
                RequestSpecification requestSpecification = apiRequests.setBaseURIAndBasicAuthentication().
                        contentType(ContentType.JSON).and().accept(ContentType.ANY);
                Document doc = xmlParser.buildDocument(new FileInputStream(createOperatorPayloadPath));
                logger.info("Payload:"+xmlParser.convertDocToString(doc));
                response = apiRequests.POST(requestSpecification, uri, xmlParser.convertDocToString(doc));
                logger.info("Operator is created " + response.asString());
                Map<String, String> param=new HashMap<>();
                param.put("operator name", ConsoleConsts.OPERATOR_NAME.text);
                param.put("operator value",ConsoleConsts.OPERATOR_VALUE.text);
                String assignSiteUri = jsonParser.getUriToAssignSite(jsonParser.getConsoleApiObject());
                logger.info("assign site uri is: "+assignSiteUri);
                Before before=new Before();
                before.saSiteAssign(assignSiteUri,param);
                before.customSiteAssign(assignSiteUri,param);
                login.consoleLogin(Credentials.OPERATOR);
                logger.info("Login to Console is successful");
                allContent.openAutomationPlanDashboard();
                logger.info("User is able to open automation plan dashboard");
            }
            else {
                login.consoleLogin(Credentials.CONSOLE);
                logger.info("Login to Console is successful");
                allContent.openAutomationPlanDashboard();
                logger.info("User is able to open automation plan dashboard");
            }
        }

    public void saSiteAssign(String assignSiteUri, Map<String, String>param) throws Exception{
        String assignSASitePayloadPath = CommonFunctions.getPath(ConsoleConsts.ASSIGN_SA_SITE_PAYLOAD_PATH.text);
        RequestSpecification saSitePermissionRequestBody = apiRequests.setBaseURIAndBasicAuthentication().
                contentType(ContentType.JSON).and().accept(ContentType.ANY).and().
                pathParams(commonFunctions.commonParams(ConsoleConsts.EXTERNAL.text, ConsoleConsts.SERVER_AUTOMATION.text));
        Document docSASite = xmlParser.buildDocument(new FileInputStream(assignSASitePayloadPath));
        logger.info("Payload:"+xmlParser.convertDocToString(docSASite));
        response = apiRequests.PUT(saSitePermissionRequestBody,param,xmlParser.convertDocToString(docSASite),assignSiteUri);
        logger.info("Sa site assign " + response.asString());

    }

    public void customSiteAssign(String assignSiteUri, Map<String, String>param) throws Exception{
        String assignCustomSitePayloadPath = CommonFunctions.getPath(ConsoleConsts.ASSIGN_CUSTOM_PAYLOAD_PATH.text);
        RequestSpecification customSitePermissionRequestBody = apiRequests.setBaseURIAndBasicAuthentication().
                contentType(ContentType.JSON).and().accept(ContentType.ANY).and().
                pathParams(commonFunctions.commonParams(ConsoleConsts.CUSTOM.text, ConsoleConsts.SA.text));
        Document docCustomSite = xmlParser.buildDocument(new FileInputStream(assignCustomSitePayloadPath));
        logger.info("Payload:"+xmlParser.convertDocToString(docCustomSite));
        response = apiRequests.PUT(customSitePermissionRequestBody,param,xmlParser.convertDocToString(docCustomSite),assignSiteUri);
        logger.info("Custom site assign " + response.asString());

    }

    public void configBeforeSuite() throws ParserConfigurationException, SAXException, IOException, TransformerException {
        logger.info("Checking if plan engine is up and running");
        //TODO NEED TO FIND SOME WAY TO SKIP THIS METHOD OR REDUCE TIME WHEN NOT REQUIRED
        consoleActions.takeActionOnRootServer(commonFunctions.setExternalSiteParams(ConsoleConsts.INSTALL_PE_FIXLET_ID.text));
    }
    }

//    public void configBeforeSuite() throws ParserConfigurationException, SAXException, IOException {
//        logger.info("Checking if plan engine is up and running");
//        //TODO NEED TO FIND SOME WAY TO SKIP THIS METHOD OR REDUCE TIME WHEN NOT REQUIRED
////        consoleActions.takeAction(ConsoleConsts.INSTALL_PE_FIXLET_ID.text);
//    }
//
//}
