
CREATE DATABASE issuetrackersystem CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE issuetrackersystem;

CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    role ENUM('USER', 'ADMIN', 'MANAGER') DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    

    INDEX idx_users_username (username),
    INDEX idx_users_email (email),
    INDEX idx_users_active (is_active),
    INDEX idx_users_role (role)
) ENGINE=InnoDB COMMENT='User accounts for authentication and assignment';

CREATE TABLE issues (
    issue_id VARCHAR(50) PRIMARY KEY,
    description TEXT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'OPEN',
    priority VARCHAR(10) NOT NULL DEFAULT 'MEDIUM',
    assigned_to_user_id INT NULL,
    reported_by_user_id INT NOT NULL,
    reporting_date DATE NOT NULL,
    due_date DATE NULL,
    resolved_date DATE NULL,
    updated_on DATE NULL,
    unit VARCHAR(50) NOT NULL,
    category VARCHAR(50) DEFAULT 'GENERAL',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    CONSTRAINT chk_status CHECK (status IN ('OPEN', 'IN_PROGRESS', 'RESOLVED', 'CLOSED', 'REOPENED')),
    CONSTRAINT chk_priority CHECK (priority IN ('LOW', 'MEDIUM', 'HIGH', 'CRITICAL')),
    
    INDEX idx_issues_status (status),
    INDEX idx_issues_priority (priority),
    INDEX idx_issues_assigned_to_user_id (assigned_to_user_id),
    INDEX idx_issues_reported_by_user_id (reported_by_user_id),
    INDEX idx_issues_reporting_date (reporting_date),
    INDEX idx_issues_unit (unit),
    INDEX idx_issues_category (category),
    INDEX idx_issues_due_date (due_date),
    INDEX idx_issues_resolved_date (resolved_date),
    
    
    INDEX idx_issues_status_priority (status, priority),
    INDEX idx_issues_assignee_status (assigned_to_user_id, status),
    INDEX idx_issues_reporter_date (reported_by_user_id, reporting_date),
    INDEX idx_issues_unit_status (unit, status)
) ENGINE=InnoDB COMMENT='Main issues table with full audit trail support';

CREATE TABLE issue_history (
    history_id INT AUTO_INCREMENT PRIMARY KEY,
    issue_id VARCHAR(50) NOT NULL,
    user_id INT NOT NULL,
    action_performed VARCHAR(100) NOT NULL,
    old_value TEXT NULL,
    new_value TEXT NULL,
    action_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    comments TEXT NULL,
    
    -- Performance indexes for audit queries
    INDEX idx_history_issue_id (issue_id),
    INDEX idx_history_user_id (user_id),
    INDEX idx_history_action_date (action_date),
    INDEX idx_history_action_performed (action_performed),
    INDEX idx_history_issue_date (issue_id, action_date)
) ENGINE=InnoDB COMMENT='Complete audit trail for issue changes';

-- adding for Store detailed comments and discussions on issues

CREATE TABLE issue_comments (
    comment_id INT AUTO_INCREMENT PRIMARY KEY,
    issue_id VARCHAR(50) NOT NULL,
    user_id INT NOT NULL,
    comment_text TEXT NOT NULL,
    is_internal BOOLEAN DEFAULT FALSE,
    parent_comment_id INT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    -- Indexes for comment retrieval
    INDEX idx_comments_issue_id (issue_id),
    INDEX idx_comments_user_id (user_id),
    INDEX idx_comments_created_at (created_at),
    INDEX idx_comments_parent (parent_comment_id)
) ENGINE=InnoDB COMMENT='Issue comments and discussions';


-- for  the  File attachments for issues (future enhancement)

CREATE TABLE issue_attachments (
    attachment_id INT AUTO_INCREMENT PRIMARY KEY,
    issue_id VARCHAR(50) NOT NULL,
    user_id INT NOT NULL,
    filename VARCHAR(255) NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    file_size BIGINT NOT NULL,
    mime_type VARCHAR(100) NOT NULL,
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_attachments_issue_id (issue_id),
    INDEX idx_attachments_user_id (user_id)
) ENGINE=InnoDB COMMENT='File attachments for issues';


--  for Track user login sessions for security audit
CREATE TABLE user_sessions (
    session_id VARCHAR(64) PRIMARY KEY,
    user_id INT NOT NULL,
    login_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_activity TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    ip_address VARCHAR(45),
    user_agent TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    
    INDEX idx_sessions_user_id (user_id),
    INDEX idx_sessions_active (is_active)
) ENGINE=InnoDB COMMENT='User session tracking';


