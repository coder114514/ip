package tobtahc.task;

/**
 * A task without a specific date or time.
 */
public class ToDo extends Task {
    /**
     * Constructs a {@code ToDo} task.
     *
     * @param description the task description
     */
    public ToDo(String description) {
        super(description);
    }

    @Override
    public String serialize() {
        return String.format("%s todo %s",
                isDone() ? "1" : "0", super.getDescription());
    }

    @Override
    public String getDescriptionWithStatus() {
        return "[T]" + super.getDescriptionWithStatus();
    }
}
