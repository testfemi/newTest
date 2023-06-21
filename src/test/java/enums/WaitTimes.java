package enums;

import helper.TestConfig;

import java.time.Duration;

/**
 * Enum values representing the common wait times used throughout the framework.
 */
public enum WaitTimes {

    WEBDRIVER_STANDARD_WAIT_TIME("WEBDRIVER_STANDARD_WAIT_TIME"),
    WEBDRIVER_IMMEDIATE_WAIT_TIME("WEBDRIVER_IMMEDIATE_WAIT_TIME"),
    WEBDRIVER_POLLING_WAIT_TIME("WEBDRIVER_POLLING_WAIT_TIME"),
    WEBDRIVER_DEFAULT_IFRAME_TIMEOUT("WEBDRIVER_DEFAULT_IFRAME_TIMEOUT"),
    WEBSITE_CONNECTIVITY_TIMEOUT("WEBSITE_CONNECTIVITY_TIMEOUT"),
    WEBSITE_ELEMENT_VISIBILITY_TIMEOUT("WEBSITE_ELEMENT_VISIBILITY_TIMEOUT"),
    WEBSITE_ELEMENT_PRESENCE_TIMEOUT("WEBSITE_ELEMENT_PRESENCE_TIMEOUT"),
    HTTP_REQUEST_TIMEOUT("HTTP_REQUEST_TIMEOUT"),
    TABLE_GET_CELL_FROM_COLUMN_TIMEOUT("TABLE_GET_CELL_FROM_COLUMN_TIMEOUT");

    public Duration WAIT_TIME;

    WaitTimes(int waitTimeInSeconds) {
        WAIT_TIME = Duration.ofSeconds(waitTimeInSeconds);
    }

    WaitTimes(String waitTimeProperty) {
        try{
            WAIT_TIME = Duration.ofSeconds(Integer.parseInt(TestConfig.getPropertyValue(waitTimeProperty)));
        }catch (NullPointerException propertyNotFound){
            //If config property is not found, try parsing as an Integer
            WAIT_TIME = Duration.ofSeconds(Integer.parseInt(waitTimeProperty));
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Overrides the inherited {@link Object#toString()} method to return
     * the {@link #WAIT_TIME} for the current {@link CommonXpathLocators}
     * enum by implicitly calling {@link String#valueOf(Object)}.
     *
     * @return the {@link #WAIT_TIME} as a {@link String}
     */
    @Override
    public String toString() {
        return String.valueOf(WAIT_TIME);
    }

}