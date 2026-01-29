package tobtahc;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import tobtahc.task.Task;

public class Data {
    private static final Path PROJECT_ROOT = Path.of(System.getProperty("user.dir")).getParent();
    private static final Path TASKS_FILE = PROJECT_ROOT.resolve("data/tasks.txt");
    private static final Path TASKS_TMP_FILE = PROJECT_ROOT.resolve("data/tasks.tmp.txt");

    public record LoadResult(int numBadLines, List<Task> tasks) {}

    private static void ensureFileExists() throws IOException {
        if (Files.notExists(TASKS_FILE)) {
            Files.createDirectories(TASKS_FILE.getParent());
            Files.createFile(TASKS_FILE);
        }
    }

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
            throw new IOException("could not load tasks");
        }
    }

    public static void saveTasks(List<Task> tasks, boolean areTasksLoaded) throws IOException {
        var sb = new StringBuilder();
        for (var task : tasks) {
            sb.append(task.serialize() + "\n");
        }
        try {
            Files.write(TASKS_TMP_FILE, sb.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new IOException("could not write to the temp file");
        }
        if (!areTasksLoaded) {
            return;
        }
        try {
            Files.move(TASKS_TMP_FILE, TASKS_FILE, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException e) {
            throw new IOException("failed to replace the save file with the temp file");
        }
    }
}
