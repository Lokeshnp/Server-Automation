package com.hcl.sa.utils;


import com.hcl.sa.constants.TimeOutConsts;
import io.appium.java_client.windows.WindowsDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class WinAppDriverActions {


    public WindowsDriver winDriver = null;
    Logger logger = LogManager.getLogger(WinAppDriverActions.class);

    public WinAppDriverActions(WindowsDriver windDriver) {
        this.winDriver = windDriver;
    }

    public String getCurrentWindowHandle() {
        return winDriver.getWindowHandle();
    }

    public Set<String> getChildWindowsHandle() {
        return winDriver.getWindowHandles();
    }

    public void switchToCurrentWindow() {
        winDriver.switchTo().window(getCurrentWindowHandle());
    }

    public void switchToChildWindow() {
        String parentWindow = getCurrentWindowHandle();
        Set<String> winHandles = getChildWindowsHandle();
        Iterator<String> it = winHandles.iterator();
        while (it.hasNext()) {
            String childWindow = it.next();
            if (parentWindow != childWindow) {
                winDriver.switchTo().window(childWindow);
            }
        }

    }

    public void rightClickUsingKeyBoard() {
        Actions action = new Actions(winDriver);
        action.sendKeys(Keys.chord(Keys.SHIFT, Keys.F10)).build().perform();
        action.release().build().perform();
    }

    public void moveByOffsetAndClick(int x, int y) {
        Actions act = new Actions(winDriver);
        act.moveByOffset(x, y).click().build().perform();
    }

    public Actions moveToWebElement(WebElement element) {
        Actions act = new Actions(winDriver);
        act.moveToElement(element).pause(Duration.ofSeconds(2)).build().perform();
        return act;
    }

    public void rightClick(WebElement ele) {
        Actions act = new Actions(winDriver);
        act.contextClick(ele).pause(Duration.ofSeconds(TimeOutConsts.WAIT_1_SEC.seconds)).build().perform();
    }

    public void moveToWebElementAndClick(WebElement element) {
        Actions act = new Actions(winDriver);
        act.moveToElement(element).click().build().perform();
    }

    public WebElement findElementByName(String name) {
        return winDriver.findElementByName(name);
    }

    public WebElement findElementByXpath(String xpath) {
        return winDriver.findElementByXPath(xpath);
    }

    public WebElement findElementByClassName(String xpath) {
        return winDriver.findElementByClassName(xpath);
    }

    public WebElement findElementByAccessibilityId(String Id) {
        return winDriver.findElementByAccessibilityId(Id);
    }

    public List<WebElement> findElementsByAccessibilityId(String Id) {
        return winDriver.findElementsByAccessibilityId(Id);
    }

    public List<WebElement> findElementsByClassName(String className) {
        return winDriver.findElementsByClassName(className);
    }

    public List<WebElement> findElementsByName(String name) {
        return winDriver.findElementsByName(name);
    }

    public List<WebElement> findElementsByXpath(String xpath) {
        return winDriver.findElementsByXPath(xpath);
    }

    public void doubleClick(WebElement wb) {
        Actions act = new Actions(winDriver);
        act.doubleClick(wb).build().perform();
    }

    public List<WebElement> findElementsByTagName(String tagName){
        return  winDriver.findElementsByTagName(tagName);
    }

    public void closeWindowApp() {
        winDriver.closeApp();
    }

    public void hardWait(int milliSeconds) {
        try {
            Thread.sleep(milliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void pressTab() {
        Actions act = new Actions(winDriver);
        act.sendKeys(Keys.TAB).build().perform();
    }

    public Actions pressKeyboardKey(int noOfTimes, CharSequence keys) {
        Actions act = new Actions(winDriver);
        for (int i = 0; i < noOfTimes; i++) {
            act.sendKeys(keys).build().perform();
        }
        act.release();
        return act;
    }



    public void waitForListVisibility(List<WebElement> list, int timeout) {
        logger.debug("Waiting for list visibility whose size is ", timeout);
        WebDriverWait wait = new WebDriverWait(winDriver, timeout);
        wait.until(ExpectedConditions.visibilityOfAllElements(list));
    }

    public void waitForNoOfWindowsToBe(int expNoOfWin, int timeout) {
        WebDriverWait wait = new WebDriverWait(winDriver, timeout);
        wait.until(ExpectedConditions.numberOfWindowsToBe(expNoOfWin));
    }

    public void waitForElementVisibilityAndClick(WebElement ele, int timeout) {
        WebDriverWait wait = new WebDriverWait(winDriver, timeout);
        wait.until(ExpectedConditions.visibilityOf(ele)).click();
    }

    public void waitForElementVisibility(WebElement ele, int timeout) {
        WebDriverWait wait = new WebDriverWait(winDriver, timeout);
        wait.until(ExpectedConditions.visibilityOf(ele));
    }


    public void waitForElementInvisibility(WebElement ele, int timeout) {
        WebDriverWait wait = new WebDriverWait(winDriver, timeout);
        wait.until(ExpectedConditions.invisibilityOf(ele));
    }


}

