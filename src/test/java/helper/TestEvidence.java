package helper;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import io.cucumber.core.backend.TestCaseState;
import io.cucumber.java.AfterAll;
import io.cucumber.java.BeforeAll;
import io.cucumber.plugin.event.PickleStepTestStep;
import io.cucumber.plugin.event.Step;
import io.cucumber.plugin.event.TestCase;

import org.apache.commons.io.FileUtils;

import factory.LogFactory;
import runner.TestRunner;
import stepdefinitions.Hooks;
import utility.DateHandler;
import utility.FileHandler;
import utility.ScreenshotHandler;

/**
 * Class for managing the evidence capturing and reporting which takes place for each execution cycle and all scenarios which execute
 * as part of that cycle.
 */
public class TestEvidence {

    private static String executionEvidenceDirectoryPath; // Folder containing the test evidence folders for each scenario in the current execution cycle
    private static String testEvidenceDirectoryPath; // Folder containing the evidence for the current scenario-under-test (SUT)
    private static ExtentReports executionSummaryReport; // The execution summary report for the current execution cycle

    /* The current SUT, so that we can perform evidence capturing and reporting of this scenario in the execution summary report */
    private static ExtentTest test,

            /* The current BDD step, so that we can perform evidence capturing and reporting of this scenario in the execution
            summary report */
            testStep = null;

    /* Collection of all the BDD steps for the current SUT */
    private static Iterator<PickleStepTestStep> testSteps;

    /**
     * Initialise the execution summary report for the current execution cycle.  This should be called
     * within the {@link BeforeAll} hook found in {@link Hooks}.
     *
     * @see Hooks#initializeExecution()
     * @throws IOException cascaded from implicit call to {@link #createExecutionReportDirectory()} or
     * if the "spark-config.xml" resource file cannot be found
     */
    public static void initialiseExecutionSummaryReport() throws IOException {
        //Create folder dir for execution evidence
        createExecutionReportDirectory();

        //Initialise spark report for execution summary
        executionSummaryReport = new ExtentReports();
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(
                String.format("%s\\%s.html",
                        executionEvidenceDirectoryPath,
                        TestConfig.getPropertyValue("execution_summary_report_filename")));
        sparkReporter.loadXMLConfig(
                Objects.requireNonNull(
                        Thread.currentThread()
                                .getContextClassLoader()
                                .getResource("spark-config.xml")) //Load spark-config resource/file
                        .getPath()
        );
        executionSummaryReport.attachReporter(sparkReporter);
    }

    /**
     * Finishes evidence capturing and reporting for this execution cycle and flushes the execution
     * summary report in its current state to create it.  This should be called within the {@link AfterAll}
     * hook found in {@link Hooks}.
     *
     * @see ExtentReports#flush()
     * @see Hooks#teardownExecution()
     */
    public static void saveExecutionEvidence(){
        //Mark execution as finished
        ScenarioContext.executionFinishTime = new Date();
        //Flush reporter outputs to HTML file in #executionReportDirectoryPath
        executionSummaryReport.flush();
    }

    /**
     * Creates a new {@link #executionEvidenceDirectoryPath} directory to save the execution summary report and
     * the individual test reports for the scenarios currently scheduled for this execution cycle (i.e. for
     * whatever scenarios have been tagged in {@link TestRunner}).
     *
     * @throws IOException if the execution report directory cannot be created successfully
     */
    private static void createExecutionReportDirectory() throws IOException {
        //Check that the "execution_reports_root_directory" currently exists, otherwise create it
        String executionReportsRootDirectoryPath = TestConfig.getPropertyValue("execution_reports_root_dir");
        if(!FileHandler.directoryExists(executionReportsRootDirectoryPath)){
            FileHandler.createFolder(executionReportsRootDirectoryPath);
        }

        //Create new folder within executionReportsDirectoryPath for the execution report
        FileHandler.createFolder(
                executionEvidenceDirectoryPath = executionReportsRootDirectoryPath + "Execution_Report-"
                        + DateHandler.formatDate(
                                ScenarioContext.executionStartTime,
                                TestConfig.getPropertyValue("execution_report_timestamp_format")
                )
        );
    }

