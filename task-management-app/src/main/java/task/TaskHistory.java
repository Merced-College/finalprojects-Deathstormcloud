package task;

public class TaskHistory {
    private static final int MAX_HISTORY = 50;
    private Task[] completedTasks;
    private int currentIndex;

    public TaskHistory() {
        this.completedTasks = new Task[MAX_HISTORY];
        this.currentIndex = 0;
    }

    public void addCompletedTask(Task task) {
        if (currentIndex >= MAX_HISTORY) {
            System.arraycopy(completedTasks, 1, completedTasks, 0, MAX_HISTORY - 1);
            currentIndex = MAX_HISTORY - 1;
        }
        completedTasks[currentIndex++] = task;
    }

    public Task[] getCompletedTasks() {
        Task[] result = new Task[currentIndex];
        System.arraycopy(completedTasks, 0, result, 0, currentIndex);
        return result;
    }

    public int getCompletedCount() {
        return currentIndex;
    }
}