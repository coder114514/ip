package tobtahc.task;

/**
 * Implements the ToDo task.
 */
public class ToDo extends Task {
    /**
     * @param description Task description.
     */
    public ToDo(String description) {
        super(description);
    }

    @Override
    public String serialize() {
        return (isDone() ? "1todo " : "0todo ") + super.getDescription();
    }

    @Override
    public String getDescriptionWithStatus() {
        return "[T]" + super.getDescriptionWithStatus();
    }
}
