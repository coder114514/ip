package tobtahc.command;

import tobtahc.storage.Storage;
import tobtahc.task.TaskList;
import tobtahc.ui.Ui;

/**
 * This is the base class for all the commands.
 */
public abstract class Command {
    /**
     * Executes the command itself.
     *
     * @param tasks the task list
     * @param ui the ui object
     * @param storage the storage object
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage);

    /**
     * {@return true if this command should terminate the bot}
     */
    public boolean isExit() {
        return false;
    }
}
