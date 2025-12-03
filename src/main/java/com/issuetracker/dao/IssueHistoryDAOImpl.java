package com.issuetracker.dao;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.issuetracker.exception.IssueTrackerException;
import com.issuetracker.model.IssueHistory;
import com.issuetracker.util.DatabaseUtil;

public class IssueHistoryDAOImpl implements IssueHistoryDAO {
    
    @Override
    public int addIssueHistory(IssueHistory history) throws IssueTrackerException {
        String sql = "INSERT INTO issue_history (issue_id, user_id, action_performed, action_date, comments) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, history.getIssueId());
            pstmt.setInt(2, history.getUserId());
            pstmt.setString(3, history.getActionPerformed());
            pstmt.setTimestamp(4, Timestamp.valueOf(history.getActionDate()));
            pstmt.setString(5, history.getComments());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new IssueTrackerException("Creating issue history failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new IssueTrackerException("Creating issue history failed, no ID obtained.");
                }
            }
            
        } catch (SQLException e) {
            throw new IssueTrackerException("Error adding issue history: " + e.getMessage());
        }
    }
    
    @Override
    public List<IssueHistory> getHistoryForIssue(String issueId) throws IssueTrackerException {
        String sql = "SELECT h.*, u.username, u.full_name " +
                    "FROM issue_history h " +
                    "LEFT JOIN users u ON h.user_id = u.user_id " +
                    "WHERE h.issue_id = ? " +
                    "ORDER BY h.action_date DESC";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, issueId);
            ResultSet rs = pstmt.executeQuery();
            List<IssueHistory> historyList = new ArrayList<>();
            
            while (rs.next()) {
                IssueHistory history = new IssueHistory();
                history.setHistoryId(rs.getInt("history_id"));
                history.setIssueId(rs.getString("issue_id"));
                history.setUserId(rs.getInt("user_id"));
                history.setActionPerformed(rs.getString("action_performed"));
                
                Timestamp actionDate = rs.getTimestamp("action_date");
                if (actionDate != null) {
                    history.setActionDate(actionDate.toLocalDateTime());
                }
                
                history.setComments(rs.getString("comments"));
                
                Timestamp createdAt = rs.getTimestamp("created_at");
                if (createdAt != null) {
                    history.setCreatedAt(createdAt.toLocalDateTime());
                }
                
                historyList.add(history);
            }
            
            return historyList;
            
        } catch (SQLException e) {
            throw new IssueTrackerException("Error deleting issue history: " + e.getMessage());
        }
    }
    
    @Override
    public List<IssueHistory> getAllHistory() throws IssueTrackerException {
        String sql = "SELECT h.*, i.description as issue_description " +
                    "FROM issue_history h " +
                    "LEFT JOIN issues i ON h.issue_id = i.issue_id " +
                    "ORDER BY h.action_date DESC " +
                    "LIMIT 50";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            List<IssueHistory> historyList = new ArrayList<>();
            
            while (rs.next()) {
                IssueHistory history = new IssueHistory();
                history.setHistoryId(rs.getInt("history_id"));
                history.setIssueId(rs.getString("issue_id"));
                history.setUserId(rs.getInt("user_id"));
                history.setActionPerformed(rs.getString("action_performed"));
                
                Timestamp actionDate = rs.getTimestamp("action_date");
                if (actionDate != null) {
                    history.setActionDate(actionDate.toLocalDateTime());
                }
                
                history.setComments(rs.getString("comments"));
                
                Timestamp createdAt = rs.getTimestamp("created_at");
                if (createdAt != null) {
                    history.setCreatedAt(createdAt.toLocalDateTime());
                }
                
                historyList.add(history);
            }
            
            return historyList;
            
        } catch (SQLException e) {
            throw new IssueTrackerException("Error retrieving user action history: " + e.getMessage());
        }
    }
    
    @Override
    public boolean deleteHistory(int historyId) throws IssueTrackerException {
        String sql = "DELETE FROM issue_history WHERE history_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, historyId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            throw new IssueTrackerException("Error deleting history record: " + e.getMessage());
        }
    }
    

    @Override
    public boolean deleteOldHistory(int daysOld) throws IssueTrackerException {
        String sql = "DELETE FROM issue_history WHERE action_date < DATE_SUB(NOW(), INTERVAL ? DAY)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, daysOld);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            throw new IssueTrackerException("Error deleting old history: " + e.getMessage());
        }
    }
    
    @Override
    public boolean deleteIssueHistory(String issueId) throws IssueTrackerException {
        String sql = "DELETE FROM issue_history WHERE issue_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, issueId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            throw new IssueTrackerException("Error deleting issue history: " + e.getMessage());
        }
    }
    @Override
    public IssueHistory getHistoryById(int historyId) throws IssueTrackerException {
        String sql = "SELECT h.*, u.username, u.full_name " +
                    "FROM issue_history h " +
                    "LEFT JOIN users u ON h.user_id = u.user_id " +
                    "WHERE h.history_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, historyId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                IssueHistory history = new IssueHistory();
                history.setHistoryId(rs.getInt("history_id"));
                history.setIssueId(rs.getString("issue_id"));
                history.setUserId(rs.getInt("user_id"));
                history.setActionPerformed(rs.getString("action_performed"));
                
                Timestamp actionDate = rs.getTimestamp("action_date");
                if (actionDate != null) {
                    history.setActionDate(actionDate.toLocalDateTime());
                }
                
                history.setComments(rs.getString("comments"));
                
                Timestamp createdAt = rs.getTimestamp("created_at");
                if (createdAt != null) {
                    history.setCreatedAt(createdAt.toLocalDateTime());
                }
                
                return history;
            } else {
                return null;
            }
            
        } catch (SQLException e) {
            throw new IssueTrackerException("Error retrieving history by ID: " + e.getMessage());
        }
    }
    
    @Override
    public boolean updateHistoryComments(int historyId, String comments) throws IssueTrackerException {
        String sql = "UPDATE issue_history SET comments = ? WHERE history_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, comments);
            pstmt.setInt(2, historyId);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            throw new IssueTrackerException("Error updating history comments: " + e.getMessage());
        }
    }
    
    public List<IssueHistory> getRecentActivity(int limit) throws IssueTrackerException {
        String sql = "SELECT h.*, u.username, u.full_name, i.description as issue_description " +
                    "FROM issue_history h " +
                    "LEFT JOIN users u ON h.user_id = u.user_id " +
                    "LEFT JOIN issues i ON h.issue_id = i.issue_id " +
                    "ORDER BY h.action_date DESC " +
                    "LIMIT ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, limit);
            ResultSet rs = pstmt.executeQuery();
            List<IssueHistory> historyList = new ArrayList<>();
            
            while (rs.next()) {
                IssueHistory history = new IssueHistory();
                history.setHistoryId(rs.getInt("history_id"));
                history.setIssueId(rs.getString("issue_id"));
                history.setUserId(rs.getInt("user_id"));
                history.setActionPerformed(rs.getString("action_performed"));
                
                Timestamp actionDate = rs.getTimestamp("action_date");
                if (actionDate != null) {
                    history.setActionDate(actionDate.toLocalDateTime());
                }
                
                history.setComments(rs.getString("comments"));
                
                Timestamp createdAt = rs.getTimestamp("created_at");
                if (createdAt != null) {
                    history.setCreatedAt(createdAt.toLocalDateTime());
                }
                
                historyList.add(history);
            }
            
            return historyList;
            
        } catch (SQLException e) {
            throw new IssueTrackerException("Error retrieving recent activity: " + e.getMessage());
        }
    }
    
    public int getHistoryCount() throws IssueTrackerException {
        String sql = "SELECT COUNT(*) FROM issue_history";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
            
        } catch (SQLException e) {
            throw new IssueTrackerException("Error counting history records: " + e.getMessage());
        }
    }
    
    public int getHistoryCountByUser(int userId) throws IssueTrackerException {
        String sql = "SELECT COUNT(*) FROM issue_history WHERE user_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
            
        } catch (SQLException e) {
            throw new IssueTrackerException("Error counting history by user: " + e.getMessage());
        }
    }
    
    public List<IssueHistory> getHistoryByDateRange(LocalDateTime startDate, LocalDateTime endDate) throws IssueTrackerException {
        String sql = "SELECT h.*, u.username, u.full_name, i.description as issue_description " +
                    "FROM issue_history h " +
                    "LEFT JOIN users u ON h.user_id = u.user_id " +
                    "LEFT JOIN issues i ON h.issue_id = i.issue_id " +
                    "WHERE h.action_date BETWEEN ? AND ? " +
                    "ORDER BY h.action_date DESC";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setTimestamp(1, Timestamp.valueOf(startDate));
            pstmt.setTimestamp(2, Timestamp.valueOf(endDate));
            
            ResultSet rs = pstmt.executeQuery();
            List<IssueHistory> historyList = new ArrayList<>();
            
            while (rs.next()) {
                IssueHistory history = new IssueHistory();
                history.setHistoryId(rs.getInt("history_id"));
                history.setIssueId(rs.getString("issue_id"));
                history.setUserId(rs.getInt("user_id"));
                history.setActionPerformed(rs.getString("action_performed"));
                
                Timestamp actionDate = rs.getTimestamp("action_date");
                if (actionDate != null) {
                    history.setActionDate(actionDate.toLocalDateTime());
                }
                
                history.setComments(rs.getString("comments"));
                
                Timestamp createdAt = rs.getTimestamp("created_at");
                if (createdAt != null) {
                    history.setCreatedAt(createdAt.toLocalDateTime());
                }
                
                historyList.add(history);
            }
            
            return historyList;
            
        } catch (SQLException e) {
            throw new IssueTrackerException("Error retrieving history by date range: " + e.getMessage());
        }
    }
    
    // Additional methods required by IssueHistoryService
    
    @Override
    public int saveHistory(IssueHistory history) throws IssueTrackerException {
        return addIssueHistory(history);
    }
    
    @Override
    public List<IssueHistory> findByIssueId(String issueId) throws IssueTrackerException {
        return getHistoryForIssue(issueId);
    }
    
    @Override
    public List<IssueHistory> findAll() throws IssueTrackerException {
        return getAllHistory();
    }
    
    @Override
    public List<IssueHistory> findByUserId(int userId) throws IssueTrackerException {
        String sql = "SELECT h.*, u.username, u.full_name " +
                    "FROM issue_history h " +
                    "LEFT JOIN users u ON h.user_id = u.user_id " +
                    "WHERE h.user_id = ? " +
                    "ORDER BY h.action_date DESC";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            List<IssueHistory> historyList = new ArrayList<>();
            
            while (rs.next()) {
                IssueHistory history = mapResultSetToHistory(rs);
                historyList.add(history);
            }
            
            return historyList;
            
        } catch (SQLException e) {
            throw new IssueTrackerException("Error retrieving history by user: " + e.getMessage());
        }
    }
    
    @Override
    public List<IssueHistory> findByActionType(String actionType) throws IssueTrackerException {
        String sql = "SELECT h.*, u.username, u.full_name " +
                    "FROM issue_history h " +
                    "LEFT JOIN users u ON h.user_id = u.user_id " +
                    "WHERE h.action_performed = ? " +
                    "ORDER BY h.action_date DESC";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, actionType);
            ResultSet rs = pstmt.executeQuery();
            List<IssueHistory> historyList = new ArrayList<>();
            
            while (rs.next()) {
                IssueHistory history = mapResultSetToHistory(rs);
                historyList.add(history);
            }
            
            return historyList;
            
        } catch (SQLException e) {
            throw new IssueTrackerException("Error retrieving history by action type: " + e.getMessage());
        }
    }
    
    /**
     * Helper method to map ResultSet to IssueHistory object
     */
    private IssueHistory mapResultSetToHistory(ResultSet rs) throws SQLException {
        IssueHistory history = new IssueHistory();
        history.setHistoryId(rs.getInt("history_id"));
        history.setIssueId(rs.getString("issue_id"));
        history.setUserId(rs.getInt("user_id"));
        history.setActionPerformed(rs.getString("action_performed"));
        
        Timestamp actionDate = rs.getTimestamp("action_date");
        if (actionDate != null) {
            history.setActionDate(actionDate.toLocalDateTime());
        }
        
        history.setComments(rs.getString("comments"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            history.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        return history;
    }
}