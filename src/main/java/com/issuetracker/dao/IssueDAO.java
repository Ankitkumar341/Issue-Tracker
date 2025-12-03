package com.issuetracker.dao;

import java.util.List;
import java.util.Map;

import com.issuetracker.exception.IssueTrackerException;
import com.issuetracker.model.Issue;
import com.issuetracker.model.IssueReport;
import com.issuetracker.model.IssueStatus;

public interface IssueDAO
{
    public abstract List<Issue> getIssueList();

    public abstract void setIssueList(List<Issue> issueList);

    /**
     * @params
     *         issue - The new issue to be added
     * 
     * @operation Stores a new issue in the issueList
     * 
     * @returns
     *          String - The issue id
     */
    public abstract String reportAnIssue(Issue issue) throws IssueTrackerException;

    /**
     * @params
     *         issueId - The issue id to be updated
     *         status - The new status
     * 
     * @operation Updates the status of the given issue
     *            with the given status
     */
    public abstract Boolean updateStatus(String issueId, IssueStatus status) throws IssueTrackerException;

    /**
     * @params
     *         issueId - The issue id to be fetched
     * 
     * @operation Fetches the issue object based on the given issue id
     * 
     * @returns
     *          Issue - The fetched issue object
     */
    public abstract Issue getIssueById(String issueId) throws IssueTrackerException;
    
    /**
     * Show issues based on filter criteria
     */
    public abstract List<IssueReport> showIssues(Map<Character, Object> filterCriteria) throws IssueTrackerException;
    
    /**
     * Delete resolved/closed issues
     */
    public abstract List<Issue> deleteIssues() throws IssueTrackerException;
    
    /**
     * Assign issue to a user
     */
    public abstract boolean assignIssue(String issueId, int assigneeId) throws IssueTrackerException;
    
    /**
     * Get issues by user
     */
    public abstract List<Issue> getIssuesByUser(int userId) throws IssueTrackerException;
    
    /**
     * Delete a specific issue
     */
    public abstract boolean deleteIssue(String issueId) throws IssueTrackerException;
}