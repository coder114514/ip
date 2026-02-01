package tobtahc.task;

/**
 * Thrown when the input is supposed to be a task but in wrong syntax.
 */
public class TaskFormatError extends Exception {
    private TaskType type;

    /**
     * @param type The type of the task.
     */
    public TaskFormatError(TaskType type) {
        this.type = type;
    }

    public TaskType getTaskType() {
        return type;
    }
}
