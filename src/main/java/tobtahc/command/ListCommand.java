package tobtahc.command;

import tobtahc.storage.Storage;
import tobtahc.task.TaskList;
import tobtahc.ui.Ui;

/**
 * The command for listing the tasks.
 */
public class ListCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.botMessageSep();
        ui.botMessageLine(String.format("You have %s tasks in your list:", tasks.size()));
        if (tasks.size() == 0) {
            ui.botMessageLine("(empty)");
        }
        for (int i = 0; i < tasks.size(); ++i) {
            ui.botMessageLine(String.format("%s.%s", i + 1, tasks.get(i)));
        }
        ui.botMessageSep();
    }
}
