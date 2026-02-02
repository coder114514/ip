package tobtahc.command;

import tobtahc.storage.Storage;
import tobtahc.task.Task;
import tobtahc.task.TaskList;
import tobtahc.ui.Ui;
import tobtahc.util.Rng;

/**
 * The command for adding a task.
 */
public class AddCommand extends ModifyingCommand {
    private Task task;
    private Rng rng;

    /**
     * @param task the task to add
     * @param rng the RNG
     */
    public AddCommand(Task task, Rng rng) {
        this.task = task;
        this.rng = rng;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        tasks.add(task);
        ui.botMessageSep();
        if (rng.getRng() % 4 == 0) {
            ui.botMessageLine("Ti tog!");
            ui.botMessageLine("  dedda ksat: " + task.getDescription());
            ui.botMessageLine(String.format("Tsil eht ni sksat %s evah uoy now.", tasks.size()));
        } else {
            ui.botMessageLine("Got it!");
            ui.botMessageLine("  task added: " + task.getDescription());
            ui.botMessageLine(String.format("Now you have %s tasks in your list.", tasks.size()));
        }
        ui.botMessageSep();
        saveTasks(tasks, ui, storage);
    }
}

