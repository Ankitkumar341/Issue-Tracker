package com.issuetracker.dao;

import java.util.List;
import com.issuetracker.model.IssueHistory;
import com.issuetracker.exception.IssueTrackerException;

public interface IssueHistoryDAO {
    
    int addIssueHistory(IssueHistory history) throws IssueTrackerException;
    
    int saveHistory(IssueHistory history) throws IssueTrackerException;
    
    List<IssueHistory> getHistoryForIssue(String issueId) throws IssueTrackerException;
    
    List<IssueHistory> findByIssueId(String issueId) throws IssueTrackerException;
    
    List<IssueHistory> getAllHistory() throws IssueTrackerException;
    
    List<IssueHistory> findAll() throws IssueTrackerException;
    
    List<IssueHistory> findByUserId(int userId) throws IssueTrackerException;
    
    List<IssueHistory> findByActionType(String actionType) throws IssueTrackerException;
    
    boolean deleteHistory(int historyId) throws IssueTrackerException;
    
    boolean deleteIssueHistory(String issueId) throws IssueTrackerException;
    
    boolean deleteOldHistory(int daysOld) throws IssueTrackerException;
    
    IssueHistory getHistoryById(int historyId) throws IssueTrackerException;
    
    boolean updateHistoryComments(int historyId, String comments) throws IssueTrackerException;
}