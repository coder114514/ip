package tobtahc.task;

/**
 * Error indicating that a task input string has an invalid format.
 */
public class TaskFormatError extends TaskParseError {
    /** The type of the task. */
    private TaskType type;

    /**
     * Constructs a {@code TaskFormatError} with the specified task type.
     *
     * @param type the type of the task
     */
    public TaskFormatError(TaskType type) {
        this.type = type;
    }

    /**
     * Returns the type of the task that caused the error.
     *
     * @return the task type
     */
    public TaskType getTaskType() {
        return type;
    }
}
