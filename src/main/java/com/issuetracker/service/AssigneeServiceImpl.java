package com.issuetracker.service;

import java.util.List;

import com.issuetracker.dao.AssigneeDAO;
import com.issuetracker.model.Assignee;
import com.issuetracker.model.Unit;

// Do Not Change Any Signature
public class AssigneeServiceImpl implements AssigneeService
{
    private AssigneeDAO assigneeDAO;

    @Override
    public List<Assignee> fetchAssignee(Unit unit)
    {
	// Your Code Goes Here

	return null;
    }

    @Override
    public void updateActiveIssueCount(String assigneeEmail,
				       Character operation)
    {
	// Your Code Goes Here
    }
}