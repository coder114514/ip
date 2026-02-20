package tobtahc.command;

import java.util.ArrayList;

/**
 * Command to sort tasks.
 */
public class SortCommand extends Command {
    @Override
    public CommandResult execute(CommandContext ctx) {
        var tasks = ctx.tasks();
        var lines = new ArrayList<String>();

        lines.add("Here are all your undone deadlines, sorted by due time:");
        for (var d : tasks.getAllUndoneDeadlinesSorted()) {
            lines.add(String.format("%d.%s", d.index() + 1, d.task()));
        }

        lines.add("");
        lines.add("Here are all your undone events, sorted by start time:");
        for (var e : tasks.getAllUndoneEventsSorted()) {
            lines.add(String.format("%d.%s", e.index() + 1, e.task()));
        }

        lines.add("");
        lines.add("Here are all your undone todos:");
        for (var t : tasks.getAllUndoneToDos()) {
            lines.add(String.format("%d.%s", t.index() + 1, t.task()));
        }

        lines.add("");
        lines.add("And here are all your done tasks:");
        for (var t : tasks.getAllDoneTasks()) {
            lines.add(String.format("%d.%s", t.index() + 1, t.task()));
        }

        return new CommandResult(lines, false, false);
    }
}
