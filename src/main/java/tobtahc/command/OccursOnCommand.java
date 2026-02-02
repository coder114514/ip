package tobtahc.command;

import java.time.LocalDate;

import tobtahc.task.Event;

/**
 * The command for "occurs on <date>", which shows all events that occurs
 * on <date>.
 */
public class OccursOnCommand extends Command {
    private LocalDate date;

    /**
     * @param date the <date> in the command
     */
    public OccursOnCommand(LocalDate date) {
        this.date = date;
    }

    @Override
    public void execute(CommandContext ctx) {
        var ui = ctx.ui();
        var tasks = ctx.tasks();

        ui.botMessageSep();
        for (int i = 0; i < tasks.size(); ++i) {
            var task = tasks.get(i);
            if (!(task instanceof Event event)) {
                continue;
            }
            if (!event.occursOn(date)) {
                continue;
            }
            ui.botMessageLine("  " + (i + 1) + ": " + event.getDescription());
        }
        ui.botMessageSep();
    }
}
