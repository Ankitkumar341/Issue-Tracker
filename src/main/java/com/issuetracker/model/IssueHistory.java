package com.issuetracker.model;

import java.time.LocalDateTime;

public class IssueHistory {
    private int historyId;
    private String issueId;
    private int userId;
    private String actionPerformed;
    private LocalDateTime actionDate;
    private String comments;
    private LocalDateTime createdAt;
    
    public IssueHistory() {}
    
    public IssueHistory(String issueId, int userId, String actionPerformed, 
                       LocalDateTime actionDate, String comments) {
        this.issueId = issueId;
        this.userId = userId;
        this.actionPerformed = actionPerformed;
        this.actionDate = actionDate;
        this.comments = comments;
        this.createdAt = LocalDateTime.now();
    }
    
    public int getHistoryId() {
        return historyId;
    }
    
    public void setHistoryId(int historyId) {
        this.historyId = historyId;
    }
    
    public String getIssueId() {
        return issueId;
    }
    
    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public String getActionPerformed() {
        return actionPerformed;
    }
    
    public void setActionPerformed(String actionPerformed) {
        this.actionPerformed = actionPerformed;
    }
    
    public LocalDateTime getActionDate() {
        return actionDate;
    }
    
    public void setActionDate(LocalDateTime actionDate) {
        this.actionDate = actionDate;
    }
    
    public String getComments() {
        return comments;
    }
    
    public void setComments(String comments) {
        this.comments = comments;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
        return "IssueHistory{" +
                "historyId=" + historyId +
                ", issueId='" + issueId + '\'' +
                ", userId=" + userId +
                ", actionPerformed='" + actionPerformed + '\'' +
                ", actionDate=" + actionDate +
                ", comments='" + comments + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}