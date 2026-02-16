package tobtahc.gui;

import tobtahc.storage.Storage;
import tobtahc.task.TaskList;
import tobtahc.util.Rng;

record UserContext(
    TaskList tasks,
    Storage storage,
    Rng rng
) {}
