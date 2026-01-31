package tobtahc.task;

import java.time.LocalDateTime;

import tobtahc.Utils;

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
