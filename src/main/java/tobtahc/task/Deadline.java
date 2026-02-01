package tobtahc.task;

import java.time.LocalDate;
import java.time.LocalDateTime;

import tobtahc.util.Utils;

/**
 * Implements the Deadline task.
 */
public class Deadline extends Task {
    private LocalDateTime deadline;

    /**
     * @param description Task description.
     * @param deadline The deadline of the task.
     */
    public Deadline(String description, LocalDateTime deadline) {
        super(description);
        this.deadline = deadline;
    }

    /**
     * {@return Returns whether the task is due on/before {@code date}.}
     */
    public boolean isBeforeOrOn(LocalDate date) {
        return !deadline.toLocalDate().isAfter(date);
    }

    @Override
    public String serialize() {
        return String.format("%s %s/by %s",
                isDone() ? "1deadline" : "0deadline", super.getDescription(),
                        Utils.serializeDateTime(deadline));
    }

    @Override
    public String getDescription() {
        return String.format("%s (by: %s)",
                super.getDescription(), Utils.formatDateTime(deadline));
    }

    @Override
    public String getDescriptionWithStatus() {
        return "[D]" + super.getDescriptionWithStatus();
    }
}
