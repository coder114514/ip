package tobtahc.command;

import java.util.List;

/**
 * The record representing the result of a command.
 *
 * @param messageLines lines of the message to print
 * @param isExit whether the chatbot should exit
 * @param needSave whether the task list needs to be saved
 */
public record CommandResult(
    List<String> messageLines,
    boolean isExit,
    boolean needSave
) {}
