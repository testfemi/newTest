package helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * For recording creating log outputs to the console/terminal during execution and saving statements
 * to a log file for each scenario-under-execution under the applicable test evidence folder.
 */
public class Log {
    private static final Logger LOGGER = LoggerFactory.getLogger(Log.class); // Create Logger object from slf4j library

    /**
     * By implicitly calling {@link String#format(String, Object...)}, format the message
     * given by {@param msg} with {@param args}, log this message at the INFO level and
     * save this statement to the test evidence.
     * 
     * @see String#format(String, Object...)
     * @param msg the message as a format string
     * @param args arguments referenced by the format specifiers in the format string
     */
    public static void info(String msg, Object... args) {
        String toLog = String.format(msg, args);
        LOGGER.info(addDots(toLog));
        if (TestEvidence.getTest() != null) {
            (TestEvidence.getStep() != null ? TestEvidence.getStep() : TestEvidence.getTest()).info(toLog);
        }
    }

    /**
     * By implicitly calling {@link String#format(String, Object...)}, format the message
     * given by {@param msg} with {@param args} and log this message at the DEBUG level.
     *
     * @see String#format(String, Object...)
     * @param msg the message as a format string
     * @param args arguments referenced by the format specifiers in the format string
     */
    public static void debug(String msg, Object... args) {
        LOGGER.debug(addDots(String.format(msg, args)));
    }

    /**
     * By implicitly calling {@link String#format(String, Object...)}, format the message
     * given by {@param msg} with {@param args}, log this message at the ERROR level and
     * save this statement to the test evidence.
     *
     * @see String#format(String, Object...)
     * @param msg the message as a format string
     * @param args arguments referenced by the format specifiers in the format string
     */
    public static void error(String msg, Object... args) {
        String toLog = String.format(msg, args);
        LOGGER.error(addDots(toLog));
        if(TestEvidence.getTest() != null) {
            (TestEvidence.getStep() != null ? TestEvidence.getStep() : TestEvidence.getTest()).fail(toLog);
        }
    }

    /**
     * Log the message {@param msg} given at the ERROR level, save this statement to the
     * test evidence and throw the {@param throwable} given.
     *
     * @param msg the message as {@link String}
     * @param throwable the {@link Throwable} object to throw after create log statement
     */
    public static void error(Throwable throwable, String msg, Object... args) {
        String toLog = String.format(msg, args);
        LOGGER.error(addDots(toLog), throwable);
        if (TestEvidence.getTest() != null) {
            (TestEvidence.getStep() != null ? TestEvidence.getStep() : TestEvidence.getTest()).fail(msg);
            (TestEvidence.getStep() != null ? TestEvidence.getStep() : TestEvidence.getTest()).fail(throwable);
        }
        throw new Error(toLog, throwable);
    }

    /**
     * By implicitly calling {@link String#format(String, Object...)}, format the message
     * given by {@param msg} with {@param args}, log this message at the WARN level and
     * save this statement to the test evidence.
     *
     * @see String#format(String, Object...)
     * @param msg the message as a format string
     * @param args arguments referenced by the format specifiers in the format string
     */
    public static void warn(String msg, Object... args) {
        String toLog = String.format(msg, args);
        LOGGER.warn(addDots(toLog));
        if (TestEvidence.getTest() != null) {
            (TestEvidence.getStep() != null ? TestEvidence.getStep() : TestEvidence.getTest()).warning(toLog);
        }
    }

    /**
     * To help with formatting the log messages carried out within the methods above,
     * implicitly calls {@link String#format(String, Object...)} to surround the given
     * {@param msg} with the following formatted string pattern:
     * <p>
     *                                      '... %s ...'
     *
     * @param msg the message {@link String} to use as an argument reference for the
     *            formatted string
     * @return the formatted {@param msg} string
     */
    private static String addDots(String msg) {
        return String.format("... %s ...", msg);
    }

}