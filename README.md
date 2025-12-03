# Issue Tracker

Java-based issue tracking system for managing organizational issues efficiently.

## Features

- **Issue Management**: Create, update, and track issues
- **User System**: User registration and authentication
- **Status Tracking**: Monitor issue progress (OPEN, IN_PROGRESS, RESOLVED, CLOSED)
- **Assignment**: Assign issues to team members
- **Reporting**: Generate issue reports with filters
- **Database Integration**: MySQL database with JDBC

## Technology Stack

- **Language**: Java 11+
- **Database**: MySQL 8.0+
- **Build Tool**: Maven
- **Logging**: Apache Log4j2
- **Testing**: JUnit 5

## Project Structure

```
src/
├── main/java/com/issuetracker/
│   ├── dao/              # Database Access Objects
│   ├── model/            # Domain Models (Issue, User, etc.)
│   ├── service/          # Business Logic
│   ├── userinterface/    # CLI Interface
│   └── validator/        # Input Validation
└── resources/
    ├── database.properties.template
    ├── configuration.properties
    └── log4j2.properties
```

## Quick Setup

### 1. Prerequisites
- JDK 11+
- Maven 3.6+
- MySQL 8.0+

### 2. Clone Repository
```bash
git clone https://github.com/Ankitkumar341/Issue-Tracker.git
cd Issue-Tracker
```

### 3. Database Setup
```sql
CREATE DATABASE issuetrackersystem;
```

```powershell
# Run migration
Get-Content sql\schema.sql | mysql -u YOUR_USERNAME -p
```

### 4. Configure Database
```bash
# Copy template and add your credentials
cp src/main/resources/database.properties.template src/main/resources/database.properties
```

Update `database.properties`:
```properties
db.url=jdbc:mysql://localhost:3306/issuetrackersystem
db.username=YOUR_USERNAME
db.password=YOUR_PASSWORD
```

### 5. Build & Run
```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="com.issuetracker.userinterface.IssueTester"
```

## Usage

### Basic Operations
1. **Register**: Create new user account
2. **Login**: Authenticate with email/password
3. **Report Issue**: Create new issue with description and unit
4. **Assign Issue**: Assign issues to team members
5. **Update Status**: Change issue status through workflow
6. **View Reports**: Filter and view issue reports

### Issue Workflow
```
OPEN → IN_PROGRESS → RESOLVED → CLOSED
```

### Units Available
- ADMINISTRATION
- CONSIGNMENT  
- PAYMENT
- SHIPMENT

## Database Schema

### Tables
- **users**: User accounts and authentication
- **issues**: Issue tracking and metadata
- **issue_history**: Audit trail for issue changes

### Key Relationships
- Users can report multiple issues
- Users can be assigned multiple issues
- Issues maintain history of all changes

## Testing

### Maven Commands
```bash
# Build project
mvn clean compile

# Run CLI application
mvn exec:java -Dexec.mainClass="com.issuetracker.userinterface.IssueTrackerCLI"

# Run feature tester
mvn exec:java -Dexec.mainClass="com.issuetracker.userinterface.CLIFeatureTester"

# Check for compilation errors
mvn clean test-compile
```

### STS (Spring Tool Suite) Setup

1. **Import Project**:
   - File → Import → Existing Maven Projects
   - Select the Issue-Tracker folder
   - STS will automatically configure JUnit 5

2. **Run Tests in STS**:
   - Right-click on `src/test/java` folder
   - Select "Run As" → "JUnit Test"
   - Or right-click specific test class: `IssueServiceTest.java`

3. **Run Applications**:
   - For CLI: Navigate to `com.issuetracker.userinterface.IssueTrackerCLI`
   - For Testing: Navigate to `com.issuetracker.userinterface.CLIFeatureTester`
   - Right-click → "Run As" → "Java Application"

### Available Applications
```
src/main/java/com/issuetracker/userinterface/
├── IssueTrackerCLI.java     # Interactive CLI application
├── CLIFeatureTester.java    # Automated feature testing
```

### Test Structure
```
src/test/java/com/issuetracker/test/
└── IssueServiceTest.java    # Business logic tests
```

## Configuration Files

### Security Note
- `database.properties` is gitignored for security
- Use `database.properties.template` as reference
- Never commit actual database credentials

## Troubleshooting

### Common Issues
- **Connection Failed**: Check MySQL server status
- **Access Denied**: Verify database credentials
- **Build Errors**: Ensure JDK 11+ installed

### Migration Issues
- Verify MySQL user has proper privileges
- Check database name matches configuration
- Ensure schema.sql contains valid enum values

## Documentation

- `JDBC_Database_Connection_Guide.md`: Database integration details
- `sql/MIGRATION.md`: Database setup instructions




