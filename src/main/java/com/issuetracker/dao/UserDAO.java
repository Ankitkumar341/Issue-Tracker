package com.issuetracker.dao;

import java.util.List;
import com.issuetracker.model.User;
import com.issuetracker.exception.IssueTrackerException;

public interface UserDAO {
    
    int saveUser(User user) throws IssueTrackerException;
    
    User findById(int userId) throws IssueTrackerException;
    
    User findByEmail(String email) throws IssueTrackerException;
    
    User findByUsername(String username) throws IssueTrackerException;
    
    List<User> getAllUsers() throws IssueTrackerException;
    
    List<User> findAll() throws IssueTrackerException;
    
    boolean updateUser(User user) throws IssueTrackerException;
    
    boolean deleteUser(int userId) throws IssueTrackerException;
    
    boolean validateCredentials(String email, String password) throws IssueTrackerException;
}