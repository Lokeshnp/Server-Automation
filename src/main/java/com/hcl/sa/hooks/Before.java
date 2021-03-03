package com.hcl.sa.hooks;

import com.google.gson.JsonObject;
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
import java.io.FileNotFoundException;
import java.io.IOException;

public class Before {
    protected Login login = new Login();
    protected AllContent allContent = new AllContent();
    private final Logger logger = LogManager.getLogger(Before.class);
    ConsoleActions consoleActions = new ConsoleActions();
    CommonFunctions commonFunctions = new CommonFunctions();
    JsonParser jsonParser = new JsonParser();
    XMLParser xmlParser = new XMLParser();
    ApiRequests apiRequests = new ApiRequests();
    Response response;

        @BeforeSpec()
        public void consoleLogin(ExecutionContext executionContext) throws FileNotFoundException, TransformerException {
            System.out.println("Loging using "+Credentials.OPERATOR.getUsername());
            System.out.println("exe context="+executionContext.getCurrentSpecification().getName());
            if(executionContext.getCurrentSpecification().getName().contains("opertor"))
            {
                String createOperatorPayloadPath = CommonFunctions.getPath(ConsoleConsts.CREATE_OPERATOR_PAYLOAD_PATH.text);
                String uri = jsonParser.getUriToCreateOperator(jsonParser.getConsoleApiObject());
                logger.info("uri: "+uri);
                RequestSpecification requestSpecification = apiRequests.setBaseURIAndBasicAuthentication().
                        contentType(ContentType.JSON).and().accept(ContentType.ANY);
                Document doc = xmlParser.buildDocument(new FileInputStream(createOperatorPayloadPath));
                logger.info("Payload:"+xmlParser.convertDocToString(doc));
                response = apiRequests.POST(requestSpecification, uri, xmlParser.convertDocToString(doc));
                logger.info("response:using requestSpecification " + response.asString());
                String assignSASitePayloadPath = CommonFunctions.getPath(ConsoleConsts.ASSIGN_SA_SITE_PAYLOAD_PATH.text);
                String assignCustomSitePayloadPath = CommonFunctions.getPath(ConsoleConsts.ASSIGN_CUSTOM_SITE_PAYLOAD_PATH.text);
                String assignSiteUri = jsonParser.getUriToAssignSite(jsonParser.getConsoleApiObject());
                logger.info("uri: "+assignSiteUri);
                RequestSpecification saSitePermissionRequestBody = apiRequests.setBaseURIAndBasicAuthentication().
                        contentType(ContentType.JSON).and().accept(ContentType.ANY).and().
                        pathParams(commonFunctions.commonParams(ConsoleConsts.EXTERNAL.text, ConsoleConsts.SERVER_AUTOMATION.text));
                Document docSASite = xmlParser.buildDocument(new FileInputStream(assignSASitePayloadPath));
                logger.info("Payload:"+xmlParser.convertDocToString(docSASite));
                response = apiRequests.PUT(saSitePermissionRequestBody, ConsoleConsts.OPERATOR_NAME.text,ConsoleConsts.OPERATOR_VALUE.text, xmlParser.convertDocToString(docSASite),assignSiteUri);
                logger.info("Sa site assign " + response.asString());
                RequestSpecification customSitePermissionRequestBody = apiRequests.setBaseURIAndBasicAuthentication().
                        contentType(ContentType.JSON).and().accept(ContentType.ANY).and().
                        pathParams(commonFunctions.commonParams(ConsoleConsts.CUSTOM.text, ConsoleConsts.SA.text));
                Document docCustomSite = xmlParser.buildDocument(new FileInputStream(assignCustomSitePayloadPath));
                logger.info("Payload:"+xmlParser.convertDocToString(docCustomSite));
                response = apiRequests.PUT(customSitePermissionRequestBody, ConsoleConsts.OPERATOR_NAME.text,ConsoleConsts.OPERATOR_VALUE.text, xmlParser.convertDocToString(docCustomSite),assignSiteUri);
                logger.info("Custom site assign " + response.asString());
                login.consoleLogin(Credentials.OPERATOR);
                logger.info("Login to Console is successful");
                allContent.openAutomationPlanDashboard();
                logger.info("User is able to open automation plan dashboard");
            }else {
                login.consoleLogin(Credentials.CONSOLE);
                logger.info("Login to Console is successful");
                allContent.openAutomationPlanDashboard();
                logger.info("User is able to open automation plan dashboard");
            }
        }

        public void configBeforeSuite() throws ParserConfigurationException, SAXException, IOException, TransformerException {
        logger.info("Checking if plan engine is up and running");
        //TODO NEED TO FIND SOME WAY TO SKIP THIS METHOD OR REDUCE TIME WHEN NOT REQUIRED
        consoleActions.takeActionOnRootServer(commonFunctions.setExternalSiteParams(ConsoleConsts.INSTALL_PE_FIXLET_ID.text));
    }
    }


//    @BeforeSuite()
//    public void consoleLogin() {
//        login.consoleLogin(Credentials.CONSOLE);
//        logger.info("Login to Console is successful");
//        allContent.openAutomationPlanDashboard();
//        logger.info("User is able to open automation plan dashboard");
//    }
//
//    public void configBeforeSuite() throws ParserConfigurationException, SAXException, IOException {
//        logger.info("Checking if plan engine is up and running");
//        //TODO NEED TO FIND SOME WAY TO SKIP THIS METHOD OR REDUCE TIME WHEN NOT REQUIRED
////        consoleActions.takeAction(ConsoleConsts.INSTALL_PE_FIXLET_ID.text);
//    }
//
//}
