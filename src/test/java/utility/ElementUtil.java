package utility;

import enums.WaitTimes;
import helper.ScenarioContext;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.*;

import static helper.ScenarioContext.driver;

public class ElementUtil {

    public static WebElement waitUntilClickable(By elementLocator) {
        return ScenarioContext.wait.until(ExpectedConditions.elementToBeClickable(elementLocator));
    }

    public static WebElement waitUntilClickable(WebElement element) {
        return ScenarioContext.wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Determines whether the given {@param element} is visible on the
     * current HTML/page instance.  Implicitly calls
     * {@link #waitForVisibilityOfElement(WebElement)}.
     *
     * @see #waitForVisibilityOfElement(WebElement)
     * @param element
     * 			  - The {@link WebElement} element to check if it is
     * 				displayed
     * @return True if displayed, otherwise false
     */
    public static boolean isElementVisible(@NotNull WebElement element) {
        return element.isDisplayed();
    }

    /**
     * Determines whether the given {@param elementLocator} is visible on the
     * current HTML/page instance.  Implicitly calls
     * {@link #waitForVisibilityOfElement(By))}.
     *
     * @see #waitForVisibilityOfElement(By)
     * @param elementLocator
     * 			  - The {@link By} locator to check if the element is
     * 				displayed
     * @return True if displayed, otherwise false
     */
    public static boolean isElementVisible(By elementLocator) {
        WebElement element = waitForVisibilityOfElement(elementLocator, WaitTimes.WEBDRIVER_IMMEDIATE_WAIT_TIME, false);
        return element != null && element.isDisplayed();
    }

    /**
     * Determines whether the given {@param elementLocator} is present on the
     * current HTML DOM instance.  Implicitly calls
     * {@link #waitForPresenceOfElement(By))}.
     *
     * @see #waitForPresenceOfElement(By)
     * @param elementLocator
     * 			  - The {@link By} locator to check if the element is
     * 				displayed
     * @return True if displayed, otherwise false
     */
    public static boolean isElementPresent(By elementLocator) {
        return waitForPresenceOfElement(elementLocator, WaitTimes.WEBDRIVER_IMMEDIATE_WAIT_TIME, false) != null;
    }

    public static @NotNull By getXpath(String xpath, Object... elementLocator) {
        try {
            return By.xpath(String.format(xpath, elementLocator));
        } catch (MissingFormatArgumentException error) {
            throw new RuntimeException(String.format(
                    "Unable to create xpath '%s' from the input data due to an invalid number of arguments", xpath),
                    error);
        }
    }

    @Contract("_ -> new")
    public static @NotNull By getXpath(String xpath) {
        return By.xpath(xpath);
    }

    /**
     * Static utility method for returning the locator of the given {@param elementLocator}
     * object as a {@link String}.
     *
     * @param elementLocator
     *            The {@link By} object to get the locator from
     * @return The locator, as a {@link String} for the given {@param elementLocator}
     */
    public static @NotNull String getLocatorAsString(By elementLocator) {
        String original = Optional.ofNullable(elementLocator)
                .orElseThrow(() -> new NullPointerException("This locator has not been defined/is null"))
                .toString();
        return original.substring(original.indexOf(" ") + 1);
    }

    public static void click(By locator) {
        if(isElementVisible(locator)){
            click(waitForVisibilityOfElement(locator));
        }else{
            click(waitForPresenceOfElement(locator));
        }
    }

    public static void click(WebElement element) {
        try {
            scrollToElement(element);
            waitUntilClickable(element).click();
        } catch (WebDriverException stale) {
            scrollToElement(element);
            waitUntilClickable(element).click();
        }
    }

    public static void jsClick(By locator) {
        if(isElementVisible(locator)){
            jsClick(waitForVisibilityOfElement(locator));
        }else{
            jsClick(waitForPresenceOfElement(locator));
        }
    }

    public static WebElement jsClick(WebElement element) {
        try {
            scrollToElement(element);
            ((JavascriptExecutor) ScenarioContext.driver).executeScript("arguments[0].click()", element);
        } catch (WebDriverException stale) {
            scrollToElement(element);
            ((JavascriptExecutor) ScenarioContext.driver).executeScript("arguments[0].click()", element);
        }
        return element;
    }

    public static Object executeJs(String script, Object... args){
        return ((JavascriptExecutor) ScenarioContext.driver).executeScript(script, args);
    }

    public static String getText(WebElement element) {
        return waitForVisibilityOfElement(element).getText();
    }

    public static void enterText(WebElement element, String text) {
        waitForVisibilityOfElement(element).clear();
        waitForVisibilityOfElement(element).sendKeys(text);
        waitForVisibilityOfElement(element).sendKeys(Keys.ENTER);
    }

    public static void enterText(By elementLocator, String text){
        waitForVisibilityOfElement(elementLocator).clear();
        waitForVisibilityOfElement(elementLocator).sendKeys(text);
    }

    public static void enterText(By containerLocator, By elementLocator, String text){
        //Rewrite xpath for locating
        By locator = getXpath("(%s%s)",
                getLocatorAsString(containerLocator),
                getLocatorAsString(elementLocator));
        waitForVisibilityOfElement(locator).clear();
        waitForVisibilityOfElement(locator).sendKeys(text);
    }

    public static void enterTextByJs(WebElement element, String text) {
        ((JavascriptExecutor) ScenarioContext.driver).executeScript("arguments[0].value='" + text + "';", element);
    }

    public static void selectDropDownValue(WebElement element, String value) {
        waitForVisibilityOfElement(element);
        Select select = new Select(element);
        try {
            select.selectByVisibleText(value);
        } catch (NoSuchElementException no) {
            throw new InputMismatchException("There is no visible text value in the the dropdown list matching: " + value);
        }
    }

    /**
     * Waits for the given {@param elementLocator} to become invisible on
     * the current HTML DOM instance.  An initial implicit call is made
     * to {@link #waitForVisibilityOfElement(By, WaitTimes, boolean)} for allowing
     * time for the given {@param elementLocator} to load onto the page, if
     * not already, thus evaluating the invisibility of the element more
     * accurately.  Invisibility of an element is determined based
     * on the conditions defined within
     * {@link ExpectedConditions#invisibilityOfElementLocated(By)}, i.e.
     * element is either not visible or not present within the DOM.
     *
     * @see ExpectedConditions#invisibilityOfElementLocated(By)
     * @param elementLocator
     * 		      - The {@link By} locator to find the element
     * @return True if element is invisible, else false
     */
    public static boolean waitForInvisibilityOfElement(By elementLocator) {
        waitForVisibilityOfElement(elementLocator, WaitTimes.WEBSITE_ELEMENT_VISIBILITY_TIMEOUT, false);
        return ScenarioContext.wait.until(ExpectedConditions.invisibilityOfElementLocated(elementLocator));
    }

    /**
     * Overloaded version of {@link #waitForInvisibilityOfElement(By)}, which allows
     * the maximum timeout for checking the invisibility of an element to be specified
     * as {@param seconds}.
     *
     * @see #waitForInvisibilityOfElement(By)
     * @param elementLocator
     * 		      - The {@link By} locator to find the element
     * @param seconds
     *            - The maximum timeout allowed for the element to become
     *            invisible
     * @return True once element becomes invisible
     * @throws TimeoutException if element does not become invisible within
     *         {@code seconds}
     */
    public static boolean waitForInvisibilityOfElement(By elementLocator, int seconds) {
        waitForVisibilityOfElement(elementLocator, WaitTimes.WEBSITE_ELEMENT_VISIBILITY_TIMEOUT, false);
        return new WebDriverWait(ScenarioContext.driver, Duration.ofSeconds(seconds)).until(ExpectedConditions.invisibilityOfElementLocated(elementLocator));
    }

    /**
     * Overloaded version of {@link #waitForInvisibilityOfElement(By)}, which
     * allows the maximum {@param timeout} for checking the invisibility of
     * an element to be specified differently, from the default timeouts configured
     * in {@link #waitForInvisibilityOfElement(By)}.
     *
     * @see #waitForInvisibilityOfElement(By)
     * @param elementLocator
     * 		      - The {@link By} locator to find the element
     * @param timeout
     *            - The maximum timeout allowed for the element to become
     *            invisible
     * @return True once element becomes invisible
     * @throws TimeoutException if element does not become invisible within the given
     *         {@code timeout}
     */
    public static boolean waitForInvisibilityOfElement(By elementLocator, WaitTimes timeout) {
        return waitForInvisibilityOfElement(elementLocator, WaitTimes.WEBSITE_ELEMENT_VISIBILITY_TIMEOUT, timeout);
    }

    /**
     * Overloaded version of {@link #waitForInvisibilityOfElement(By)}, where the timeouts
     * for allowing the element to appear and disappear can be specified differently
     * from the default timeouts configured in {@link #waitForInvisibilityOfElement(By)}.
     *
     * @see #waitForInvisibilityOfElement(By)
     * @param elementLocator
     * 			  - The {@link By} locator to find the element
     * @param appearTimeout
     *            - The maximum timeout allowed for the element to become
     *            visible
     * @param disappearTimeout
     *    		  - The maximum timeout allowed for the element to become
     *            invisible
     * @return True once element becomes invisible
     * @throws TimeoutException if element does not become invisible within the given
     *         {@code disappearTimeout}
     */
    public static boolean waitForInvisibilityOfElement(By elementLocator, WaitTimes appearTimeout, @NotNull WaitTimes disappearTimeout) {
        waitForVisibilityOfElement(elementLocator, appearTimeout, false);
        return new WebDriverWait(ScenarioContext.driver, disappearTimeout.WAIT_TIME).until(ExpectedConditions.invisibilityOfElementLocated(elementLocator));
    }

    /**
     * Waits for the given {@param elementLocator} to become absent from the current
     * HTML DOM instance.  This differs from {@link #waitForInvisibilityOfElement(By)},
     * in that the absence of an element is defined by the logical opposite of
     * {@link ExpectedConditions#presenceOfElementLocated(By)}.  In other words,
     * if the element is not visible but is still present within the DOM then
     * that element is not considered absent, but it would be considered invisible
     * (which is mainly how this function differs compared to the OR predicate
     * logic defined within the {@link #waitForInvisibilityOfElement(By)} operation).
     * By default, {@link WaitTimes#WEBSITE_ELEMENT_PRESENCE_TIMEOUT} is the timeout
     * allowed for the element to become present before evaluating its absence
     * using the configured timeout of {@link WaitTimes#WEBDRIVER_STANDARD_WAIT_TIME}.
     * <p>
     * @see ExpectedConditions#presenceOfElementLocated(By)
     * @see #waitForInvisibilityOfElement(By)
     * @param elementLocator the {@link By} locator to find the element
     * @return True once element is absent from the DOM
     * @throws TimeoutException if element does not become absent within the
     *         configured timeout
     */
    public static boolean waitForAbsenceOfElement(By elementLocator) {
        waitForPresenceOfElement(elementLocator, WaitTimes.WEBSITE_ELEMENT_PRESENCE_TIMEOUT, false);
        return ScenarioContext.wait.until(ExpectedConditions.not(ExpectedConditions.presenceOfElementLocated(elementLocator)));
    }

    /**
     * Overloaded version of {@link #waitForAbsenceOfElement(By)}, where the timeout
     * for allowing the element to disappear can be specified differently from the
     * default timeouts configured in {@link #waitForAbsenceOfElement(By)}.
     *
     * @see #waitForAbsenceOfElement(By)
     * @param elementLocator
     * 	          - The {@link By} locator to find the element
     * @param timeout
     *            - The maximum timeout allowed for the element to become
     *            absent
     * @return True once element is absent from the DOM
     * @throws TimeoutException if element does not become absent within the given
     *         {@code timeout}
     */
    public static boolean waitForAbsenceOfElement(By elementLocator, WaitTimes timeout) {
        return waitForAbsenceOfElement(elementLocator, WaitTimes.WEBSITE_ELEMENT_PRESENCE_TIMEOUT, timeout);
    }

    /**
     * Overloaded version of {@link #waitForAbsenceOfElement(By)}, where the timeouts
     * for allowing the element to appear and disappear can be specified differently
     * from the default timeouts configured in {@link #waitForAbsenceOfElement(By)}.
     *
     * @see #waitForAbsenceOfElement(By)
     * @param elementLocator
     * 	          - The {@link By} locator to find the element
     * @param appearTimeout
     *            - The maximum timeout allowed for the element to become
     *            present
     * @param disappearTimeout
     *    		  - The maximum timeout allowed for the element to become
     *            absent
     * @return True once element is absent from the DOM
     * @throws TimeoutException if element does not become absent within the given
     *         {@code disappearTimeout}
     */
    public static boolean waitForAbsenceOfElement(By elementLocator, WaitTimes appearTimeout, @NotNull WaitTimes disappearTimeout) {
        waitForPresenceOfElement(elementLocator, appearTimeout, false);
        return new WebDriverWait(ScenarioContext.driver, disappearTimeout.WAIT_TIME)
                .until(ExpectedConditions.not(ExpectedConditions.presenceOfElementLocated(elementLocator)));
    }


    /**
     * Waits and locates a visible {@code element} within the current HTML
     * DOM instance.  Visibility of the {@link WebElement} is determined
     * based on the conditions defined by
     * {@link ExpectedConditions#visibilityOf(WebElement)}.
     *
     * @see ExpectedConditions#visibilityOf(WebElement)
     * @param element The {@link WebElement} to be located
     * @return The {@link WebElement} if located successfully
     * @throws TimeoutException Thrown by {@link FluentWait#until} if
     * timeout expires before element is located/becomes visible
     */
    public static WebElement waitForVisibilityOfElement(WebElement element) {
        return ScenarioContext.wait.until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Overridden version of {@link #waitForVisibilityOfElement(WebElement)}
     * method which instead takes specified {@param elementLocator} as a
     * {@link By} object type.  Visibility of the {@link WebElement} is determined
     * based on the conditions defined by
     * {@link ExpectedConditions#visibilityOfElementLocated(By)}.
     *
     * @see ExpectedConditions#visibilityOfElementLocated(By)
     * @param elementLocator The {@link By} to locate the {@link WebElement} with
     * @return The {@link WebElement} if located successfully
     * @throws TimeoutException Thrown by {@link FluentWait#until} if timeout expires
     * before element is located/becomes visible
     */
    public static WebElement waitForVisibilityOfElement(By elementLocator) {
        return ScenarioContext.wait.until(ExpectedConditions.visibilityOfElementLocated(elementLocator));
    }

    public static WebElement waitForVisibilityOfElement(By elementLocator, boolean strict) {
        return waitForVisibilityOfElement(elementLocator, Math.toIntExact(WaitTimes.WEBDRIVER_STANDARD_WAIT_TIME.WAIT_TIME.toSeconds()), strict);
    }

    public static WebElement waitForVisibilityOfElement(By elementLocator, @NotNull WaitTimes seconds) {
        return waitForVisibilityOfElement(elementLocator, Math.toIntExact(seconds.WAIT_TIME.toSeconds()));
    }

    public static WebElement waitForVisibilityOfElement(By elementLocator, int seconds) {
        return waitForVisibilityOfElement(elementLocator, seconds, true);
    }

    public static WebElement waitForVisibilityOfElement(By elementLocator, @NotNull WaitTimes waitTimes, boolean strict) {
        return waitForVisibilityOfElement(elementLocator, Math.toIntExact(waitTimes.WAIT_TIME.toSeconds()), strict);
    }

    public static @Nullable WebElement waitForVisibilityOfElement(By elementLocator, int seconds, boolean strict) {
        try {
            return new WebDriverWait(ScenarioContext.driver, Duration.ofSeconds(seconds))
                    .until(ExpectedConditions.visibilityOfElementLocated(elementLocator));
        } catch (TimeoutException e) {
            if(!strict) {
                return null;
            }
            throw e;
        }
    }

    public static WebElement waitForPresenceOfElement(By elementLocator) {
        return ScenarioContext.wait.until(ExpectedConditions.presenceOfElementLocated(elementLocator));
    }

    public static WebElement waitForPresenceOfElement(By elementLocator, boolean strict) {
        return waitForPresenceOfElement(elementLocator, Math.toIntExact(WaitTimes.WEBDRIVER_STANDARD_WAIT_TIME.WAIT_TIME.toSeconds()), strict);
    }

    public static WebElement waitForPresenceOfElement(By elementLocator, @NotNull WaitTimes seconds) {
        return waitForPresenceOfElement(elementLocator, Math.toIntExact(seconds.WAIT_TIME.toSeconds()));
    }

    public static WebElement waitForPresenceOfElement(By elementLocator, int seconds) {
        return waitForPresenceOfElement(elementLocator, seconds, true);
    }

    public static WebElement waitForPresenceOfElement(By elementLocator, @NotNull WaitTimes waitTimes, boolean strict) {
        return waitForPresenceOfElement(elementLocator, Math.toIntExact(waitTimes.WAIT_TIME.toSeconds()), strict);
    }

    public static @Nullable WebElement waitForPresenceOfElement(By elementLocator, int seconds, boolean strict) {
        try {
            return new WebDriverWait(ScenarioContext.driver, Duration.ofSeconds(seconds))
                    .until(ExpectedConditions.presenceOfElementLocated(elementLocator));
        } catch (TimeoutException e) {
            if(!strict) {
                return null;
            }
            throw e;
        }
    }

    public static List<WebElement> waitForPresenceOfElements(By elementLocator) {
        return ScenarioContext.wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(elementLocator));
    }

    public static List<WebElement> waitForPresenceOfElements(By elementLocator, boolean strict) {
        return waitForPresenceOfElements(elementLocator, Math.toIntExact(WaitTimes.WEBDRIVER_STANDARD_WAIT_TIME.WAIT_TIME.toSeconds()), strict);
    }

    public static List<WebElement> waitForPresenceOfElements(By elementLocator, @NotNull WaitTimes seconds) {
        return waitForPresenceOfElements(elementLocator, Math.toIntExact(seconds.WAIT_TIME.toSeconds()));
    }

    public static List<WebElement> waitForPresenceOfElements(By elementLocator, int seconds) {
        return waitForPresenceOfElements(elementLocator, seconds, true);
    }

    public static List<WebElement> waitForPresenceOfElements(By elementLocator, @NotNull WaitTimes waitTimes, boolean strict) {
        return waitForPresenceOfElements(elementLocator, Math.toIntExact(waitTimes.WAIT_TIME.toSeconds()), strict);
    }

    public static @Nullable List<WebElement> waitForPresenceOfElements(By elementLocator, int seconds, boolean strict) {
        try {
            if(strict) { // Perform if-statement to save on memory usage (i.e., reuse ScenarioContext#wait if strict = true)
                return ScenarioContext.wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(elementLocator));
            } else {
                return new WebDriverWait(ScenarioContext.driver, Duration.ofSeconds(seconds))
                        .until(ExpectedConditions.presenceOfAllElementsLocatedBy(elementLocator));
            }
        } catch (TimeoutException e) {
            if(!strict) {
                return null;
            }
            throw e;
        }
    }

    public static WebElement scrollToElement(By elementLocator) {
        return waitForVisibilityOfElement(elementLocator);
    }

    public static WebElement scrollToElement(WebElement element) {
        try {
            ((JavascriptExecutor) ScenarioContext.driver).executeScript(
                    "function scrollIntoView(el) {" + "var offsetTop = $(el).offset().top;"
                            + "var adjustment = Math.max(0,( $(window).height() - $(el).outerHeight(true) ) / 2);"
                            + "var scrollTop = offsetTop - adjustment;" + "$('html,body').animate({" + "scrollTop: scrollTop"
                            + "}, 0);" + "} scrollIntoView(arguments[0]);", element);
        } catch (WebDriverException web) {
            scrollIntoView(element);
        }
        return element;
    }

    private static WebElement scrollIntoView(WebElement element) {
        try {
            ((JavascriptExecutor) ScenarioContext.driver).executeScript("arguments[0].scrollIntoView(true);", element);
        } catch (WebDriverException web) {
            // Do nothing
        }
        return element;
    }


    public static By keyBoard(By elementLocator) {
/*        waitForVisibilityOfElement(elementLocator).clear();
        waitForVisibilityOfElement(elementLocator).sendKeys(Keys.TAB);
        waitForVisibilityOfElement(elementLocator).sendKeys(Keys.ENTER);
        return elementLocator;*/

        //  Keys cmdCtrl = Platform.getCurrent().is(Platform.WINDOWS) ? Keys.COMMAND : Keys.CONTROL;
        new Actions(driver)
                .sendKeys(Keys.TAB)
                .keyDown(Keys.ENTER);
        return elementLocator;
    }
}