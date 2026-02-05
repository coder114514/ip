package tobtahc.command;

import java.util.List;

/**
 * The command for exiting the chat bot, aka "bye".
 */
public class ExitCommand extends Command {
    @Override
    public CommandResult execute(CommandContext ctx) {
        return new CommandResult(List.of(), true, false);
    }
}
