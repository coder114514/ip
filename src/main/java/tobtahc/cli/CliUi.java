package tobtahc.cli;

import java.util.Scanner;

/**
 * This class implements CLI helpers.
 */
class CliUi {
    private Scanner scanner;

    /**
     * Initializes the UI actor for CLI app.
     */
    public CliUi() {
        scanner = new Scanner(System.in);
    }

    /**
     * {@return the user input from stdin, null if EOF is met}
     */
    public String readInput() {
        if (!scanner.hasNextLine()) {
            return null;
        }
        return scanner.nextLine();
    }

    /**
     * A helper for displaying the bot's response with an indentation.
     * When called with no arguments, it just outputs a newline.
     */
    public void printMessageLine() {
        System.out.println();
    }

    /**
     * A helper for displaying the bot's response with an indentation.
     *
     * @param message message to display
     */
    public void printMessageLine(String message) {
        System.out.println("    " + message);
    }

    /**
     * A helper for displaying the separator line.
     */
    public void printMessageSep() {
        printMessageLine("___________________________________________________________________\n");
    }

    /**
     * A helper for displaying the separator line for errors.
     */
    public void printMessageSepError() {
        printMessageLine("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }

    /**
     * A helper for displaying the intro message.
     */
    public void printIntro() {
        printMessageSep();
        printMessageLine("Hello! I'm TobTahc. Tob tahc a ma I.");
        printMessageLine("What can I do for you?");
        printMessageSep();
    }

    /**
     * A helper for displaying the bye message.
     *
     * @param endByEof if the chat is ended by an EOF instead of the user input 'bye',
     *     display an info message.
     */
    public void printBye(boolean endByEof) {
        printMessageSep();
        if (endByEof) {
            printMessageLine("EOF DETECTED! Remember to say 'bye' next time!");
        }
        printMessageLine("Noos niaga uoy ees ot epoh! Eyb.");
        printMessageSep();
    }
}
