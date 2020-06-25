package com.hcl.sa.utils;

import com.hcl.sa.constants.ConsoleConsts;
import com.hcl.sa.constants.TimeOutConsts;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
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

    public long convertMilliSecToSec(String time) {
        long seconds = TimeUnit.MILLISECONDS.toSeconds(Long.parseLong(TimeOutConsts.TIME_OUT.text));
        return seconds;
    }

    public HashMap<String, String> commonParams(String fixletID) {
        HashMap<String, String> commonPara = new HashMap<>();
        commonPara.put(ConsoleConsts.SITE_TYPE.text, jsonParser.getSiteTypeObject().get(ConsoleConsts.EXTERNAL.text).getAsString());
        commonPara.put(ConsoleConsts.SITE_NAME.text, jsonParser.getSiteNameObject().get(ConsoleConsts.SERVER_AUTOMATION.text).getAsString());
        commonPara.put(ConsoleConsts.FIXLET_ID.text, fixletID);
        return commonPara;
    }
}