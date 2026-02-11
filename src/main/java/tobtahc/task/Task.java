package tobtahc.task;

/**
 * This is the base class for the three kinds of tasks.
 * It implements the common behavior.
 */
public abstract class Task {
    private String description;
    private boolean isDone;

    /**
     * Initiailizes the task with its descriptoin.
     *
     * @param description description of the task
     */
    public Task(String description) {
        this.description = description;
        isDone = false;
    }

    /**
     * Serializes the task object.
     * The behavior is delegated to each child class.
     * Since I'm lazy, the format is largely reusing the command syntax.
     * The first character is either 0 or 1, representing undone/done.
     * The rest of the string is just the command used to create the task.
     *
     * @return serialized task object
     */
    public abstract String serialize();

    @Override
    public String toString() {
        return getDescriptionWithStatus();
    }

    /**
     * {@return description of the task}
     */
    public String getDescription() {
        return description;
    }

    /**
     * {@return task status plus the description of the task}
     */
    public String getDescriptionWithStatus() {
        return "[" + getStatusIcon() + "] " + getDescription();
    }

    /**
     * {@return whether the task is marked done}
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * {@return X if the task is done, space otherwise}
     */
    public String getStatusIcon() {
        return isDone() ? "X" : " ";
    }

    /**
     * Marks the task as done.
     */
    public void mark() {
        isDone = true;
    }

    /**
     * Unmarks the task.
     */
    public void unmark() {
        isDone = false;
    }
}
