package factory;

import helper.Log;
import helper.TestConfig;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.util.ContextInitializer;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;

/**
 * Utility class for configuring the {@link Log} for logging during execution with
 * the default {@link LoggerFactory} instance which is bound at compile time.
 */
public class LogFactory {

    /**
     * Allow the directory/location for saving the log file to be set dynamically for
     * each individual test report.
     *
     * @param directory - The directory to save the log to
     */
    public static void setLogDirectory(String directory) {
        //Set "execution_log_dir" property
        TestConfig.setPropertyValue("execution_log_dir", directory);

        //Set "execution_log_filepath" property
        TestConfig.setPropertyValue("execution_log_filepath", String.format(
                "%s\\%s.txt",
                TestConfig.getPropertyValue("execution_log_dir"),
                TestConfig.getPropertyValue("execution_log_filename")
        ));

        //Reload logger
        reloadLogger();
    }

    /**
     * In order to make sure that the property configurations applied by
     * {@link #setLogDirectory(String)} take proper effect, we need to reload
     * the logger and its context in order for these changes to take effect.
     */
    private static void reloadLogger() {
        //Get the current LoggerContext
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();

        //Create a new ContextInitializer for this current LoggerContext
        ContextInitializer ci = new ContextInitializer(lc);

        try{
            //Reset the current LoggerContext
            lc.reset();
            /* Re-configure/refresh the Logger with this new ContextInitializer to apply changes
            made to system properties */
            ci.autoConfig();
        }catch (JoranException jex){
            //StatusPrinter will handle this
        }
        StatusPrinter.printInCaseOfErrorsOrWarnings(lc);
        Log.info("Log file instantiated, located at %s", TestConfig.getPropertyValue("execution_log_filepath"));
    }

}