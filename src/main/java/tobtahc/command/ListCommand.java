package tobtahc.command;

import java.util.ArrayList;

/**
 * The command for listing the tasks.
 */
public class ListCommand extends Command {
    @Override
    public CommandResult execute(CommandContext ctx) {
        var tasks = ctx.tasks();

        var lines = new ArrayList<String>();

        lines.add(String.format("You have %s tasks in your list:", tasks.size()));
        if (tasks.size() == 0) {
            lines.add("(empty)");
        }
        for (int i = 0; i < tasks.size(); ++i) {
            lines.add(String.format("%s.%s", i + 1, tasks.get(i)));
        }

        return new CommandResult(lines, false, false);
    }
}
