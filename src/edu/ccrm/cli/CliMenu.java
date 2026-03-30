package edu.ccrm.cli;

public class CliMenu {

    public static void displayWelcomeMessage() {
        System.out.println("*************************************************");
        System.out.println("*   Welcome to Campus Course & Records Manager  *");
        System.out.println("*************************************************");
    }

    public static void displayMainMenu() {
        System.out.println("\n--- Main Menu ---");
        System.out.println("1. Manage Students");
        System.out.println("2. Manage Courses");
        System.out.println("3. Manage Enrollment & Grading");
        System.out.println("4. File Operations (Import/Export/Backup)");
        System.out.println("5. Generate Reports");
        System.out.println("0. Exit");
    }

}
