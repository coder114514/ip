package tobtahc.command;

import tobtahc.task.TaskList;
import tobtahc.util.RandomProvider;

/**
 * The command context passed to {@code execute}.
 *
 * @param tasks the task list
 * @param rng the rng actor
 */
public record CommandContext(
    TaskList tasks,
    RandomProvider rng
) {}
