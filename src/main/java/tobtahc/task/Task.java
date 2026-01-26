package tobtahc.task;

import java.util.regex.Pattern;

public abstract class Task {
    private String description;
    private boolean isDone;

    public static Task parseTask(String input) {
        var patternToDo = Pattern.compile("todo (.+)");
        var matcherToDo = patternToDo.matcher(input);

        if (matcherToDo.find()) {
            var desc = matcherToDo.group(1).trim();
            return new ToDo(desc);
        }

        var patternDeadline = Pattern.compile("deadline (.+)/by (.+)");
        var matcherDeadline = patternDeadline.matcher(input);

        if (matcherDeadline.find()) {
            var desc = matcherDeadline.group(1).trim();
            var deadline = matcherDeadline.group(2).trim();
            return new Deadline(desc, deadline);
        }

        var patternEvent = Pattern.compile("event (.+)/from (.+)/to (.+)");
        var matcherEvent = patternEvent.matcher(input);

        if (matcherEvent.find()) {
            var desc = matcherEvent.group(1).trim();
            var from = matcherEvent.group(2).trim();
            var to = matcherEvent.group(3).trim();
            return new Event(desc, from, to);
        }

        return null;
    }

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    @Override
    public String toString() {
        return this.getDescriptionWithStatus();
    }

    public String getDescription() {
        return this.description;
    }

    public String getDescriptionWithStatus() {
        return "[" + this.getStatusIcon() + "] " + this.getDescription();
    }

    public boolean isDone() {
        return this.isDone;
    }

    public String getStatusIcon() {
        return this.isDone() ? "X" : " ";
    }

    public void markAsDone() {
        this.isDone = true;
    }

    public void markAsDone(boolean isDone) {
        this.isDone = isDone;
    }

    public void markAsUndone() {
        this.isDone = false;
    }

    public void markAsUndone(boolean isUndone) {
        this.isDone = !isUndone;
    }
}
