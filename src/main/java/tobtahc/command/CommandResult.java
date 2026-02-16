package tobtahc.command;

import java.util.List;

/**
 * Result of a command execution.
 *
 * @param messageLines the lines of text to be displayed to the user
 * @param isExit {@code true} if the application should terminate
 * @param needSave {@code true} if changes to the task list should be persisted
 */
public record CommandResult(
    List<String> messageLines,
    boolean isExit,
    boolean needSave
) {}
