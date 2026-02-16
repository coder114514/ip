package tobtahc.storage;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import tobtahc.task.Task;
import tobtahc.task.TaskList;
import tobtahc.task.TaskParser;

/**
 * Manager for saving and loading tasks from the local file system.
 */
public class Storage {
    private Path dataDir;
    private Path saveFilePath;
    private String saveFileName;

    /**
     * Result of a task loading operation.
     *
     * @param numBadLines the number of corrupted or unreadable lines found
     * @param tasks the list of tasks successfully recovered from the file
     */
    public record LoadResult(int numBadLines, TaskList tasks) {}

    /**
     * Constructs a {@code Storage} instance with the specified directory and file name.
     *
     * @param dataDirPath the path of the directory to store data
     * @param saveFileName the name of the file to save tasks in
     */
    public Storage(String dataDirPath, String saveFileName) {
        dataDir = Path.of(dataDirPath);
        saveFilePath = Path.of(dataDirPath, saveFileName);
        this.saveFileName = saveFileName;
    }

    /**
     * Loads the tasks from the save file.
     *
     * @return the loaded tasks and number of corrupted lines
     * @throws IOException if an I/O error occurs during the file reading process
     */
    public LoadResult loadTasks() throws IOException {
        ensureFileExists();
        try (var lines = Files.lines(saveFilePath, StandardCharsets.UTF_8)) {
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
     * Saves the provided tasks to the save file.
     *
     * @param tasks the list of tasks to be persisted
     * @throws IOException if an I/O error occurs during the file writing process
     */
    public void saveTasks(TaskList tasks) throws IOException {
        ensureDirExists();
        var tmp = Files.createTempFile(dataDir, saveFileName, ".tmp");
        try (FileChannel c = FileChannel.open(tmp, StandardOpenOption.CREATE, StandardOpenOption.WRITE,
                StandardOpenOption.TRUNCATE_EXISTING);
             BufferedWriter w = new BufferedWriter(Channels.newWriter(c, StandardCharsets.UTF_8.name()))) {
            for (var task : tasks) {
                w.write(task.serialize());
                w.newLine();
            }
            w.flush();
            c.force(true);
        } catch (IOException e) {
            throw new IOException("could not write to the temp file", e);
        }
        try {
            try {
                Files.move(tmp, saveFilePath, StandardCopyOption.REPLACE_EXISTING,
                        StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(tmp, saveFilePath, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new IOException("failed to replace the save file with the temp file", e);
        }
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
            throw new IOException("failed to create the initial save file", e);
        }
    }
}
