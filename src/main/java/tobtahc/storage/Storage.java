package tobtahc.storage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import tobtahc.task.Task;
import tobtahc.task.TaskList;
import tobtahc.task.TaskParser;

/**
 * This class implements the save file mechanics.
 */
public class Storage {
    private Path dataDir;
    private Path saveFilePath;
    private Path tempFilePath;
    private boolean replaceSaveFile;

    /**
     * @param numBadLines number of bad lines in the save file
     * @param tasks the deserialized task objects
     */
    public record LoadResult(int numBadLines, TaskList tasks) {}

    /**
     * @param dataDirPath path of the date directory
     * @param saveFilePath name of the save file
     * @param tempFilePath name of the temp file
     */
    public Storage(String dataDirPath, String saveFileName, String tempFileName) {
        dataDir = Path.of(dataDirPath);
        saveFilePath = Path.of(dataDirPath, saveFileName);
        tempFilePath = Path.of(dataDirPath, tempFileName);
        replaceSaveFile = true;
    }

    /**
     * Switch the storage behavior to only saving to the temp file.
     */
    public void switchToFallbackMode() {
        replaceSaveFile = false;
    }

    private void ensureDirExists() throws IOException {
        try {
            if (Files.notExists(dataDir)) {
                Files.createDirectories(dataDir);
            }
        } catch (IOException e) {
            throw new IOException("failed to create the data directory", e);
        }
    }

    private void ensureFileExists() throws IOException {
        try {
            ensureDirExists();
            if (Files.notExists(saveFilePath)) {
                Files.createFile(saveFilePath);
            }
        } catch (IOException e) {
            throw new IOException("failed to create the inital save file", e);
        }
    }

    /**
     * Loads the tasks from the save file.
     *
     * @return a LoadResult
     * @throws IOException if there was an IO exception
     */
    public LoadResult loadTasks() throws IOException {
        ensureFileExists();
        try (var lines = Files.lines(saveFilePath)) {
            var numBadLines = new AtomicInteger(0);
            var tasks = lines.<Task>mapMulti((line, consumer) -> {
                var task = TaskParser.deserialize(line);
                if (task != null) {
                    consumer.accept(task);
                } else {
                    numBadLines.getAndIncrement();
                }
            }).collect(Collectors.toCollection(TaskList::new));
            return new LoadResult(numBadLines.intValue(), tasks);
        } catch (IOException e) {
            throw new IOException("could not load tasks", e);
        }
    }

    /**
     * Saves the tasks to the temp file, and on default, replaces the save file with the temp file,
     * if switched to fallback mode, then only saves to the temp file.
     * @param tasks tasks to save
     */
    public void saveTasks(TaskList tasks) throws IOException {
        ensureDirExists();
        var sb = new StringBuilder();
        for (var task : tasks) {
            sb.append(task.serialize()).append("\n");
        }
        try {
            Files.write(tempFilePath, sb.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new IOException("could not write to the temp file", e);
        }
        if (!replaceSaveFile) {
            return;
        }
        try {
            try {
                Files.move(tempFilePath, saveFilePath, StandardCopyOption.REPLACE_EXISTING,
                        StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(tempFilePath, saveFilePath, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new IOException("failed to replace the save file with the temp file", e);
        }
    }
}
