package tobtahc.command;

/**
 * The command for echoing the user input.
 */
public class EchoCommand extends Command {
    private String input;

    /**
     * @param input the user input
     */
    public EchoCommand(String input) {
        this.input = input;
    }

    @Override
    public void execute(CommandContext ctx) {
        var ui = ctx.ui();
        var rng = ctx.rng();

        ui.botMessageSep();
        if (rng.getRng() % 4 == 0) {
            ui.botMessageLine("ohce: " + input);
        } else {
            ui.botMessageLine("echo: " + input);
        }
        ui.botMessageSep();
    }
}
