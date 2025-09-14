# Note0 - Academic Material Sharing Platform

A comprehensive Java Spring Boot application for sharing academic materials, notes, and resources among students.

## 🚀 Quick Start

The application has been completely converted from Node.js to Java Spring Boot. All old files have been removed and the new application is ready to use.

### Prerequisites
- Java 17+ (Download from: https://adoptium.net)
- Maven 3.6+ (Download from: https://maven.apache.org/download.cgi)
- MySQL 8.0+ (Download from: https://dev.mysql.com/downloads/installer/)

### Windows Setup Guide

1. **Install Prerequisites**:
   - Install Java: Run the installer and follow the prompts
   - Install Maven: 
     - Extract the downloaded archive to `C:\Program Files\Maven`
     - Add `C:\Program Files\Maven\bin` to your System PATH
   - Install PostgreSQL:
     - Run the installer and note down your password
     - Keep the default port (5432)
     - Launch pgAdmin 4 to verify installation

2. **Running the Application**:

   **For Windows:**
   ```powershell
   # Open PowerShell as Administrator
   cd note0
   
   # Run the Windows script
   .\run-app.ps1
   ```

   If you get a security error, run:
   ```powershell
   Set-ExecutionPolicy -ExecutionPolicy Bypass -Scope Process
   .\run-app.ps1
   ```

   **For Linux/macOS:**
   ```bash
   # Make the script executable
   chmod +x run-app.sh
   
   # Run the script
   ./run-app.sh
   ```

### Installation & Setup

1. **Navigate to the application directory:**
   ```bash
   cd note0-application
   ```

2. **Set up the database:**
   ```bash
   # Create PostgreSQL database
   sudo -u postgres psql
   CREATE DATABASE notes_app;
   \q
   
   # Run the schema
   psql -U postgres -d notes_app -f database/schema.sql
   ```

3. **Run the application:**

   **For Windows (PowerShell):**
   ```powershell
   .\run.ps1
   ```

   **For Linux/macOS:**
   ```bash
   chmod +x run.sh  # Make script executable (first time only)
   ./run.sh
   ```

   **Manual method (any platform):**
   ```bash
   # Build the application
   mvn clean install
   
   # Run the application
   mvn spring-boot:run
   ```

4. **Access the application:**
   - Web Interface: http://localhost:8080
   - API Base: http://localhost:8080/api
   
   The application typically takes a few seconds to start. Wait for the message "Started Note0Application" in the console.

## 📁 Project Structure

```
note0/
├── note0-application/          # Main Java Spring Boot application
│   ├── src/main/java/com/note0/
│   │   ├── controller/         # REST & Web controllers
│   │   ├── dto/               # Data Transfer Objects
│   │   ├── entity/            # JPA entities
│   │   ├── repository/        # Data repositories
│   │   ├── security/          # Security configuration
│   │   ├── service/           # Business logic
│   │   └── Note0Application.java
│   ├── src/main/resources/
│   │   ├── templates/         # Thymeleaf templates
│   │   ├── application.yml    # Configuration
│   │   └── data.sql          # Sample data
│   ├── database/schema.sql    # Database schema
│   ├── README.md             # Detailed documentation
│   ├── DEPLOYMENT.md         # Deployment guide
│   ├── test-api.http         # API testing
│   └── run.sh               # Startup script
├── CONVERSION_SUMMARY.md     # Conversion details
├── report.md                # Original analysis report
└── README.md               # This file
```

## 🔑 Default Credentials

- **Admin Email**: admin@note0.com
- **Admin Password**: admin123

## ✨ Features

- **User Authentication**: JWT-based authentication with role-based access control
- **Material Upload**: Upload and share academic materials with file validation
- **Rating System**: Rate materials and view community feedback
- **Subject Organization**: Organize materials by subjects and modules
- **Modern UI**: Responsive web interface built with Bootstrap and Thymeleaf
- **Admin Panel**: User management and material verification

## 🛠️ Tech Stack

- **Backend**: Spring Boot 3.2.0, Spring Security, Spring Data JPA
- **Database**: PostgreSQL
- **Frontend**: Thymeleaf, Bootstrap 5, JavaScript
- **Authentication**: JWT (JSON Web Tokens)
- **Build Tool**: Maven

## 📚 Documentation

- **[Application README](note0-application/README.md)** - Detailed application documentation
- **[Deployment Guide](note0-application/DEPLOYMENT.md)** - Production deployment instructions
- **[Conversion Summary](CONVERSION_SUMMARY.md)** - Details about the Node.js to Java conversion
- **[API Tests](note0-application/test-api.http)** - API endpoint testing

## 🔄 Migration Notes

This project was successfully converted from a Node.js application to Java Spring Boot. All original functionality has been preserved and enhanced:

- ✅ All API endpoints maintained
- ✅ Enhanced security with Spring Security
- ✅ Modern responsive UI
- ✅ Improved code organization and maintainability
- ✅ Production-ready configuration

## 🚀 Getting Started

1. Follow the setup instructions above
2. Access the web interface at http://localhost:8080
3. Register a new account or use the admin credentials
4. Start uploading and sharing materials!

For detailed information, see the [Application README](note0-application/README.md).
