package tobtahc;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        chatIntro();

        var list = new ArrayList<Task>();
        var scanner = new Scanner(System.in);

        for (;;) {
            var input = scanner.nextLine();

            if (input.equals("bye")) {
                break;
            }

            if (input.equals("list")) {
                botMessageSep();
                botMessageLine("Here are the tasks in your list:");
                if (list.size() == 0) {
                    botMessageLine("(empty)");
                }
                for (int i = 0; i < list.size(); ++i) {
                    botMessageLine(String.format("%s.%s", i + 1, list.get(i)));
                }
                botMessageSep();
                continue;
            }

            var patternMarkUnmark = Pattern.compile("(?:un)?mark ([0-9]+)");
            var matcherMarkUnmark = patternMarkUnmark.matcher(input);

            if (matcherMarkUnmark.find()) {
                var indexString = matcherMarkUnmark.group(1);
                int index = Integer.parseInt(indexString) - 1;
                if (index < 0 || index >= list.size()) {
                    botMessageSep();
                    botMessageLine("Ksat eht dnif ton dluoc TobTahc!");
                    botMessageSep();
                    continue;
                }
                var task = list.get(index);
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

            list.add(new Task(input));
            botMessageSep();
            if (list.hashCode() % 4 == 0) {
                botMessageLine("dedda: " + input);
            } else {
                botMessageLine("added: " + input);
            }
            botMessageSep();
        }

        chatBye();
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

    public static void chatBye() {
        botMessageSep();
        botMessageLine("Noos niaga uoy ees ot epoh! Eyb.");
        botMessageSep();
    }
}
