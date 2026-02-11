package tobtahc.command;

import java.util.ArrayList;

/**
 * The command for deleting a task.
 */
public class DeleteCommand extends Command {
    private int index;

    /**
     * Initializes the command with the index of the task to delete.
     *
     * @param index the index of the task
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
        lines.add(String.format("Now you have %s tasks in your list.", tasks.size()));

        return new CommandResult(lines, false, true);
    }
}

