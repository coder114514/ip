package tobtahc;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.regex.Pattern;

import tobtahc.storage.Storage;
import tobtahc.task.Deadline;
import tobtahc.task.Event;
import tobtahc.task.NotATask;
import tobtahc.task.TaskFormatError;
import tobtahc.task.TaskList;
import tobtahc.task.TaskParser;
import tobtahc.ui.Ui;
import tobtahc.util.Rng;
import tobtahc.util.Utils;

/**
 * The main program.
 */
public class Main {
    private Storage storage;
    private Ui ui;

    /** The pattern for matching the commands. The \s* before the number makes it more forgiving. */
    private static final Pattern PATTERN_MARK_UNMARK = Pattern.compile("^(?:un)?mark\\s*([0-9]+)\\s*$");
    private static final Pattern PATTERN_DELETE = Pattern.compile("^delete\\s*([0-9]+)\\s*$");

    /**
     * The main program launcher.
     *
     * @param args command line arguments passed to the program
     */
    public static void main(String[] args) {
        new Main("../data", "tasks.txt", "tasks.tmp.txt").run();
    }

    /**
     * @param dataDirPath path of the date directory
     * @param saveFilePath name of the save file
     * @param tempFilePath name of the temp file
     */
    public Main(String dataDir, String saveFileName, String tempFileName) {
        storage = new Storage("../data", "tasks.txt", "tasks.tmp.txt");
        ui = new Ui();
    }

    /**
     * The main program logic.
     */
    public void run() {
        ui.chatIntro();

        Rng rng = new Rng();
        boolean endByEof = false;
        var scanner = new Scanner(System.in);

        TaskList tasks;
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
            tasks = new TaskList();
            storage.switchToFallbackMode();
            ui.botMessageSepError();
            ui.botMessageLine("Error: " + e.getMessage() + ".");
            ui.botMessageLine("Tasks will only be written to the temp file.");
            ui.botMessageSepError();
            ui.botMessageLine();
        }

