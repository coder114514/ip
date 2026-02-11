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
 * This class implements the save file mechanics.
 */
public class Storage {
    private Path dataDir;
    private Path saveFilePath;
    private String saveFileName;

    /**
     * The result of loading the save file.
     *
     * @param numBadLines number of bad lines in the save file
     * @param tasks the deserialized task objects
     */
    public record LoadResult(int numBadLines, TaskList tasks) {}

    /**
     * @param dataDirPath path of the date directory
     * @param saveFileName name of the save file
     */
    public Storage(String dataDirPath, String saveFileName) {
        dataDir = Path.of(dataDirPath);
        saveFilePath = Path.of(dataDirPath, saveFileName);
        this.saveFileName = saveFileName;
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

    /**
     * Loads the tasks from the save file.
     *
     * @return a {@code LoadResult}
     * @throws IOException if there was an IO exception
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
     * Saves the tasks to the save file.
     *
     * @param tasks tasks to save
     * @throws IOException if there are IO Exceptions
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
}
