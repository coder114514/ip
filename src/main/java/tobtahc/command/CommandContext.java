package tobtahc.command;

import tobtahc.task.TaskList;
import tobtahc.util.Rng;

/**
 * The command context passed to {@code execute}.
 *
 * @param tasks the task list
 * @param rng the rng actor
 */
public record CommandContext(
    TaskList tasks,
    Rng rng
) {}
