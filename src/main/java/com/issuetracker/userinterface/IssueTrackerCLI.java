package com.issuetracker.userinterface;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

import com.issuetracker.dao.*;
import com.issuetracker.exception.IssueTrackerException;
import com.issuetracker.model.*;
import com.issuetracker.service.*;

/**
 * Console-based Issue Tracker CLI.
 * Simplified UI, no emojis, reduced clutter.
 */
public class IssueTrackerCLI {

    private static IssueService issueService;
    private static UserService userService;
    private static IssueHistoryService issueHistoryService;
    private static Scanner scanner;
    private static User currentUser;

    public static void main(String[] args) {
        try {
            initializeServices();
            scanner = new Scanner(System.in);

            showWelcomeMessage();

            if (authenticateUser()) {
                runMainApplication();
            } else {
                System.out.println("Authentication failed. Exiting.");
            }

        } catch (Exception e) {
            System.err.println("Application error: " + e.getMessage());
        } finally {
            if (scanner != null) scanner.close();
        }
    }

    // Initialize DAOs and Services
    private static void initializeServices() {
        issueService = new IssueServiceImpl(new IssueDAOJdbcImpl());
        userService = new UserServiceImpl(new UserDAOImpl());
        issueHistoryService = new IssueHistoryServiceImpl(new IssueHistoryDAOImpl());
    }

    // Welcome Screen
    private static void showWelcomeMessage() {
        System.out.println("\n------------------------------------------------------------");
        System.out.println(" Issue Tracker System ");
        System.out.println("------------------------------------------------------------");
        System.out.println("Advanced Issue Management with Database Integration");
        System.out.println("------------------------------------------------------------");
    }

    // Login or Register
    private static boolean authenticateUser() {
        System.out.println("\nUSER AUTHENTICATION");
        System.out.println("------------------------------");
        System.out.println("1. Login");
        System.out.println("2. Register New User");
        System.out.println("0. Exit");

        int choice = getIntInput("Select option: ");

        switch (choice) {
            case 1: return loginUser();
            case 2: return registerUser();
            case 0: return false;
            default:
                System.out.println("Invalid choice.");
                return authenticateUser();
        }
    }

    private static boolean loginUser() {
        System.out.println("\nLOGIN");
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        try {
            if (userService.validateCredentials(email, password)) {
                currentUser = userService.findUserByEmail(email);
                System.out.println("Login successful. Welcome, " + currentUser.getFullName());
                return true;
            } else {
                System.out.println("Invalid credentials.");
                return authenticateUser();
            }
        } catch (IssueTrackerException e) {
            System.out.println("Login error: " + e.getMessage());
            return false;
        }
    }

    private static boolean registerUser() {
        System.out.println("\nUSER REGISTRATION");
        System.out.print("Full Name: ");
        String fullName = scanner.nextLine().trim();
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        try {
            if (userService.findUserByEmail(email) != null) {
                System.out.println("A user with this email already exists.");
                return authenticateUser();
            }

            User user = new User(username, email, password, fullName);
            int userId = userService.registerUser(user);
            user.setUserId(userId);
            currentUser = user;

            System.out.println("Registration successful. Welcome, " + fullName);
            return true;

        } catch (IssueTrackerException e) {
            System.out.println("Registration error: " + e.getMessage());
            return false;
        }
    }

    // Main App Loop
    private static void runMainApplication() {
        boolean running = true;

        while (running) {
            showMainMenu();
            int choice = getIntInput("Select option: ");

            switch (choice) {
                case 1: reportNewIssue(); break;
                case 2: updateIssueStatus(); break;
                case 3: showIssuesMenu(); break;
                case 4: assignIssue(); break;
                case 5: viewIssueHistory(); break;
                case 6: deleteResolvedIssues(); break;
                case 7: manageUsers(); break;
                case 8: showSystemInfo(); break;
                case 0:
                    running = false;
                    showExitMessage();
                    break;
                default:
                    System.out.println("Invalid choice.");
            }

            if (running) waitForEnter();
        }
    }

    // Main Menu UI
    private static void showMainMenu() {
        System.out.println("\nMAIN MENU - " + currentUser.getFullName());
        System.out.println("------------------------------");
        System.out.println("1. Report New Issue");
        System.out.println("2. Update Issue Status");
        System.out.println("3. Show Issues");
        System.out.println("4. Assign Issue");
        System.out.println("5. View Issue History");
        System.out.println("6. Delete Resolved Issues");
        System.out.println("7. Manage Users");
        System.out.println("8. System Information");
        System.out.println("0. Exit");
        System.out.println("------------------------------");
    }