        for (;;) {
            if (!scanner.hasNextLine()) {
                endByEof = true;
                break;
            }

            var input = scanner.nextLine().trim();
            rng.nextRng(input.hashCode());

            if (input.equals("bye")) {
                break;
            } else if (input.startsWith("bye")) {
                ui.botMessageSep();
                ui.botMessageLine("Enter 'bye' to quit.");
                ui.botMessageSep();
                continue;
            }

            if (input.equals("list")) {
                ui.botMessageSep();
                ui.botMessageLine(String.format("You have %s tasks in your list:",
                        tasks.size()));
                if (tasks.size() == 0) {
                    ui.botMessageLine("(empty)");
                }
                for (int i = 0; i < tasks.size(); ++i) {
                    ui.botMessageLine(String.format("%s.%s", i + 1, tasks.get(i)));
                }
                ui.botMessageSep();
                continue;
            } else if (input.startsWith("list")) {
                ui.botMessageSep();
                ui.botMessageLine("Enter 'list' to list your tasks.");
                ui.botMessageSep();
                continue;
            }

            if (input.equals("clear")) {
                ui.botMessageSep();
                ui.botMessageLine("Your tasks are all cleared.");
                tasks.clear();
                ui.botMessageSep();
                continue;
            } else if (input.startsWith("clear")) {
                ui.botMessageSep();
                ui.botMessageLine("Enter 'clear' to clear your tasks.");
                ui.botMessageSep();
                continue;
            }

            if (input.startsWith("before or on")) {
                try {
                    ui.botMessageSep();
                    var date = LocalDate.parse(input.substring(12).trim());
                    for (int i = 0; i < tasks.size(); ++i) {
                        var task = tasks.get(i);
                        if (!(task instanceof Deadline ddl)) {
                            continue;
                        }
                        if (!ddl.isBeforeOrOn(date)) {
                            continue;
                        }
                        ui.botMessageLine("  " + (i + 1) + ": " + ddl.getDescription());
                    }
                    ui.botMessageSep();
                } catch (DateTimeParseException e) {
                    ui.botMessageSep();
                    ui.botMessageLine("Syntax error! Correct syntax:");
                    ui.botMessageLine("  before or on <date>");
                    ui.botMessageSep();
                }
                continue;
            }

            if (input.startsWith("occurs on")) {
                try {
                    ui.botMessageSep();
                    var date = LocalDate.parse(input.substring(9).trim());
                    for (int i = 0; i < tasks.size(); ++i) {
                        var task = tasks.get(i);
                        if (!(task instanceof Event event)) {
                            continue;
                        }
                        if (!event.occursOn(date)) {
                            continue;
                        }
                        ui.botMessageLine("  " + (i + 1) + ": " + event.getDescription());
                    }
                    ui.botMessageSep();
                } catch (DateTimeParseException e) {
                    ui.botMessageSep();
                    ui.botMessageLine("Syntax error! Correct syntax:");
                    ui.botMessageLine("  occurs on <date>");
                    ui.botMessageSep();
                }
                continue;
            }

            var matcherMarkUnmark = PATTERN_MARK_UNMARK.matcher(input);

            if (matcherMarkUnmark.find()) {
                var indexString = matcherMarkUnmark.group(1);
                int index;
                try {
                    index = Integer.parseInt(indexString) - 1;
                } catch (NumberFormatException e) {
                    ui.botMessageSep();
                    ui.botMessageLine("Ksat eht dnif ton dluoc TobTahc!");
                    ui.botMessageSep();
                    continue;
                }
                if (index < 0 || index >= tasks.size()) {
                    ui.botMessageSep();
                    ui.botMessageLine("Ksat eht dnif ton dluoc TobTahc!");
                    ui.botMessageSep();
                    continue;
                }
                var task = tasks.get(index);
                if (input.charAt(0) == 'm') {
                    task.mark();
                    ui.botMessageSep();
                    ui.botMessageLine("Task marked as done!");
                    ui.botMessageLine("  " + task);
                    ui.botMessageSep();
                } else {
                    task.unmark();
                    ui.botMessageSep();
                    ui.botMessageLine("Dekramnu ksat!");
                    ui.botMessageLine("  " + task);
                    ui.botMessageSep();
                }
                saveTasks(tasks);
                continue;
            } else if (input.startsWith("mark") || input.startsWith("unmark")) {
                ui.botMessageSep();
                ui.botMessageLine("Syntax error! Correct syntax:");
                ui.botMessageLine("  mark <no> or unmark <no>");
                ui.botMessageSep();
                continue;
            }

            var matcherDelete = PATTERN_DELETE.matcher(input);

            if (matcherDelete.find()) {
                var indexString = matcherDelete.group(1);
                int index;
                try {
                    index = Integer.parseInt(indexString) - 1;
                } catch (NumberFormatException e) {
                    ui.botMessageSep();
                    ui.botMessageLine("Ksat eht dnif ton dluoc TobTahc!");
                    ui.botMessageSep();
                    continue;
                }
                if (index < 0 || index >= tasks.size()) {
                    ui.botMessageSep();
                    ui.botMessageLine("Ksat eht dnif ton dluoc TobTahc!");
                    ui.botMessageSep();
                    continue;
                }
                var task = tasks.get(index);
                tasks.remove(index);
                ui.botMessageSep();
                if (task.isDone()) {
                    ui.botMessageLine("Ksat removed!");
                } else {
                    ui.botMessageLine("Task removed, but still UNDONE!");
                }
                ui.botMessageLine("  " + task);
                ui.botMessageLine(String.format("Now you have %s tasks in your list.",
                        tasks.size()));
                ui.botMessageSep();
                saveTasks(tasks);
                continue;
            } else if (input.startsWith("delete")) {
                ui.botMessageSep();
                ui.botMessageLine("Syntax error! Correct syntax:");
                ui.botMessageLine("  delete <no>");
                ui.botMessageSep();
                continue;
            }

            try {
                var task = TaskParser.parse(input);
                tasks.add(task);
                ui.botMessageSep();
                if (rng.getRng() % 4 == 0) {
                    ui.botMessageLine("Ti tog!");
                    ui.botMessageLine("  dedda ksat: " + task.getDescription());
                    ui.botMessageLine(String.format("Tsil eht ni sksat %s evah uoy now.",
                            tasks.size()));
                } else {
                    ui.botMessageLine("Got it!");
                    ui.botMessageLine("  task added: " + task.getDescription());
                    ui.botMessageLine(String.format("Now you have %s tasks in your list.",
                            tasks.size()));
                }
                ui.botMessageSep();
                saveTasks(tasks);

            } catch (TaskFormatError e) {
                ui.botMessageSep();
                ui.botMessageLine("Syntax error! Correct syntax:");
                switch (e.getTaskType()) {
                case TODO:
                    ui.botMessageLine("  todo <task>");
                    break;
                case DEADLINE:
                    ui.botMessageLine("  deadline <task> /by <time>");
                    ui.botMessageLine("And the correct form of <time> should be " + Utils.DATE_TIME_FORMATTER_INPUT_STRING);
                    break;
                case EVENT:
                    ui.botMessageLine("  event <task> /from <time> /to <time>");
                    ui.botMessageLine("And the correct form of <time> should be " + Utils.DATE_TIME_FORMATTER_INPUT_STRING);
                    break;
                }
                ui.botMessageSep();

            } catch (NotATask e) {
                ui.botMessageSep();
                if (rng.getRng() % 4 == 0) {
                    ui.botMessageLine("ohce: " + input);
                } else {
                    ui.botMessageLine("echo: " + input);
                }
                ui.botMessageSep();
            }
        }

        saveTasks(tasks);
        ui.chatBye(endByEof);
        scanner.close();
    }

    private void saveTasks(TaskList tasks) {
        try {
            storage.saveTasks(tasks);
        } catch (IOException e) {
            ui.botMessageSepError();
            ui.botMessageLine("Error: " + e.getMessage() + ".");
            ui.botMessageSepError();
            ui.botMessageLine();
        }
    }
}
