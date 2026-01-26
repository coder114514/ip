package tobtahc.task;

public class Deadline extends Task {
    private String deadline;

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
