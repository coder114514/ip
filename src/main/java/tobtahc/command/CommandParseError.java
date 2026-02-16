package tobtahc.command;

/**
 * Error indicating invalid command syntax.
 * This occurs when user input resembles a known command but does not follow the correct format.
 */
public class CommandParseError extends Exception {
    /** The lines of the error message. */
    private final String[] lines;

    /**
     * Constructs a {@code CommandParseError} with specific error messages.
     *
     * @param lines the descriptive lines of the error
     */
    public CommandParseError(String... lines) {
        this.lines = lines;
    }

    /**
     * Returns the error message lines to be displayed.
     *
     * @return the array of error message lines
     */
    public String[] getLines() {
        return lines;
    }
}
