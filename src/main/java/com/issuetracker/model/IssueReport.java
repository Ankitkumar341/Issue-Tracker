package com.issuetracker.model;

import java.time.LocalDate;

public class IssueReport
{
    private String issueId;
    private String issueDescription;
    private String assigneeEmail;
    private String reporterEmail;
    private IssueStatus status;
    private Priority priority;
    private Unit unit;
    private LocalDate reportingDate;
    private LocalDate closingDate;
    private LocalDate updatedOn;

    public IssueReport() {
        // Default constructor
    }

    public IssueReport(String issueId, String issueDescription, String assigneeEmail, IssueStatus status) {
        this.issueId = issueId;
        this.issueDescription = issueDescription;
        this.assigneeEmail = assigneeEmail;
        this.status = status;
    }

    public IssueReport(String issueId, String issueDescription, String assigneeEmail, String reporterEmail, 
                      IssueStatus status, Priority priority, Unit unit, LocalDate reportingDate, LocalDate closingDate) {
        this.issueId = issueId;
        this.issueDescription = issueDescription;
        this.assigneeEmail = assigneeEmail;
        this.reporterEmail = reporterEmail;
        this.status = status;
        this.priority = priority;
        this.unit = unit;
        this.reportingDate = reportingDate;
        this.closingDate = closingDate;
    }

    public String getIssueId() {
        return issueId;
    }

    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }

    public String getIssueDescription() {
        return issueDescription;
    }

    public void setIssueDescription(String issueDescription) {
        this.issueDescription = issueDescription;
    }

    public String getAssigneeEmail() {
        return assigneeEmail;
    }

    public void setAssigneeEmail(String assigneeEmail) {
        this.assigneeEmail = assigneeEmail;
    }

    public String getReporterEmail() {
        return reporterEmail;
    }

    public void setReporterEmail(String reporterEmail) {
        this.reporterEmail = reporterEmail;
    }

    public IssueStatus getStatus() {
        return status;
    }

    public void setStatus(IssueStatus status) {
        this.status = status;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public LocalDate getReportingDate() {
        return reportingDate;
    }

    public void setReportingDate(LocalDate reportingDate) {
        this.reportingDate = reportingDate;
    }

    public LocalDate getClosingDate() {
        return closingDate;
    }

    public void setClosingDate(LocalDate closingDate) {
        this.closingDate = closingDate;
    }

    public LocalDate getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(LocalDate updatedOn) {
        this.updatedOn = updatedOn;
    }

    @Override
    public String toString() {
        return "IssueReport{" +
                "issueId='" + issueId + '\'' +
                ", issueDescription='" + issueDescription + '\'' +
                ", assigneeEmail='" + assigneeEmail + '\'' +
                ", reporterEmail='" + reporterEmail + '\'' +
                ", status=" + status +
                ", priority=" + priority +
                ", unit=" + unit +
                ", reportingDate=" + reportingDate +
                ", closingDate=" + closingDate +
                '}' + "\n";
    }
}