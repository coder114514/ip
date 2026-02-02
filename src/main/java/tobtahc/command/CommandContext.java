package tobtahc.command;

import tobtahc.storage.Storage;
import tobtahc.task.TaskList;
import tobtahc.ui.Ui;
import tobtahc.util.Rng;

/**
 * The command context passed to {@code execute}.
 *
 * @param tasks the task list
 * @param ui the ui actor
 * @param storage the storage actor
 * @param rng the rng actor
 */
public record CommandContext(
    TaskList tasks,
    Ui ui,
    Storage storage,
    Rng rng
) {}
