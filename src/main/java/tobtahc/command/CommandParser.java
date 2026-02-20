package tobtahc.command;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.regex.Pattern;

import tobtahc.task.NotATask;
import tobtahc.task.TaskFormatError;
import tobtahc.task.TaskParser;
import tobtahc.util.DateTimeUtil;
import tobtahc.util.ParserUtil;

/**
 * Parser for user input strings into executable commands.
 */
public class CommandParser {
    // Pattern for matching numeric commands (mark, unmark, delete).
    // The \s* before the number makes it more permissive for user input.
    private static final Pattern PATTERN_NUMERIC_CMD =
            Pattern.compile("^(mark|unmark|delete)\\s*(0*[1-9]\\d*)\\s*$", Pattern.CASE_INSENSITIVE);

    /**
     * Parses the user input into a command.
     *
     * @param input the raw input string from the user
     * @return the command represented by the input
     * @throws CommandParseError if the input resembles a command but has invalid syntax
     */
    public static Command parse(String input) throws CommandParseError {
        assert input != null : "CommandParser.parse: input must not be null";

        var trimmed = input.trim();
        var lower = trimmed.toLowerCase(Locale.ROOT);

        var numericCmd = tryParseNumericCommand(trimmed, lower);
        if (numericCmd != null) {
            return numericCmd;
        }

        var keywordCmd = tryParseKeywordCommand(trimmed, lower);
        if (keywordCmd != null) {
            return keywordCmd;
        }

        return parseTaskOrEcho(trimmed);
    }

    private static Command tryParseNumericCommand(String trimmed, String lower) throws CommandParseError {
        if (lower.startsWith("mark") || lower.startsWith("unmark") || lower.startsWith("delete")) {
            var m = PATTERN_NUMERIC_CMD.matcher(trimmed);
            if (!m.matches()) {
                if (lower.startsWith("mark")) {
                    throw new CommandParseError("Syntax error! Correct syntax:", "  mark <no>");
                } else if (lower.startsWith("unmark")) {
                    throw new CommandParseError("Syntax error! Correct syntax:", "  unmark <no>");
                } else {
                    throw new CommandParseError("Syntax error! Correct syntax:", "  delete <no>");
                }
            }

            String verb = m.group(1).toLowerCase(Locale.ROOT);
            int index = Integer.parseInt(m.group(2)) - 1;

            assert verb.equals("mark") || verb.equals("unmark")
                    || verb.equals("delete") : "Unexpected numeric command verb: " + verb;

            return switch (verb) {
            case "mark" -> new MarkCommand(index);
            case "unmark" -> new UnmarkCommand(index);
            case "delete" -> new DeleteCommand(index);
            default -> throw new AssertionError("unreachable");
            };
        }
        return null;
    }

    private static Command tryParseKeywordCommand(String trimmed, String lower) throws CommandParseError {
        var lowerToks = lower.split("\\s+");
        assert lowerToks.length >= 1 : "Tokenization produced no tokens";

        String verb = null;
        String arg = null;

        if (lowerToks.length >= 3
                && lowerToks[0].equals("before")
                && lowerToks[1].equals("or")
                && lowerToks[2].equals("on")) {
            verb = "before or on";
            arg = trimmed.substring(ParserUtil.findIndexAfterTokens(trimmed, 3));

        } else if (lowerToks.length >= 2
                && lowerToks[0].equals("occurs")
                && lowerToks[1].equals("on")) {
            verb = "occurs on";
            arg = trimmed.substring(ParserUtil.findIndexAfterTokens(trimmed, 2));

        } else if (lowerToks.length >= 1
                && lowerToks[0].equals("find")) {
            verb = "find";
            arg = trimmed.substring(ParserUtil.findIndexAfterTokens(trimmed, 1));

        } else if (lowerToks.length >= 1
                && lowerToks[0].equals("list")) {
            verb = "list";
            arg = lowerToks.length == 1 ? "" : null;

        } else if (lowerToks.length >= 1
                && lowerToks[0].equals("sort")) {
            verb = "sort";
            arg = lowerToks.length == 1 ? "" : null;

        } else if (lowerToks.length >= 1
                && lowerToks[0].equals("clear")) {
            verb = "clear";
            arg = lowerToks.length == 1 ? "" : null;

        } else if (lowerToks.length >= 1
                && lowerToks[0].equals("bye")) {
            verb = "bye";
            arg = lowerToks.length == 1 ? "" : null;
        }

        if (verb != null) {
            switch (verb) {
            case "before or on":
                try {
                    return new BeforeOrOnCommand(LocalDate.parse(arg));
                } catch (DateTimeParseException e) {
                    throw new CommandParseError("Syntax error! Correct syntax:", "  before or on <date>");
                }
            case "occurs on":
                try {
                    return new OccursOnCommand(LocalDate.parse(arg));
                } catch (DateTimeParseException e) {
                    throw new CommandParseError("Syntax error! Correct syntax:", "  occurs on <date>");
                }
            case "find":
                return new FindCommand(arg);
            case "list":
                if (arg != null) {
                    return new ListCommand();
                } else {
                    throw new CommandParseError("Enter 'list' to list your tasks.");
                }
            case "sort":
                if (arg != null) {
                    return new SortCommand();
                } else {
                    throw new CommandParseError("Enter 'sort' to list your tasks in a sorted way.");
                }
            case "clear":
                if (arg != null) {
                    return new ClearCommand();
                } else {
                    throw new CommandParseError("Enter 'clear' to clear your tasks.");
                }
            case "bye":
                if (arg != null) {
                    return new ExitCommand();
                } else {
                    throw new CommandParseError("Enter 'bye' to quit.");
                }
            default:
                throw new AssertionError("unreachable");
            }
        }

        return null;
    }

    private static Command parseTaskOrEcho(String trimmed) throws CommandParseError {
        try {
            var task = TaskParser.parse(trimmed);
            return new AddCommand(task);
        } catch (TaskFormatError e) {
            switch (e.getTaskType()) {
            case TODO:
                throw new CommandParseError("Syntax error! Correct syntax:",
                        "  todo <task>");
            case DEADLINE:
                throw new CommandParseError("Syntax error! Correct syntax:",
                        "  deadline <task> /by <time>",
                                "And the correct form of <time> should be "
                                        + DateTimeUtil.DATE_TIME_FORMATTER_INPUT_STRING);
            case EVENT:
                throw new CommandParseError("Syntax error! Correct syntax:",
                        "  event <task> /from <time> /to <time>",
                                "And the correct form of <time> should be "
                                        + DateTimeUtil.DATE_TIME_FORMATTER_INPUT_STRING);
            default:
                throw new AssertionError("unreachable");
            }

        } catch (NotATask e) {
            return new EchoCommand(trimmed);
        }
    }
}
