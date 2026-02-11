package tobtahc.command;

import java.util.ArrayList;

/**
 * The command for marking a task as done.
 */
public class MarkCommand extends Command {
    private int index;

    /**
     * Initiailizing the command for marking a task.
     *
     * @param index the index of the task
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
