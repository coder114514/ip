package tobtahc.command;

import tobtahc.task.TaskList;
import tobtahc.util.RandomProvider;

/**
 * Contextual data required to execute a command.
 *
 * @param tasks the task list to be operated on
 * @param rng the RNG provider
 */
public record CommandContext(
    TaskList tasks,
    RandomProvider rng
) {}
