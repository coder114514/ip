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
     * @param dataDirPath path of the date directory
     * @param saveFilePath name of the save file
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
        cliUi.chatIntro();

        try {
            var result = storage.loadTasks();
            tasks = result.tasks();
            if (result.numBadLines() > 0) {
                cliUi.botMessageSepError();
                cliUi.botMessageLine(String.format(
                        "Info: there were %d bad lines in the save file, which will be removed.",
                                result.numBadLines()));
                cliUi.botMessageSepError();
                cliUi.botMessageLine();
            }
        } catch (IOException e) {
            cliUi.botMessageSepError();
            cliUi.botMessageLine("Could not load the save file.");
            cliUi.botMessageLine("IO Error: " + e.getMessage() + ".");
            cliUi.botMessageLine("Aborting.");
            cliUi.botMessageSepError();
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
                    cliUi.botMessageSep();
                    for (var line : lines) {
                        cliUi.botMessageLine(line);
                    }
                    cliUi.botMessageSep();
                }

                if (result.needSave()) {
                    try {
                        storage.saveTasks(tasks);
                    } catch (IOException e) {
                        cliUi.botMessageSepError();
                        cliUi.botMessageLine("Could not save the tasks.");
                        cliUi.botMessageLine("IO Error: " + e.getMessage() + ".");
                        cliUi.botMessageSepError();
                        cliUi.botMessageLine();
                    }
                }

                if (result.isExit()) {
                    break;
                }
            } catch (CommandParseError e) {
                cliUi.botMessageSep();
                for (var line : e.getLines()) {
                    cliUi.botMessageLine(line);
                }
                cliUi.botMessageSep();
            }
        }

        cliUi.chatBye(endByEof);

        return 0;
    }
}
