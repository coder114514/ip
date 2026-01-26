package tobtahc.task;

/**
 * Implements the Deadline task.
 */
public class Deadline extends Task {
    private String deadline;

    /**
     * @param description Task description.
     * @param deadline The deadline of the task.
     */
    public Deadline(String description, String deadline) {
        super(description);
        this.deadline = deadline;
    }

    @Override
    public String getDescription() {
        return String.format("%s (by: %s)",
                super.getDescription(), deadline);
    }

    @Override
    public String getDescriptionWithStatus() {
        return "[D]" + super.getDescriptionWithStatus();
    }
}
