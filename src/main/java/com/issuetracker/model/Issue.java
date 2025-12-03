package com.issuetracker.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Issue
{
    private String issueId;
    private String issueDescription;
    private Unit issueUnit;
    private LocalDate reportedOn;
    private LocalDate updatedOn;
    private String assigneeEmail;
    private IssueStatus status;
    private Priority priority;
    private int reportedByUserId;
    private int assignedToUserId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Issue() {}

    public Issue(String issueId, String issueDescription, Unit issueUnit,
		 LocalDate reportedOn, LocalDate updatedOn,
		 String assigneeEmail, IssueStatus status)
    {
	this.issueId = issueId;
	this.issueDescription = issueDescription;
	this.issueUnit = issueUnit;
	this.reportedOn = reportedOn;
	this.updatedOn = updatedOn;
	this.assigneeEmail = assigneeEmail;
	this.status = status;
	this.priority = Priority.MEDIUM;
	this.createdAt = LocalDateTime.now();
	this.updatedAt = LocalDateTime.now();
    }
    
    public Issue(String issueId, String issueDescription, Unit issueUnit,
                 LocalDate reportedOn, String assigneeEmail, IssueStatus status, 
                 Priority priority, int reportedByUserId, int assignedToUserId)
    {
        this.issueId = issueId;
        this.issueDescription = issueDescription;
        this.issueUnit = issueUnit;
        this.reportedOn = reportedOn;
        this.updatedOn = LocalDate.now();
        this.assigneeEmail = assigneeEmail;
        this.status = status;
        this.priority = priority;
        this.reportedByUserId = reportedByUserId;
        this.assignedToUserId = assignedToUserId;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public String getIssueId()
    {
	return issueId;
    }

    public void setIssueId(String issueId)
    {
	this.issueId = issueId;
    }

    public String getIssueDescription()
    {
	return issueDescription;
    }

    public void setIssueDescription(String issueDescription)
    {
	this.issueDescription = issueDescription;
    }

    public Unit getIssueUnit()
    {
	return issueUnit;
    }

    public void setIssueUnit(Unit issueUnit)
    {
	this.issueUnit = issueUnit;
    }

    public LocalDate getReportedOn()
    {
	return reportedOn;
    }

    public void setReportedOn(LocalDate reportedOn)
    {
	this.reportedOn = reportedOn;
    }

    public LocalDate getUpdatedOn()
    {
	return updatedOn;
    }

    public void setUpdatedOn(LocalDate updatedOn)
    {
	this.updatedOn = updatedOn;
    }

    public String getAssigneeEmail()
    {
	return assigneeEmail;
    }

    public void setAssigneeEmail(String assigneeEmail)
    {
	this.assigneeEmail = assigneeEmail;
    }

    public IssueStatus getStatus()
    {
	return status;
    }

    public void setStatus(IssueStatus status)
    {
	this.status = status;
    }
    
    public Priority getPriority() {
        return priority;
    }
    
    public void setPriority(Priority priority) {
        this.priority = priority;
    }
    
    public int getReportedByUserId() {
        return reportedByUserId;
    }
    
    public void setReportedByUserId(int reportedByUserId) {
        this.reportedByUserId = reportedByUserId;
    }
    
    public int getAssignedToUserId() {
        return assignedToUserId;
    }
    
    public void setAssignedToUserId(int assignedToUserId) {
        this.assignedToUserId = assignedToUserId;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}