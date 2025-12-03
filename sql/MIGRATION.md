# Database Migration

## Quick Setup
```powershell
# Navigate to project root
cd "C:\Path\To\Issue-Tracker"

# Execute migration
Get-Content sql\schema.sql | mysql -u YOUR_USERNAME -p
```

## Verify Setup
```sql
USE issuetrackersystem;
SHOW TABLES;
```

Expected: `users`, `issues`, `issue_history` tables created with sample data.
