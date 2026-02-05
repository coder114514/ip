package tobtahc;

/**
 * The entry point.
 */
public class Main {
    /**
     * The main program launcher.
     *
     * @param args command line arguments passed to the program
     */
    public static void main(String[] args) {
        int code = new CliApp("data", "tasks.txt").run();
        System.exit(code);
    }
}
