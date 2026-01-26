package tobtahc.exceptions;

import tobtahc.task.TaskType;

public class TaskParseError extends TobTahcException {
    private TaskType type;

    public TaskParseError(TaskType type) {
        this.type = type;
    }

    public TaskType getTaskType() {
        return this.type;
    }
}
