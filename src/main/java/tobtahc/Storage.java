package tobtahc;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import tobtahc.task.Task;

/**
 * This class implements the save file mechanics.
 */
public class Storage {
    /** Basically we always run the program from /text-ui-test, so no need to check for null */
    private static final Path PROJECT_ROOT = Path.of(System.getProperty("user.dir")).getParent();
    private static final Path DATA_DIR = PROJECT_ROOT.resolve("data");
    private static final Path TASKS_FILE = DATA_DIR.resolve("tasks.txt");
    private static final Path TASKS_TMP_FILE = DATA_DIR.resolve("tasks.tmp.txt");

    /**
     * @param numBadLines The number of bad lines in the save file.
     * @param tasks The deserialized task objects.
     */
    public record LoadResult(int numBadLines, List<Task> tasks) {}

    private static void ensureDirExists() throws IOException {
        try {
            if (Files.notExists(DATA_DIR)) {
                Files.createDirectories(DATA_DIR);
            }
        } catch (IOException e) {
            throw new IOException("failed to create the data directory", e);
        }
    }

    private static void ensureFileExists() throws IOException {
        try {
            ensureDirExists();
            if (Files.notExists(TASKS_FILE)) {
                Files.createFile(TASKS_FILE);
            }
        } catch (IOException e) {
            throw new IOException("failed to create inital tasks.txt", e);
        }
    }

    /**
     * Loads the tasks from the save file.
     *
     * @return A LoadResult.
     * @throws IOException If there are IO exceptions.
     */
    public static LoadResult loadTasks() throws IOException {
        ensureFileExists();
        try (var lines = Files.lines(TASKS_FILE)) {
            var numBadLines = new AtomicInteger(0);
            var tasks = lines.<Task>mapMulti((line, consumer) -> {
                var task = Task.deserialize(line);
                if (task != null) {
                    consumer.accept(task);
                } else {
                    numBadLines.getAndIncrement();
                }
            }).collect(Collectors.toCollection(ArrayList::new));
            return new LoadResult(numBadLines.intValue(), tasks);
        } catch (IOException e) {
            throw new IOException("could not load tasks", e);
        }
    }

    /**
     * Save the tasks to the temp file, and if {@code areTasksLoaded} is {@code true},
     * replace the save file with the temp file.
     *
     * @param tasks The tasks to save.
     * @param areTasksLoaded Whether the tasks were successfully loaded at the start.
     * If not, then we would only write to the temp file and will not replace the save
     * file.
     */
    public static void saveTasks(List<Task> tasks, boolean areTasksLoaded) throws IOException {
        ensureDirExists();
        var sb = new StringBuilder();
        for (var task : tasks) {
            sb.append(task.serialize()).append("\n");
        }
        try {
            Files.write(TASKS_TMP_FILE, sb.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new IOException("could not write to the temp file", e);
        }
        if (!areTasksLoaded) {
            return;
        }
        try {
            try {
                Files.move(TASKS_TMP_FILE, TASKS_FILE, StandardCopyOption.REPLACE_EXISTING,
                        StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(TASKS_TMP_FILE, TASKS_FILE, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new IOException("failed to replace the save file with the temp file", e);
        }
    }
}
