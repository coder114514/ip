package tobtahc.task;

/**
 * Thrown when the input is supposed to be a task but in wrong syntax.
 */
public class TaskFormatError extends TaskParseError {
    /** the type of the task */
    private TaskType type;

    /**
     * Initializes the exception with the type of the task.
     *
     * @param type type of the task
     */
    public TaskFormatError(TaskType type) {
        this.type = type;
    }

    /**
     * {@return the type of the task}
     */
    public TaskType getTaskType() {
        return type;
    }
}
