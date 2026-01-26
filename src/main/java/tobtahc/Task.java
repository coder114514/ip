package tobtahc;

class Task {
    private String description;
    private boolean isDone;

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
        return "[" + this.getStatusIcon() + "] " + this.description;
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
