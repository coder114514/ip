package tobtahc.command;

/**
 * The command for clearing the tasks.
 */
public class ClearCommand extends ModifyingCommand {
    @Override
    public void execute(CommandContext ctx) {
        var ui = ctx.ui();
        var tasks = ctx.tasks();
        var storage = ctx.storage();

        ui.botMessageSep();
        ui.botMessageLine("Your tasks are all cleared.");
        tasks.clear();
        ui.botMessageSep();
        saveTasks(tasks, ui, storage);
    }
}
