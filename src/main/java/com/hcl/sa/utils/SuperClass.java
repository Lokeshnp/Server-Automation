package com.hcl.sa.utils;

import com.hcl.sa.capabilities.WinAppDriverCaps;
import com.hcl.sa.constants.WinAppConsts;
import com.thoughtworks.gauge.datastore.DataStore;
import com.thoughtworks.gauge.datastore.DataStoreFactory;
import io.appium.java_client.windows.WindowsDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.openqa.selenium.WebElement;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class SuperClass {

    private volatile  static SuperClass obj;
    private JsonElement jsonElement = null;
    public static DataStore specStore = DataStoreFactory.getSpecDataStore();
    private Logger logger = LogManager.getLogger(com.hcl.sa.utils.SuperClass.class);
    private Runtime runtime = null;
    private static Process process = null;
    private volatile WindowsDriver<WebElement> windowsDriver = null;


    private SuperClass() {
    }

    public static SuperClass getInstance() {
        if (obj == null){
            // To make thread safe
            synchronized (SuperClass.class) {
                // check again as multiple threads
                // can reach above step
                if (obj == null)
                    obj = new SuperClass();
            }
        }
        return obj;
    }

    public JsonElement getJsonParser(String filePath) {
        try {
            JsonParser  jsonParser = null;
            jsonParser = new JsonParser();
            jsonElement = jsonParser.parse(new FileReader(filePath));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return jsonElement;
    }

    public WindowsDriver<WebElement> getWinAppDriver(String exePath) {
        if (windowsDriver == null){
            try {
                startWinAppDriver();
                windowsDriver = new WindowsDriver<WebElement>(new URL(WinAppConsts.WIN_APP_DRIVER_REMOTE_SERVER_URL.value), WinAppDriverCaps.getCapabilities(exePath));
                windowsDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        return windowsDriver;
    }

    public static Process startWinAppDriver()throws IOException, InterruptedException {
        String cmd = WinAppConsts.WINAPP_DRIVER_INVOKE_CMD.value;
        process =  getInstance().getRunTime().exec(cmd);
        return process;
    }

    public Runtime getRunTime(){
        runtime = Runtime.getRuntime();
        return runtime;
    }

    public void killWinAppDriver(){
        try {
            getInstance().getRunTime().exec(WinAppConsts.CLOSE_WINAPP_EXE.value);
            logger.info("Win app driver server is stopped");
            closeCmd();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeCmd(){
        try {
            getInstance().getRunTime().exec(WinAppConsts.CLOSE_CMD.value);
            logger.info("Command Prompt is closed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}