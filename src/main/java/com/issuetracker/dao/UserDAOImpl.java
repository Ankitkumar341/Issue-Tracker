package com.issuetracker.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.issuetracker.model.User;
import com.issuetracker.util.DatabaseUtil;
import com.issuetracker.exception.IssueTrackerException;

public class UserDAOImpl implements UserDAO {
    
    @Override
    public int saveUser(User user) throws IssueTrackerException {
        String sql = "INSERT INTO users (username, email, password, full_name, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getFullName());
            pstmt.setTimestamp(5, Timestamp.valueOf(user.getCreatedAt()));
            pstmt.setTimestamp(6, Timestamp.valueOf(user.getUpdatedAt()));
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new IssueTrackerException("Failed to create user");
            }
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new IssueTrackerException("Failed to get user ID");
                }
            }
        } catch (SQLException e) {
            throw new IssueTrackerException("Database error while saving user: " + e.getMessage());
        }
    }
    
    @Override
    public User findById(int userId) throws IssueTrackerException {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new IssueTrackerException("Database error while finding user: " + e.getMessage());
        }
    }
    
    @Override
    public User findByEmail(String email) throws IssueTrackerException {
        String sql = "SELECT * FROM users WHERE email = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, email);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new IssueTrackerException("Database error while finding user by email: " + e.getMessage());
        }
    }
    
    @Override
    public User findByUsername(String username) throws IssueTrackerException {
        String sql = "SELECT * FROM users WHERE username = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new IssueTrackerException("Database error while finding user by username: " + e.getMessage());
        }
    }
    
    @Override
    public List<User> getAllUsers() throws IssueTrackerException {
        String sql = "SELECT * FROM users ORDER BY username";
        List<User> users = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
            return users;
        } catch (SQLException e) {
            throw new IssueTrackerException("Database error while getting all users: " + e.getMessage());
        }
    }
    
    @Override
    public List<User> findAll() throws IssueTrackerException {
        return getAllUsers();
    }
    
    @Override
    public boolean updateUser(User user) throws IssueTrackerException {
        String sql = "UPDATE users SET username = ?, email = ?, password = ?, full_name = ?, updated_at = ? WHERE user_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getFullName());
            pstmt.setTimestamp(5, Timestamp.valueOf(user.getUpdatedAt()));
            pstmt.setInt(6, user.getUserId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IssueTrackerException("Database error while updating user: " + e.getMessage());
        }
    }
    
    @Override
    public boolean deleteUser(int userId) throws IssueTrackerException {
        String sql = "DELETE FROM users WHERE user_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IssueTrackerException("Database error while deleting user: " + e.getMessage());
        }
    }
    
    @Override
    public boolean validateCredentials(String email, String password) throws IssueTrackerException {
        String sql = "SELECT password FROM users WHERE email = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, email);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String storedPassword = rs.getString("password");
                    return password.equals(storedPassword);
                }
                return false;
            }
        } catch (SQLException e) {
            throw new IssueTrackerException("Database error while validating credentials: " + e.getMessage());
        }
    }
    
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setFullName(rs.getString("full_name"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            user.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            user.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        return user;
    }
}