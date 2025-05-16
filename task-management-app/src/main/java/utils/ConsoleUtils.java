package utils;

// Utility class for console I/O operations
// Part of basic Java structure setup
import java.util.Scanner;

public class ConsoleUtils {

    private static final Scanner scanner = new Scanner(System.in);

    public static String readInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public static void printMessage(String message) {
        System.out.println(message);
    }

    public static void printError(String errorMessage) {
        System.err.println("Error: " + errorMessage);
    }
}