package com.hcl.sa.capabilities;


import com.hcl.sa.constants.WinAppConsts;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.remote.DesiredCapabilities;


public class WinAppDriverCaps {

    private static DesiredCapabilities caps;
    private static final Logger logger = LogManager.getLogger(WinAppDriverCaps.class);

    private WinAppDriverCaps(){
    }



    public static DesiredCapabilities getCapabilities(String exePath)
    {
        if (caps == null) {
            caps = new DesiredCapabilities();
            logger.debug("Win App Driver Caps Key="+WinAppConsts.WIN_APP_DRIVER_CAP_APP.value);
            logger.debug("Win App Driver Exe path="+exePath);
            caps.setCapability(WinAppConsts.WIN_APP_DRIVER_CAP_APP.value,exePath);
            caps.setCapability("ms:experimental-webdriver", false);

        }
        return caps;
    }

}
