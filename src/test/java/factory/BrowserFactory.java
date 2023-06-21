package factory;

import enums.BrowserOptions;
import helper.Log;
import helper.ScenarioContext;
import helper.TestConfig;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.*;

/**
 * Utility class which configures the {@link DesiredCapabilities} for the browser
 * of the {@link WebDriver}.
 */
public class BrowserFactory {

    /**
     * Based on the "browser" property-value configured in the {@link TestConfig}, apply the
     * applicable set of pre-configured capabilities hardcoded into this method for configuring
     * the browser of the {@link WebDriver} instance.
     * <p>
     * Note: The {@param caps} provided should already include the platform capabilities, by
     * being passed through the {@link PlatformFactory#initialisePlatform(DesiredCapabilities)}
     * method, before being supplied to this method.
     *
     * @param caps the {@link DesiredCapabilities} object to set capabilities for
     * @return the {@link WebDriver} instance configured with the {@param caps} given
     * and with the additional hardcoded capabilities set out in this method for the
     * applicable browser type
     * @see CapabilityType documentation for further details on what available capabilites can
     * be configured.
     */
    public static WebDriver initialiseBrowser(DesiredCapabilities caps) {
        // Retrieve the "browser" property-value specified from the TestConfig (i.e. from the applicable ".properties" file)
        ScenarioContext.browser = BrowserOptions.valueOf(TestConfig.getPropertyValue("browser").toUpperCase());

        //Set browser type based on the available options in BrowserOptions
        Log.debug("SETTING BROWSER CAPABILITY: Set Browser type to \"%s\"", ScenarioContext.browser.name());
        caps.setBrowserName(ScenarioContext.browser.browserName());

        //Set browser's page loading strategy
        PageLoadStrategy pls = PageLoadStrategy.NORMAL; //Waits for entire web page to load
        Log.debug("SETTING BROWSER CAPABILITY: Set Page Load Strategy to \"%s\"", pls.name());
        caps.setCapability(CapabilityType.PAGE_LOAD_STRATEGY, pls);

        WebDriver driver;
        //Set additional capabilities specific to each type of browser
        switch (ScenarioContext.browser) {
            case CHROME -> {
                ChromeOptions ops = new ChromeOptions();
                ops.addArguments("--remote-allow-origins=*");
                ops = ops.merge(caps); //Add given caps to ChromeOptions object to merge options together
                Log.info("Creating WebDriver for Chrome browser compatibility");
                driver = new ChromeDriver(ops.addArguments(TestConfig.getPropertyValues("chrome_driver_options")));
            }
            case IE -> {
                Log.info("Creating WebDriver for Microsoft Internet Explorer (IE) browser compatibility");
                /* TODO - Retrieve additional capabilities from TestConfig/.properties file
                           (i.e., .capabilities(getListOfCapabilitiesFromProperty("ie_driver_options")) */
                driver = new InternetExplorerDriver();
            }
            case EDGE -> {
                Log.info("Creating WebDriver for Microsoft Edge browser compatibility");
                /* TODO - Retrieve additional capabilities from TestConfig/.properties file
                           (i.e., .capabilities(getListOfCapabilitiesFromProperty("ie_driver_options")) */
                driver = new EdgeDriver();
            }
            case FIREFOX -> {
                Log.info("Creating WebDriver for Firefox browser compatibility");
                /* TODO - Retrieve additional capabilities from TestConfig/.properties file
                           (i.e., .capabilities(getListOfCapabilitiesFromProperty("ie_driver_options")) */
                driver = new FirefoxDriver();
            }
            default -> {
                Log.warn("Browser Not Specified");
                Log.info("Setting Default Platform Capabilities:");
                throw new WebDriverException("No default browser configuration available.  You must specify a browser.");
            }
        }

        //Return the driver once it has been configured successfully
        return driver;
    }

}