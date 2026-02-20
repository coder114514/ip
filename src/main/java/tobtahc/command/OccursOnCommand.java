package tobtahc.command;

import java.time.LocalDate;
import java.util.ArrayList;

import tobtahc.task.Event;

/**
 * Command to list all events occurring on a specific date.
 */
public class OccursOnCommand extends Command {
    private LocalDate date;

    /**
     * Constructs an {@code OccursOnCommand} with the target date.
     *
     * @param date the date to filter events by
     */
    public OccursOnCommand(LocalDate date) {
        this.date = date;
    }

    @Override
    public CommandResult execute(CommandContext ctx) {
        var tasks = ctx.tasks();

        var lines = new ArrayList<String>();

        for (int i = 0; i < tasks.size(); ++i) {
            var task = tasks.get(i);
            if (!(task instanceof Event event)) {
                continue;
            }
            if (!event.occursOn(date)) {
                continue;
            }
            lines.add(String.format("%d.%s", i + 1, event));
        }

        return new CommandResult(lines, false, false);
    }
}
