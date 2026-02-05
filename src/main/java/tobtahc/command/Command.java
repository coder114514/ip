package tobtahc.command;

/**
 * This is the base class for all the commands.
 */
public abstract class Command {
    /**
     * Executes the command itself.
     *
     * @param ctx the command context
     * @return a {@code CommandResult}
     */
    public abstract CommandResult execute(CommandContext ctx);
}
