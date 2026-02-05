package tobtahc.command;

import java.util.ArrayList;

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
    public CommandResult execute(CommandContext ctx) {
        var rng = ctx.rng();

        var lines = new ArrayList<String>();

        if (rng.chance(1, 4)) {
            lines.add("ohce: " + input);
        } else {
            lines.add("echo: " + input);
        }

        return new CommandResult(lines, false, false);
    }
}
