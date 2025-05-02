import java.util.Scanner;

// Main application entry point
// Sets up basic Java structure and program flow
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Task Management Terminal Application with Pomodoro Timer!");
        
        // Initialize application components here
        // For example, set up the task queue and timer
        
        // Main application loop
        while (true) {
            System.out.println("Please choose an option:");
            System.out.println("1. Add Task");
            System.out.println("2. Start Pomodoro Timer");
            System.out.println("3. Exit");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            switch (choice) {
                case 1:
                    // Logic to add a task
                    break;
                case 2:
                    // Logic to start the Pomodoro timer
                    break;
                case 3:
                    System.out.println("Exiting the application. Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}