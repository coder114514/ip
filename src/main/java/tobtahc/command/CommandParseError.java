package tobtahc.command;

/**
 * The exception for all command parse errors.
 * Used when the user input resembles some command but in wrong syntax.
 */
public class CommandParseError extends Exception {
    /** the lines of messages  */
    private final String[] lines;

    /**
     * Initializes the exception with the lines of messages to show.
     *
     * @param lines lines of messages
     */
    public CommandParseError(String... lines) {
        this.lines = lines;
    }

    /**
     * {@return the lines of messages to show}
     */
    public String[] getLines() {
        return lines;
    }
}
