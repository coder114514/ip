package tobtahc.task;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Implements the task list, which loads and saves tasks to the save file.
 */
public class TaskList extends AbstractList<Task> {
    private final List<Task> tasks;

    /**
     * Initializes an empty task list.
     */
    public TaskList() {
        tasks = new ArrayList<>();
    }

    /**
     * Initializes a task list with {@code tasks}.
     *
     * @param tasks the raw list of tasks
     */
    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    @Override
    public int size() {
        return tasks.size();
    }

    @Override
    public Task get(int index) {
        return tasks.get(index);
    }

    @Override
    public boolean add(Task task) {
        return tasks.add(task);
    }

    @Override
    public Task remove(int index) {
        return tasks.remove(index);
    }

    @Override
    public void clear() {
        tasks.clear();
    }

    @Override
    public Iterator<Task> iterator() {
        return tasks.iterator();
    }
}
