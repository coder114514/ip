package tobtahc.command;

import java.util.ArrayList;

/**
 * Command to mark a task as completed.
 */
public class MarkCommand extends Command {
    private int index;

    /**
     * Constructs a {@code MarkCommand} with the target index.
     *
     * @param index the zero-based index of the task to mark
     */
    public MarkCommand(int index) {
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
        task.mark();
        lines.add("Task marked as done!");
        lines.add("  " + task);
        return new CommandResult(lines, false, true);
    }
}
