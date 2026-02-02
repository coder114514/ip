package tobtahc.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

import tobtahc.util.DateTime;

/**
 * This class implements task parser and deserializer.
 */
public class TaskParser {
    /**
     * The pattern for matching the commands.
     * The (.+)s without a space before the slashes make it more forgiving.
     */
    private static final Pattern PATTERN_TODO = Pattern.compile("^todo (.+)");
    private static final Pattern PATTERN_DEADLINE = Pattern.compile("^deadline (.+)/by (.+)");
    private static final Pattern PATTERN_EVENT = Pattern.compile("^event (.+)/from (.+)/to (.+)");

    /**
     * Parses the user input to get the task.
     *
     * @param input user input
     * @return task object
     * @throws NotATask if the input does not resemble a task at all
     * @throws TaskFormatError if the input resembles a task but is not in the correct syntax
     */
    public static Task parse(String input) throws NotATask, TaskFormatError {
        var matcherToDo = PATTERN_TODO.matcher(input);

        if (matcherToDo.find()) {
            var desc = matcherToDo.group(1).trim();
            return new ToDo(desc);
        } else if (input.startsWith("todo")) {
            throw new TaskFormatError(TaskType.TODO);
        }

        var matcherDeadline = PATTERN_DEADLINE.matcher(input);

        if (matcherDeadline.find()) {
            try {
                var desc = matcherDeadline.group(1).trim();
                var deadlineString = matcherDeadline.group(2).trim();
                var deadline = LocalDateTime.parse(deadlineString, DateTime.DATE_TIME_FORMATTER_INPUT);
                return new Deadline(desc, deadline);
            } catch (DateTimeParseException e) {
                throw new TaskFormatError(TaskType.DEADLINE);
            }
        } else if (input.startsWith("deadline")) {
            throw new TaskFormatError(TaskType.DEADLINE);
        }

        var matcherEvent = PATTERN_EVENT.matcher(input);

        if (matcherEvent.find()) {
            try {
                var desc = matcherEvent.group(1).trim();
                var fromString = matcherEvent.group(2).trim();
                var from = LocalDateTime.parse(fromString, DateTime.DATE_TIME_FORMATTER_INPUT);
                var toString = matcherEvent.group(3).trim();
                var to = LocalDateTime.parse(toString, DateTime.DATE_TIME_FORMATTER_INPUT);
                return new Event(desc, from, to);
            } catch (DateTimeParseException e) {
                throw new TaskFormatError(TaskType.DEADLINE);
            }
        } else if (input.startsWith("event")) {
            throw new TaskFormatError(TaskType.EVENT);
        }

        throw new NotATask();
    }

    /**
     * Deserializes the task object.
     * Since I'm lazy, the format is largely reusing the command syntax.
     * The first character is either 0 or 1, representing undone/done.
     * The rest of the string is just the command used to create the task.
     * So that is why we can simply use Task.parseTask() here.
     *
     * @param input the string to deserialize into a task object
     * @return a task object if successful, else null
     */
    public static Task deserialize(String input) {
        if (input.length() <= 1) {
            return null;
        }
        boolean isDone;
        if (input.charAt(0) == '0') {
            isDone = false;
        } else if (input.charAt(0) == '1') {
            isDone = true;
        } else {
            return null;
        }
        try {
            var task = parse(input.substring(1));
            if (isDone) {
                task.mark();
            }
            return task;
        } catch (TaskParseError e) {
            return null;
        }
    }
}
