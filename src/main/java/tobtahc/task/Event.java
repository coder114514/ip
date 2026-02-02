package tobtahc.task;

import java.time.LocalDate;
import java.time.LocalDateTime;

import tobtahc.util.DateTimeUtil;

/**
 * Implements the Event task.
 */
public class Event extends Task {
    private LocalDateTime from;
    private LocalDateTime to;

    /**
     * @param description description of the task
     * @param from starting time of the event
     * @param to ending time of the event
     */
    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * {@return whether the event occurs on {@code date}}
     */
    public boolean occursOn(LocalDate date) {
        return !to.toLocalDate().isBefore(date)
                && !from.toLocalDate().isAfter(date);
    }

    @Override
    public String serialize() {
        return String.format("%s event %s /from %s /to %s",
                isDone() ? "1" : "0", super.getDescription(),
                        DateTimeUtil.serializeDateTime(from), DateTimeUtil.serializeDateTime(to));
    }

    @Override
    public String getDescription() {
        return String.format("%s (from: %s to: %s)",
                super.getDescription(),
                        DateTimeUtil.formatDateTime(from), DateTimeUtil.formatDateTime(to));
    }

    @Override
    public String getDescriptionWithStatus() {
        return "[E]" + super.getDescriptionWithStatus();
    }
}
