package com.issuetracker.service;

import com.issuetracker.exception.IssueTrackerException;
import com.issuetracker.model.User;
import java.util.List;

/**
 * Service interface for User management operations
 */
public interface UserService {
    
    /**
     * Register a new user in the system
     * @param user User object containing user details
     * @return User ID of the newly created user
     * @throws IssueTrackerException if user creation fails
     */
    int registerUser(User user) throws IssueTrackerException;
    
    /**
     * Validate user credentials for authentication
     * @param email User email
     * @param password User password
     * @return true if credentials are valid, false otherwise
     * @throws IssueTrackerException if validation fails
     */
    boolean validateCredentials(String email, String password) throws IssueTrackerException;
    
    /**
     * Find user by email address
     * @param email User email
     * @return User object if found, null otherwise
     * @throws IssueTrackerException if operation fails
     */
    User findUserByEmail(String email) throws IssueTrackerException;
    
    /**
     * Find user by ID
     * @param userId User ID
     * @return User object if found, null otherwise
     * @throws IssueTrackerException if operation fails
     */
    User findUserById(int userId) throws IssueTrackerException;
    
    /**
     * Get all users in the system
     * @return List of all users
     * @throws IssueTrackerException if operation fails
     */
    List<User> getAllUsers() throws IssueTrackerException;
    
    /**
     * Update user information
     * @param user User object with updated information
     * @return true if update successful, false otherwise
     * @throws IssueTrackerException if update fails
     */
    boolean updateUser(User user) throws IssueTrackerException;
    
    /**
     * Delete user from the system
     * @param userId User ID to delete
     * @return true if deletion successful, false otherwise
     * @throws IssueTrackerException if deletion fails
     */
    boolean deleteUser(int userId) throws IssueTrackerException;
    
    /**
     * Check if email already exists in the system
     * @param email Email to check
     * @return true if email exists, false otherwise
     * @throws IssueTrackerException if operation fails
     */
    boolean isEmailExists(String email) throws IssueTrackerException;
    
    /**
     * Check if username already exists in the system
     * @param username Username to check
     * @return true if username exists, false otherwise
     * @throws IssueTrackerException if operation fails
     */
    boolean isUsernameExists(String username) throws IssueTrackerException;
}