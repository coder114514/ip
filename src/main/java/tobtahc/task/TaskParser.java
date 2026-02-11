package tobtahc.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import tobtahc.util.DateTimeUtil;
import tobtahc.util.ParserUtil;

/**
 * This class implements task parser and deserializer.
 */
public class TaskParser {
    /**
     * Parses the user input to get the task.
     *
     * @param input user input
     * @return task object
     * @throws NotATask if the input does not resemble a task at all
     * @throws TaskFormatError if the input resembles a task but is not in the correct syntax
     */
    public static Task parse(String input) throws NotATask, TaskFormatError {
        var splitted = input.split("\\s+", 2);
        if (splitted.length == 1) {
            var lower = input.toLowerCase(Locale.ROOT);
            if (lower.equals("todo") || lower.startsWith("todo/")) {
                throw new TaskFormatError(TaskType.TODO);
            } else if (lower.equals("deadline") || lower.startsWith("deadline/")) {
                throw new TaskFormatError(TaskType.DEADLINE);
            } else if (lower.equals("event") || lower.startsWith("event/")) {
                throw new TaskFormatError(TaskType.EVENT);
            } else {
                throw new NotATask();
            }
        }

        var verb = splitted[0].toLowerCase(Locale.ROOT);
        var payload = splitted[1];
        var m = ParserUtil.parseSwitches(payload);

        switch (verb) {
        case "todo":
            return parseToDo(m);
        case "deadline":
            return parseDeadline(m);
        case "event":
            return parseEvent(m);
        default:
            throw new NotATask();
        }
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
            var task = parse(input.substring(1).trim());
            if (isDone) {
                task.mark();
            }
            return task;
        } catch (TaskParseError e) {
            return null;
        }
    }

    private static Task parseToDo(Map<String, String> m) throws TaskFormatError {
        if (m == null) {
            throw new TaskFormatError(TaskType.TODO);
        }
        var desc = m.get("");
        if (desc == null || desc.isEmpty()) {
            throw new TaskFormatError(TaskType.TODO);
        }
        var allowed = Set.of("");
        for (var key : m.keySet()) {
            if (!allowed.contains(key)) {
                throw new TaskFormatError(TaskType.TODO);
            }
        }
        return new ToDo(desc);
    }

    private static Task parseDeadline(Map<String, String> m) throws TaskFormatError {
        if (m == null) {
            throw new TaskFormatError(TaskType.DEADLINE);
        }
        var desc = m.get("");
        if (desc == null || desc.isEmpty()) {
            throw new TaskFormatError(TaskType.DEADLINE);
        }
        var by = m.get("by");
        if (by == null || by.isEmpty()) {
            throw new TaskFormatError(TaskType.DEADLINE);
        }
        var allowed = Set.of("", "by");
        for (var key : m.keySet()) {
            if (!allowed.contains(key)) {
                throw new TaskFormatError(TaskType.DEADLINE);
            }
        }
        try {
            var deadline = LocalDateTime.parse(by, DateTimeUtil.DATE_TIME_FORMATTER_INPUT);
            return new Deadline(desc, deadline);
        } catch (DateTimeParseException e) {
            throw new TaskFormatError(TaskType.DEADLINE);
        }
    }

    private static Task parseEvent(Map<String, String> m) throws TaskFormatError {
        if (m == null) {
            throw new TaskFormatError(TaskType.EVENT);
        }
        var desc = m.get("");
        if (desc == null || desc.isEmpty()) {
            throw new TaskFormatError(TaskType.EVENT);
        }
        var from = m.get("from");
        if (from == null || from.isEmpty()) {
            throw new TaskFormatError(TaskType.EVENT);
        }
        var to = m.get("to");
        if (to == null || to.isEmpty()) {
            throw new TaskFormatError(TaskType.EVENT);
        }
        var allowed = Set.of("", "from", "to");
        for (var key : m.keySet()) {
            if (!allowed.contains(key)) {
                throw new TaskFormatError(TaskType.EVENT);
            }
        }
        try {
            var f = LocalDateTime.parse(from, DateTimeUtil.DATE_TIME_FORMATTER_INPUT);
            var t = LocalDateTime.parse(to, DateTimeUtil.DATE_TIME_FORMATTER_INPUT);
            return new Event(desc, f, t);
        } catch (DateTimeParseException e) {
            throw new TaskFormatError(TaskType.EVENT);
        }
    }
}