    // REPORT ISSUE
    private static void reportNewIssue() {
        System.out.println("\nREPORT NEW ISSUE");

        try {
            System.out.print("Issue ID: ");
            String issueId = scanner.nextLine().trim();

            System.out.print("Description: ");
            String description = scanner.nextLine().trim();

            // Select unit
            Unit[] units = Unit.values();
            for (int i = 0; i < units.length; i++)
                System.out.println((i + 1) + ". " + units[i]);
            int unitChoice = getIntInput("Select unit: ") - 1;
            if (unitChoice < 0 || unitChoice >= units.length) {
                System.out.println("Invalid unit.");
                return;
            }
            Unit unit = units[unitChoice];

            // Select priority
            Priority[] priorities = Priority.values();
            for (int i = 0; i < priorities.length; i++)
                System.out.println((i + 1) + ". " + priorities[i].getDisplayName());
            int pChoice = getIntInput("Select priority: ") - 1;
            if (pChoice < 0 || pChoice >= priorities.length) {
                System.out.println("Invalid priority.");
                return;
            }
            Priority priority = priorities[pChoice];

            // Date
            System.out.print("Reporting Date (YYYY-MM-DD) or Enter for today: ");
            String dateInput = scanner.nextLine().trim();
            LocalDate reportingDate = LocalDate.now();

            if (!dateInput.isEmpty()) {
                try {
                    reportingDate = LocalDate.parse(
                        dateInput, DateTimeFormatter.ISO_LOCAL_DATE
                    );
                } catch (DateTimeParseException e) {
                    System.out.println("Invalid date. Using today.");
                }
            }

            Issue issue = new Issue(
                issueId, description, unit, reportingDate,
                null, IssueStatus.OPEN, priority,
                currentUser.getUserId(), 0
            );

            String resultId = issueService.reportAnIssue(issue);

            System.out.println("\nIssue reported successfully.");
            System.out.println("Issue ID: " + (resultId != null ? resultId : issueId));

        } catch (Exception e) {
            System.out.println("Error reporting issue: " + e.getMessage());
        }
    }

    // UPDATE ISSUE STATUS
    private static void updateIssueStatus() {
        System.out.println("\nUPDATE ISSUE STATUS");
        System.out.print("Enter Issue ID: ");
        String issueId = scanner.nextLine().trim();

        IssueStatus[] statuses = IssueStatus.values();
        for (int i = 0; i < statuses.length; i++)
            System.out.println((i + 1) + ". " + statuses[i]);

        int choice = getIntInput("Select status: ") - 1;
        if (choice < 0 || choice >= statuses.length) {
            System.out.println("Invalid status.");
            return;
        }

        try {
            Boolean updated = issueService.updateStatus(issueId, statuses[choice]);

            if (Boolean.TRUE.equals(updated)) {
                System.out.println("Status updated successfully.");
            } else {
                System.out.println("Update failed. Issue may not exist.");
            }

        } catch (Exception e) {
            System.out.println("Error updating issue: " + e.getMessage());
        }
    }

    // SHOW ISSUES MENU
    private static void showIssuesMenu() {
        System.out.println("\nSHOW ISSUES");
        System.out.println("1. Show All Issues");
        System.out.println("2. Filter by Status");
        System.out.println("3. Filter by Assignee");
        System.out.println("4. Show My Issues");
        System.out.println("0. Back");

        int choice = getIntInput("Select option: ");

        switch (choice) {
            case 1: showAllIssues(); break;
            case 2: showIssuesByStatus(); break;
            case 3: showIssuesByAssignee(); break;
            case 4: showMyIssues(); break;
            case 0: return;
            default: System.out.println("Invalid choice.");
        }
    }

    private static void showAllIssues() {
        try {
            List<IssueReport> issues = issueService.showIssues(new HashMap<>());
            displayIssueReports(issues, "All Issues");
        } catch (Exception e) {
            System.out.println("Error retrieving issues: " + e.getMessage());
        }
    }

    private static void showIssuesByStatus() {
        IssueStatus[] statuses = IssueStatus.values();
        for (int i = 0; i < statuses.length; i++)
            System.out.println((i + 1) + ". " + statuses[i]);

        int choice = getIntInput("Select status: ") - 1;
        if (choice < 0 || choice >= statuses.length) {
            System.out.println("Invalid selection.");
            return;
        }

        try {
            Map<Character, Object> filter = new HashMap<>();
            filter.put('S', statuses[choice]);
            displayIssueReports(
                issueService.showIssues(filter),
                "Issues with Status: " + statuses[choice]
            );

        } catch (Exception e) {
            System.out.println("Error retrieving issues: " + e.getMessage());
        }
    }

    private static void showIssuesByAssignee() {
        System.out.print("Enter assignee email: ");
        String email = scanner.nextLine().trim();

        Map<Character, Object> filter = new HashMap<>();
        filter.put('A', email);

        try {
            displayIssueReports(
                issueService.showIssues(filter),
                "Issues for Assignee: " + email
            );
        } catch (Exception e) {
            System.out.println("Error retrieving issues: " + e.getMessage());
        }
    }

