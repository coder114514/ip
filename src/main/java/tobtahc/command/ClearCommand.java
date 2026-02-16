package tobtahc.command;

import java.util.ArrayList;

/**
 * Command to clear tasks.
 */
public class ClearCommand extends Command {
    @Override
    public CommandResult execute(CommandContext ctx) {
        var tasks = ctx.tasks();

        var lines = new ArrayList<String>();

        tasks.clear();
        lines.add("Your tasks are all cleared.");

        return new CommandResult(lines, false, true);
    }
}