CREATE TABLE system_settings (
    setting_id INT AUTO_INCREMENT PRIMARY KEY,
    setting_key VARCHAR(100) NOT NULL UNIQUE,
    setting_value TEXT NOT NULL,
    description TEXT,
    is_system_setting BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_settings_key (setting_key)
) ENGINE=InnoDB COMMENT='System configuration settings';


INSERT INTO users (username, email, password, full_name, role) VALUES
('admin', 'admin@monttrance.com', 'admin123', 'System Administrator', 'ADMIN'),
('john_doe', 'john.doe@monttrance.com', 'password123', 'John Doe', 'USER'),
('jane_smith', 'jane.smith@monttrance.com', 'password123', 'Jane Smith', 'MANAGER'),
('mike_wilson', 'mike.wilson@monttrance.com', 'password123', 'Mike Wilson', 'USER'),
('sarah_johnson', 'sarah.johnson@monttrance.com', 'password123', 'Sarah Johnson', 'USER'),
('alex_brown', 'alex.brown@monttrance.com', 'password123', 'Alex Brown', 'MANAGER'),
('lisa_garcia', 'lisa.garcia@monttrance.com', 'password123', 'Lisa Garcia', 'USER'),
('david_lee', 'david.lee@monttrance.com', 'password123', 'David Lee', 'USER'),
('emma_davis', 'emma.davis@monttrance.com', 'password123', 'Emma Davis', 'USER'),
('ryan_miller', 'ryan.miller@monttrance.com', 'password123', 'Ryan Miller', 'USER');

INSERT INTO issues (issue_id, description, status, priority, unit, category, reported_by_user_id, assigned_to_user_id, reporting_date, due_date) VALUES
('MTI-I-001-ADM', 'Login system not working properly - users unable to authenticate', 'OPEN', 'HIGH', 'ADMINISTRATION', 'Bug', 2, 3, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 3 DAY)),
('MTI-I-002-ADM', 'User management interface improvements needed', 'IN_PROGRESS', 'MEDIUM', 'ADMINISTRATION', 'Enhancement', 3, 4, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 7 DAY)),
('MTI-I-003-CSG', 'Inventory tracking shows discrepancies', 'OPEN', 'HIGH', 'CONSIGNMENT', 'Bug', 4, NULL, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 2 DAY)),
('MTI-I-004-PAY', 'Financial report generation very slow', 'RESOLVED', 'MEDIUM', 'PAYMENT', 'Performance', 5, 6, DATE_SUB(CURDATE(), INTERVAL 10 DAY), DATE_SUB(CURDATE(), INTERVAL 3 DAY)),
('MTI-I-005-ADM', 'Employee onboarding process automation', 'OPEN', 'LOW', 'ADMINISTRATION', 'Feature Request', 6, 7, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 14 DAY)),
('MTI-I-006-PAY', 'Payment processing timeout issues', 'OPEN', 'CRITICAL', 'PAYMENT', 'Bug', 7, 8, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 1 DAY)),
('MTI-I-007-SHP', 'Shipping label printer not responding', 'IN_PROGRESS', 'HIGH', 'SHIPMENT', 'Hardware', 8, 9, DATE_SUB(CURDATE(), INTERVAL 1 DAY), CURDATE()),
('MTI-I-008-ADM', 'Security audit findings implementation', 'OPEN', 'HIGH', 'ADMINISTRATION', 'Security', 9, 10, DATE_SUB(CURDATE(), INTERVAL 2 DAY), DATE_ADD(CURDATE(), INTERVAL 5 DAY)),
('MTI-I-009-CSG', 'Course completion tracking inaccurate', 'CLOSED', 'MEDIUM', 'CONSIGNMENT', 'Bug', 10, 2, DATE_SUB(CURDATE(), INTERVAL 20 DAY), DATE_SUB(CURDATE(), INTERVAL 5 DAY)),
('MTI-I-010-CSG', 'Barcode scanner integration needed', 'OPEN', 'MEDIUM', 'CONSIGNMENT', 'Feature Request', 2, NULL, DATE_SUB(CURDATE(), INTERVAL 3 DAY), DATE_ADD(CURDATE(), INTERVAL 10 DAY));

