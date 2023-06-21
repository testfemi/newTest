package helper;

import enums.BrowserOptions;
import enums.Pages;
import enums.PlatformOptions;
import enums.WaitTimes;
import io.cucumber.java.Scenario;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Date;

/**
 * Class to act as a shared data store across the framework for variables and data values which
 * specifically relate to the current scenario/test under execution.  This is useful for when we
 * have situations where we need to retain and persist data values across different scenarios/tests
 * within the same execution cycle.
 * <p>
 * (NOTE: Variables of this class must be referenced statically)
 */
public class ScenarioContext {
    public static boolean
            developerMode = Boolean.parseBoolean(TestConfig.getPropertyValue("developer_mode"));
    public static Date currentTestStartTime,
            currentTestFinishTime,
            executionStartTime,
            executionFinishTime;

    public static WebDriver driver;

    // WebDriverWait configured with standard wait time for acquiring elements
    public static FluentWait<WebDriver> wait;

    public static PlatformOptions platform;

    public static BrowserOptions browser;

    public static Scenario scenario;

    public static Pages
            currentPage,
            previousPage;

}