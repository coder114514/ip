package tobtahc;

import javafx.application.Application;
import tobtahc.cli.CliApp;
import tobtahc.gui.GuiApp;

/**
 * The entry point.
 */
public class Main {
    private static final String DATA_DIR = "data";
    private static final String SAVE_FILE_NAME = "tasks.txt";

    /**
     * The main program launcher.
     *
     * @param args command line arguments passed to the program
     */
    public static void main(String[] args) {
        if (args.length > 1) {
            System.err.println(
                    "Usage: java -jar tobtahc.jar [-g | -c | --gui | --cli | /gui | /cli | -h | --help | /?]");
            System.exit(1);
        }

        boolean useCli = false;

        if (args.length == 1) {
            var flag = args[0].toLowerCase();
            switch (flag) {
            case "-g":
            case "--gui":
            case "/gui":
                break;
            case "-c":
            case "--cli":
            case "/cli":
                useCli = true;
                break;
            case "-h":
            case "--help":
            case "/?":
                System.out.println("Usage: java -jar tobtahc.jar [options]");
                System.out.println();
                System.out.println("Options: ");
                System.out.printf("  %-20s %s%n", "-g, --gui, /gui",
                        "Start the application in GUI mode (default)");
                System.out.printf("  %-20s %s%n", "-c, --cli, /cli",
                        "Start the application in CLI mode");
                System.out.printf("  %-20s %s%n", "-h, --help, /?",
                        "Show this help message");
                System.exit(0);
                break; // not reachable, just to make checkstyle happy
            default:
                System.err.println(
                       "Usage: java -jar tobtahc.jar [-g | -c | --gui | --cli | /gui | /cli | -h | --help | /?]");
                System.exit(1);
            }
        }

        if (useCli) {
            int status = new CliApp(DATA_DIR, SAVE_FILE_NAME).run();
            System.exit(status);
        } else {
            String[] guiArgs = { DATA_DIR, SAVE_FILE_NAME };
            Application.launch(GuiApp.class, guiArgs);
        }
    }
}