    private static void showMyIssues() {
        Map<Character, Object> filter = new HashMap<>();
        filter.put('A', currentUser.getEmail());

        try {
            displayIssueReports(
                issueService.showIssues(filter),
                "My Issues"
            );
        } catch (Exception e) {
            System.out.println("Error retrieving issues: " + e.getMessage());
        }
    }

    // ASSIGN ISSUE
    private static void assignIssue() {
        System.out.println("\nASSIGN ISSUE");
        System.out.print("Enter Issue ID: ");
        String issueId = scanner.nextLine().trim();

        try {
            List<User> users = userService.getAllUsers();

            if (users.isEmpty()) {
                System.out.println("No users available.");
                return;
            }

            for (int i = 0; i < users.size(); i++)
                System.out.println((i + 1) + ". " + users.get(i).getFullName());

            int choice = getIntInput("Select user: ") - 1;
            if (choice < 0 || choice >= users.size()) {
                System.out.println("Invalid user.");
                return;
            }

            User selectedUser = users.get(choice);
            Boolean result = issueService.assignIssue(issueId, selectedUser.getUserId());

            if (Boolean.TRUE.equals(result)) {
                System.out.println("Issue assigned to " + selectedUser.getFullName());
            } else {
                System.out.println("Assignment failed.");
            }

        } catch (Exception e) {
            System.out.println("Error assigning issue: " + e.getMessage());
        }
    }

    // ISSUE HISTORY
    private static void viewIssueHistory() {
        System.out.println("\nISSUE HISTORY");
        System.out.println("1. View specific issue history");
        System.out.println("2. View all recent history");
        System.out.println("3. View my actions");
        System.out.println("0. Back");

        int choice = getIntInput("Select option: ");

        try {
            switch (choice) {
                case 1: viewSpecificIssueHistory(); break;
                case 2: viewAllRecentHistory(); break;
                case 3: viewMyActions(); break;
                case 0: return;
                default: System.out.println("Invalid choice.");
            }
        } catch (Exception e) {
            System.out.println("Error retrieving history: " + e.getMessage());
        }
    }

    private static void viewSpecificIssueHistory() throws IssueTrackerException {
        System.out.print("Enter Issue ID: ");
        String id = scanner.nextLine().trim();

        List<IssueHistory> history = issueHistoryService.getIssueHistory(id);

        if (history.isEmpty()) {
            System.out.println("No history found.");
            return;
        }

        System.out.println("\nHistory for Issue: " + id);
        System.out.println("------------------------------------------------------------");
        System.out.printf("%-4s %-15s %-12s %-20s %s%n",
                "ID", "Action", "User", "Date", "Comments");

        for (IssueHistory h : history) {
            User user = userService.findUserById(h.getUserId());
            String userName = user != null ? user.getUsername() : "Unknown";

            System.out.printf("%-4d %-15s %-12s %-20s %s%n",
                h.getHistoryId(),
                h.getActionPerformed(),
                userName,
                h.getActionDate().toLocalDate(),
                h.getComments() != null ? h.getComments() : ""
            );
        }
    }

    private static void viewAllRecentHistory() throws IssueTrackerException {
        List<IssueHistory> history = issueHistoryService.getAllHistory();

        if (history.isEmpty()) {
            System.out.println("No history found.");
            return;
        }

        System.out.println("\nRecent Activity (Last 50)");
        System.out.println("------------------------------------------------------------");
        System.out.printf("%-15s %-15s %-12s %-20s %s%n",
                "Issue ID", "Action", "User", "Date", "Comments");

        int count = 0;
        for (IssueHistory h : history) {
            if (count++ >= 50) break;

            User user = userService.findUserById(h.getUserId());
            String userName = user != null ? user.getUsername() : "Unknown";

            String comments = h.getComments() != null ? 
                h.getComments().substring(0, Math.min(30, h.getComments().length())) : "";

            System.out.printf("%-15s %-15s %-12s %-20s %s%n",
                h.getIssueId(),
                h.getActionPerformed(),
                userName,
                h.getActionDate().toLocalDate(),
                comments
            );
        }
    }

    private static void viewMyActions() throws IssueTrackerException {
        List<IssueHistory> history = issueHistoryService.getHistoryByUser(currentUser.getUserId());

        if (history.isEmpty()) {
            System.out.println("No activity found.");
            return;
        }

        System.out.println("\nYour Activity History");
        System.out.println("------------------------------------------------------------");
        System.out.printf("%-15s %-15s %-20s %s%n",
                "Issue ID", "Action", "Date", "Comments");

        for (IssueHistory h : history) {
            System.out.printf("%-15s %-15s %-20s %s%n",
                h.getIssueId(),
                h.getActionPerformed(),
                h.getActionDate().toLocalDate(),
                h.getComments() != null ? h.getComments() : ""
            );
        }
    }

