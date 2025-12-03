package com.issuetracker.service;

import com.issuetracker.exception.IssueTrackerException;
import com.issuetracker.model.IssueHistory;
import java.util.List;

/**
 * Service interface for IssueHistory management operations
 */
public interface IssueHistoryService {
    
    /**
     * Record a new action in issue history
     * @param issueHistory IssueHistory object containing action details
     * @return History ID of the newly created record
     * @throws IssueTrackerException if operation fails
     */
    int recordAction(IssueHistory issueHistory) throws IssueTrackerException;
    
    /**
     * Get history for a specific issue
     * @param issueId Issue ID
     * @return List of history records for the issue
     * @throws IssueTrackerException if operation fails
     */
    List<IssueHistory> getIssueHistory(String issueId) throws IssueTrackerException;
    
    /**
     * Get all history records
     * @return List of all history records
     * @throws IssueTrackerException if operation fails
     */
    List<IssueHistory> getAllHistory() throws IssueTrackerException;
    
    /**
     * Get history by user
     * @param userId User ID
     * @return List of history records by the user
     * @throws IssueTrackerException if operation fails
     */
    List<IssueHistory> getHistoryByUser(int userId) throws IssueTrackerException;
    
    /**
     * Get history by action type
     * @param actionType Type of action (e.g., STATUS_CHANGE, ASSIGNMENT)
     * @return List of history records of the specified action type
     * @throws IssueTrackerException if operation fails
     */
    List<IssueHistory> getHistoryByActionType(String actionType) throws IssueTrackerException;
    
    /**
     * Delete history record
     * @param historyId History ID to delete
     * @return true if deletion successful, false otherwise
     * @throws IssueTrackerException if deletion fails
     */
    boolean deleteHistory(int historyId) throws IssueTrackerException;
    
    /**
     * Record status change action
     * @param issueId Issue ID
     * @param oldStatus Old status
     * @param newStatus New status
     * @param changedBy User who made the change
     * @param comments Optional comments
     * @throws IssueTrackerException if operation fails
     */
    void recordStatusChange(String issueId, String oldStatus, String newStatus, 
                          int changedBy, String comments) throws IssueTrackerException;
    
    /**
     * Record assignment action
     * @param issueId Issue ID
     * @param oldAssignee Old assignee ID (null if not assigned before)
     * @param newAssignee New assignee ID
     * @param changedBy User who made the change
     * @param comments Optional comments
     * @throws IssueTrackerException if operation fails
     */
    void recordAssignment(String issueId, Integer oldAssignee, Integer newAssignee,
                         int changedBy, String comments) throws IssueTrackerException;
    
    /**
     * Record issue creation action
     * @param issueId Issue ID
     * @param createdBy User who created the issue
     * @param comments Optional comments
     * @throws IssueTrackerException if operation fails
     */
    void recordIssueCreation(String issueId, int createdBy, String comments) throws IssueTrackerException;
}