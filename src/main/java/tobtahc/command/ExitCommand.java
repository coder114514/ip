package tobtahc.command;

/**
 * The command for exiting the chat bot, aka "bye".
 */
public class ExitCommand extends Command {
    @Override
    public void execute(CommandContext ctx) {
    }

    @Override
    public boolean isExit() {
        return true;
    }
}