INSERT INTO issue_history (issue_id, user_id, action_performed, old_value, new_value, comments) VALUES
('MTI-I-001-ADM', 2, 'ISSUE_CREATED', NULL, 'OPEN', 'Login authentication system failing for multiple users'),
('MTI-I-001-ADM', 1, 'ISSUE_ASSIGNED', NULL, '3', 'Assigned to Jane Smith for investigation'),
('MTI-I-002-ADM', 3, 'ISSUE_CREATED', NULL, 'OPEN', 'UI improvements requested by users'),
('MTI-I-002-ADM', 3, 'STATUS_CHANGED', 'OPEN', 'IN_PROGRESS', 'Started working on user interface enhancements'),
('MTI-I-002-ADM', 3, 'ISSUE_ASSIGNED', NULL, '4', 'Assigned to Mike Wilson for development'),
('MTI-I-003-CSG', 4, 'ISSUE_CREATED', NULL, 'OPEN', 'Inventory count not matching system records'),
('MTI-I-004-PAY', 5, 'ISSUE_CREATED', NULL, 'OPEN', 'Report generation taking over 30 minutes'),
('MTI-I-004-PAY', 1, 'ISSUE_ASSIGNED', NULL, '6', 'Assigned to Alex Brown for performance optimization'),
('MTI-I-004-PAY', 6, 'STATUS_CHANGED', 'OPEN', 'IN_PROGRESS', 'Investigating database query performance'),
('MTI-I-004-PAY', 6, 'STATUS_CHANGED', 'IN_PROGRESS', 'RESOLVED', 'Optimized SQL queries and added indexes'),
('MTI-I-005-ADM', 6, 'ISSUE_CREATED', NULL, 'OPEN', 'Manual onboarding process needs automation'),
('MTI-I-005-ADM', 1, 'ISSUE_ASSIGNED', NULL, '7', 'Assigned to Lisa Garcia for automation development'),
('MTI-I-006-PAY', 7, 'ISSUE_CREATED', NULL, 'OPEN', 'Payment processing timeout after 30 seconds'),
('MTI-I-006-PAY', 1, 'ISSUE_ASSIGNED', NULL, '8', 'CRITICAL: Assigned to David Lee immediately'),
('MTI-I-007-SHP', 8, 'ISSUE_CREATED', NULL, 'OPEN', 'Zebra printer not responding to print commands'),
('MTI-I-007-SHP', 1, 'ISSUE_ASSIGNED', NULL, '9', 'Assigned to Emma Davis for hardware troubleshooting'),
('MTI-I-007-SHP', 9, 'STATUS_CHANGED', 'OPEN', 'IN_PROGRESS', 'Checking printer drivers and connections'),
('MTI-I-008-ADM', 9, 'ISSUE_CREATED', NULL, 'OPEN', 'Security audit found vulnerabilities to address'),
('MTI-I-008-ADM', 1, 'ISSUE_ASSIGNED', NULL, '10', 'Assigned to Ryan Miller for security implementation'),
('MTI-I-009-CSG', 10, 'ISSUE_CREATED', NULL, 'OPEN', 'Course completion percentages not calculating correctly'),
('MTI-I-009-CSG', 1, 'ISSUE_ASSIGNED', NULL, '2', 'Assigned to John Doe for learning system fix'),
('MTI-I-009-CSG', 2, 'STATUS_CHANGED', 'OPEN', 'IN_PROGRESS', 'Investigating calculation logic'),
('MTI-I-009-CSG', 2, 'STATUS_CHANGED', 'IN_PROGRESS', 'RESOLVED', 'Fixed calculation algorithm'),
('MTI-I-009-CSG', 10, 'STATUS_CHANGED', 'RESOLVED', 'CLOSED', 'Verified fix in production'),
('MTI-I-010-CSG', 2, 'ISSUE_CREATED', NULL, 'OPEN', 'Need barcode scanner integration for inventory management');

INSERT INTO issue_comments (issue_id, user_id, comment_text, is_internal) VALUES
('MTI-I-001-ADM', 2, 'Users are reporting login failures since yesterday evening. Affects approximately 15% of users.', FALSE),
('MTI-I-001-ADM', 3, 'Investigating authentication service logs. Found unusual error patterns.', TRUE),
('MTI-I-001-ADM', 1, 'Priority escalated due to business impact. Please provide hourly updates.', TRUE),
('MTI-I-002-ADM', 3, 'Users requesting improved search functionality and bulk operations.', FALSE),
('MTI-I-002-ADM', 4, 'Started implementing enhanced search filters and pagination.', FALSE),
('MTI-I-003-CSG', 4, 'Physical count shows 50 items but system shows 47. Need to investigate.', FALSE),
('MTI-I-004-PAY', 5, 'Monthly reports taking 35+ minutes. Users timing out.', FALSE),
('MTI-I-004-PAY', 6, 'Database queries optimized. Report generation now under 2 minutes.', FALSE),
('MTI-I-006-PAY', 7, 'Critical issue - customers unable to complete purchases.', FALSE),
('MTI-I-006-PAY', 8, 'Investigating payment gateway timeout settings.', TRUE);

