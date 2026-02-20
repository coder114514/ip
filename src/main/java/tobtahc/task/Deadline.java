package tobtahc.task;

import java.time.LocalDate;
import java.time.LocalDateTime;

import tobtahc.util.DateTimeUtil;

/**
 * A task with a specific deadline.
 */
public class Deadline extends Task {
    private LocalDateTime deadline;

    /**
     * Constructs a {@code Deadline} task.
     *
     * @param description the task description
     * @param deadline the deadline date and time
     */
    public Deadline(String description, LocalDateTime deadline) {
        super(description);
        this.deadline = deadline;
    }

    /**
     * Returns the due time.
     *
     * @return the due time
     */
    public LocalDateTime getBy() {
        return deadline;
    }

    /**
     * Returns {@code true} if the task is due on or before the given date.
     *
     * @param date the date to compare
     * @return {@code true} if due on or before {@code date}; {@code false} otherwise
     */
    public boolean isBeforeOrOn(LocalDate date) {
        return !deadline.toLocalDate().isAfter(date);
    }

    @Override
    public String serialize() {
        return String.format("%s deadline %s /by %s",
                isDone() ? "1" : "0", super.getDescription(),
                        DateTimeUtil.serializeDateTime(deadline));
    }

    @Override
    public String getDescription() {
        return String.format("%s (by: %s)",
                super.getDescription(), DateTimeUtil.formatDateTime(deadline));
    }

    @Override
    public String getDescriptionWithStatus() {
        return "[D]" + super.getDescriptionWithStatus();
    }
}
