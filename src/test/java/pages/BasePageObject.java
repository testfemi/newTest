package pages;

import enums.Pages;
import enums.WaitTimes;
import helper.Log;
import helper.ScenarioContext;
import org.jetbrains.annotations.NotNull;
import utility.TimeLimit;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.*;

public class BasePageObject {

    /* Store the current Iframe here rather than in ScenarioContext, as this could be
    different for each specific page object */
    protected static By currentIframe; //Track the current iframe of the application
    protected static String
            frameByNameXpath = "//iframe[@name='%s']", //Common xpath to identify iframe element by name
           frameByTitleXpath = "//iframe[@title='%s']"; //Common xpath to identify iframe element by title



    /**
     * Instructs the current {@link ScenarioContext#driver} to switch to the default iframe
     * for the current page by implicitly calling {@link WebDriver.TargetLocator#defaultContent()}.
     * This action will be attempted until the {@link WaitTimes#WEBDRIVER_DEFAULT_IFRAME_TIMEOUT}
     * timeout has elapsed, or until the default iframe has been successfully switched to.
     *
     * @see WebDriver.TargetLocator#defaultContent()
     * @see WaitTimes#WEBDRIVER_DEFAULT_IFRAME_TIMEOUT
     */
    public void defaultIframe() {
        TimeLimit limit = new TimeLimit(WaitTimes.WEBDRIVER_DEFAULT_IFRAME_TIMEOUT);
        Exception lastException = null;
        while(limit.timeLeft()) {
            try {
                ScenarioContext.driver.switchTo().defaultContent();
                currentIframe = null;
                return;
            }catch(Exception e) {
                lastException = e;
            }
        }
        Log.error(lastException, "Failed to switch to the default iframe after waiting %d seconds",
                Math.toIntExact(WaitTimes.WEBDRIVER_DEFAULT_IFRAME_TIMEOUT.WAIT_TIME.toSeconds()));
    }

    public void changeIframe(String iframeName) {
        changeIframe(By.xpath(String.format(frameByNameXpath, iframeName)));
    }

    public void changeIframe(By iframeLocator) {
        try {
            ScenarioContext.wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(iframeLocator));
            currentIframe = iframeLocator;
        } catch (WebDriverException web) {
            if (web.getMessage().contains("element is not attached to the page document")
                    || web.getMessage().contains("Object id doesn't reference a Node")) {
                changeIframe(iframeLocator);
                return;
            }
            throw web;
        }
    }

    public void changeIframeByTitle(String title) {
        changeIframe(By.xpath(String.format(frameByTitleXpath, title)));
    }

    public void changeToIframeFromDefaultIframe(String iframeLocator) {
        defaultIframe();
        changeIframe(iframeLocator);
    }

    public void changeToIframeFromDefaultIframe(By iframeLocator) {
        defaultIframe();
        changeIframe(iframeLocator);
    }

    public void changeToIframeFromDefaultIframeByTitle(String title) {
        defaultIframe();
        changeIframeByTitle(title);
    }

    public By getCurrentFrame() {
        return currentIframe;
    }

    /**
     * Cleans the current browser session by deleting all cookies in this current
     * session/domain (i.e., implicitly calls {@link #deleteSessionCookies()}) and
     * then refreshing the browser session (i.e., implicitly calls
     * {@link #refreshSession()}).
     *
     * @see #deleteSessionCookies()
     * @see #refreshSession()
     */
    public void cleanSession() {
        deleteSessionCookies();
        refreshSession();
    }

    /**
     * Refreshes the current browser session/page by implicitly calling
     * {@link WebDriver.Navigation#refresh()}.
     *
     * @see WebDriver.Navigation#refresh()
     */
    protected void refreshSession() {
        ScenarioContext.driver.navigate().refresh();
    }

    /**
     * Deletes all cookies in the current browser session/domain by implicitly calling
     * {@link WebDriver.Options#deleteAllCookies()}.
     *
     * @see WebDriver.Options#deleteAllCookies()
     */
    public void deleteSessionCookies() {
        ScenarioContext.driver.manage().deleteAllCookies();
    }

    public static void switchToTab(int tab) {
        ArrayList<String> tabs = new ArrayList<>(ScenarioContext.driver.getWindowHandles());
        ScenarioContext.driver.switchTo().window(tabs.get(tab));
    }

    public static void switchToTabForPage(@NotNull Pages page) {
        page.switchToTab();
    }

    public void acceptAlert() {
        try {
            ScenarioContext.driver.switchTo().alert().accept();
        } catch (NoAlertPresentException noAlertPresent) {
            // Alert no longer present, so just continue
        }
    }

}