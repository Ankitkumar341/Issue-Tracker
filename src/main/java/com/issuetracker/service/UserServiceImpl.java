package com.issuetracker.service;

import com.issuetracker.dao.UserDAO;
import com.issuetracker.exception.IssueTrackerException;
import com.issuetracker.model.User;
import com.issuetracker.validator.Validator;
import java.util.List;

/**
 * Service implementation for User management operations
 */
public class UserServiceImpl implements UserService {
    
    private UserDAO userDAO;
    private Validator validator;
    
    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
        this.validator = new Validator();
    }
    
    @Override
    public int registerUser(User user) throws IssueTrackerException {
        // Validate user input
        validateUserInput(user);
        
        // Check if email already exists
        if (isEmailExists(user.getEmail())) {
            throw new IssueTrackerException("User with email '" + user.getEmail() + "' already exists");
        }
        
        // Check if username already exists
        if (isUsernameExists(user.getUsername())) {
            throw new IssueTrackerException("User with username '" + user.getUsername() + "' already exists");
        }
        
        // Save user
        try {
            return userDAO.saveUser(user);
        } catch (Exception e) {
            throw new IssueTrackerException("Failed to register user: " + e.getMessage());
        }
    }
    
    @Override
    public boolean validateCredentials(String email, String password) throws IssueTrackerException {
        if (email == null || email.trim().isEmpty()) {
            throw new IssueTrackerException("Email cannot be null or empty");
        }
        
        if (password == null || password.trim().isEmpty()) {
            throw new IssueTrackerException("Password cannot be null or empty");
        }
        
        try {
            return userDAO.validateCredentials(email.trim(), password);
        } catch (Exception e) {
            throw new IssueTrackerException("Failed to validate credentials: " + e.getMessage());
        }
    }
    
    @Override
    public User findUserByEmail(String email) throws IssueTrackerException {
        if (email == null || email.trim().isEmpty()) {
            throw new IssueTrackerException("Email cannot be null or empty");
        }
        
        try {
            return userDAO.findByEmail(email.trim());
        } catch (Exception e) {
            throw new IssueTrackerException("Failed to find user by email: " + e.getMessage());
        }
    }
    
    @Override
    public User findUserById(int userId) throws IssueTrackerException {
        if (userId <= 0) {
            throw new IssueTrackerException("User ID must be positive");
        }
        
        try {
            return userDAO.findById(userId);
        } catch (Exception e) {
            throw new IssueTrackerException("Failed to find user by ID: " + e.getMessage());
        }
    }
    
    @Override
    public List<User> getAllUsers() throws IssueTrackerException {
        try {
            return userDAO.findAll();
        } catch (Exception e) {
            throw new IssueTrackerException("Failed to retrieve all users: " + e.getMessage());
        }
    }
    
    @Override
    public boolean updateUser(User user) throws IssueTrackerException {
        if (user == null) {
            throw new IssueTrackerException("User cannot be null");
        }
        
        if (user.getUserId() <= 0) {
            throw new IssueTrackerException("User ID must be positive");
        }
        
        // Validate user input
        validateUserInputForUpdate(user);
        
        try {
            return userDAO.updateUser(user);
        } catch (Exception e) {
            throw new IssueTrackerException("Failed to update user: " + e.getMessage());
        }
    }
    
    @Override
    public boolean deleteUser(int userId) throws IssueTrackerException {
        if (userId <= 0) {
            throw new IssueTrackerException("User ID must be positive");
        }
        
        try {
            return userDAO.deleteUser(userId);
        } catch (Exception e) {
            throw new IssueTrackerException("Failed to delete user: " + e.getMessage());
        }
    }
    
    @Override
    public boolean isEmailExists(String email) throws IssueTrackerException {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        
        try {
            User user = userDAO.findByEmail(email.trim());
            return user != null;
        } catch (Exception e) {
            throw new IssueTrackerException("Failed to check email existence: " + e.getMessage());
        }
    }
    
    @Override
    public boolean isUsernameExists(String username) throws IssueTrackerException {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        
        try {
            User user = userDAO.findByUsername(username.trim());
            return user != null;
        } catch (Exception e) {
            throw new IssueTrackerException("Failed to check username existence: " + e.getMessage());
        }
    }
    
    /**
     * Validate user input for registration
     */
    private void validateUserInput(User user) throws IssueTrackerException {
        if (user == null) {
            throw new IssueTrackerException("User cannot be null");
        }
        
        // Validate email
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IssueTrackerException("Email cannot be null or empty");
        }
        if (!validator.isValidEmail(user.getEmail().trim())) {
            throw new IssueTrackerException("Invalid email format");
        }
        
        // Validate username
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new IssueTrackerException("Username cannot be null or empty");
        }
        if (user.getUsername().trim().length() < 3) {
            throw new IssueTrackerException("Username must be at least 3 characters long");
        }
        if (user.getUsername().trim().length() > 50) {
            throw new IssueTrackerException("Username cannot exceed 50 characters");
        }
        
        // Validate password
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new IssueTrackerException("Password cannot be null or empty");
        }
        if (user.getPassword().trim().length() < 6) {
            throw new IssueTrackerException("Password must be at least 6 characters long");
        }
        
        // Validate full name
        if (user.getFullName() == null || user.getFullName().trim().isEmpty()) {
            throw new IssueTrackerException("Full name cannot be null or empty");
        }
        if (user.getFullName().trim().length() > 100) {
            throw new IssueTrackerException("Full name cannot exceed 100 characters");
        }
    }
    
    /**
     * Validate user input for update (less strict than registration)
     */
    private void validateUserInputForUpdate(User user) throws IssueTrackerException {
        // Validate email if provided
        if (user.getEmail() != null && !user.getEmail().trim().isEmpty()) {
            if (!validator.isValidEmail(user.getEmail().trim())) {
                throw new IssueTrackerException("Invalid email format");
            }
        }
        
        // Validate username if provided
        if (user.getUsername() != null && !user.getUsername().trim().isEmpty()) {
            if (user.getUsername().trim().length() < 3) {
                throw new IssueTrackerException("Username must be at least 3 characters long");
            }
            if (user.getUsername().trim().length() > 50) {
                throw new IssueTrackerException("Username cannot exceed 50 characters");
            }
        }
        
        // Validate password if provided
        if (user.getPassword() != null && !user.getPassword().trim().isEmpty()) {
            if (user.getPassword().trim().length() < 6) {
                throw new IssueTrackerException("Password must be at least 6 characters long");
            }
        }
        
        // Validate full name if provided
        if (user.getFullName() != null && !user.getFullName().trim().isEmpty()) {
            if (user.getFullName().trim().length() > 100) {
                throw new IssueTrackerException("Full name cannot exceed 100 characters");
            }
        }
    }
}