package tobtahc.command;

import java.util.List;

/**
 * Command to exit the application.
 */
public class ExitCommand extends Command {
    @Override
    public CommandResult execute(CommandContext ctx) {
        return new CommandResult(List.of(), true, false);
    }
}
