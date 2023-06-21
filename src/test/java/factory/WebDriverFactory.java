package factory;

import helper.Log;
import helper.TestConfig;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * Utility class for configuring the {@link WebDriver} for performing automated execution.  This
 * class makes implicit calls to {@link PlatformFactory} and {@link BrowserFactory} in order to
 * set specific properties and {@link DesiredCapabilities} to the WebDriver instance, e.g., the
 * browser type, platform type, window size, browser version, headless mode for Chrome etc...
 */
public class WebDriverFactory {

    /**
     * Creates the WebDriver instance based on the property configurations loaded
     * in by {@link TestConfig}.  At a high-level, the following is
     * configured:
     * <ol type="1">
     * <li>Configure the desired OS/device Platform capabilities for the driver.</li>
     * <li>Configure the desired Web Browser capabilities for the driver.</li>
     * <li>Create and Launch the WebDriver with all these capabilities configured.</li>
     * </ol>
     * <p>
     * @return WebDriver
     */
    public static WebDriver initialiseWebDriver() {
        //Define new object for allocating desired capabilities for the WebDriver
        DesiredCapabilities driver_capabilities = new DesiredCapabilities();

        //Retrieve what specific environment to run Selenium in (either "local" or "remote" version)
        String seleniumEnv = TestConfig.getPropertyValue("selenium_env");
        Log.info("Selenium run-time environment identified as \"%s\"", seleniumEnv);

        //If Selenium is to be executed locally, set these appropriate capabilities
        if(seleniumEnv.equalsIgnoreCase("local")){
            /* Configure Platform for the WebDriver (if this property DOES NOT exist
            on the local system's properties configuration, return the value provided by
            the (@link TestConfig))*/
            Log.info("Configuring Desired Platform Capabilities for WebDriver");
            PlatformFactory.initialisePlatform(driver_capabilities);
            Log.info("Configuring Desired Browser Capabilities for WebDriver");
            return BrowserFactory.initialiseBrowser(driver_capabilities);
        }

        //Return null if this point is reached (i.e., WebDriver not properly supported/created)
        return null;
    }

}