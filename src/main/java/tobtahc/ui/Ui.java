package tobtahc.ui;

/**
 * This class implements UI stuff.
 */
public class Ui {
    /**
     * A helper for displaying the bot's response with an indentation.
     * When called with no arguments, it just outputs a newline.
     */
    public void botMessageLine() {
        System.out.println();
    }

    /**
     * A helper for displaying the bot's response with an indentation.
     *
     * @param message message to display
     */
    public void botMessageLine(String message) {
        System.out.println("    " + message);
    }
 
    /**
     * A helper for displaying the separator line.
     */
    public void botMessageSep() {
        botMessageLine("___________________________________________________________________\n");
    }

    /**
     * A helper for displaying the separator line for errors.
     */
    public void botMessageSepError() {
        botMessageLine("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }

    /**
     * A helper for displaying the intro message.
     */
    public void chatIntro() {
        botMessageSep();
        botMessageLine("Hello! I'm TobTahc. Tob tahc a ma I.");
        botMessageLine("What can I do for you?");
        botMessageSep();
    }

    /**
     * A helper for displaying the bye message.
     *
     * @param endByEof if the chat is ended by an EOF instead of the user input 'bye',
     *     display an info message.
     */
    public void chatBye(boolean endByEof) {
        botMessageSep();
        if (endByEof) {
            botMessageLine("EOF DETECTED! Remember to say 'bye' next time!");
        }
        botMessageLine("Noos niaga uoy ees ot epoh! Eyb.");
        botMessageSep();
    }
}
