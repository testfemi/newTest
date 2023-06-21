package utility;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import org.apache.commons.lang3.time.DateUtils;

/**
 * Utility which provides various methods to quickly and efficiently help with conversion
 * of date-strings into {@link Date} objects (and vice-versa),
 */
public class DateHandler {

    /*
     * Configuration for what default date formats are recognised when wanting to convert an
     * existing date-string into an {@link Date} object via the #convertToDate method.  This
     * reduces the amount of code needed when converting and dealing with conversions of
     * common date-time formats.
     */
    private final static String[] DEFAULT_DATE_FORMATS = new String[] {
            "dd-MM-yyyy", "dd-MM-yyyy HH-mm-ss", "dd-MM-yyyy HH:mm:ss", "dd-MM-yyyy HH:mm",
            "dd.MM.yyyy", "dd.MM.yyyy HH-mm-ss", "dd.MM.yyyy HH:mm:ss", "dd.MM.yyyy HH:mm",
            "yyyy-MM-dd", "yyyy-MM-dd HH-mm-ss", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm",
            "yyyy.MM.dd", "yyyy.MM.dd HH-mm-ss", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm"
    };

    /**
     * Returns the current date as a {@link String} and formats it using the given
     * SimpleDateFormat {@param format}.
     *
     * @param format the SimpleDateFormat pattern to format the current date with.
     * @see SimpleDateFormat documentation for further details of supported format
     * patterns which can be used for {@param format}.
     * @return the current date as a formatted {@link String}
     */
    public static String getCurrentDate(String format){
        return getCurrentDateInPastFuture(format, 0);
    }

    /**
     * Adds/Subtracts the number of {@param calendarDays} specified to the {@param date}),
     * and returns this new date as a {@link String} in the specified SimpleDateFormat
     * {@param format} provided.
     *
     * @param date the Date object we wish to format.
     * @param format the SimpleDateFormat pattern to format the date with.
     * @see SimpleDateFormat documentation for further details of supported format
     * patterns which can be used for {@param format}.
     * @param calendarDays
     *            - Number of calendar days to add/subtract from the {@param date}.
     * @return Formatted Date object.
     */
    public static String getDateInPastFuture(Date date, String format, int calendarDays) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, calendarDays);
        return formatDate(cal.getTime(), format);
    }

    /**
     * Adds/Subtracts the number of {@param calendarDays} specified to the {@param date}),
     * and returns this new date as a {@link String} in the specified SimpleDateFormat
     * {@param format} provided.  This method includes an implicit call to
     * {@link #getDateInPastFuture(Date, String, int)}.
     *
     * @param date
     *            - The Date object we wish to format.
     * @param format
     *            - The SimpleDateFormat pattern to format the date with.
     * @see SimpleDateFormat documentation for further details of supported format
     * patterns which can be used for {@param format}.
     * @param calendarDays
     *            - Number of calendar days to add/subtract from the {@param date}.  If
     *            {@param calendarDays} is null or empty, then the value of 0 (as an int)
     *            will be passed when implicitly calling
     *            {@link #getDateInPastFuture(Date, String, int)}.
     * @return Formatted Date object.
     */
    public static String getDateInPastFuture(Date date, String format, String calendarDays) {
        return getDateInPastFuture(date, format, calendarDays == null || calendarDays.isEmpty() ? 0 : Integer.parseInt(calendarDays));
    }

    /**
     * Adds/Subtracts the number of {@param calendarDays} specified to the current date
     * (calculated at run-time), and returns the new date as a {@link String} in the
     * specified SimpleDateFormat {@param format} provided.  This method includes an
     * implicit call to {@link #getDateInPastFuture(Date, String, String)}.
     *
     * @param format
     *            - The SimpleDateFormat pattern to format the date with.
     * @see SimpleDateFormat documentation for further details of supported format
     * patterns which can be used for {@param format}.
     * @param calendarDays
     *            - Number of calendar days to add/subtract from the current date.
     * @return Formatted Date object.
     */
    public static String getCurrentDateInPastFuture(String format, String calendarDays) {
        return getDateInPastFuture(new Date(), format, calendarDays);
    }

    /**
     * Adds/Subtracts the number of {@param calendarDays} specified to the current date
     * (calculated at run-time), and returns the new date as a {@link String} in the
     * specified SimpleDateFormat {@param format} provided.  This method includes an
     * implicit call to {@link #getDateInPastFuture(Date, String, int)}.
     *
     * @param format
     *            - The SimpleDateFormat pattern to format the date with.
     * @see SimpleDateFormat documentation for further details of supported format
     * patterns which can be used for {@param format}.
     * @param calendarDays
     *            - Number of calendar days to add/subtract from the current date
     * @return Formatted Date object.
     */
    public static String getCurrentDateInPastFuture(String format, int calendarDays) {
        return getDateInPastFuture(new Date(), format, calendarDays);
    }

    /**
     * Format the given {@param date} using the given SimpleDateFormat {@param format}
     * and return this formatted date as a {@link String}.
     *
     * @param date
     *            - The date to format.
     * @param format
     *            - The SimpleDateFormat pattern to format the date with.
     * @see SimpleDateFormat documentation for further details of supported format
     * patterns which can be used for {@param format}.
     * @return Formatted date.
     */
    public static String formatDate(Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }

    /**
     * Convert the given {@link String} representation, i.e., {@param date}, into an equivalent
     * {@link Date} object, e.g., 15-12-2021, 2021.12.15 12:10 as Strings will be converted
     * to their equivalent {@link Date} object.  The format of {@param date} must match one of
     * the {@link SimpleDateFormat} format patterns within {@link #DEFAULT_DATE_FORMATS} in order
     * to ensure successful conversion.
     *
     * @param date
     *            - String representation of our date.
     * @return the {@param date} as a Date object, if successful.
     * @throws Error - if the format of {@param date} is not recognised
     * @see SimpleDateFormat documentation for further details on the format patterns used in
     * {@link #DEFAULT_DATE_FORMATS}.
     */
    public static Date convertToDateObject(String date) {
        try {
            return DateUtils.parseDate(date, DEFAULT_DATE_FORMATS);
        } catch (ParseException e) {
            throw new Error("The date: \"" + date + "\" must match one of the following SimpleDateFormat patterns: "
                    + Arrays.toString(DEFAULT_DATE_FORMATS));
        }
    }

    /**
     * Convert the given {@link String} representation, i.e., {@param date}, into an equivalent
     * {@link Date} object, e.g., 15-12-2021, 2021.12.15 12:10 as Strings will be converted
     * to their equivalent {@link Date} object.  The format of {@param date} must match the
     * given SimpleDateFormat {@param format} pattern in order to ensure successful conversion.
     *
     * @param date
     *            - String representation of our date.
     * @param format
     *            - The current SimpleDateFormat format pattern which {@param date} is formatted
     *            with.
     * @return the {@param date} as a Date object, if successful.
     * @throws Error - if the {@param format} does not match {@param date} correctly
     * @see SimpleDateFormat documentation for further details of supported format
     * patterns which can be used for {@param format}.
     */
    public static Date convertToDateObject(String date, String format) {
        try {
            return DateUtils.parseDate(date, format);
        } catch (ParseException e) {
            throw new Error("The date: \"" + date + "\" must match the SimpleDateFormat pattern: " + format);
        }
    }

}
