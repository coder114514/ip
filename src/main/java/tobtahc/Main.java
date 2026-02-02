package tobtahc;

import java.io.IOException;

import tobtahc.command.CommandContext;
import tobtahc.command.CommandParseError;
import tobtahc.command.CommandParser;
import tobtahc.storage.Storage;
import tobtahc.task.TaskList;
import tobtahc.ui.Ui;
import tobtahc.util.Rng;

/**
 * The main program.
 */
public class Main {
    private Storage storage;
    private Ui ui;
    private Rng rng;
    private TaskList tasks;
    private boolean endByEof;
    private CommandContext ctx;

    /**
     * The main program launcher.
     *
     * @param args command line arguments passed to the program
     */
    public static void main(String[] args) {
        new Main("../data", "tasks.txt").run();
    }

    /**
     * @param dataDirPath path of the date directory
     * @param saveFilePath name of the save file
     */
    public Main(String dataDir, String saveFileName) {
        storage = new Storage(dataDir, saveFileName);
        ui = new Ui();
        rng = new Rng();
        endByEof = false;
    }

    /**
     * The main program logic.
     */
    public void run() {
        ui.chatIntro();

        try {
            var result = storage.loadTasks();
            tasks = result.tasks();
            if (result.numBadLines() > 0) {
                ui.botMessageSepError();
                ui.botMessageLine(String.format(
                        "Info: there were %d bad lines in the save file, which will be removed.",
                                result.numBadLines()));
                ui.botMessageSepError();
                ui.botMessageLine();
            }
        } catch (IOException e) {
            ui.botMessageSepError();
            ui.botMessageLine("Error: " + e.getMessage() + ".");
            ui.botMessageLine("Aborting");
            ui.botMessageSepError();
            return;
        }

        ctx = new CommandContext(tasks, ui, storage, rng);

        for (;;) {
            String input = ui.readInput();
            if (input == null) {
                endByEof = true;
                break;
            }
            rng.nextRng(input.hashCode());

            try {
                var cmd = CommandParser.parse(input);
                cmd.execute(ctx);

                if (cmd.isExit()) {
                    break;
                }
            } catch (CommandParseError e) {
                ui.botMessageSep();
                for (var line : e.getLines()) {
                    ui.botMessageLine(line);
                }
                ui.botMessageSep();
            }
        }

        ui.chatBye(endByEof);
    }
}
