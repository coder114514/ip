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
public class Data {
    /** Basically we always run the program from /text-ui-test, so no need to check for null */
    private static final Path PROJECT_ROOT = Path.of(System.getProperty("user.dir")).getParent();
    private static final Path TASKS_FILE = PROJECT_ROOT.resolve("data/tasks.txt");
    private static final Path TASKS_TMP_FILE = PROJECT_ROOT.resolve("data/tasks.tmp.txt");

    /**
     * @param numBadLines The number of bad lines in the save file.
     * @param tasks The deserialized task objects.
     */
    public record LoadResult(int numBadLines, List<Task> tasks) {}

    private static void ensureFileExists() throws IOException {
        if (Files.notExists(TASKS_FILE)) {
            Files.createDirectories(TASKS_FILE.getParent());
            Files.createFile(TASKS_FILE);
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
     * Save the tasks to the save file.
     *
     * @param tasks The tasks to save.
     * @param areTasksLoaded Whether the tasks were successfully loaded at the start.
     * If not, then we would only write to the temp file and will not replace the save
     * file.
     */
    public static void saveTasks(List<Task> tasks, boolean areTasksLoaded) throws IOException {
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
                Files.move(TASKS_TMP_FILE, TASKS_FILE, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(TASKS_TMP_FILE, TASKS_FILE, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new IOException("failed to replace the save file with the temp file", e);
        }
    }
}
