package tobtahc.task;

/**
 * Abstract base class for all tasks.
 */
public abstract class Task {
    private String description;
    private boolean isDone;

    /**
     * Constructs a {@code Task} with the specified description.
     *
     * @param description the description of the task
     */
    public Task(String description) {
        this.description = description;
        isDone = false;
    }

    /**
     * Returns a string representation of the task for storage.
     *
     * @return the serialized task string
     */
    public abstract String serialize();

    @Override
    public String toString() {
        return getDescriptionWithStatus();
    }

    /**
     * Returns the description of the task.
     *
     * @return the task description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the task icon(s) (including status) and the description.
     *
     * @return the formatted icon(s) (including status) and description string
     */
    public String getDescriptionWithStatus() {
        return "[" + getStatusIcon() + "] " + getDescription();
    }

    /**
     * Returns {@code true} if the task is marked as done.
     *
     * @return {@code true} if the task is done; {@code false} otherwise
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Returns the status icon based on whether the task is done.
     *
     * @return "X" if done, or a space character if not done
     */
    public String getStatusIcon() {
        return isDone() ? "X" : " ";
    }

    /**
     * Marks the task as completed.
     */
    public void mark() {
        isDone = true;
    }

    /**
     * Marks the task as not completed.
     */
    public void unmark() {
        isDone = false;
    }
}
