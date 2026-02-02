package tobtahc.command;

import tobtahc.task.Task;

/**
 * The command for adding a task.
 */
public class AddCommand extends ModifyingCommand {
    private Task task;

    /**
     * @param task the task to add
     * @param rng the RNG
     */
    public AddCommand(Task task) {
        this.task = task;
    }

    @Override
    public void execute(CommandContext ctx) {
        var ui = ctx.ui();
        var tasks = ctx.tasks();
        var storage = ctx.storage();
        var rng = ctx.rng();

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

