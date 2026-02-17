package tobtahc.gui;

import tobtahc.storage.Storage;
import tobtahc.task.TaskList;
import tobtahc.util.Rng;

/**
 * Contextual data for the current application user.
 *
 * @param tasks the list of tasks for the user
 * @param storage the storage manager for task persistence
 * @param rng the RNG provider
 */
record UserContext(
    TaskList tasks,
    Storage storage,
    Rng rng
) {}
