package tobtahc.command;

import java.io.IOException;

import tobtahc.storage.Storage;
import tobtahc.task.TaskList;
import tobtahc.ui.Ui;

/**
 * The base command class for commands that modify the storage.
 * Provides a helper for doing this.
 */
public abstract class ModifyingCommand extends Command {
    /**
     * A helper for saving the tasks and reporting errors.
     *
     * @param tasks the task list
     * @param ui the ui actor
     * @param storage the storage actor
     */
    public void saveTasks(TaskList tasks, Ui ui, Storage storage) {
        try {
            storage.saveTasks(tasks);
        } catch (IOException e) {
            ui.botMessageSepError();
            ui.botMessageLine("Error: " + e.getMessage() + ".");
            ui.botMessageSepError();
            ui.botMessageLine();
        }
    }
}
