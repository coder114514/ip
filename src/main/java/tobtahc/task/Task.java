package tobtahc.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

import tobtahc.util.Utils;

/**
 * This is the base class for the three kinds of tasks.
 * It implements the common behavior.
 */
public abstract class Task {
    /**
     * The pattern for matching the commands.
     * The (.+)s without a space before the slashes make it more forgiving.
     */
    private static final Pattern PATTERN_TODO = Pattern.compile("^todo (.+)");
    private static final Pattern PATTERN_DEADLINE = Pattern.compile("^deadline (.+)/by (.+)");
    private static final Pattern PATTERN_EVENT = Pattern.compile("^event (.+)/from (.+)/to (.+)");

    private String description;
    private boolean isDone;

    /**
     * @param description Task description.
     */
    public Task(String description) {
        this.description = description;
        isDone = false;
    }

    /**
     * Parses the user input to get the task.
     *
     * @param input User input.
     * @return Task object.
     * @throws NotATask if the input does not resemble a task at all.
     * @throws TaskFormatError if the input resembles a task but is not in the correct syntax.
     */
    public static Task parseTask(String input) throws NotATask, TaskFormatError {
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
                var deadline = LocalDateTime.parse(deadlineString, Utils.DATE_TIME_FORMATTER_INPUT);
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
                var from = LocalDateTime.parse(fromString, Utils.DATE_TIME_FORMATTER_INPUT);
                var toString = matcherEvent.group(3).trim();
                var to = LocalDateTime.parse(toString, Utils.DATE_TIME_FORMATTER_INPUT);
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
     * Serializes the task object.
     * The behavior is delegated to each child class.
     * Since I'm lazy, the format is largely reusing the command syntax.
     * The first character is either 0 or 1, representing undone/done.
     * The rest of the string is just the command used to create the task.
     *
     * @return Serialized task object.
     */
    public abstract String serialize();

    /**
     * Deserializes the task object.
     * Since I'm lazy, the format is largely reusing the command syntax.
     * The first character is either 0 or 1, representing undone/done.
     * The rest of the string is just the command used to create the task.
     * So that is why we can simply use Task.parseTask() here.
     *
     * @param input The string to deserialize into a task object.
     * @return A task object if successful, else null.
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
            var task = parseTask(input.substring(1));
            if (isDone) {
                task.markAsDone();
            }
            return task;
        } catch (NotATask e) {
            return null;
        } catch (TaskFormatError e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return getDescriptionWithStatus();
    }

    public String getDescription() {
        return description;
    }

    /**
     * @return Return in the form of [X]description, which is treated as the string form of the task.
     */
    public String getDescriptionWithStatus() {
        return "[" + getStatusIcon() + "] " + getDescription();
    }

    public boolean isDone() {
        return isDone;
    }

    /**
     * @return Returns X if the task is done.
     */
    public String getStatusIcon() {
        return isDone() ? "X" : " ";
    }

    /**
     * Mark the task as done.
     */
    public void markAsDone() {
        isDone = true;
    }

    /**
     * Unmark the task.
     */
    public void markAsUndone() {
        isDone = false;
    }
}
