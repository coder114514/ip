package tobtahc.task;

import java.util.regex.Pattern;

import tobtahc.exceptions.NotATask;
import tobtahc.exceptions.TaskParseError;

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
     * @throws TaskParseError if the input resembles a task but is not in the correct syntax.
     */
    public static Task parseTask(String input) throws NotATask, TaskParseError {
        var matcherToDo = PATTERN_TODO.matcher(input);

        if (matcherToDo.find()) {
            var desc = matcherToDo.group(1).trim();
            return new ToDo(desc);
        } else if (input.startsWith("todo")) {
            throw new TaskParseError(TaskType.TODO);
        }

        var matcherDeadline = PATTERN_DEADLINE.matcher(input);

        if (matcherDeadline.find()) {
            var desc = matcherDeadline.group(1).trim();
            var deadline = matcherDeadline.group(2).trim();
            return new Deadline(desc, deadline);
        } else if (input.startsWith("deadline")) {
            throw new TaskParseError(TaskType.DEADLINE);
        }

        var matcherEvent = PATTERN_EVENT.matcher(input);

        if (matcherEvent.find()) {
            var desc = matcherEvent.group(1).trim();
            var from = matcherEvent.group(2).trim();
            var to = matcherEvent.group(3).trim();
            return new Event(desc, from, to);
        } else if (input.startsWith("event")) {
            throw new TaskParseError(TaskType.EVENT);
        }

        throw new NotATask();
    }

    public abstract String serialize();

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
            } else {
                task.markAsUndone();
            }
            return task;
        } catch (NotATask e) {
            return null;
        } catch (TaskParseError e) {
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
