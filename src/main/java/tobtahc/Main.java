package tobtahc;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import tobtahc.storage.Storage;
import tobtahc.task.Deadline;
import tobtahc.task.Event;
import tobtahc.task.NotATask;
import tobtahc.task.Task;
import tobtahc.task.TaskParseError;

/**
 * The main program.
 */
public class Main {
    /** The pattern for matching the commands. The \s* before the number makes it more forgiving. */
    private static final Pattern PATTERN_MARK_UNMARK = Pattern.compile("^(?:un)?mark\\s*([0-9]+)\\s*$");
    private static final Pattern PATTERN_DELETE = Pattern.compile("^delete\\s*([0-9]+)\\s*$");

    /**
     * The main program.
     *
     * @param args The command line args passed to the program.
     */
    public static void main(String[] args) {
        chatIntro();

        int rng = 0;
        boolean endByEof = false;
        var scanner = new Scanner(System.in);

        List<Task> tasks;
        boolean areTasksLoaded;
        try {
            var result = Storage.loadTasks();
            tasks = result.tasks();
            areTasksLoaded = true;
            if (result.numBadLines() > 0) {
                botMessageSepError();
                botMessageLine(String.format(
                        "Info: there were %d bad lines in the save file, which will be removed.",
                                result.numBadLines()));
                botMessageSepError();
                botMessageLine();
            }
        } catch (IOException e) {
            tasks = new ArrayList<>();
            areTasksLoaded = false;
            botMessageSepError();
            botMessageLine("Error: " + e.getMessage() + ".");
            botMessageLine("Tasks will only be written to the temp file.");
            botMessageSepError();
            botMessageLine();
        }

        for (;;) {
            if (!scanner.hasNextLine()) {
                endByEof = true;
                break;
            }

            var input = scanner.nextLine().trim();
            rng = Utils.nextRng(rng, input.hashCode());

            if (input.equals("bye")) {
                break;
            } else if (input.startsWith("bye")) {
                botMessageSep();
                botMessageLine("Enter 'bye' to quit.");
                botMessageSep();
                continue;
            }

            if (input.equals("list")) {
                botMessageSep();
                botMessageLine(String.format("You have %s tasks in your list:",
                        tasks.size()));
                if (tasks.size() == 0) {
                    botMessageLine("(empty)");
                }
                for (int i = 0; i < tasks.size(); ++i) {
                    botMessageLine(String.format("%s.%s", i + 1, tasks.get(i)));
                }
                botMessageSep();
                continue;
            } else if (input.startsWith("list")) {
                botMessageSep();
                botMessageLine("Enter 'list' to list your tasks.");
                botMessageSep();
                continue;
            }

            if (input.equals("clear")) {
                botMessageSep();
                botMessageLine("Your tasks are all cleared.");
                tasks.clear();
                botMessageSep();
                continue;
            } else if (input.startsWith("clear")) {
                botMessageSep();
                botMessageLine("Enter 'clear' to clear your tasks.");
                botMessageSep();
                continue;
            }

            if (input.startsWith("before or on")) {
                try {
                    botMessageSep();
                    var date = LocalDate.parse(input.substring(12).trim());
                    for (int i = 0; i < tasks.size(); ++i) {
                        var task = tasks.get(i);
                        if (!(task instanceof Deadline ddl)) {
                            continue;
                        }
                        if (!ddl.isBeforeOrOn(date)) {
                            continue;
                        }
                        botMessageLine("  " + (i + 1) + ": " + ddl.getDescription());
                    }
                    botMessageSep();
                } catch (DateTimeParseException e) {
                    botMessageSep();
                    botMessageLine("Syntax error! Correct syntax:");
                    botMessageLine("  before or on <date>");
                    botMessageSep();
                }
                continue;
            }

            if (input.startsWith("occurs on")) {
                try {
                    botMessageSep();
                    var date = LocalDate.parse(input.substring(9).trim());
                    for (int i = 0; i < tasks.size(); ++i) {
                        var task = tasks.get(i);
                        if (!(task instanceof Event event)) {
                            continue;
                        }
                        if (!event.occursOn(date)) {
                            continue;
                        }
                        botMessageLine("  " + (i + 1) + ": " + event.getDescription());
                    }
                    botMessageSep();
                } catch (DateTimeParseException e) {
                    botMessageSep();
                    botMessageLine("Syntax error! Correct syntax:");
                    botMessageLine("  occurs on <date>");
                    botMessageSep();
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
                    botMessageSep();
                    botMessageLine("Ksat eht dnif ton dluoc TobTahc!");
                    botMessageSep();
                    continue;
                }
                if (index < 0 || index >= tasks.size()) {
                    botMessageSep();
                    botMessageLine("Ksat eht dnif ton dluoc TobTahc!");
                    botMessageSep();
                    continue;
                }
                var task = tasks.get(index);
                if (input.charAt(0) == 'm') {
                    task.markAsDone();
                    botMessageSep();
                    botMessageLine("Task marked as enod!");
                    botMessageLine("  " + task);
                    botMessageSep();
                } else {
                    task.markAsUndone();
                    botMessageSep();
                    botMessageLine("Task marked as enodnu!");
                    botMessageLine("  " + task);
                    botMessageSep();
                }
                saveTasks(tasks, areTasksLoaded);
                continue;
            } else if (input.startsWith("mark") || input.startsWith("unmark")) {
                botMessageSep();
                botMessageLine("Syntax error! Correct syntax:");
                botMessageLine("  mark <no> or unmark <no>");
                botMessageSep();
                continue;
            }

            var matcherDelete = PATTERN_DELETE.matcher(input);

            if (matcherDelete.find()) {
                var indexString = matcherDelete.group(1);
                int index;
                try {
                    index = Integer.parseInt(indexString) - 1;
                } catch (NumberFormatException e) {
                    botMessageSep();
                    botMessageLine("Ksat eht dnif ton dluoc TobTahc!");
                    botMessageSep();
                    continue;
                }
                if (index < 0 || index >= tasks.size()) {
                    botMessageSep();
                    botMessageLine("Ksat eht dnif ton dluoc TobTahc!");
                    botMessageSep();
                    continue;
                }
                var task = tasks.get(index);
                tasks.remove(index);
                botMessageSep();
                if (task.isDone()) {
                    botMessageLine("Ksat removed!");
                } else {
                    botMessageLine("Task removed, but still UNDONE!");
                }
                botMessageLine("  " + task);
                botMessageLine(String.format("Now you have %s tasks in your list.",
                        tasks.size()));
                botMessageSep();
                saveTasks(tasks, areTasksLoaded);
                continue;
            } else if (input.startsWith("delete")) {
                botMessageSep();
                botMessageLine("Syntax error! Correct syntax:");
                botMessageLine("  delete <no>");
                botMessageSep();
                continue;
            }

            try {
                var task = Task.parseTask(input);
                tasks.add(task);
                botMessageSep();
                if (rng % 4 == 0) {
                    botMessageLine("Ti tog!");
                    botMessageLine("  dedda ksat: " + task.getDescription());
                    botMessageLine(String.format("Tsil eht ni sksat %s evah uoy now.",
                            tasks.size()));
                } else {
                    botMessageLine("Got it!");
                    botMessageLine("  task added: " + task.getDescription());
                    botMessageLine(String.format("Now you have %s tasks in your list.",
                            tasks.size()));
                }
                botMessageSep();
                saveTasks(tasks, areTasksLoaded);

            } catch (TaskParseError e) {
                botMessageSep();
                botMessageLine("Syntax error! Correct syntax:");
                switch (e.getTaskType()) {
                case TODO:
                    botMessageLine("  todo <task>");
                    break;
                case DEADLINE:
                    botMessageLine("  deadline <task> /by <time>");
                    botMessageLine("And the correct form of <time> should be " + Utils.DATE_TIME_FORMATTER_INPUT_STRING);
                    break;
                case EVENT:
                    botMessageLine("  event <task> /from <time> /to <time>");
                    botMessageLine("And the correct form of <time> should be " + Utils.DATE_TIME_FORMATTER_INPUT_STRING);
                    break;
                }
                botMessageSep();

            } catch (NotATask e) {
                botMessageSep();
                if (rng % 4 == 0) {
                    botMessageLine("ohce: " + input);
                } else {
                    botMessageLine("echo: " + input);
                }
                botMessageSep();
            }
        }

        saveTasks(tasks, areTasksLoaded);
        chatBye(endByEof);
        scanner.close();
    }

