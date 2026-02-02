package tobtahc.command;

import tobtahc.storage.Storage;
import tobtahc.task.TaskList;
import tobtahc.ui.Ui;

/**
 * The command for marking a task as done.
 */
public class MarkCommand extends ModifyingCommand {
    private int index;

    /**
     * @param index the index of the task
     */
    public MarkCommand(int index) {
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
        task.mark();
        ui.botMessageSep();
        ui.botMessageLine("Task marked as done!");
        ui.botMessageLine("  " + task);
        ui.botMessageSep();
        saveTasks(tasks, ui, storage);
    }
}
