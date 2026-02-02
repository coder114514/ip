package tobtahc.task;

/**
 * Implements the ToDo task.
 */
public class ToDo extends Task {
    /**
     * @param description description of the task
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
