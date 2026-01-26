package tobtahc;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

import tobtahc.exceptions.NotATask;
import tobtahc.exceptions.TaskParseError;
import tobtahc.task.Task;
import tobtahc.task.TaskType;

public class Main {
    public static void main(String[] args) {
        chatIntro();

        int rng = 0;
        boolean endByEof = false;
        var tasks = new ArrayList<Task>();
        var scanner = new Scanner(System.in);

        for (;;) {
            if (!scanner.hasNextLine()) {
                endByEof = true;
                break;
            }

            var input = scanner.nextLine();

            if (input.equals("bye")) {
                break;
            }

            rng = Utils.nextRng(rng, input.hashCode());

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
            }

            var patternMarkUnmark = Pattern.compile("(?:un)?mark\\s+([0-9]+)\\s*");
            var matcherMarkUnmark = patternMarkUnmark.matcher(input);

            if (matcherMarkUnmark.find()) {
                var indexString = matcherMarkUnmark.group(1);
                int index = Integer.parseInt(indexString) - 1;
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
                    botMessageLine(String.format("Now you have %s tasks in the list.",
                                                 tasks.size()));
                }
                botMessageSep();

            } catch (TaskParseError e) {
                botMessageSep();
                botMessageLine("Syntax error! Correct syntax:");
                switch (e.getTaskType()) {
                    case TODO: {
                        botMessageLine("  todo <task>");
                        break;
                    }
                    case DEADLINE: {
                        botMessageLine("  deadline <task> /by <time>");
                        break;
                    }
                    case EVENT: {
                        botMessageLine("  event <task> /from <time> /to <time>");
                        break;
                    }
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

        chatBye(endByEof);
        scanner.close();
    }

    public static void botMessageLine() {
        System.out.println();
    }

    public static void botMessageLine(String str) {
        System.out.println("    " + str);
    }

    public static void botMessageSep() {
        botMessageLine("___________________________________________________________________\n");
    }

    public static void chatIntro() {
        botMessageSep();
        botMessageLine("Hello! I'm TobTahc. Tob tahc a ma I.");
        botMessageLine("What can I do for you?");
        botMessageSep();
        botMessageLine();
    }

    public static void chatBye(boolean endByEof) {
        botMessageSep();
        if (endByEof) {
            botMessageLine("EOF DETECTED! Remember to say 'bye' next time!");
        }
        botMessageLine("Noos niaga uoy ees ot epoh! Eyb.");
        botMessageSep();
    }
}
