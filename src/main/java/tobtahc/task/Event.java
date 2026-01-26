package tobtahc.task;

/**
 * Implements the Event task.
 */
public class Event extends Task {
    private String from;
    private String to;

    /**
     * @param description Task description.
     * @param from The starting time of the event.
     * @param to The ending time of the event.
     */
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
