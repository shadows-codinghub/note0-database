# Note0 - Academic Material Sharing Platform

A comprehensive Java Spring Boot application for sharing academic materials, notes, and resources among students.

## Features

- **User Authentication**: JWT-based authentication with role-based access control
- **Material Upload**: Upload and share academic materials with file validation
- **Rating System**: Rate materials and view community feedback
- **Subject Organization**: Organize materials by subjects and modules
- **Modern UI**: Responsive web interface built with Bootstrap and Thymeleaf
- **Admin Panel**: User management and material verification

## Tech Stack

- **Backend**: Spring Boot 3.2.0, Spring Security, Spring Data JPA
- **Database**: PostgreSQL
- **Frontend**: Thymeleaf, Bootstrap 5, JavaScript
- **Authentication**: JWT (JSON Web Tokens)
- **File Upload**: Spring MultipartFile
- **Build Tool**: Maven

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- PostgreSQL 12+
- Git

## Installation & Setup

### 1. Clone the Repository
```bash
git clone <repository-url>
cd note0-application
```

### 2. Environment Setup

1. Copy the example environment file:
   ```bash
   cp .env.example .env
   ```

2. Edit the `.env` file and set your database password and other configurations:
   ```env
   DB_PASSWORD=your_password_here
   JWT_SECRET=your_jwt_secret_here
   ```

### 3. Database Setup

#### Automated Setup (Recommended)

**For Windows:**
```powershell
# Run the database setup script
.\setup-db.ps1
```

**For Linux/macOS:**
```bash
# Make the script executable
chmod +x setup-db.sh

# Run the database setup script
./setup-db.sh
```

#### Manual Setup
If you prefer to set up the database manually:
```bash
# Create PostgreSQL database
sudo -u postgres psql
CREATE DATABASE notes_app;
\q

# Run the schema script
psql -U postgres -d notes_app -f database/schema.sql
```

### 3. Configuration
Update the database credentials in `src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/notes_app
    username: your_username
    password: your_password
```

### 4. Build and Run

#### Using Scripts (Recommended)

**For Linux/macOS:**
```bash
# Make the script executable
chmod +x run.sh

# Run the application
./run.sh
```

**For Windows:**
```powershell
# Run the application using PowerShell
.\run.ps1
```

#### Manual Method
```bash
# Build the application
mvn clean install

# Run the application
mvn spring-boot:run
```

The application will be available at `http://localhost:8080`

## API Endpoints

### Authentication
- `POST /api/auth/register` - User registration
- `POST /api/auth/login` - User login

### User Management
- `GET /api/users/me` - Get current user profile (Protected)

### Materials
- `GET /api/materials` - Get all approved materials (Public)
- `POST /api/materials/upload` - Upload new material (Protected)
- `POST /api/materials/{id}/rate` - Rate a material (Protected)

### Admin
- `GET /api/admin/users` - Get all users (Admin only)
- `PUT /api/admin/users/{id}/verify` - Verify user (Admin only)

## Default Admin Account

- **Email**: admin@note0.com
- **Password**: admin123

## File Upload

The application supports the following file types:
- Documents: PDF, DOC, DOCX, PPT, PPTX
- Images: JPG, JPEG, PNG, GIF
- Maximum file size: 10MB

## Security Features

- JWT-based authentication
- Password encryption with BCrypt
- Role-based access control (REGISTERED, VERIFIED, ADMIN)
- File type validation
- CORS configuration

## Development

### Running in Development Mode

**Linux/macOS:**
```bash
export SPRING_PROFILES_ACTIVE=dev
mvn spring-boot:run
```

**Windows (PowerShell):**
```powershell
$env:SPRING_PROFILES_ACTIVE = "dev"
mvn spring-boot:run
```

**Windows (Command Prompt):**
```cmd
set SPRING_PROFILES_ACTIVE=dev
mvn spring-boot:run
```

### Database Schema Updates
The application uses Hibernate's `ddl-auto: update` in production and `create-drop` in development mode.

## Project Structure

```
src/
├── main/
│   ├── java/com/note0/
│   │   ├── controller/     # REST and Web controllers
│   │   ├── dto/           # Data Transfer Objects
│   │   ├── entity/        # JPA entities
│   │   ├── repository/    # Data repositories
│   │   ├── security/      # Security configuration
│   │   ├── service/       # Business logic
│   │   └── Note0Application.java
│   └── resources/
│       ├── templates/     # Thymeleaf templates
│       ├── application.yml
│       └── data.sql
└── test/
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License.

## Support

For support and questions, please contact the development team or create an issue in the repository.
