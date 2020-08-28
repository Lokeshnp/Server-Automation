package com.hcl.sa.utils;

import com.hcl.sa.constants.ConsoleConsts;
import com.hcl.sa.constants.CreatePlanConsts;
import com.hcl.sa.constants.TimeOutConsts;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CommonFunctions {

    private Logger logger = LogManager.getLogger(ConsoleActions.class);
    JsonParser jsonParser = new JsonParser();

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

    public String getIpAddress(String url) {
        String ipAddress = url.split("/")[2].split(":")[0];
        logger.debug("ip Address is = " + ipAddress);
        return ipAddress;
    }

    public long convertToMilliSeconds(long seconds) {
        long milliSeconds = TimeUnit.SECONDS.toMillis(seconds);
        return milliSeconds;
    }

    public HashMap<String, String> setExternalSiteParams(String fixletID) {
        HashMap<String, String> commonPara = new HashMap<>();
        commonPara.put(ConsoleConsts.SITE_TYPE.text, jsonParser.getSiteTypeObject().get(ConsoleConsts.EXTERNAL.text).getAsString());
        commonPara.put(ConsoleConsts.SITE_NAME.text, jsonParser.getSiteNameObject().get(ConsoleConsts.SERVER_AUTOMATION.text).getAsString());
        commonPara.put(ConsoleConsts.FIXLET_ID.text, fixletID);
        return commonPara;
    }

    public HashMap<String, String> commonParams(String siteType, String siteName) {
        HashMap<String, String> commonPara = new HashMap<>();
        commonPara.put(ConsoleConsts.SITE_TYPE.text, jsonParser.getSiteTypeObject().get(siteType).getAsString());
        commonPara.put(ConsoleConsts.SITE_NAME.text, jsonParser.getSiteNameObject().get(siteName).getAsString());
        logger.debug("Common Params=" + commonPara);
        return commonPara;
    }

    public HashMap<String, String> commonParams(String siteType, String siteName, String fixletID) {
        HashMap<String, String> commonPara = new HashMap<>();
        commonPara.put(ConsoleConsts.SITE_TYPE.text, jsonParser.getSiteTypeObject().get(siteType).getAsString());
        commonPara.put(ConsoleConsts.SITE_NAME.text, jsonParser.getSiteNameObject().get(siteName).getAsString());
        commonPara.put(ConsoleConsts.FIXLET_ID.text, fixletID);
        logger.debug("Common Params=" + commonPara);
        return commonPara;
    }

    public HashMap<String, String> saRestCommonParams(String siteType, String siteName, String planID) {
        HashMap<String, String> commonPara = new HashMap<>();
        commonPara.put(ConsoleConsts.SITE_TYPE.text, jsonParser.getSiteTypeObject().get(siteType).getAsString());
        commonPara.put(ConsoleConsts.SITE_NAME.text, jsonParser.getSiteNameObject().get(siteName).getAsString());
        commonPara.put(CreatePlanConsts.PLAN_ID.text, planID);
        logger.debug("Common Params=" + commonPara);
        return commonPara;
    }

    public StringBuilder readTextFile(String textFilepath) throws IOException {
        File file = new File(textFilepath);
        logger.debug("Text File Path = " + textFilepath);
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        StringBuilder stringBuilder = new StringBuilder();
        String textData = bufferedReader.readLine();
        while (textData != null) {
            stringBuilder.append(textData).append("\n");
            textData = bufferedReader.readLine();
        }
        return stringBuilder;
    }

    public static String getPath(String staticPath) {
        return staticPath.replace("{user.dir}", System.getProperty("user.dir"));

    }

    public List<String> getAllFileNames(String folderPath) {
        List<String> fileNames = new ArrayList<>();
        File directory = new File(folderPath);
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                fileNames.add(file.getName());
            }
        }
        return fileNames;
    }


}
