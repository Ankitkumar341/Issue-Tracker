package com.issuetracker.service;

import java.util.List;
import java.util.Map;

import com.issuetracker.dao.IssueDAO;
import com.issuetracker.dao.IssueHistoryDAO;
import com.issuetracker.dao.IssueHistoryDAOImpl;
import com.issuetracker.dao.UserDAO;
import com.issuetracker.dao.UserDAOImpl;
import com.issuetracker.exception.IssueTrackerException;
import com.issuetracker.model.Issue;
import com.issuetracker.model.IssueReport;
import com.issuetracker.model.IssueStatus;
import com.issuetracker.model.User;

public class IssueServiceImpl implements IssueService {
    
    private IssueDAO issueDAO;
    private UserService userService;
    private IssueHistoryService issueHistoryService;
    
    public IssueServiceImpl(IssueDAO issueDAO) {
        this.issueDAO = issueDAO;
        
        // Initialize dependent services
        UserDAO userDAO = new UserDAOImpl();
        this.userService = new UserServiceImpl(userDAO);
        
        IssueHistoryDAO issueHistoryDAO = new IssueHistoryDAOImpl();
        this.issueHistoryService = new IssueHistoryServiceImpl(issueHistoryDAO);
    }
    
    public IssueServiceImpl(IssueDAO issueDAO, UserService userService, IssueHistoryService issueHistoryService) {
        this.issueDAO = issueDAO;
        this.userService = userService;
        this.issueHistoryService = issueHistoryService;
    }
    
    @Override
    public String reportAnIssue(Issue issue) throws IssueTrackerException {
        // Validate that the creator exists
        User creator = userService.findUserById(issue.getReportedByUserId());
        if (creator == null) {
            throw new IssueTrackerException("Invalid creator user ID: " + issue.getReportedByUserId());
        }
        
        // Report the issue
        String result = issueDAO.reportAnIssue(issue);
        
        // Record issue creation in history
        try {
            issueHistoryService.recordIssueCreation(issue.getIssueId(), issue.getReportedByUserId(), 
                "Issue created: " + issue.getIssueDescription());
        } catch (Exception e) {
            System.err.println("Warning: Failed to record issue creation in history: " + e.getMessage());
        }
        
        return result;
    }

    @Override
    public Boolean updateStatus(String issueId, IssueStatus status) throws IssueTrackerException {
        // Get current issue to record old status
        Issue currentIssue = getIssueById(issueId);
        if (currentIssue == null) {
            throw new IssueTrackerException("Issue not found: " + issueId);
        }
        
        String oldStatus = currentIssue.getStatus() != null ? currentIssue.getStatus().toString() : "UNKNOWN";
        String newStatus = status.toString();
        
        // Update the status
        Boolean result = issueDAO.updateStatus(issueId, status);
        
        // Record status change in history
        if (result) {
            try {
                issueHistoryService.recordStatusChange(issueId, oldStatus, newStatus, 
                    currentIssue.getReportedByUserId(), "Status updated");
            } catch (Exception e) {
                System.err.println("Warning: Failed to record status change in history: " + e.getMessage());
            }
        }
        
        return result;
    }

    @Override
    public List<IssueReport> showIssues(Map<Character, Object> filterCriteria) throws IssueTrackerException {
        return issueDAO.showIssues(filterCriteria);
    }

    @Override
    public List<Issue> deleteIssues() throws IssueTrackerException {
        return issueDAO.deleteIssues();
    }

    @Override
    public Boolean assignIssue(String issueId, int userId) throws IssueTrackerException {
        // Validate that the assignee exists
        User assignee = userService.findUserById(userId);
        if (assignee == null) {
            throw new IssueTrackerException("Invalid assignee user ID: " + userId);
        }
        
        // Get current issue to record old assignment
        Issue currentIssue = getIssueById(issueId);
        if (currentIssue == null) {
            throw new IssueTrackerException("Issue not found: " + issueId);
        }
        
        Integer oldAssignee = currentIssue.getAssignedToUserId() > 0 ? currentIssue.getAssignedToUserId() : null;
        
        // Assign the issue
        Boolean result = issueDAO.assignIssue(issueId, userId);
        
        // Record assignment in history
        if (result) {
            try {
                issueHistoryService.recordAssignment(issueId, oldAssignee, userId, 
                    currentIssue.getReportedByUserId(), "Issue assigned to " + assignee.getFullName());
            } catch (Exception e) {
                System.err.println("Warning: Failed to record assignment in history: " + e.getMessage());
            }
        }
        
        return result;
    }
    
    /**
     * Helper method to get issue by ID
     */
    private Issue getIssueById(String issueId) throws IssueTrackerException {
        try {
            return issueDAO.getIssueById(issueId);
        } catch (Exception e) {
            return null; // Issue not found
        }
    }
    
    // Additional methods for integration
    
    /**
     * Get user service instance
     */
    public UserService getUserService() {
        return userService;
    }
    
    /**
     * Get issue history service instance
     */
    public IssueHistoryService getIssueHistoryService() {
        return issueHistoryService;
    }
}