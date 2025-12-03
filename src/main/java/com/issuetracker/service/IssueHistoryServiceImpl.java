package com.issuetracker.service;

import com.issuetracker.dao.IssueHistoryDAO;
import com.issuetracker.exception.IssueTrackerException;
import com.issuetracker.model.IssueHistory;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service implementation for IssueHistory management operations
 */
public class IssueHistoryServiceImpl implements IssueHistoryService {
    
    private IssueHistoryDAO issueHistoryDAO;
    
    public IssueHistoryServiceImpl(IssueHistoryDAO issueHistoryDAO) {
        this.issueHistoryDAO = issueHistoryDAO;
    }
    
    @Override
    public int recordAction(IssueHistory issueHistory) throws IssueTrackerException {
        validateIssueHistory(issueHistory);
        
        try {
            return issueHistoryDAO.saveHistory(issueHistory);
        } catch (Exception e) {
            throw new IssueTrackerException("Failed to record action in history: " + e.getMessage());
        }
    }
    
    @Override
    public List<IssueHistory> getIssueHistory(String issueId) throws IssueTrackerException {
        if (issueId == null || issueId.trim().isEmpty()) {
            throw new IssueTrackerException("Issue ID cannot be null or empty");
        }
        
        try {
            return issueHistoryDAO.findByIssueId(issueId.trim());
        } catch (Exception e) {
            throw new IssueTrackerException("Failed to get issue history: " + e.getMessage());
        }
    }
    
    @Override
    public List<IssueHistory> getAllHistory() throws IssueTrackerException {
        try {
            return issueHistoryDAO.findAll();
        } catch (Exception e) {
            throw new IssueTrackerException("Failed to get all history: " + e.getMessage());
        }
    }
    
    @Override
    public List<IssueHistory> getHistoryByUser(int userId) throws IssueTrackerException {
        if (userId <= 0) {
            throw new IssueTrackerException("User ID must be positive");
        }
        
        try {
            return issueHistoryDAO.findByUserId(userId);
        } catch (Exception e) {
            throw new IssueTrackerException("Failed to get history by user: " + e.getMessage());
        }
    }
    
    @Override
    public List<IssueHistory> getHistoryByActionType(String actionType) throws IssueTrackerException {
        if (actionType == null || actionType.trim().isEmpty()) {
            throw new IssueTrackerException("Action type cannot be null or empty");
        }
        
        try {
            return issueHistoryDAO.findByActionType(actionType.trim());
        } catch (Exception e) {
            throw new IssueTrackerException("Failed to get history by action type: " + e.getMessage());
        }
    }
    
    @Override
    public boolean deleteHistory(int historyId) throws IssueTrackerException {
        if (historyId <= 0) {
            throw new IssueTrackerException("History ID must be positive");
        }
        
        try {
            return issueHistoryDAO.deleteHistory(historyId);
        } catch (Exception e) {
            throw new IssueTrackerException("Failed to delete history: " + e.getMessage());
        }
    }
    
    @Override
    public void recordStatusChange(String issueId, String oldStatus, String newStatus, 
                                 int changedBy, String comments) throws IssueTrackerException {
        String oldValue = oldStatus != null ? oldStatus : "NULL";
        String newValue = newStatus != null ? newStatus : "NULL";
        
        IssueHistory history = new IssueHistory();
        history.setIssueId(issueId);
        history.setActionPerformed("STATUS_CHANGE");
        history.setUserId(changedBy);
        history.setActionDate(LocalDateTime.now());
        history.setComments(String.format("Status changed from %s to %s%s", 
            oldValue, newValue, comments != null ? " - " + comments : ""));
        
        recordAction(history);
    }
    
    @Override
    public void recordAssignment(String issueId, Integer oldAssignee, Integer newAssignee,
                               int changedBy, String comments) throws IssueTrackerException {
        String oldValue = oldAssignee != null ? oldAssignee.toString() : "UNASSIGNED";
        String newValue = newAssignee != null ? newAssignee.toString() : "UNASSIGNED";
        
        IssueHistory history = new IssueHistory();
        history.setIssueId(issueId);
        history.setActionPerformed("ASSIGNMENT");
        history.setUserId(changedBy);
        history.setActionDate(LocalDateTime.now());
        history.setComments(String.format("Assignment changed from %s to %s%s", 
            oldValue, newValue, comments != null ? " - " + comments : ""));
        
        recordAction(history);
    }
    
    @Override
    public void recordIssueCreation(String issueId, int createdBy, String comments) throws IssueTrackerException {
        IssueHistory history = new IssueHistory();
        history.setIssueId(issueId);
        history.setActionPerformed("ISSUE_CREATED");
        history.setUserId(createdBy);
        history.setActionDate(LocalDateTime.now());
        history.setComments(comments != null ? comments : "Issue created");
        
        recordAction(history);
    }
    
    /**
     * Validate IssueHistory object
     */
    private void validateIssueHistory(IssueHistory issueHistory) throws IssueTrackerException {
        if (issueHistory == null) {
            throw new IssueTrackerException("IssueHistory cannot be null");
        }
        
        if (issueHistory.getIssueId() == null || issueHistory.getIssueId().trim().isEmpty()) {
            throw new IssueTrackerException("Issue ID cannot be null or empty");
        }
        
        if (issueHistory.getUserId() <= 0) {
            throw new IssueTrackerException("User ID must be positive");
        }
        
        if (issueHistory.getActionPerformed() == null || issueHistory.getActionPerformed().trim().isEmpty()) {
            throw new IssueTrackerException("Action performed cannot be null or empty");
        }
        
        if (issueHistory.getActionDate() == null) {
            issueHistory.setActionDate(LocalDateTime.now());
        }
    }
}