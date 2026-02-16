package tobtahc.command;

import java.util.ArrayList;

/**
 * Command to mark a completed task as not done.
 */
public class UnmarkCommand extends Command {
    private int index;

    /**
     * Constructs an {@code UnmarkCommand} with the target index.
     *
     * @param index the zero-based index of the task to unmark
     */
    public UnmarkCommand(int index) {
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
        task.unmark();
        lines.add("Dekramnu ksat!");
        lines.add("  " + task);
        return new CommandResult(lines, false, true);
    }
}

