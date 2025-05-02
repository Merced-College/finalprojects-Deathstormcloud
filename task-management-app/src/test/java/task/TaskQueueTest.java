// Unit tests for TaskQueue implementation
// Verifies queue operations for task management

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskQueueTest {
    private TaskQueue taskQueue;

    @BeforeEach
    void setUp() {
        taskQueue = new TaskQueue();
    }

    @Test
    void testEnqueue() {
        Task task = new Task("Test Task", 1, null, false);
        taskQueue.enqueue(task);
        assertEquals(task, taskQueue.peek());
    }

    @Test
    void testDequeue() {
        Task task1 = new Task("Test Task 1", 1, null, false);
        Task task2 = new Task("Test Task 2", 2, null, false);
        taskQueue.enqueue(task1);
        taskQueue.enqueue(task2);
        
        assertEquals(task1, taskQueue.dequeue());
        assertEquals(task2, taskQueue.peek());
    }

    @Test
    void testIsEmpty() {
        assertTrue(taskQueue.isEmpty());
        taskQueue.enqueue(new Task("Test Task", 1, null, false));
        assertFalse(taskQueue.isEmpty());
    }

    @Test
    void testSize() {
        assertEquals(0, taskQueue.size());
        taskQueue.enqueue(new Task("Test Task", 1, null, false));
        assertEquals(1, taskQueue.size());
        taskQueue.dequeue();
        assertEquals(0, taskQueue.size());
    }
}