// Queue implementation for Pomodoro timer sessions
// Manages task queue operations using LinkedList
import java.util.LinkedList;
import java.util.Queue;
import task.Task;

public class TaskQueue {
    private Queue<Task> taskQueue;

    public TaskQueue() {
        this.taskQueue = new LinkedList<>();
    }

    public void enqueue(Task task) {
        taskQueue.offer(task);
    }

    public Task dequeue() {
        return taskQueue.poll();
    }

    public Task peek() {
        return taskQueue.peek();
    }

    public boolean isEmpty() {
        return taskQueue.isEmpty();
    }

    public int size() {
        return taskQueue.size();
    }
}