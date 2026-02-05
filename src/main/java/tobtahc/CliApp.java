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
 * The app object.
 */
public class CliApp {
    private Storage storage;
    private Ui ui;
    private Rng rng;
    private TaskList tasks;
    private boolean endByEof;
    private CommandContext ctx;

    /**
     * @param dataDirPath path of the date directory
     * @param saveFilePath name of the save file
     */
    public CliApp(String dataDir, String saveFileName) {
        storage = new Storage(dataDir, saveFileName);
        ui = new Ui();
        rng = new Rng();
        endByEof = false;
    }

    /**
     * The main program logic.
     *
     * @return program exit code
     */
    public int run() {
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
            ui.botMessageLine("Could not load the save file.");
            ui.botMessageLine("IO Error: " + e.getMessage() + ".");
            ui.botMessageLine("Aborting.");
            ui.botMessageSepError();
            return 1;
        }

        ctx = new CommandContext(tasks, rng);

        for (;;) {
            String input = ui.readInput();
            if (input == null) {
                endByEof = true;
                break;
            }

            try {
                var cmd = CommandParser.parse(input);
                var result = cmd.execute(ctx);
                var lines = result.messageLines();

                if (!lines.isEmpty()) {
                    ui.botMessageSep();
                    for (var line : lines) {
                        ui.botMessageLine(line);
                    }
                    ui.botMessageSep();
                }

                if (result.needSave()) {
                    try {
                        storage.saveTasks(tasks);
                    } catch (IOException e) {
                        ui.botMessageSepError();
                        ui.botMessageLine("Could not save the tasks.");
                        ui.botMessageLine("IO Error: " + e.getMessage() + ".");
                        ui.botMessageSepError();
                        ui.botMessageLine();
                    }
                }

                if (result.isExit()) {
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

        return 0;
    }
}
