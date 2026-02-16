package tobtahc.command;

import java.time.LocalDate;
import java.util.ArrayList;

import tobtahc.task.Deadline;

/**
 * Command to list tasks occurring before or on a specific date.
 */
public class BeforeOrOnCommand extends Command {
    private LocalDate date;

    /**
     * Constructs a {@code BeforeOrOnCommand}.
     *
     * @param date the date to filter events by
     */
    public BeforeOrOnCommand(LocalDate date) {
        this.date = date;
    }

    @Override
    public CommandResult execute(CommandContext ctx) {
        var tasks = ctx.tasks();

        var lines = new ArrayList<String>();

        for (int i = 0; i < tasks.size(); ++i) {
            var task = tasks.get(i);
            if (!(task instanceof Deadline ddl)) {
                continue;
            }
            if (!ddl.isBeforeOrOn(date)) {
                continue;
            }
            lines.add(String.format("%s.%s", i + 1, ddl));
        }

        return new CommandResult(lines, false, false);
    }
}
