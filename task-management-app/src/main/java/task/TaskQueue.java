package task;

import java.util.LinkedList;
import java.util.Queue;

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

    @Override
    public String toString() {
        if (taskQueue.isEmpty()) {
            return "No tasks in queue";
        }
        StringBuilder sb = new StringBuilder();
        int index = 1;
        for (Task task : taskQueue) {
            sb.append(index++).append(". ").append(task.toString()).append("\n");
        }
        return sb.toString();
    }
}