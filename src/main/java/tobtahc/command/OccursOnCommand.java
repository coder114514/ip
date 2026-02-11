package tobtahc.command;

import java.time.LocalDate;
import java.util.ArrayList;

import tobtahc.task.Event;

/**
 * The command for "occurs on {@literal <date>}", which shows all events that occur
 * on {@literal <date>}.
 */
public class OccursOnCommand extends Command {
    private LocalDate date;

    /**
     * Initializes the command for showing all events that occur on some date.
     *
     * @param date the {@literal <date>} in the command
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
            lines.add(String.format("%s.%s", i + 1, event));
        }

        return new CommandResult(lines, false, false);
    }
}
