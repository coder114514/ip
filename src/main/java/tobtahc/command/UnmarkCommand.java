package tobtahc.command;

import tobtahc.storage.Storage;
import tobtahc.task.TaskList;
import tobtahc.ui.Ui;

/**
 * The command for unmarking a task.
 */
public class UnmarkCommand extends ModifyingCommand {
    private int index;

    /**
     * @param index the index of the task
     */
    public UnmarkCommand(int index) {
        this.index = index;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        if (index < 0 || index >= tasks.size()) {
            ui.botMessageSep();
            ui.botMessageLine("Ksat eht dnif ton dluoc TobTahc!");
            ui.botMessageSep();
            return;
        }
        var task = tasks.get(index);
        task.unmark();
        ui.botMessageSep();
        ui.botMessageLine("Dekramnu ksat!");
        ui.botMessageLine("  " + task);
        ui.botMessageSep();
        saveTasks(tasks, ui, storage);
    }
}

