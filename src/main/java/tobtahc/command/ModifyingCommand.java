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
