package tobtahc.command;

import java.time.LocalDate;

import tobtahc.storage.Storage;
import tobtahc.task.Deadline;
import tobtahc.task.TaskList;
import tobtahc.ui.Ui;

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
    public void execute(TaskList tasks, Ui ui, Storage storage) {
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
