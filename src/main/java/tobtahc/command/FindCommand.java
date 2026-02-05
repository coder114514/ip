package tobtahc.command;

import java.util.ArrayList;

/**
 * The command for listing the tasks.
 */
public class FindCommand extends Command {
    private String keyword;

    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public CommandResult execute(CommandContext ctx) {
        var tasks = ctx.tasks();
        int cnt = 0;

        var lines = new ArrayList<String>();

        lines.add("Here are the matching tasks in your list:");
        for (int i = 0; i < tasks.size(); ++i) {
            if (tasks.get(i).getDescription().indexOf(keyword) < 0) {
                continue;
            }
            ++cnt;
            lines.add(String.format("%s.%s", i + 1, tasks.get(i)));
        }
        if (cnt == 0) {
            lines.add("(empty)");
        }

        return new CommandResult(lines, false, false);
    }
}
