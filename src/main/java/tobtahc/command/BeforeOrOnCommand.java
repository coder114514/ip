package tobtahc.command;

import java.time.LocalDate;

import tobtahc.task.Deadline;

/**
 * The command for "before or on <date>", which shows all deadline tasks before
 * or on <date>.
 */
public class BeforeOrOnCommand extends Command {
    private LocalDate date;

    /**
     * @param date the <date> in the command
     */
    public BeforeOrOnCommand(LocalDate date) {
        this.date = date;
    }

    @Override
    public void execute(CommandContext ctx) {
        var ui = ctx.ui();
        var tasks = ctx.tasks();

        ui.botMessageSep();
        for (int i = 0; i < tasks.size(); ++i) {
            var task = tasks.get(i);
            if (!(task instanceof Deadline ddl)) {
                continue;
            }
            if (!ddl.isBeforeOrOn(date)) {
                continue;
            }
            ui.botMessageLine("  " + (i + 1) + ": " + ddl.getDescription());
        }
        ui.botMessageSep();
    }
}
