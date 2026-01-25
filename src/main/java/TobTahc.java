import java.util.Scanner;

public class TobTahc {
    public static void main(String[] args) {
        chatIntro();

        var scanner = new Scanner(System.in);
        for (;;) {
            var input = scanner.nextLine();
            if (input.equals("bye")) {
                break;
            }
            botMessageLine("___________________________________________________________________");
            botMessageLine(input);
            botMessageLine("___________________________________________________________________\n");
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

    public static void chatIntro() {
        botMessageLine("___________________________________________________________________");
        botMessageLine("Hello! I'm TobTahc. Tob tahc a ma I.");
        botMessageLine("What can I do for you?");
        botMessageLine("___________________________________________________________________");
        botMessageLine();
    }

    public static void chatBye() {
        botMessageLine("___________________________________________________________________");
        botMessageLine("Noos niaga uoy ees ot epoh! Eyb.");
        botMessageLine("___________________________________________________________________");
    }
}
