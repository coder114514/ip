package tobtahc.command;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

import tobtahc.task.NotATask;
import tobtahc.task.TaskFormatError;
import tobtahc.task.TaskParser;
import tobtahc.util.DateTime;
import tobtahc.util.Rng;

/**
 * Implements the command parser.
 */
public class CommandParser {
    /** The pattern for matching the commands. The \s* before the number makes it more forgiving. */
    private static final Pattern PATTERN_MARK_UNMARK = Pattern.compile("^(?:un)?mark\\s*([0-9]+)\\s*$");
    private static final Pattern PATTERN_DELETE = Pattern.compile("^delete\\s*([0-9]+)\\s*$");

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
        input = input.trim();
        rng.nextRng(input.hashCode());

        if (input.equals("bye")) {
            return new ExitCommand();
        } else if (input.startsWith("bye")) {
            throw new CommandParseError("Enter 'bye' to quit.");
        }

        if (input.equals("list")) {
            return new ListCommand();
        } else if (input.startsWith("list")) {
            throw new CommandParseError("Enter 'list' to list your tasks.");
        }

        if (input.equals("clear")) {
            return new ClearCommand();
        } else if (input.startsWith("clear")) {
            throw new CommandParseError("Enter 'clear' to clear your tasks.");
        }

        if (input.startsWith("before or on")) {
            try {
                var date = LocalDate.parse(input.substring(12).trim());
                return new BeforeOrOnCommand(date);
            } catch (DateTimeParseException e) {
                throw new CommandParseError("Syntax error! Correct syntax:",
                        "  before or on <date>");
            }
        }

        if (input.startsWith("occurs on")) {
            try {
                var date = LocalDate.parse(input.substring(9).trim());
                return new OccursOnCommand(date);
            } catch (DateTimeParseException e) {
                throw new CommandParseError("Syntax error! Correct syntax:",
                        "  occurs on <date>");
            }
        }

        var matcherMarkUnmark = PATTERN_MARK_UNMARK.matcher(input);

        if (matcherMarkUnmark.find()) {
            var indexString = matcherMarkUnmark.group(1);
            int index;
            try {
                index = Integer.parseInt(indexString) - 1;
            } catch (NumberFormatException e) {
                throw new CommandParseError("Syntax error! Correct syntax:",
                        "  mark <no> or unmark <no>");
            }
            if (input.charAt(0) == 'm') {
                return new MarkCommand(index);
            } else {
                return new UnmarkCommand(index);
            }
        } else if (input.startsWith("mark") || input.startsWith("unmark")) {
            throw new CommandParseError("Syntax error! Correct syntax:",
                    "  mark <no> or unmark <no>");
        }

        var matcherDelete = PATTERN_DELETE.matcher(input);

        if (matcherDelete.find()) {
            var indexString = matcherDelete.group(1);
            int index;
            try {
                index = Integer.parseInt(indexString) - 1;
            } catch (NumberFormatException e) {
                throw new CommandParseError("Syntax error! Correct syntax:",
                        "  delete <no>");
            }
            return new DeleteCommand(index);
        } else if (input.startsWith("delete")) {
            throw new CommandParseError("Syntax error! Correct syntax:",
                    "  delete <no>");
        }

        try {
            var task = TaskParser.parse(input);
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
                                        + DateTime.DATE_TIME_FORMATTER_INPUT_STRING);
            case EVENT:
                throw new CommandParseError("Syntax error! Correct syntax:",
                        "  event <task> /from <time> /to <time>",
                                "And the correct form of <time> should be "
                                        + DateTime.DATE_TIME_FORMATTER_INPUT_STRING);
            }

        } catch (NotATask e) {
            return new EchoCommand(input, rng);
        }

        // not reachable
        // just to make the compiler happy
        return null;
    }
}
