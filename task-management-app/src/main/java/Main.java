import java.util.Scanner;
import java.time.LocalDateTime;
import task.Task;
import task.TaskQueue;
import timer.PomodoroTimer;
import timer.TimerState;

// Main application entry point
// Sets up basic Java structure and program flow
public class Main {
    private static TaskQueue taskQueue;
    private static PomodoroTimer pomodoroTimer;
    private static boolean isTimerRunning = false;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Task Management Terminal Application!");
        
        taskQueue = new TaskQueue();
        pomodoroTimer = new PomodoroTimer(new PomodoroTimer.TimerCallback() {
            @Override
            public void onTick(long remainingTime) {
                System.out.printf("\rTime remaining: %02d:%02d", 
                    remainingTime/60000, (remainingTime/1000)%60);
            }
            @Override
            public void onStateChange(TimerState newState) {
                System.out.println("\nTimer state: " + newState);
            }
        });

        // Main application loop
        while (true) {
            System.out.println("\n=== Task Management Menu ===");
            System.out.println("1. Add Task");
            System.out.println("2. View Tasks");
            System.out.println("3. Start Pomodoro");
            System.out.println("4. Pause/Resume Timer");
            System.out.println("5. Exit");
            System.out.print("Choice: ");
            
            String choice = scanner.nextLine().trim();
            
            switch (choice) {
                case "1":
                    addTask(scanner);
                    break;
                case "2":
                    viewTasks();
                    break;
                case "3":
                    startPomodoro();
                    break;
                case "4":
                    toggleTimer();
                    break;
                case "5":
                    System.out.println("Goodbye!");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    private static void addTask(Scanner scanner) {
        System.out.print("Enter task title: ");
        String title = scanner.nextLine();
        
        System.out.print("Enter priority (1-5): ");
        int priority = Integer.parseInt(scanner.nextLine());
        
        LocalDateTime dueDate = null;
        System.out.print("Enter due date (YYYY-MM-DD) or press Enter for no due date: ");
        String dueDateStr = scanner.nextLine().trim();
        
        if (!dueDateStr.isEmpty()) {
            try {
                dueDate = LocalDateTime.parse(dueDateStr + "T00:00:00");
            } catch (Exception e) {
                System.out.println("Invalid date format. Using no due date.");
            }
        }
        
        Task task = new Task(title, priority, dueDate, false);
        taskQueue.enqueue(task);
        System.out.println("Task added successfully!");
    }

    private static void viewTasks() {
        if (taskQueue.isEmpty()) {
            System.out.println("No tasks in queue!");
            return;
        }
        System.out.println("\nCurrent Tasks:");
        // We'll need to modify TaskQueue to support viewing all tasks
        System.out.println(taskQueue.toString());
    }

    private static void startPomodoro() {
        if (!isTimerRunning) {
            System.out.println("Starting Pomodoro timer...");
            pomodoroTimer.start();
            isTimerRunning = true;
        } else {
            System.out.println("Timer is already running!");
        }
    }

    private static void toggleTimer() {
        if (isTimerRunning) {
            pomodoroTimer.pause();
            isTimerRunning = false;
        } else {
            pomodoroTimer.start();
            isTimerRunning = true;
        }
    }
}