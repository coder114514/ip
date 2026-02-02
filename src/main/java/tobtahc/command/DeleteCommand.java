package tobtahc.command;

import tobtahc.storage.Storage;
import tobtahc.task.TaskList;
import tobtahc.ui.Ui;

/**
 * The command for deleting a task.
 */
public class DeleteCommand extends ModifyingCommand {
    private int index;

    /**
     * @param index the index of the task
     */
    public DeleteCommand(int index) {
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
        tasks.remove(index);
        ui.botMessageSep();
        if (task.isDone()) {
            ui.botMessageLine("Ksat removed!");
        } else {
            ui.botMessageLine("Task removed, but still UNDONE!");
        }
        ui.botMessageLine("  " + task);
        ui.botMessageLine(String.format("Now you have %s tasks in your list.", tasks.size()));
        ui.botMessageSep();
        saveTasks(tasks, ui, storage);
    }
}

