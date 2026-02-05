package tobtahc.command;

import java.util.ArrayList;

import tobtahc.task.Task;

/**
 * The command for adding a task.
 */
public class AddCommand extends Command {
    private Task task;

    /**
     * @param task the task to add
     * @param rng the RNG
     */
    public AddCommand(Task task) {
        this.task = task;
    }

    @Override
    public CommandResult execute(CommandContext ctx) {
        var tasks = ctx.tasks();
        var rng = ctx.rng();

        var lines = new ArrayList<String>();

        tasks.add(task);
        if (rng.getRng() % 4 == 0) {
            lines.add("Ti tog!");
            lines.add("  dedda ksat: " + task.getDescription());
            lines.add(String.format("Tsil eht ni sksat %s evah uoy now.", tasks.size()));
        } else {
            lines.add("Got it!");
            lines.add("  task added: " + task.getDescription());
            lines.add(String.format("Now you have %s tasks in your list.", tasks.size()));
        }

        return new CommandResult(lines, false, true);
    }
}

