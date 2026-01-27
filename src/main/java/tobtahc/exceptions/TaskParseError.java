package tobtahc.exceptions;

import tobtahc.task.TaskType;

/**
 * Is thrown when the input is supposed to be a task but in wrong syntax.
 */
public class TaskParseError extends TobTahcException {
    private TaskType type;

    /**
     * @param type The type of the task.
     */
    public TaskParseError(TaskType type) {
        this.type = type;
    }

    public TaskType getTaskType() {
        return type;
    }
}
