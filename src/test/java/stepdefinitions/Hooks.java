package stepdefinitions;

import enums.WaitTimes;
import factory.WebDriverFactory;
import helper.Log;
import helper.ScenarioContext;
import helper.TestConfig;
import helper.TestEvidence;

import java.io.IOException;
import java.util.Properties;

import io.cucumber.java.*;

import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Hooks of code which run before and/or after each scenario within an execution cycle.
 *
 * @author Created by Femi on 01/08/2022.
 */
public class Hooks {

    /**
     * Executes the following tasks (ONLY once) when this class is first loaded into
     * memory/before any scenarios are executed, in order to initialise reporting and
     * evidence capturing for this execution:
     * <ul>
     * <li>Initialise a new execution summary report for this execution cycle (implicitly
     * calls {@link TestEvidence#initialiseExecutionSummaryReport()}).</li>
     * <li>Save all property configurations configured for this execution cycle to the
     * new evidence folder created (implicitly calls
     * {@link TestEvidence#saveToExecutionEvidence(Properties, String)})</li>
     * </ul>
     * <p>
     * @see BeforeAll
     * @throws RuntimeException if execution cannot be initialised
     */
    @BeforeAll
    public static void initializeExecution() {
        //Run before any scenarios have executed
        try {
            TestEvidence.initialiseExecutionSummaryReport(); //Create new dir for storing evidence for tests in this execution
            //Capture TestConfig in test evidence folder for this scenario
            Properties props = TestConfig.getAllConfigsAsProperties();
            if(props != null){
                TestEvidence.saveToExecutionEvidence(TestConfig.getAllConfigsAsProperties(), "automation_framework_config");
            }
        } catch (Throwable e) {
            Log.error("Failed to perform static initialisation of Hooks");
            throw new RuntimeException(e);
        }
    }

    /**
     * Executes the following tasks before every test scenario:
     * <ul>
     * <li>Read the "config.properties" file and the ".properties" file referenced against the
     * "test_env" property in "config.properties" (if present).</li>
     * <li>Initialise test evidence reporting (implicitly calls
     * {@link TestEvidence#initialiseTestEvidence()}).</li>
     * <li>Initialise WebDriver and Start Browser session (implicitly calls
     * {@link WebDriverFactory#initialiseWebDriver()}).</li>
     * </ul>
     * <p>
     * @see Before
     * @param scenario the Scenario object which gets automatically passed in by Cucumber,
     *                 providing information about the scenario currently under execution.
     * @throws IOException cascaded from {@link TestEvidence#createTestEvidenceDirectory()}.
     */
    @Before
    public void initializeTest(Scenario scenario) throws IOException, NoSuchFieldException, IllegalAccessException {
        ScenarioContext.scenario = scenario; //Attach Scenario object to Hooks class

        //Create a new folder to store evidence for the current SUT and initialise the Log for this SUT
        TestEvidence.initialiseTestEvidence();
        Log.info("Initiating new Test Scenario: %s", ScenarioContext.scenario.getName());

        //Initialise WebDriver
        Log.info("Configuring WebDriver");
        ScenarioContext.driver = WebDriverFactory.initialiseWebDriver(); //Save WebDriver to ExecutionContext as well
        assert ScenarioContext.driver != null;
        ScenarioContext.wait = new WebDriverWait(ScenarioContext.driver, WaitTimes.WEBDRIVER_STANDARD_WAIT_TIME.WAIT_TIME)
                .pollingEvery(WaitTimes.WEBDRIVER_POLLING_WAIT_TIME.WAIT_TIME)
                .ignoring(StaleElementReferenceException.class);

        //Log all framework property configurations which will be used for this test case
        Properties props = TestConfig.getAllConfigsAsProperties();
        if(props != null) {
            Log.debug("List of Framework Configuration properties:");
            props.stringPropertyNames()
                    .forEach(prop -> Log.debug("%s=%s", prop, props.getProperty(prop)));
        }

        //Log local system properties
        Log.debug("List of System properties:");
        Properties sysProps = System.getProperties();
        sysProps.stringPropertyNames()
                .forEach(prop -> Log.debug("%s=%s", sysProps, sysProps.getProperty(prop)));
    }

