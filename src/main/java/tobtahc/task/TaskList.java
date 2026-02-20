package tobtahc.task;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * A list of tasks.
 */
public class TaskList extends AbstractList<Task> {
    private final List<Task> tasks;

    /**
     * A task with its index in the task list.
     *
     * @param <T> the concrete task type
     * @param task the task
     * @param index the index of the task in the task list
     */
    public record TaskWithIndex<T extends Task>(T task, int index) {}

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

    /**
     * Returns a list of all undone {@code ToDo} tasks with their indices.
     *
     * @return a list of all undone {@code ToDo} tasks with their indices
     */
    public List<TaskWithIndex<ToDo>> getAllUndoneToDos() {
        var ret = new ArrayList<TaskWithIndex<ToDo>>();
        for (int i = 0; i < tasks.size(); ++i) {
            var task = tasks.get(i);
            if (task instanceof ToDo todo && !task.isDone()) {
                ret.add(new TaskWithIndex<>(todo, i));
            }
        }
        return ret;
    }

    /**
     * Returns a list of all undone {@code Deadline} tasks with their indices, sorted by due time.
     *
     * @return a list of all undone {@code Deadline} tasks with their indices, sorted by due time
     */
    public List<TaskWithIndex<Deadline>> getAllUndoneDeadlinesSorted() {
        var ret = new ArrayList<TaskWithIndex<Deadline>>();
        for (int i = 0; i < tasks.size(); ++i) {
            var task = tasks.get(i);
            if (task instanceof Deadline deadline && !task.isDone()) {
                ret.add(new TaskWithIndex<>(deadline, i));
            }
        }
        ret.sort(Comparator.comparing((TaskWithIndex<Deadline> twi) -> twi.task().getBy())
                .thenComparing(twi -> twi.index()));
        return ret;
    }

    /**
     * Returns a list of all undone {@code Event} tasks with their indices, sorted by start time.
     *
     * @return a list of all undone {@code Event} tasks with their indices, sorted by start time
     */
    public List<TaskWithIndex<Event>> getAllUndoneEventsSorted() {
        var ret = new ArrayList<TaskWithIndex<Event>>();
        for (int i = 0; i < tasks.size(); ++i) {
            var task = tasks.get(i);
            if (task instanceof Event event && !task.isDone()) {
                ret.add(new TaskWithIndex<>(event, i));
            }
        }
        ret.sort(Comparator.comparing((TaskWithIndex<Event> twi) -> twi.task().getStart())
                .thenComparing(twi -> twi.index()));
        return ret;
    }

    /**
     * Returns a list of all done tasks with their indices.
     *
     * @return a list of all done tasks with their indices
     */
    public List<TaskWithIndex<Task>> getAllDoneTasks() {
        var ret = new ArrayList<TaskWithIndex<Task>>();
        for (int i = 0; i < tasks.size(); ++i) {
            var task = tasks.get(i);
            if (task.isDone()) {
                ret.add(new TaskWithIndex<>(task, i));
            }
        }
        return ret;
    }
}
