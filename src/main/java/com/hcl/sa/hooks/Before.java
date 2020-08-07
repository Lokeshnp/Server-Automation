package com.hcl.sa.hooks;

import com.hcl.sa.constants.ConsoleConsts;
import com.hcl.sa.utils.ConsoleActions;
import com.thoughtworks.gauge.BeforeSuite;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class Before {

    private final Logger logger = LogManager.getLogger(Before.class);
    ConsoleActions consoleActions = new ConsoleActions();


    @BeforeSuite()
    public void configBeforeSuite() throws ParserConfigurationException, SAXException, IOException {
        logger.info("Checking if plan engine is up and running");
        //TODO NEED TO FIND SOME WAY TO SKIP THIS METHOD OR REDUCE TIME WHEN NOT REQUIRED
//        consoleActions.takeAction(ConsoleConsts.INSTALL_PE_FIXLET_ID.text);
    }
}
