package tobtahc.task;

import java.time.LocalDate;
import java.time.LocalDateTime;

import tobtahc.util.DateTimeUtil;

/**
 * A task occurring during a specific time period.
 */
public class Event extends Task {
    private LocalDateTime from;
    private LocalDateTime to;

    /**
     * Constructs an {@code Event} task.
     *
     * @param description the task description
     * @param from the starting time of the event
     * @param to the ending time of the event
     */
    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the start time.
     *
     * @return the start time
     */
    public LocalDateTime getStart() {
        return from;
    }

    /**
     * Returns {@code true} if the event occurs on the given date.
     *
     * @param date the date to check
     * @return {@code true} if the event occurs on {@code date}; {@code false} otherwise
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
