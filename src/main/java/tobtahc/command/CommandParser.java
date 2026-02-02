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
import tobtahc.util.Rng;

/**
 * Implements the command parser.
 */
public class CommandParser {
    /** The patterns for matching the commands. The \s* before the number makes it more permissive. */
    private static final Pattern PATTERN_NUMERIC_CMD =
            Pattern.compile("^(mark|unmark|delete)\\s*(\\d+)\\s*$", Pattern.CASE_INSENSITIVE);

    private Rng rng;

    /**
     * @param rng the RNG used to make the output have more variety
     */
    public CommandParser(Rng rng) {
        this.rng = rng;
    }

    /**
     * Parse the user input to get the command.
     *
     * @param input the user input
     * @return the command parsed from the user input
     * @throws CommandParseError if the user input resembles a command but in a wrong syntax
     */
    public Command parse(String input) throws CommandParseError {
        var trimmed = input.trim();
        rng.nextRng(trimmed.hashCode());

        var lower = trimmed.toLowerCase(Locale.ROOT);

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

            return switch (verb) {
                case "mark" -> new MarkCommand(index);
                case "unmark" -> new UnmarkCommand(index);
                case "delete" -> new DeleteCommand(index);
                default -> throw new AssertionError("unreachable");
            };
        }

        var lowerToks = lower.split("\\s+");
        String verb = null;
        String arg = null;

        if (lowerToks.length >= 3
                && lowerToks[0].equals("before")
                && lowerToks[1].equals("or")
                && lowerToks[2].equals("on")) {
            verb = "before or on";
            arg = trimmed.substring(ParserUtil.indexAfterTokens(trimmed, 3));

        } else if (lowerToks.length >= 2
                && lowerToks[0].equals("occurs")
                && lowerToks[1].equals("on")) {
            verb = "occurs on";
            arg = trimmed.substring(ParserUtil.indexAfterTokens(trimmed, 2));

        } else if (lowerToks.length >= 1
                && lowerToks[0].equals("list")) {
            verb = "list";
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
            case "bye":
                if (arg != null) {
                    return new ExitCommand();
                } else {
                    throw new CommandParseError("Enter 'bye' to quit.");
                }
            case "list":
                if (arg != null) {
                    return new ListCommand();
                } else {
                    throw new CommandParseError("Enter 'list' to list your tasks.");
                }
            case "clear":
                if (arg != null) {
                    return new ClearCommand();
                } else {
                    throw new CommandParseError("Enter 'clear' to clear your tasks.");
                }
            }
        }

        try {
            var task = TaskParser.parse(trimmed);
            return new AddCommand(task, rng);
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
            }

        } catch (NotATask e) {
            return new EchoCommand(trimmed, rng);
        }

        throw new AssertionError("unreachable");
    }
}
