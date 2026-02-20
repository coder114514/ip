package tobtahc.command;

import java.util.ArrayList;

import tobtahc.task.Task;

/**
 * Command to add a task.
 */
public class AddCommand extends Command {
    private Task task;

    /**
     * Constructs an {@code AddCommand}.
     *
     * @param task the task to add
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
        if (rng.chance(1, 4)) {
            lines.add("Ti tog!");
            lines.add("  dedda ksat: " + task.getDescription());
            lines.add(String.format("Tsil eht ni sksat %d evah uoy now.", tasks.size()));
        } else {
            lines.add("Got it!");
            lines.add("  task added: " + task.getDescription());
            lines.add(String.format("Now you have %d tasks in your list.", tasks.size()));
        }

        return new CommandResult(lines, false, true);
    }
}

