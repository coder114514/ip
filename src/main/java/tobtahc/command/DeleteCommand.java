package tobtahc.command;

import java.util.ArrayList;

/**
 * Command to delete a task.
 */
public class DeleteCommand extends Command {
    private int index;

    /**
     * Constructs a {@code DeleteCommand} with the target index.
     *
     * @param index the zero-based index of the task to delete
     */
    public DeleteCommand(int index) {
        this.index = index;
    }

    @Override
    public CommandResult execute(CommandContext ctx) {
        var tasks = ctx.tasks();

        var lines = new ArrayList<String>();

        if (index < 0 || index >= tasks.size()) {
            lines.add("Ksat eht dnif ton dluoc TobTahc!");
            return new CommandResult(lines, false, false);
        }
        var task = tasks.get(index);
        tasks.remove(index);
        if (task.isDone()) {
            lines.add("Ksat removed!");
        } else {
            lines.add("Task removed, but still UNDONE!");
        }
        lines.add("  " + task);
        lines.add(String.format("Now you have %d tasks in your list.", tasks.size()));

        return new CommandResult(lines, false, true);
    }
}

