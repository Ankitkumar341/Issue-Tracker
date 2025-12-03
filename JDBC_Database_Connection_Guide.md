# JDBC Database Connection Guide

## Overview
Issue Tracker uses **JDBC** to connect to MySQL database with DAO pattern.

**Key Components:**
- Driver: MySQL Connector/J
- Database: MySQL 8.0+
- Pattern: DAO (Data Access Object)

## Architecture
```
CLI Interface → Service Layer → DAO Layer → JDBC → MySQL Database
```

## Database Configuration

### database.properties
```properties
db.driver=com.mysql.cj.jdbc.Driver
db.url=jdbc:mysql://localhost:3306/issuetrackersystem?useSSL=false&serverTimezone=UTC
db.username=YOUR_USERNAME
db.password=YOUR_PASSWORD
db.initialConnections=5
db.maxConnections=20
```

## Database Schema

### Tables Structure:
```
USERS                    ISSUES                   ISSUE_HISTORY
- user_id (PK)          - issue_id (PK)          - history_id (PK)
- username              - description            - issue_id (FK)
- email                 - unit                   - user_id (FK)
- password              - status                 - action_performed
- full_name             - user_id (FK)           - action_date
- created_at            - assignee_id            - comments
```

## Implementation Examples

### 1. Database Utility
```java
public class DatabaseUtil {
    public static Connection getConnection() throws SQLException {
        loadProperties();
        String url = properties.getProperty("db.url");
        String username = properties.getProperty("db.username"); 
        String password = properties.getProperty("db.password");
        
        return DriverManager.getConnection(url, username, password);
    }
}
```

### 2. DAO Implementation
```java
public class IssueDAOImpl implements IssueDAO {
    
    @Override
    public String saveIssue(Issue issue) throws IssueTrackerException {
        String sql = "INSERT INTO issues (issue_id, description, unit, status, user_id) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, issue.getIssueId());
            pstmt.setString(2, issue.getIssueDescription());
            pstmt.setString(3, issue.getUnit().toString());
            pstmt.setString(4, issue.getStatus().toString());
            pstmt.setInt(5, issue.getUserId());
            
            pstmt.executeUpdate();
            return issue.getIssueId();
        } catch (SQLException e) {
            throw new IssueTrackerException("Error saving issue: " + e.getMessage());
        }
    }
}
```

### 3. User Authentication
```java
public boolean validateCredentials(String email, String password) throws IssueTrackerException {
    String sql = "SELECT password FROM users WHERE email = ?";
    
    try (Connection conn = DatabaseUtil.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        pstmt.setString(1, email);
        ResultSet rs = pstmt.executeQuery();
        
        if (rs.next()) {
            return password.equals(rs.getString("password"));
        }
        return false;
    }
}
```

## Best Practices

### 1. Resource Management
```java
// Use try-with-resources for automatic cleanup
try (Connection conn = DatabaseUtil.getConnection();
     PreparedStatement pstmt = conn.prepareStatement(sql);
     ResultSet rs = pstmt.executeQuery()) {
    
    // Database operations
    
} catch (SQLException e) {
    throw new IssueTrackerException("Database error: " + e.getMessage());
}
```

### 2. SQL Injection Prevention
```java
//  NEVER do this:
String sql = "SELECT * FROM users WHERE email = '" + email + "'";

//  ALWAYS use PreparedStatement:
String sql = "SELECT * FROM users WHERE email = ?";
pstmt.setString(1, email);
```

### 3. Connection Pooling Benefits
- **Performance**: Reuses connections
- **Resource Management**: Limits concurrent connections
- **Scalability**: Handles multiple users



## Troubleshooting

**Common Issues:**
- **Connection Failed**: Check MySQL server status
- **Access Denied**: Verify username/password in properties
- **Driver Not Found**: Add MySQL Connector/J dependency
- **Too Many Connections**: Reduce maxConnections setting

## Migration Command
```powershell
Get-Content sql\schema.sql | mysql -u YOUR_USERNAME -p
```

