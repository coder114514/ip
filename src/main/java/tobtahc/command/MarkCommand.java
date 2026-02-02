package tobtahc.command;

/**
 * The command for marking a task as done.
 */
public class MarkCommand extends ModifyingCommand {
    private int index;

    /**
     * @param index the index of the task
     */
    public MarkCommand(int index) {
        this.index = index;
    }

    @Override
    public void execute(CommandContext ctx) {
        var ui = ctx.ui();
        var tasks = ctx.tasks();
        var storage = ctx.storage();

        if (index < 0 || index >= tasks.size()) {
            ui.botMessageSep();
            ui.botMessageLine("Ksat eht dnif ton dluoc TobTahc!");
            ui.botMessageSep();
            return;
        }
        var task = tasks.get(index);
        task.mark();
        ui.botMessageSep();
        ui.botMessageLine("Task marked as done!");
        ui.botMessageLine("  " + task);
        ui.botMessageSep();
        saveTasks(tasks, ui, storage);
    }
}