-- Insert system settings
INSERT INTO system_settings (setting_key, setting_value, description, is_system_setting) VALUES
('max_file_upload_size', '10485760', 'Maximum file upload size in bytes (10MB)', TRUE),
('session_timeout', '3600', 'User session timeout in seconds (1 hour)', TRUE),
('email_notifications_enabled', 'true', 'Enable email notifications for issue updates', FALSE),
('auto_assign_issues', 'false', 'Automatically assign issues to available users', FALSE),
('issue_id_prefix', 'MTI-I', 'Prefix for auto-generated issue IDs', TRUE),
('default_priority', 'MEDIUM', 'Default priority for new issues', FALSE),
('cleanup_resolved_days', '90', 'Days to keep resolved issues before archival', TRUE),
('max_issues_per_user', '20', 'Maximum active issues per user', FALSE),
('require_due_date', 'false', 'Require due date when creating issues', FALSE),
('enable_issue_comments', 'true', 'Enable commenting system for issues', FALSE);


-- View for active issues with complete user details
CREATE VIEW v_active_issues AS
SELECT 
    i.issue_id,
    i.description,
    i.status,
    i.priority,
    i.unit,
    i.category,
    i.reporting_date,
    i.due_date,
    i.resolved_date,
    u_reporter.user_id AS reporter_id,
    u_reporter.full_name AS reporter_name,
    u_reporter.email AS reporter_email,
    u_assignee.user_id AS assignee_id,
    u_assignee.full_name AS assignee_name,
    u_assignee.email AS assignee_email,
    i.created_at,
    i.updated_at,
    DATEDIFF(COALESCE(i.due_date, CURDATE()), CURDATE()) AS days_until_due
FROM issues i
    LEFT JOIN users u_reporter ON i.reported_by_user_id = u_reporter.user_id
    LEFT JOIN users u_assignee ON i.assigned_to_user_id = u_assignee.user_id
WHERE i.status NOT IN ('RESOLVED', 'CLOSED');

--  for the View for issue statistics and KPIs
CREATE VIEW v_issue_statistics AS
SELECT 
    COUNT(*) AS total_issues,
    COUNT(CASE WHEN status = 'OPEN' THEN 1 END) AS open_issues,
    COUNT(CASE WHEN status = 'IN_PROGRESS' THEN 1 END) AS in_progress_issues,
    COUNT(CASE WHEN status = 'RESOLVED' THEN 1 END) AS resolved_issues,
    COUNT(CASE WHEN status = 'CLOSED' THEN 1 END) AS closed_issues,
    COUNT(CASE WHEN priority = 'CRITICAL' THEN 1 END) AS critical_issues,
    COUNT(CASE WHEN priority = 'HIGH' THEN 1 END) AS high_priority_issues,
    COUNT(CASE WHEN assigned_to_user_id IS NULL THEN 1 END) AS unassigned_issues,
    COUNT(CASE WHEN due_date < CURDATE() AND status NOT IN ('RESOLVED', 'CLOSED') THEN 1 END) AS overdue_issues
FROM issues;

-- View for user workload analysis
CREATE VIEW v_user_workload AS
SELECT 
    u.user_id,
    u.username,
    u.full_name,
    u.email,
    u.role,
    COUNT(i.issue_id) AS total_assigned_issues,
    COUNT(CASE WHEN i.status = 'OPEN' THEN 1 END) AS open_assignments,
    COUNT(CASE WHEN i.status = 'IN_PROGRESS' THEN 1 END) AS in_progress_assignments,
    COUNT(CASE WHEN i.priority = 'CRITICAL' THEN 1 END) AS critical_assignments,
    COUNT(CASE WHEN i.due_date < CURDATE() AND i.status NOT IN ('RESOLVED', 'CLOSED') THEN 1 END) AS overdue_assignments
FROM users u
    LEFT JOIN issues i ON u.user_id = i.assigned_to_user_id AND i.status NOT IN ('RESOLVED', 'CLOSED')
WHERE u.is_active = TRUE
GROUP BY u.user_id, u.username, u.full_name, u.email, u.role
ORDER BY total_assigned_issues DESC;

===========================================================

DELIMITER //