    /**
     *
     *
     * @throws IOException
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     */
    public static void initialiseTestEvidence() throws IOException, IllegalAccessException, NoSuchFieldException {
        //Mark test as started
        ScenarioContext.currentTestStartTime = new Date();

        //Create folder dir for SUT/test evidence
        createTestEvidenceDirectory();

        //Create a new ExtentTest object
        test = executionSummaryReport.createTest(ScenarioContext.scenario.getName());

        //Set folder dir to save log file for SUT
        LogFactory.setLogDirectory(testEvidenceDirectoryPath);

        //Use Java Reflection to get state for current test case (found in #delegate field of Scenario object)
        Field testCaseStateField = ScenarioContext.scenario.getClass().getDeclaredField("delegate");
        testCaseStateField.setAccessible(true);
        TestCaseState testCaseState = (TestCaseState) testCaseStateField.get(ScenarioContext.scenario);

        //Use Java Reflection to get the current test case
        Field testCaseField = testCaseState.getClass().getDeclaredField("testCase");
        testCaseField.setAccessible(true);
        TestCase testCase = (TestCase) testCaseField.get(testCaseState);

        //Get BDD steps for the current test
        testSteps = testCase.getTestSteps()
                .stream()
                .filter(step -> step instanceof PickleStepTestStep)
                .map(step -> (PickleStepTestStep) step)
                .toList()
                .iterator();
    }

    /**
     *
     * @return
     */
    public static ExtentTest getTest(){
        return test;
    }

    /**
     *
     * @return
     */
    public static ExtentTest getStep(){
        return testStep;
    }

    /**
     *
     */
    public static void startStep(){
        //Create a new node in the report for the current BDD step
        Step step = testSteps.next().getStep();
        Log.debug("Current BDD Step: %s", step.getText());
        testStep = test.createNode(step.getKeyword(), step.getText());
    }

    /**
     *
     */
    public static void finishStep(){
        if(testStep.getStatus() != Status.FAIL){
            testStep.pass("BDD Step Successfully Completed");
        }
    }

    /**
     *
     */
    public static void saveTestEvidence(){
        //Attach tags, test environment and platform to the execution summary report
        test.assignCategory(TestConfig.TEST_ENVIRONMENT)
                .assignCategory(ScenarioContext.scenario.getSourceTagNames().toArray(String[]::new))
                .assignDevice(ScenarioContext.platform.getPlatform().toString());
        //Mark test as finished
        ScenarioContext.currentTestFinishTime = new Date();
        testSteps = null;
        testStep = null;
    }

    /**
     * Creates a folder within the {@link #executionEvidenceDirectoryPath} directory
     * for saving the individual evidence (e.g., report, screenshots, resource files
     * logs etc...) for the current test scenario-under-execution.
     *
     * @throws IOException - cascaded from {@link FileHandler#createFile(String)}
     */
    private static void createTestEvidenceDirectory() throws IOException {
        //Create a new folder within executionReportDirectoryPath to the store the evidence for the current SUT
        FileHandler.createFolder(
                testEvidenceDirectoryPath = String.format("%s%sTest(%s)",
                        executionEvidenceDirectoryPath,
                        System.getProperty("file.separator"),
                        DateHandler.formatDate(
                                ScenarioContext.currentTestStartTime,
                                TestConfig.getPropertyValue("test_evidence_folder_timestamp_format")
                        )
                )
        );
    }

    /**
     *
     * @return
     */
    public static String getTestEvidenceDirectoryPath() {
        return testEvidenceDirectoryPath;
    }

    /**
     *
     * @return
     */
    public static String getExecutionReportDirectoryPath() {
        return executionEvidenceDirectoryPath;
    }

    /**
     *
     * @param logLevel
     * @param screenshot
     */
    private static void saveScreenshotToTestReport(Status logLevel, File screenshot) {
        /*Attach screenshot to the current BDD step (if exists), otherwise attach to
        the overall test report */
        (TestEvidence.getStep() != null
                ? TestEvidence.getStep()
                : TestEvidence.getTest())
                .log(logLevel, MediaEntityBuilder
                        .createScreenCaptureFromPath(screenshot.getPath())
                        .build());
    }

