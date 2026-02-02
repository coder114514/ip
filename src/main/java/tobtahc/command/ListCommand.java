package tobtahc.command;

/**
 * The command for listing the tasks.
 */
public class ListCommand extends Command {
    @Override
    public void execute(CommandContext ctx) {
        var ui = ctx.ui();
        var tasks = ctx.tasks();

        ui.botMessageSep();
        ui.botMessageLine(String.format("You have %s tasks in your list:", tasks.size()));
        if (tasks.size() == 0) {
            ui.botMessageLine("(empty)");
        }
        for (int i = 0; i < tasks.size(); ++i) {
            ui.botMessageLine(String.format("%s.%s", i + 1, tasks.get(i)));
        }
        ui.botMessageSep();
    }
}
