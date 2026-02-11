package tobtahc;

import java.io.IOException;

import tobtahc.command.CommandContext;
import tobtahc.command.CommandParseError;
import tobtahc.command.CommandParser;
import tobtahc.storage.Storage;
import tobtahc.task.TaskList;
import tobtahc.ui.CliUi;
import tobtahc.util.Rng;

/**
 * The app object.
 */
public class CliApp {
    private Storage storage;
    private CliUi cliUi;
    private Rng rng;
    private TaskList tasks;
    private boolean endByEof;
    private CommandContext ctx;

    /**
     * Initializes the CLI app object.
     *
     * @param dataDir path of the date directory
     * @param saveFileName name of the save file
     */
    public CliApp(String dataDir, String saveFileName) {
        storage = new Storage(dataDir, saveFileName);
        cliUi = new CliUi();
        rng = new Rng();
        endByEof = false;
    }

    /**
     * The main program logic.
     *
     * @return program exit code
     */
    public int run() {
        cliUi.printIntro();

        try {
            var result = storage.loadTasks();
            tasks = result.tasks();
            if (result.numBadLines() > 0) {
                cliUi.printMessageSepError();
                cliUi.printMessageLine(String.format(
                        "Info: there were %d bad lines in the save file, which will be removed.",
                                result.numBadLines()));
                cliUi.printMessageSepError();
                cliUi.printMessageLine();
            }
        } catch (IOException e) {
            cliUi.printMessageSepError();
            cliUi.printMessageLine("Could not load the save file.");
            cliUi.printMessageLine("IO Error: " + e.getMessage() + ".");
            cliUi.printMessageLine("Aborting.");
            cliUi.printMessageSepError();
            return 1;
        }

        ctx = new CommandContext(tasks, rng);

        for (;;) {
            String input = cliUi.readInput();
            if (input == null) {
                endByEof = true;
                break;
            }

            try {
                var cmd = CommandParser.parse(input);
                var result = cmd.execute(ctx);
                var lines = result.messageLines();

                if (!lines.isEmpty()) {
                    cliUi.printMessageSep();
                    for (var line : lines) {
                        cliUi.printMessageLine(line);
                    }
                    cliUi.printMessageSep();
                }

                if (result.needSave()) {
                    try {
                        storage.saveTasks(tasks);
                    } catch (IOException e) {
                        cliUi.printMessageSepError();
                        cliUi.printMessageLine("Could not save the tasks.");
                        cliUi.printMessageLine("IO Error: " + e.getMessage() + ".");
                        cliUi.printMessageSepError();
                        cliUi.printMessageLine();
                    }
                }

                if (result.isExit()) {
                    break;
                }
            } catch (CommandParseError e) {
                cliUi.printMessageSep();
                for (var line : e.getLines()) {
                    cliUi.printMessageLine(line);
                }
                cliUi.printMessageSep();
            }
        }

        cliUi.printBye(endByEof);

        return 0;
    }
}
