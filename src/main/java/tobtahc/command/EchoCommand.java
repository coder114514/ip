package tobtahc.command;

import tobtahc.storage.Storage;
import tobtahc.task.TaskList;
import tobtahc.ui.Ui;
import tobtahc.util.Rng;

/**
 * The command for echoing the user input.
 */
public class EchoCommand extends Command {
    private String input;
    private Rng rng;

    /**
     * @param input the user input
     */
    public EchoCommand(String input, Rng rng) {
        this.input = input;
        this.rng = rng;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.botMessageSep();
        if (rng.getRng() % 4 == 0) {
            ui.botMessageLine("ohce: " + input);
        } else {
            ui.botMessageLine("echo: " + input);
        }
        ui.botMessageSep();
    }
}