    /**
     * Executes the following tasks before every test step:
     * <ul>
     * <li>Instruct test evidence to begin evidence reporting on the new/current BDD
     * step (implicitly calls {@link TestEvidence#startStep()}).</li>
     * </ul>
     * <p>
     * @see BeforeStep
     * @param scenario the Scenario object which gets automatically passed in by Cucumber,
     *                 providing information about the scenario currently under execution.
     */
    @BeforeStep
    public void beforeStep(Scenario scenario) {
        TestEvidence.startStep();
    }

    /**
     * Executes the following tasks after every test step:
     * <ul>
     * <li>Capture screenshot (implicitly calls
     * {@link TestEvidence#captureScreenshotAsTestEvidence(com.aventstack.extentreports.Status)}).</li>
     * <li>Instruct test evidence to finish evidence reporting for the current BDD step (implicitly
     * calls {@link TestEvidence#finishStep()}).</li>
     * </ul>
     * <p>
     * @see AfterStep
     * @param scenario the Scenario object which gets automatically passed in by Cucumber, providing
     *                 information about the scenario currently under execution.
     */
    @AfterStep
    public void afterStep(Scenario scenario) {
        Log.info("Capturing Screenshot");
        TestEvidence.captureScreenshotAsTestEvidence(com.aventstack.extentreports.Status.INFO);
        TestEvidence.finishStep();
    }

    /**
     * Executes the following tasks after every test scenario:
     * <ul>
     * <li>Log scenario pass/failure result.</li>
     * <li>Capture screenshot (implicitly calls
     * {@link TestEvidence#captureScreenshotAsTestEvidence(com.aventstack.extentreports.Status, String)}).</li>
     * <li>If {@link ScenarioContext#developerMode} is false, shutdown WebDriver instance (implicitly
     * calls {@link WebDriver#quit()}).</li>
     * <li>Instruct test evidence to finish capturing evidence for this scenario (implicitly calls
     * {@link TestEvidence#saveTestEvidence()}).</li>
     * </ul>
     * <p>
     * @see After
     * @param scenario the Scenario object which gets automatically passed in by Cucumber, providing
     *                 information about the scenario currently under execution.
     */
    @After
    public void teardownTest(Scenario scenario) {
        if (scenario.getStatus() == Status.FAILED){ //If scenario failed, perform these steps
            Log.error("SCENARIO FAILED");
            Log.info("Logging Scenario Failure in Test Evidence");
            Log.info("Capturing Failure Screenshot");
            TestEvidence.captureScreenshotAsTestEvidence(com.aventstack.extentreports.Status.FAIL,
                    "FAILURE_SCREENSHOT");
        }else if (scenario.getStatus() == Status.PASSED){ //If scenario passed, perform these steps
            Log.info("SCENARIO PASSED");
            Log.info("Logging Scenario Pass in Test Evidence");
            Log.info("Capturing Pass Screenshot");
            TestEvidence.captureScreenshotAsTestEvidence(com.aventstack.extentreports.Status.PASS,
                    "PASS_SCREENSHOT");
            Log.info("Shutting down WebDriver gracefully");
        }
        if(ScenarioContext.developerMode){ //If executing in developer mode, DO NOT kill/delete the WebDriver instance
            Log.warn("Developer Mode active, WebDriver WILL NOT be killed");
        } else{ //If not in developer mode, kill/delete the WebDriver instance
            if(ScenarioContext.driver != null){
                ScenarioContext.driver.quit();
                Log.info("WebDriver killed");
            }
        }
        TestEvidence.saveTestEvidence();
    }

    /**
     * Executes the following tasks (ONLY once) once all scenarios have been executed, in
     * order to finalise reporting and evidence capturing for this execution:
     * <ul>
     * <li>Finish capturing and save evidence for this execution cycle (implicitly calls
     * {@link TestEvidence#saveExecutionEvidence()}).</li>
     * </ul>
     * <p>
     * @see AfterAll
     */
    @AfterAll
    public static void teardownExecution() {
        //Run after all scenarios have executed
        TestEvidence.saveExecutionEvidence();

    }
}