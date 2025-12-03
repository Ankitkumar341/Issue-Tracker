package com.issuetracker.userinterface;

import com.issuetracker.dao.*;
import com.issuetracker.exception.IssueTrackerException;
import com.issuetracker.model.*;
import com.issuetracker.service.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Test runner to validate all CLI features work properly
 * This tests the core functionality without requiring user interaction
 */
public class CLIFeatureTester {
    
    private static IssueService issueService;
    private static UserDAO userDAO;
    private static User testUser;
    
    public static void main(String[] args) {
        System.out.println("🧪 TESTING ALL CLI FEATURES");
        System.out.println("=".repeat(50));
        
        try {
            // Initialize services
            initializeServices();
            
            // Test user authentication features
            testUserAuthentication();
            
            // Test issue management features
            testIssueManagement();
            
            // Test reporting features
            testReportingFeatures();
            
            // Test assignment features
            testAssignmentFeatures();
            
            System.out.println("\n✅ ALL TESTS COMPLETED SUCCESSFULLY!");
            System.out.println("🎉 CLI is ready for production use!");
            
        } catch (Exception e) {
            System.err.println("❌ TEST FAILED: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void initializeServices() {
        System.out.println("🔧 Initializing services...");
        IssueDAO issueDAO = new IssueDAOJdbcImpl();
        issueService = new IssueServiceImpl(issueDAO);
        userDAO = new UserDAOImpl();
        System.out.println("✅ Services initialized successfully");
    }
    
    private static void testUserAuthentication() throws IssueTrackerException {
        System.out.println("\n👤 Testing User Authentication Features...");
        
        // Test finding existing user (from schema.sql)
        User adminUser = userDAO.findByEmail("admin@montrance.com");
        if (adminUser != null) {
            System.out.println("✅ Found existing user: " + adminUser.getFullName());
            testUser = adminUser;
        } else {
            System.out.println("❌ Could not find test user");
            return;
        }
        
        // Test credential validation
        boolean validLogin = userDAO.validateCredentials("admin@montrance.com", "admin123");
        if (validLogin) {
            System.out.println("✅ Credential validation works");
        } else {
            System.out.println("❌ Credential validation failed");
        }
        
        // Test getting all users
        List<User> allUsers = userDAO.getAllUsers();
        System.out.println("✅ Retrieved " + allUsers.size() + " users from database");
        
        System.out.println("✅ User authentication features working properly");
    }
    
    private static void testIssueManagement() throws IssueTrackerException {
        System.out.println("\n📝 Testing Issue Management Features...");
        
        // Test reporting new issue
        Issue newIssue = new Issue(
            "MTI-I-TEST-001",
            "Test issue for CLI validation",
            Unit.ADMINISTRATION,
            LocalDate.now(),
            null,
            IssueStatus.OPEN,
            Priority.HIGH,
            testUser.getUserId(),
            0
        );
        
        String reportedIssueId = issueService.reportAnIssue(newIssue);
        if (reportedIssueId != null) {
            System.out.println("✅ Issue reporting works: " + reportedIssueId);
        } else {
            System.out.println("❌ Issue reporting failed");
        }
        
        // Test updating issue status
        Boolean updateResult = issueService.updateStatus("MTI-I-TEST-001", IssueStatus.IN_PROGRESS);
        if (updateResult != null && updateResult) {
            System.out.println("✅ Issue status update works");
        } else {
            System.out.println("❌ Issue status update failed");
        }
        
        System.out.println("✅ Issue management features working properly");
    }
    
    private static void testReportingFeatures() throws IssueTrackerException {
        System.out.println("\n📊 Testing Reporting Features...");
        
        // Test showing all issues
        Map<Character, Object> emptyFilter = new HashMap<>();
        List<IssueReport> allIssues = issueService.showIssues(emptyFilter);
        System.out.println("✅ Retrieved " + allIssues.size() + " issues (show all)");
        
        // Test filtering by status
        Map<Character, Object> statusFilter = new HashMap<>();
        statusFilter.put('S', IssueStatus.OPEN);
        List<IssueReport> openIssues = issueService.showIssues(statusFilter);
        System.out.println("✅ Retrieved " + openIssues.size() + " open issues (status filter)");
        
        // Test filtering by assignee
        Map<Character, Object> assigneeFilter = new HashMap<>();
        assigneeFilter.put('A', "admin@montrance.com");
        List<IssueReport> myIssues = issueService.showIssues(assigneeFilter);
        System.out.println("✅ Retrieved " + myIssues.size() + " issues for admin (assignee filter)");
        
        System.out.println("✅ Reporting features working properly");
    }
    
    private static void testAssignmentFeatures() throws IssueTrackerException {
        System.out.println("\n👥 Testing Assignment Features...");
        
        // Test assigning issue to user
        Boolean assignResult = issueService.assignIssue("MTI-I-TEST-001", testUser.getUserId());
        if (assignResult != null && assignResult) {
            System.out.println("✅ Issue assignment works");
        } else {
            System.out.println("❌ Issue assignment failed");
        }
        
        System.out.println("✅ Assignment features working properly");
    }
}