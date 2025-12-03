package com.issuetracker.dao;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.issuetracker.exception.IssueTrackerException;
import com.issuetracker.model.*;
import com.issuetracker.util.DatabaseUtil;

public class IssueDAOJdbcImpl implements IssueDAO {
    
    @Override
    public List<Issue> getIssueList() {
        // This method is deprecated in JDBC implementation
        // Use showIssues with empty criteria instead
        try {
            return convertIssueReportsToIssues(showIssues(new HashMap<>()));
        } catch (IssueTrackerException e) {
            return new ArrayList<>();
        }
    }
    
    @Override
    public void setIssueList(List<Issue> issueList) {
        // This method is not applicable for JDBC implementation
        // Issues are stored in database, not in memory
        throw new UnsupportedOperationException("setIssueList is not supported in JDBC implementation");
    }
    
    @Override
    public String reportAnIssue(Issue issue) throws IssueTrackerException {
        String sql = "INSERT INTO issues (issue_id, description, unit, reporting_date, status, priority, reported_by_user_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, issue.getIssueId());
            pstmt.setString(2, issue.getIssueDescription());
            pstmt.setString(3, issue.getIssueUnit().toString());
            pstmt.setDate(4, Date.valueOf(issue.getReportedOn()));
            pstmt.setString(5, issue.getStatus().toString());
            pstmt.setString(6, issue.getPriority().toString());
            pstmt.setInt(7, issue.getReportedByUserId());
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                return issue.getIssueId();
            }
            return null;
            
        } catch (SQLException e) {
            throw new IssueTrackerException("Error reporting issue: " + e.getMessage());
        }
    }
    
    @Override
    public Boolean updateStatus(String issueId, IssueStatus status) throws IssueTrackerException {
        String sql = "UPDATE issues SET status = ?, updated_on = ? WHERE issue_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status.toString());
            pstmt.setDate(2, Date.valueOf(LocalDate.now()));
            pstmt.setString(3, issueId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            throw new IssueTrackerException("Error updating issue status: " + e.getMessage());
        }
    }
    
    @Override
    public Issue getIssueById(String issueId) throws IssueTrackerException {
        String sql = "SELECT * FROM issues WHERE issue_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, issueId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToIssue(rs);
                }
                return null;
            }
            
        } catch (SQLException e) {
            throw new IssueTrackerException("Error retrieving issue: " + e.getMessage());
        }
    }
    
    @Override
    public List<IssueReport> showIssues(Map<Character, Object> filterCriteria) throws IssueTrackerException {
        StringBuilder sql = new StringBuilder("SELECT i.*, u.email as reporter_email, ua.email as assignee_email FROM issues i ");
        sql.append("LEFT JOIN users u ON i.reported_by_user_id = u.user_id ");
        sql.append("LEFT JOIN users ua ON i.assigned_to_user_id = ua.user_id ");
        
        List<Object> parameters = new ArrayList<>();
        
        if (filterCriteria != null && !filterCriteria.isEmpty()) {
            sql.append("WHERE ");
            boolean first = true;
            
            for (Map.Entry<Character, Object> entry : filterCriteria.entrySet()) {
                if (!first) {
                    sql.append(" AND ");
                }
                
                Character key = entry.getKey();
                Object value = entry.getValue();
                
                switch (key) {
                    case 'S': // Status
                        sql.append("i.status = ?");
                        parameters.add(value.toString());
                        break;
                    case 'A': // Assignee
                        sql.append("ua.email = ?");
                        parameters.add(value.toString());
                        break;
                    case 'U': // Unit
                        sql.append("i.unit = ?");
                        parameters.add(value.toString());
                        break;
                    case 'P': // Priority
                        sql.append("i.priority = ?");
                        parameters.add(value.toString());
                        break;
                }
                first = false;
            }
        }
        
        sql.append(" ORDER BY i.reporting_date DESC");
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            
            for (int i = 0; i < parameters.size(); i++) {
                pstmt.setObject(i + 1, parameters.get(i));
            }
            
            try (ResultSet rs = pstmt.executeQuery()) {
                List<IssueReport> reports = new ArrayList<>();
                
                while (rs.next()) {
                    IssueReport report = new IssueReport();
                    report.setIssueId(rs.getString("issue_id"));
                    report.setIssueDescription(rs.getString("description"));
                    report.setReportingDate(rs.getDate("reporting_date").toLocalDate());
                    report.setStatus(IssueStatus.valueOf(rs.getString("status")));
                    report.setUnit(Unit.valueOf(rs.getString("unit")));
                    report.setAssigneeEmail(rs.getString("assignee_email"));
                    
                    String priorityStr = rs.getString("priority");
                    if (priorityStr != null) {
                        report.setPriority(Priority.valueOf(priorityStr));
                    }
                    
                    Date updatedOn = rs.getDate("updated_on");
                    if (updatedOn != null) {
                        report.setUpdatedOn(updatedOn.toLocalDate());
                    }
                    
                    reports.add(report);
                }
                
                return reports;
            }
            
        } catch (SQLException e) {
            throw new IssueTrackerException("Error retrieving issues: " + e.getMessage());
        }
    }
    
    @Override
    public List<Issue> deleteIssues() throws IssueTrackerException {
        // Get resolved/closed issues that are at least 14 days old
        String selectSql = "SELECT * FROM issues WHERE (status = 'RESOLVED' OR status = 'CLOSED') AND updated_on <= DATE_SUB(CURDATE(), INTERVAL 14 DAY)";
        String deleteSql = "DELETE FROM issues WHERE (status = 'RESOLVED' OR status = 'CLOSED') AND updated_on <= DATE_SUB(CURDATE(), INTERVAL 14 DAY)";
        
        List<Issue> deletedIssues = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection()) {
            // First, get the issues to be deleted
            try (PreparedStatement selectStmt = conn.prepareStatement(selectSql);
                 ResultSet rs = selectStmt.executeQuery()) {
                
                while (rs.next()) {
                    deletedIssues.add(mapResultSetToIssue(rs));
                }
            }
            
            // Then delete them
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                deleteStmt.executeUpdate();
            }
            
            return deletedIssues;
            
        } catch (SQLException e) {
            throw new IssueTrackerException("Error deleting resolved issues: " + e.getMessage());
        }
    }
    
    @Override
    public boolean assignIssue(String issueId, int assigneeId) throws IssueTrackerException {
        String sql = "UPDATE issues SET assigned_to_user_id = ? WHERE issue_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, assigneeId);
            pstmt.setString(2, issueId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            throw new IssueTrackerException("Error assigning issue: " + e.getMessage());
        }
    }
    
    @Override
    public List<Issue> getIssuesByUser(int userId) throws IssueTrackerException {
        String sql = "SELECT * FROM issues WHERE assigned_to_user_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                List<Issue> issues = new ArrayList<>();
                
                while (rs.next()) {
                    issues.add(mapResultSetToIssue(rs));
                }
                
                return issues;
            }
            
        } catch (SQLException e) {
            throw new IssueTrackerException("Error retrieving user issues: " + e.getMessage());
        }
    }
    
    @Override
    public boolean deleteIssue(String issueId) throws IssueTrackerException {
        String sql = "DELETE FROM issues WHERE issue_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, issueId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            throw new IssueTrackerException("Error deleting issue: " + e.getMessage());
        }
    }
    
    private Issue mapResultSetToIssue(ResultSet rs) throws SQLException {
        Issue issue = new Issue();
        issue.setIssueId(rs.getString("issue_id"));
        issue.setIssueDescription(rs.getString("description"));
        issue.setIssueUnit(Unit.valueOf(rs.getString("unit")));
        issue.setReportedOn(rs.getDate("reporting_date").toLocalDate());
        issue.setStatus(IssueStatus.valueOf(rs.getString("status")));
        issue.setReportedByUserId(rs.getInt("reported_by_user_id"));
        
        int assignedToUserId = rs.getInt("assigned_to_user_id");
        if (!rs.wasNull()) {
            issue.setAssignedToUserId(assignedToUserId);
        }
        
        String priorityStr = rs.getString("priority");
        if (priorityStr != null) {
            issue.setPriority(Priority.valueOf(priorityStr));
        }
        
        Date updatedOn = rs.getDate("updated_on");
        if (updatedOn != null) {
            issue.setUpdatedOn(updatedOn.toLocalDate());
        }
        
        return issue;
    }
    
    private List<Issue> convertIssueReportsToIssues(List<IssueReport> reports) {
        List<Issue> issues = new ArrayList<>();
        for (IssueReport report : reports) {
            Issue issue = new Issue();
            issue.setIssueId(report.getIssueId());
            issue.setIssueDescription(report.getIssueDescription());
            issue.setIssueUnit(report.getUnit());
            issue.setReportedOn(report.getReportingDate());
            issue.setStatus(report.getStatus());
            issue.setPriority(report.getPriority());
            if (report.getUpdatedOn() != null) {
                issue.setUpdatedOn(report.getUpdatedOn());
            }
            issues.add(issue);
        }
        return issues;
    }
}