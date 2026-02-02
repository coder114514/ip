package tobtahc.command;

import tobtahc.storage.Storage;
import tobtahc.task.TaskList;
import tobtahc.ui.Ui;

/**
 * The command for clearing the tasks.
 */
public class ClearCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.botMessageSep();
        ui.botMessageLine("Your tasks are all cleared.");
        tasks.clear();
        ui.botMessageSep();
    }
}
