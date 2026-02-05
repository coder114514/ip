package tobtahc.command;

import java.time.LocalDate;
import java.util.ArrayList;

import tobtahc.task.Deadline;

/**
 * The command for "before or on {@literal <date>}", which shows all deadline tasks before
 * or on {@literal <date>}.
 */
public class BeforeOrOnCommand extends Command {
    private LocalDate date;

    /**
     * @param date the {@literal <date>} in the command
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
