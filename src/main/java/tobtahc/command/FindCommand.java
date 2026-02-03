package tobtahc.command;

/**
 * The command for listing the tasks.
 */
public class FindCommand extends Command {
    private String keyword;

    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public void execute(CommandContext ctx) {
        var ui = ctx.ui();
        var tasks = ctx.tasks();
        int cnt = 0;

        ui.botMessageSep();
        ui.botMessageLine("Here are the matching tasks in your list:");
        for (int i = 0; i < tasks.size(); ++i) {
            if (tasks.get(i).getDescription().indexOf(keyword) < 0) {
                continue;
            }
            ++cnt;
            ui.botMessageLine(String.format("%s.%s", i + 1, tasks.get(i)));
        }
        if (cnt == 0) {
            ui.botMessageLine("(empty)");
        }
        ui.botMessageSep();
    }
}
