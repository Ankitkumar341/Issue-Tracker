package com.issuetracker.model;


public class Assignee
{
private String assigneeId ;
private String assigneeName ;
private String assigneeEmail ;
Unit wokringUnit ;
private  int numberOfIssuesActive  ;


    public Assignee(String assigneeId, String assigneeName, String assigneeEmail, Unit wokringUnit, int numberOfIssuesActive) {
        this.assigneeId = assigneeId;
        this.assigneeName = assigneeName;
        this.assigneeEmail = assigneeEmail;
        this.wokringUnit = wokringUnit;
        this.numberOfIssuesActive = numberOfIssuesActive;
    }

    public String getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(String assigneeId) {
        this.assigneeId = assigneeId;
    }

    public String getAssigneeName() {
        return assigneeName;
    }

    public void setAssigneeName(String assigneeName) {
        this.assigneeName = assigneeName;
    }

    public String getAssigneeEmail() {
        return assigneeEmail;
    }

    public void setAssigneeEmail(String assigneeEmail) {
        this.assigneeEmail = assigneeEmail;
    }

    public Unit getWokringUnit() {
        return wokringUnit;
    }

    public void setWokringUnit(Unit wokringUnit) {
        this.wokringUnit = wokringUnit;
    }

    public int getNumberOfIssuesActive() {
        return numberOfIssuesActive;
    }

    public void setNumberOfIssuesActive(int numberOfIssuesActive) {
        this.numberOfIssuesActive = numberOfIssuesActive;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "Assignee{" +
                "assigneeId='" + assigneeId + '\'' +
                ", assigneeName='" + assigneeName + '\'' +
                ", assigneeEmail='" + assigneeEmail + '\'' +
                ", wokringUnit=" + wokringUnit +
                ", numberOfIssuesActive=" + numberOfIssuesActive +
                '}' +"\n";
    }
}