    // DELETE RESOLVED / CLOSED
    private static void deleteResolvedIssues() {
        System.out.println("\nDELETE RESOLVED ISSUES");
        System.out.print("Confirm delete all resolved issues? (y/N): ");
        String confirm = scanner.nextLine().trim().toLowerCase();

        if (!confirm.equals("y")) {
            System.out.println("Operation cancelled.");
            return;
        }

        try {
            List<Issue> deleted = issueService.deleteIssues();

            if (deleted != null && !deleted.isEmpty()) {
                System.out.println("Deleted " + deleted.size() + " issues.");
            } else {
                System.out.println("No resolved issues found.");
            }

        } catch (Exception e) {
            System.out.println("Error deleting issues: " + e.getMessage());
        }
    }

    // USER MANAGEMENT
    private static void manageUsers() {
        System.out.println("\nUSER MANAGEMENT");
        System.out.println("1. View All Users");
        System.out.println("2. Add New User");
        System.out.println("0. Back");

        int choice = getIntInput("Select option: ");

        switch (choice) {
            case 1: viewAllUsers(); break;
            case 2: addNewUser(); break;
            case 0: return;
            default: System.out.println("Invalid choice.");
        }
    }

    private static void viewAllUsers() {
        try {
            List<User> users = userService.getAllUsers();

            System.out.println("\nALL USERS");
            System.out.println("------------------------------------------------------------");
            System.out.printf("%-5s %-20s %-30s %-20s%n",
                    "ID", "Username", "Email", "Full Name");

            for (User u : users) {
                System.out.printf("%-5d %-20s %-30s %-20s%n",
                    u.getUserId(), u.getUsername(), u.getEmail(), u.getFullName());
            }

        } catch (Exception e) {
            System.out.println("Error retrieving users: " + e.getMessage());
        }
    }

    private static void addNewUser() {
        System.out.println("\nADD NEW USER");
        System.out.print("Full Name: ");
        String fullName = scanner.nextLine().trim();
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        try {
            if (userService.findUserByEmail(email) != null) {
                System.out.println("User with this email already exists.");
                return;
            }

            User user = new User(username, email, password, fullName);
            int id = userService.registerUser(user);

            System.out.println("User created successfully. ID: " + id);

        } catch (Exception e) {
            System.out.println("Error creating user: " + e.getMessage());
        }
    }

    // Display Issue List
    private static void displayIssueReports(List<IssueReport> issues, String title) {
        System.out.println("\n" + title.toUpperCase());
        System.out.println("------------------------------------------------------------");

        if (issues == null || issues.isEmpty()) {
            System.out.println("No issues found.");
            return;
        }

        System.out.printf("%-15s %-35s %-25s %-15s%n",
                "Issue ID", "Description", "Assignee", "Status");

        for (IssueReport i : issues) {
            String desc = i.getIssueDescription();
            if (desc != null && desc.length() > 32)
                desc = desc.substring(0, 32) + "...";

            String assignee = (i.getAssigneeEmail() != null) ? i.getAssigneeEmail() : "Unassigned";

            System.out.printf("%-15s %-35s %-25s %-15s%n",
                i.getIssueId(), desc, assignee, i.getStatus()
            );
        }
    }

    // System Information
    private static void showSystemInfo() {
        System.out.println("\nSYSTEM INFORMATION");
        System.out.println("------------------------------------------------------------");
        System.out.println("System: Issue Tracker ");
        System.out.println("Current User: " + currentUser.getFullName());
        System.out.println("Date: " + LocalDate.now());

        System.out.println("\nUnits:");
        for (Unit u : Unit.values()) System.out.println(" - " + u);

        System.out.println("\nStatuses:");
        for (IssueStatus s : IssueStatus.values()) System.out.println(" - " + s);

        System.out.println("\nPriority Levels:");
        for (Priority p : Priority.values())
            System.out.println(" - " + p.getDisplayName() + " (Level " + p.getLevel() + ")");
    }

    // Exit Message
    private static void showExitMessage() {
        System.out.println("\n------------------------------------------------------------");
        System.out.println("Thank you for using Mont Trance Inc. Issue Tracker.");
        System.out.println("Goodbye, " + currentUser.getFullName() + ".");
        System.out.println("------------------------------------------------------------");
    }

    // Input Helpers
    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (Exception e) {
                System.out.println("Invalid number. Try again.");
            }
        }
    }

    private static void waitForEnter() {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
        clearScreen();
    }

    // Clear Screen (Windows or fallback)
    private static void clearScreen() {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (Exception e) {
            // simple fallback
            for (int i = 0; i < 3; i++) System.out.println();
        }
    }
}
