package com.issuetracker.service;

import java.util.List;
import java.util.Map;

import com.issuetracker.dao.IssueDAO;
import com.issuetracker.exception.IssueTrackerException;
import com.issuetracker.model.Issue;
import com.issuetracker.model.IssueReport;
import com.issuetracker.model.IssueStatus;
import com.issuetracker.validator.Validator;

// Do Not Change Any Signature
public class IssueServiceImpl implements IssueService
{
    private AssigneeService assigneeService;
    private IssueDAO issueDAO;
    private Validator validator;

    @Override
    public String reportAnIssue(Issue issue) throws IssueTrackerException
    {
	// Your Code Goes Here

	return null;
    }

    @Override
    public Boolean updateStatus(String issueId,
				IssueStatus status) throws IssueTrackerException
    {
	// Your Code Goes Here

	return null;
    }

    @Override
    public List<IssueReport> showIssues(Map<Character, Object> filterCriteria) throws IssueTrackerException
    {
	// Your Code Goes Here

	return null;
    }

    @Override
    public List<Issue> deleteIssues() throws IssueTrackerException
    {
	// Your Code Goes Here

	return null;
    }
}