package tobtahc.command;

import java.util.ArrayList;

/**
 * Command to echo the user input back as a response.
 */
public class EchoCommand extends Command {
    private String input;

    /**
     * Constructs an {@code EchoCommand} with the text to be echoed.
     *
     * @param input the input string to be repeated
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