    /**
     * Take a screenshot of the current system window and save this as a file, to the
     * {@link #testEvidenceDirectoryPath} directory, with the given filename as
     * {@param screenshotName}.
     *
     * @param screenshotName
     *           - The name to assign the screenshot file which gets saved to the test
     *           evidence
     */
    public static void captureScreenshotAsTestEvidence(Status logLevel, String screenshotName) {
        //Save screenshot as a file
        File screenshot = ScreenshotHandler.captureScreenshot();

        //If screenshot has been captured, save it to test evidence
        try {
            File dest;
            if(screenshotName == null){
                /* If screenshotName is null, use filename of the temp screenshot file which gets
                created from calling ScreenshotHandler#captureScreenshot above */
                dest = new File(testEvidenceDirectoryPath
                        + System.getProperty("file.separator")
                        + Objects.requireNonNull(screenshot).getName());
            }else {
                //Copy file to the test evidence folder with the given screenshotName
                dest = new File(testEvidenceDirectoryPath
                        + System.getProperty("file.separator")
                        + FileHandler.generateUniqueFileName(testEvidenceDirectoryPath,
                        screenshotName,
                        ".png")
                        + ".png");
            }

            //Copy file to the test evidence folder with the given screenshotName
            FileUtils.copyFile(Objects.requireNonNull(screenshot), dest);

            //Attach screenshot to the test report
            ScenarioContext.scenario.attach(FileUtils.readFileToByteArray(screenshot), "image/png", FileHandler.getBaseName(dest));
            saveScreenshotToTestReport(logLevel, dest);
        }catch (Exception ex) {
            Log.error("Failed to save screenshot to test evidence");
        }
    }

    /**
     *
     * @param logLevel
     */
    public static void captureScreenshotAsTestEvidence(Status logLevel) {
        captureScreenshotAsTestEvidence(logLevel, null);
    }

    /**
     *
     * @param file
     * @throws IOException
     */
    public static void saveToTestEvidence(File file) throws IOException {
        FileUtils.copyFile(file,
                new File(testEvidenceDirectoryPath
                        + System.getProperty("file.separator")
                        + file.getName()
                )
        );
    }

    /**
     *
     * @param src
     * @param destFileBaseName
     * @param destFileExtension
     * @throws IOException
     */
    public static void saveToTestEvidence(File src, String destFileBaseName, String destFileExtension) throws IOException {
        FileUtils.copyFile(src,
                new File(testEvidenceDirectoryPath
                        + System.getProperty("file.separator")
                        + destFileBaseName
                        + destFileExtension
                )
        );
    }

    /**
     *
     * @param src
     * @param destFileBaseName
     * @param destFileExtension
     * @throws IOException
     */
    public static void saveToTestEvidence(InputStream src, String destFileBaseName, String destFileExtension) throws IOException {
        FileUtils.copyInputStreamToFile(src,
                new File(testEvidenceDirectoryPath
                        + System.getProperty("file.separator")
                        + destFileBaseName
                        + destFileExtension
                )
        );
    }

    /**
     *
     * @param src
     * @param destFileBaseName
     * @throws IOException
     */
    public static void saveToTestEvidence(Properties src, String destFileBaseName) throws IOException {
        //Create output file to save properties to
        Path propsFile = Paths.get(testEvidenceDirectoryPath
                + System.getProperty("file.separator")
                + destFileBaseName
                + ".properties");
        OutputStream out = Files.newOutputStream(propsFile);

        //Store properties to this output file
        src.store(out, "Properties File");
    }

    /**
     *
     * @param src
     * @param destFileBaseName
     */
    public static void saveToExecutionEvidence(Properties src, String destFileBaseName) {
        //Create output file to save properties to
        File propsFile = new File(executionEvidenceDirectoryPath
                + System.getProperty("file.separator")
                + destFileBaseName
                + ".properties");

        //Write each property-value pair to this output file
        src.stringPropertyNames()
                .forEach(prop -> {
                    try {
                        FileUtils.writeStringToFile(propsFile,
                                String.format("%s=%s\n", prop, src.getProperty(prop)),
                                Charset.defaultCharset(),
                                true);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

}