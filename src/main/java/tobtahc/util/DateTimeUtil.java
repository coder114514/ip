package tobtahc.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Utility for formatting and parsing date-time objects.
 */
public final class DateTimeUtil {
    /** The date time format string for parsing user input. */
    public static final String DATE_TIME_FORMATTER_INPUT_STRING =
            "y-M-d HH:mm";

    /** The date time formatter for parsing user input. */
    public static final DateTimeFormatter DATE_TIME_FORMATTER_INPUT =
            DateTimeFormatter.ofPattern(DATE_TIME_FORMATTER_INPUT_STRING,
                    Locale.ROOT);

    /** The date time format string for printing dates. */
    public static final String DATE_TIME_FORMATTER_OUTPUT_STRING =
            "MMM dd yyyy HH:mm";

    /** The date time formatter for printing dates. */
    public static final DateTimeFormatter DATE_TIME_FORMATTER_OUTPUT =
            DateTimeFormatter.ofPattern(DATE_TIME_FORMATTER_OUTPUT_STRING,
                    Locale.ROOT);

    /**
     * Formats a {@code LocalDateTime} object for user-facing output.
     *
     * @param ldt the date-time object to format
     * @return the formatted date-time string
     */
    public static String formatDateTime(LocalDateTime ldt) {
        return ldt.format(DATE_TIME_FORMATTER_OUTPUT);
    }

    /**
     * Formats a {@code LocalDateTime} object for task serialization.
     *
     * @param ldt the date-time object to serialize
     * @return the serialized date-time string
     */
    public static String serializeDateTime(LocalDateTime ldt) {
        return ldt.format(DATE_TIME_FORMATTER_INPUT);
    }
}
