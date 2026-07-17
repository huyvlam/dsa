package mymodel;

public class Task implements Comparable<Task> {
    private final String description;
    private final int priority;

    public Task(String description, int priority) {
        this.description = description;
        this.priority = priority;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public int compareTo(Task other) {
        return Integer.compare(this.priority, other.priority);
    }

    @Override
    public String toString() {
        return String.format("Task{desc='%s', priority=%d}", description, priority);
    }
}
