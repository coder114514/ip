package tobtahc.command;

/**
 * Abstract base class for all commands.
 */
public abstract class Command {
    /**
     * Executes the command logic using the provided context.
     *
     * @param ctx the context in which the command runs
     * @return the execution result
     */
    public abstract CommandResult execute(CommandContext ctx);
}