    private static void saveTasks(List<Task> tasks, boolean areTasksLoaded) {
        try {
            Storage.saveTasks(tasks, areTasksLoaded);
        } catch (IOException e) {
            botMessageSepError();
            botMessageLine("Error: " + e.getMessage() + ".");
            botMessageSepError();
            botMessageLine();
        }
    }

    /**
     * A helper for displaying the bot's response with an indentation.
     * When called with no arguments, it just outputs a newline.
     */
    private static void botMessageLine() {
        System.out.println();
    }

    /**
     * A helper for displaying the bot's response with an indentation.
     *
     * @param message The message to display.
     */
    private static void botMessageLine(String message) {
        System.out.println("    " + message);
    }

    /**
     * A helper for displaying the separator line.
     */
    private static void botMessageSep() {
        botMessageLine("___________________________________________________________________\n");
    }

    private static void botMessageSepError() {
        botMessageLine("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }

    /**
     * A helper for displaying the intro message.
     */
    private static void chatIntro() {
        botMessageSep();
        botMessageLine("Hello! I'm TobTahc. Tob tahc a ma I.");
        botMessageLine("What can I do for you?");
        botMessageSep();
    }

    /**
     * A helper for displaying the bye message.
     *
     * @param endByEof If the chat is ended by an EOF instead of the user input 'bye',
     * display an info message.
     */
    private static void chatBye(boolean endByEof) {
        botMessageSep();
        if (endByEof) {
            botMessageLine("EOF DETECTED! Remember to say 'bye' next time!");
        }
        botMessageLine("Noos niaga uoy ees ot epoh! Eyb.");
        botMessageSep();
    }
}
