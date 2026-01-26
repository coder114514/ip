package tobtahc.task;

public class Event extends Task {
    private String from;
    private String to;

    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String getDescription() {
        return String.format("%s (from: %s to: %s)",
                super.getDescription(), from, to);
    }

    @Override
    public String getDescriptionWithStatus() {
        return "[E]" + super.getDescriptionWithStatus();
    }
}
