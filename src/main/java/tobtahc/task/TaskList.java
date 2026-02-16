package tobtahc.task;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A list of tasks.
 */
public class TaskList extends AbstractList<Task> {
    private final List<Task> tasks;

    /**
     * Constructs an empty task list.
     */
    public TaskList() {
        tasks = new ArrayList<>();
    }

    /**
     * Constructs a task list with the specified tasks.
     *
     * @param tasks the initial list of tasks
     */
    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /**
     * Returns the number of tasks in this list.
     *
     * @return the number of tasks
     */
    @Override
    public int size() {
        return tasks.size();
    }

    /**
     * Returns the task at the specified position in this list.
     *
     * @param index the index of the task to return
     * @return the task at the specified index
     */
    @Override
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Appends the specified task to the end of this list.
     *
     * @param task the task to be added
     * @return {@code true} after the task is successfully added
     */
    @Override
    public boolean add(Task task) {
        return tasks.add(task);
    }

    /**
     * Removes the task at the specified position in this list.
     *
     * @param index the index of the task to be removed
     * @return the task that was removed from the list
     */
    @Override
    public Task remove(int index) {
        return tasks.remove(index);
    }

    /**
     * Removes all tasks from this list.
     */
    @Override
    public void clear() {
        tasks.clear();
    }

    /**
     * Returns an iterator over the tasks in this list in proper sequence.
     *
     * @return an iterator over the tasks
     */
    @Override
    public Iterator<Task> iterator() {
        return tasks.iterator();
    }
}
