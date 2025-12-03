package com.issuetracker.service;

import java.util.ArrayList;
import java.util.List;
import com.issuetracker.dao.UserDAO;
import com.issuetracker.dao.UserDAOImpl;
import com.issuetracker.exception.IssueTrackerException;
import com.issuetracker.model.Assignee;
import com.issuetracker.model.Unit;
import com.issuetracker.model.User;

public class AssigneeServiceImpl implements AssigneeService
{
    private UserDAO userDAO = new UserDAOImpl();

    @Override
    public List<Assignee> fetchAssignee(Unit unit)
    {
        try {
            List<User> users = userDAO.getAllUsers();
            List<Assignee> assignees = new ArrayList<>();
            
            for (User user : users) {
                Assignee assignee = new Assignee();
                assignee.setAssigneeId("USR-" + user.getUserId());
                assignee.setAssigneeName(user.getFullName());
                assignee.setAssigneeEmailId(user.getEmail());
                assignee.setUnit(unit);
                assignee.setActiveIssuesCount(0);
                assignees.add(assignee);
            }
            
            return assignees;
        } catch (IssueTrackerException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public void updateActiveIssueCount(String assigneeEmail, Character operation)
    {
        // This method would require additional database logic to track active issue counts
        // For now, it's a placeholder since we're managing counts differently in the database
    }
}