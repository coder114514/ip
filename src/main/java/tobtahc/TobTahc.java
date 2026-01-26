package tobtahc;

import java.util.Scanner;
import java.util.ArrayList;

public class TobTahc {
    public static void main(String[] args) {
        chatIntro();

        var list = new ArrayList<String>();
        var scanner = new Scanner(System.in);
        for (;;) {
            var input = scanner.nextLine();
            if (input.equals("bye")) {
                break;
            }
            if (input.equals("list")) {
                botMessageSep();
                if (list.size() == 0) {
                    botMessageLine("(empty)");
                }
                for (int i = 0; i < list.size(); ++i) {
                    botMessageLine(String.format("%s: %s", i + 1, list.get(i)));
                }
                botMessageSep();
                continue;
            }
            list.add(input);
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
