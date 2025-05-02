// Basic Task class structure design
// Represents core task entity with priority and status tracking
import java.time.LocalDateTime;

public class Task {
    private String title;
    private int priority;
    private LocalDateTime dueDate;
    private boolean isCompleted;

    public Task(String title, int priority, LocalDateTime dueDate) {
        this.title = title;
        this.priority = priority;
        this.dueDate = dueDate;
        this.isCompleted = false;
    }

    public String getTitle() {
        return title;
    }

    public int getPriority() {
        return priority;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void markAsCompleted() {
        this.isCompleted = true;
    }

    @Override
    public String toString() {
        return "Task{" +
                "title='" + title + '\'' +
                ", priority=" + priority +
                ", dueDate=" + dueDate +
                ", isCompleted=" + isCompleted +
                '}';
    }
}