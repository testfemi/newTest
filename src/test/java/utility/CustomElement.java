package utility;

import enums.WaitTimes;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

public class CustomElement {

    private WebElement element;
    private By locator;
    private boolean expectedVisible;

    public CustomElement(By locator){
        new CustomElement(locator, true);
    }

    public CustomElement(WebElement element, By locator){
        new CustomElement(element, locator, true);
    }

    public CustomElement(By locator, boolean expectedVisible) {
        this.locator = locator;
        this.expectedVisible = expectedVisible;
        try {
            refreshElement();
        } catch(WebDriverException e) {
            wasElementDetached(e);
        }
    }

    private CustomElement(WebElement element, By locator, boolean expectedVisible) {
        this.element = element;
        this.locator = locator;
        this.expectedVisible = expectedVisible;
        try {
            refreshElement();
        } catch(WebDriverException e) {
            wasElementDetached(e);
        }
    }

    public By getLocator() {
        return locator;
    }

    public String getLocatorAsString() {
        return ElementUtil.getLocatorAsString(locator);
    }

    public WebElement getElement(){
        return element;
    }

    public String getText() {
        return ElementUtil.getText(element);
    }

    public void enterText(String text) {
        ElementUtil.enterText(element, text);
    }

    /**
     * Gets the 'type' attribute for this element
     *
     * @return type attribute
     */
    public String getType() {
        return getAttribute("type");
    }

    /**
     * Gets the specified attribute as a String
     *
     * @param attr Attribute to retrieve
     * @return the value of this attribute as a {@link String}, else null
     */
    public String getAttribute(String attr) {
        try {
            return element.getAttribute(attr);
        } catch (WebDriverException exception) {
            wasElementDetached(exception);
            return getAttribute(attr);
        }
    }

    public CustomElement getChild(By childLocator) {
        By childXpath = By.xpath(getLocatorAsString() + ElementUtil.getLocatorAsString(childLocator));
        return new CustomElement(childXpath, false);
    }

    public CustomElement getChild(By childLocator, WaitTimes waitTime) {
        By childXpath = By.xpath(getLocatorAsString() + ElementUtil.getLocatorAsString(childLocator));
        return new CustomElement(ElementUtil.waitForPresenceOfElement(childXpath, waitTime), childXpath, false);
    }

    public List<CustomElement> getChildren(By childLocator, WaitTimes waitTime) {
        try {
            By childrenXpath = By.xpath(getLocatorAsString() + ElementUtil.getLocatorAsString(childLocator));
            List<WebElement> elements = ElementUtil.waitForPresenceOfElements(childrenXpath, waitTime);
            return elements
                    .stream()
                    .map(elem -> new CustomElement(elem, By.xpath(String.format("(%s)[%d]", childrenXpath, elements.indexOf(elem) + 1)), false))
                    .collect(Collectors.toList());
        } catch (WebDriverException e) {
            wasElementDetached(e);
            return getChildren(locator);
        }
    }

    public List<CustomElement> getChildren(By childLocator){
        try {
            By childrenXpath = By.xpath(getLocatorAsString() + ElementUtil.getLocatorAsString(childLocator));
            List<WebElement> elements = ElementUtil.waitForPresenceOfElements(childrenXpath);
            return elements
                    .stream()
                    .map(elem -> new CustomElement(elem, By.xpath(String.format("(%s)[%d]", childrenXpath, elements.indexOf(elem) + 1)), false))
                    .collect(Collectors.toList());
        } catch (WebDriverException e) {
            wasElementDetached(e);
            return getChildren(locator);
        }
    }

    /**
     * Given a {@link WebDriverException} has occurred, this checks to see if it was
     * in instance of {@link StaleElementReferenceException} or contains a message
     * indicating a context failure.  If so, then the element was detached and
     * therefore can be refreshed.
     *
     * @param exception {@link WebDriverException} The exception which occurred
     * @throws WebDriverException If it does not indicate that the element
     * has been detached
     */
    private void wasElementDetached(WebDriverException exception) {
        if (exception instanceof StaleElementReferenceException
                || exception.getMessage().contains("Cannot find context with specified id")) {
            refreshElement();
            return;
        }
        throw exception;
    }

    /**
     * Refreshes the instance of this SmartElement and finds the element again
     * within the DOM
     */
    public void refreshElement() {
        element = expectedVisible
                ? ElementUtil.waitForVisibilityOfElement(locator)
                : ElementUtil.waitForPresenceOfElement(locator);
    }

}