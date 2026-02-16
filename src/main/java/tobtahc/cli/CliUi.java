package tobtahc.cli;

import java.util.Scanner;

class CliUi {
    private static final String INDENT = "    ";
    private static final String SEPARATOR =
            "___________________________________________________________________";

    private Scanner scanner;

    public CliUi() {
        scanner = new Scanner(System.in);
    }

    public String readInput() {
        return scanner.hasNextLine() ? scanner.nextLine() : null;
    }

    public void printMessageLine() {
        System.out.println();
    }

    public void printMessageLine(String message) {
        System.out.println(INDENT + message);
    }

    public void printMessageSep() {
        printMessageLine(SEPARATOR);
        printMessageLine();
    }

    public void printMessageSepError() {
        printMessageLine("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }

    public void printIntro() {
        printMessageSep();
        printMessageLine("Hello! I'm TobTahc. Tob tahc a ma I.");
        printMessageLine("What can I do for you?");
        printMessageSep();
    }

    public void printBye(boolean isEndByEof) {
        printMessageSep();
        if (isEndByEof) {
            printMessageLine("EOF DETECTED! Remember to say 'bye' next time!");
        }
        printMessageLine("Noos niaga uoy ees ot epoh! Eyb.");
        printMessageSep();
    }
}
