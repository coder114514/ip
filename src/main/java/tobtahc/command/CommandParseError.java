package tobtahc.command;

/**
 * The exception for all command parse errors.
 * Used when the user input resembles some command but in wrong syntax.
 */

public class CommandParseError extends Exception {
    private final String[] lines;

    /**
     * @param lines lines of messages
     */
    public CommandParseError(String... lines) {
        this.lines = lines;
    }

    /**
     * {@return the lines of messages}
     */
    public String[] getLines() {
        return lines;
    }
}
