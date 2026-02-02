package tobtahc.task;

import java.time.LocalDate;
import java.time.LocalDateTime;

import tobtahc.util.DateTime;

/**
 * Implements the Deadline task.
 */
public class Deadline extends Task {
    private LocalDateTime deadline;

    /**
     * @param description description of the task
     * @param deadline The deadline of the task
     */
    public Deadline(String description, LocalDateTime deadline) {
        super(description);
        this.deadline = deadline;
    }

    /**
     * {@return whether the task is due on/before {@code date}}
     */
    public boolean isBeforeOrOn(LocalDate date) {
        return !deadline.toLocalDate().isAfter(date);
    }

    @Override
    public String serialize() {
        return String.format("%s deadline %s /by %s",
                isDone() ? "1" : "0", super.getDescription(),
                        DateTime.serializeDateTime(deadline));
    }

    @Override
    public String getDescription() {
        return String.format("%s (by: %s)",
                super.getDescription(), DateTime.formatDateTime(deadline));
    }

    @Override
    public String getDescriptionWithStatus() {
        return "[D]" + super.getDescriptionWithStatus();
    }
}
