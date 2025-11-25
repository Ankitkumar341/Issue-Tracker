# Issue Tracker

A comprehensive Java-based issue tracking system designed to help organizations manage and track issues efficiently.

## 🚀 Features

- **Issue Management**: Create, update, and track issues with detailed descriptions
- **Assignee Management**: Automatic assignment of issues to appropriate team members
- **Status Tracking**: Monitor issue progress through different status stages
- **Validation System**: Comprehensive input validation for data integrity
- **Logging**: Detailed logging using Log4j2 for monitoring and debugging
- **Reporting**: Generate detailed issue reports and analytics
- **Exception Handling**: Robust error handling with custom exceptions

## 🏗️ Project Structure

```
IssueTracker/
├── src/
│   ├── main/
│   │   ├── java/com/issuetracker/
│   │   │   ├── dao/               # Data Access Objects
│   │   │   ├── exception/         # Custom Exception Classes
│   │   │   ├── model/             # Domain Models
│   │   │   │   ├── Issue.java
│   │   │   │   ├── Assignee.java
│   │   │   │   ├── IssueReport.java
│   │   │   │   ├── IssueStatus.java
│   │   │   │   └── Unit.java
│   │   │   ├── service/           # Business Logic Layer
│   │   │   │   ├── IssueService.java
│   │   │   │   ├── IssueServiceImpl.java
│   │   │   │   ├── AssigneeService.java
│   │   │   │   └── AssigneeServiceImpl.java
│   │   │   ├── userinterface/     # User Interface Components
│   │   │   └── validator/         # Input Validation
│   │   └── resources/
│   │       ├── configuration.properties
│   │       └── log4j2.properties
│   └── test/                      # Comprehensive Test Suite
├── verification/                  # Code Quality & Verification
├── pom.xml                       # Maven Configuration
├── DetailedReport.json           # Detailed Analysis Report
└── OverallReport.json            # Summary Report
```

## 🛠️ Technology Stack

- **Language**: Java 11
- **Build Tool**: Maven
- **Logging**: Apache Log4j2 (v2.17.1)
- **Testing**: JUnit 5 Jupiter (v5.6.2)
- **Utilities**: Apache Commons (Collections, Configuration, Lang3, Text)
- **Code Quality**: PMD, Custom Verification Framework

## 📋 Prerequisites

Before running this application, ensure you have:

- Java Development Kit (JDK) 11 or higher
- Apache Maven 3.6+ for dependency management
- Git for version control

## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/Ankitkumar341/Issue-Tracker.git
cd Issue-Tracker
```

### 2. Build the Project

```bash
mvn clean compile
```

### 3. Run Tests

```bash
mvn test
```

### 4. Generate Reports

```bash
mvn surefire-report:report
```

## 🧪 Testing

The project includes a comprehensive testing framework with:

- **Structural Verification**: Tests for class structure and design
- **Logical Verification**: Business logic validation tests
- **Code Analysis**: PMD-based code quality checks

### Test Categories

- **Unit Tests**: Individual component testing
- **Integration Tests**: Service layer integration testing
- **Verification Tests**: Custom verification framework for code quality

Run specific test categories:

```bash
# Run all tests
mvn test

# Run only structural verification tests
mvn test -Dtest="*StructuralVerification"

# Run only logical verification tests
mvn test -Dtest="*LogicalVerification"
```

## 📊 Code Quality & Reports

The project maintains high code quality standards with:

- **PMD Analysis**: Static code analysis for best practices
- **Custom Verification**: Specialized verification for project requirements
- **Test Coverage Reports**: Generated in `target/surefire-reports/`

### Generated Reports

- `DetailedReport.json`: Comprehensive analysis of all components
- `OverallReport.json`: Summary of project metrics
- `target/surefire-reports/`: Test execution reports

## 🔧 Configuration

### Application Configuration

Configure the application using `src/main/resources/configuration.properties`:

```properties
# Add your application-specific configurations here
```

### Logging Configuration

Logging is configured via `src/main/resources/log4j2.properties`. The current setup provides:

- Console and file logging
- Different log levels for different components
- Structured log format for easy parsing

## 🚦 Usage Example

```java
// Create an issue
Issue issue = new Issue(
    "ISS-001",
    "Application crashes on startup",
    Unit.TECHNICAL,
    LocalDate.now(),
    LocalDate.now(),
    "developer@company.com",
    IssueStatus.OPEN
);

// Report the issue through service
IssueService issueService = new IssueServiceImpl();
issueService.reportIssue(issue);

// Generate reports
IssueReport report = issueService.generateIssueReport();
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Development Guidelines

- Follow Java coding conventions
- Write comprehensive tests for new features
- Update documentation for any API changes
- Ensure all verification tests pass before submitting PR



