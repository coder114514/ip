package tobtahc.task;

import java.time.LocalDateTime;

import tobtahc.Utils;

/**
 * Implements the Event task.
 */
public class Event extends Task {
    private LocalDateTime from;
    private LocalDateTime to;

    /**
     * @param description Task description.
     * @param from The starting time of the event.
     * @param to The ending time of the event.
     */
    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String serialize() {
        return String.format("%s %s/from %s/to %s",
                isDone() ? "1event" : "0event", super.getDescription(),
                        Utils.serializeDateTime(from), Utils.serializeDateTime(to));
    }

    @Override
    public String getDescription() {
        return String.format("%s (from: %s to: %s)",
                super.getDescription(),
                        Utils.formatDateTime(from), Utils.formatDateTime(to));
    }

    @Override
    public String getDescriptionWithStatus() {
        return "[E]" + super.getDescriptionWithStatus();
    }
}
