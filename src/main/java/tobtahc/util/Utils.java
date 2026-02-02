package tobtahc.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * This class contains some utilities.
 */
final public class Utils {
    /**
     * The date time formats used for parsing user input and printing tasks.
     */
    public static final String DATE_TIME_FORMATTER_INPUT_STRING =
            "y-M-d HH:mm";
    public static final DateTimeFormatter DATE_TIME_FORMATTER_INPUT =
            DateTimeFormatter.ofPattern(DATE_TIME_FORMATTER_INPUT_STRING,
                    Locale.ROOT);
    public static final String DATE_TIME_FORMATTER_OUTPUT_STRING =
            "MMM dd yyyy HH:mm";
    public static final DateTimeFormatter DATE_TIME_FORMATTER_OUTPUT =
            DateTimeFormatter.ofPattern(DATE_TIME_FORMATTER_OUTPUT_STRING,
                    Locale.ROOT);

    /**
     * Outputs a string of the {@code LocalDateTime} object used for printing tasks.
     *
     * @param ldt the {@code LocalDateTime} object
     * @return formatted string of the object for output
     */
    public static String formatDateTime(LocalDateTime ldt) {
        return ldt.format(DATE_TIME_FORMATTER_OUTPUT);
    }

    /**
     * Outputs a string of the {@code LocalDateTime} object used for serializing tasks.
     *
     * @param ldt the {@code LocalDateTime} object
     * @return formatted string of the object for serialization
     */
    public static String serializeDateTime(LocalDateTime ldt) {
        return ldt.format(DATE_TIME_FORMATTER_INPUT);
    }
}