--for the  safely assign issue to user with validation
CREATE PROCEDURE sp_assign_issue(
    IN p_issue_id VARCHAR(50),
    IN p_user_id INT,
    IN p_assigned_by INT,
    IN p_comments TEXT
)
BEGIN
    DECLARE v_current_assignee INT DEFAULT NULL;
    DECLARE v_issue_exists INT DEFAULT 0;
    DECLARE v_user_exists INT DEFAULT 0;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    START TRANSACTION;
    
    -- Validate issue exists
    SELECT COUNT(*), assigned_to_user_id INTO v_issue_exists, v_current_assignee
    FROM issues WHERE issue_id = p_issue_id;
    
    IF v_issue_exists = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Issue not found';
    END IF;
    
    -- Validate user exists
    SELECT COUNT(*) INTO v_user_exists FROM users WHERE user_id = p_user_id AND is_active = TRUE;
    
    IF v_user_exists = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'User not found or inactive';
    END IF;
    
    -- Update issue assignment
    UPDATE issues 
    SET assigned_to_user_id = p_user_id,
        updated_at = CURRENT_TIMESTAMP
    WHERE issue_id = p_issue_id;
    
    -- Record in history
    INSERT INTO issue_history (issue_id, user_id, action_performed, old_value, new_value, comments)
    VALUES (p_issue_id, p_assigned_by, 'ISSUE_ASSIGNED', v_current_assignee, p_user_id, 
            CONCAT('Assigned to user ID: ', p_user_id, '. ', COALESCE(p_comments, '')));
    
    COMMIT;
END //

-- Procedure to update issue status with validation and history
CREATE PROCEDURE sp_update_issue_status(
    IN p_issue_id VARCHAR(50),
    IN p_new_status VARCHAR(20),
    IN p_user_id INT,
    IN p_comments TEXT
)
BEGIN
    DECLARE v_old_status VARCHAR(20);
    DECLARE v_issue_exists INT DEFAULT 0;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    START TRANSACTION;
    
    -- Get current status and validate issue exists
    SELECT status INTO v_old_status FROM issues WHERE issue_id = p_issue_id;
    
    IF v_old_status IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Issue not found';
    END IF;
    
    -- Validate status transition (basic validation)
    IF v_old_status = p_new_status THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'No status change detected';
    END IF;
    
    -- Update issue status
    UPDATE issues 
    SET status = p_new_status,
        updated_at = CURRENT_TIMESTAMP,
        resolved_date = CASE 
            WHEN p_new_status = 'RESOLVED' AND v_old_status != 'RESOLVED' THEN CURDATE() 
            WHEN p_new_status != 'RESOLVED' THEN NULL
            ELSE resolved_date 
        END,
        updated_on = CURDATE()
    WHERE issue_id = p_issue_id;
    
    -- Record in history
    INSERT INTO issue_history (issue_id, user_id, action_performed, old_value, new_value, comments)
    VALUES (p_issue_id, p_user_id, 'STATUS_CHANGED', v_old_status, p_new_status, p_comments);
    
    COMMIT;
END //

DELIMITER ;
======================================================================

-- Additional composite indexes for complex queries
CREATE INDEX idx_issues_status_assignee_priority ON issues(status, assigned_to_user_id, priority);
CREATE INDEX idx_issues_unit_priority_date ON issues(unit, priority, reporting_date);
CREATE INDEX idx_issues_due_date_status ON issues(due_date, status);
CREATE INDEX idx_history_user_action_date ON issue_history(user_id, action_performed, action_date);


-- Display schema creation summary
SELECT 'Issue Tracker Database Schema Created Successfully!' AS status,
       (SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'issuetrackersystem') AS total_tables,
       (SELECT COUNT(*) FROM information_schema.views WHERE table_schema = 'issuetrackersystem') AS total_views,
       (SELECT COUNT(*) FROM information_schema.routines WHERE routine_schema = 'issuetrackersystem') AS total_procedures;

-- Display sample data counts
SELECT 'Sample Data Loaded' AS info,
       (SELECT COUNT(*) FROM users) AS users_count,
       (SELECT COUNT(*) FROM issues) AS issues_count,
       (SELECT COUNT(*) FROM issue_history) AS history_count,
       (SELECT COUNT(*) FROM issue_comments) AS comments_count;

-- Show active issues summary
SELECT 'Active Issues by Status' AS summary, status, COUNT(*) AS count
FROM issues 
WHERE status NOT IN ('RESOLVED', 'CLOSED')
GROUP BY status 
ORDER BY count DESC;
