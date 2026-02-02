package tobtahc.command;

/**
 * This is the base class for all the commands.
 */
public abstract class Command {
    /**
     * Executes the command itself.
     *
     * @param ctx the command context
     */
    public abstract void execute(CommandContext ctx);

    /**
     * {@return true if this command should terminate the bot}
     */
    public boolean isExit() {
        return false;
    }
}
