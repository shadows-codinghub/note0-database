# Deployment Guide for Note0 Application

## Quick Start

### Prerequisites
- Java 17+
- Maven 3.6+
- PostgreSQL 12+
- Git

### 1. Database Setup
```bash
# Install PostgreSQL (Ubuntu/Debian)
sudo apt update
sudo apt install postgresql postgresql-contrib

# Start PostgreSQL service
sudo systemctl start postgresql
sudo systemctl enable postgresql

# Create database and user
sudo -u postgres psql
CREATE DATABASE notes_app;
CREATE USER note0_user WITH PASSWORD 'your_password';
GRANT ALL PRIVILEGES ON DATABASE notes_app TO note0_user;
\q

# Run schema
psql -U note0_user -d notes_app -f database/schema.sql
```

### 2. Application Setup
```bash
# Clone and build
git clone <repository-url>
cd note0-application

# Make run script executable
chmod +x run.sh

# Run the application
./run.sh
```

### 3. Access the Application
- Web Interface: http://localhost:8080
- API Base URL: http://localhost:8080/api

## Production Deployment

### Environment Variables
Create a `.env` file or set environment variables:
```bash
export DB_USER=note0_user
export DB_PASSWORD=your_secure_password
export DB_HOST=localhost
export DB_DATABASE=notes_app
export DB_PORT=5432
export JWT_SECRET=your_very_long_and_secure_jwt_secret_key
export FILE_UPLOAD_DIR=/var/note0/uploads
```

### Production Configuration
Update `application.yml` for production:
```yaml
spring:
  profiles:
    active: prod
  datasource:
    url: jdbc:postgresql://${DB_HOST}:${DB_PORT}/${DB_DATABASE}
    username: ${DB_USER}
    password: ${DB_PASSWORD}
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false

jwt:
  secret: ${JWT_SECRET}
  expiration: 604800000

file:
  upload-dir: ${FILE_UPLOAD_DIR}
```

### Docker Deployment (Optional)
Create `Dockerfile`:
```dockerfile
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/note0-application-1.0.0.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
```

Build and run:
```bash
mvn clean package
docker build -t note0-app .
docker run -p 8080:8080 -e DB_HOST=host.docker.internal note0-app
```

## Security Considerations

1. **Change Default Passwords**: Update admin password and JWT secret
2. **Database Security**: Use strong passwords and limit database access
3. **File Upload Security**: Implement virus scanning and file validation
4. **HTTPS**: Use SSL certificates in production
5. **Firewall**: Restrict database access to application servers only

## Monitoring and Maintenance

### Log Files
- Application logs: Check console output or configure log files
- Database logs: `/var/log/postgresql/`

### Backup Strategy
```bash
# Database backup
pg_dump -U note0_user notes_app > backup_$(date +%Y%m%d).sql

# File uploads backup
tar -czf uploads_backup_$(date +%Y%m%d).tar.gz uploads/
```

### Performance Optimization
1. **Database Indexing**: Already configured in schema
2. **Connection Pooling**: Configured in Spring Boot
3. **File Storage**: Consider cloud storage for large files
4. **Caching**: Implement Redis for session management

## Troubleshooting

### Common Issues

1. **Database Connection Failed**
   - Check PostgreSQL service status
   - Verify database credentials
   - Ensure database exists

2. **File Upload Issues**
   - Check upload directory permissions
   - Verify file size limits
   - Check available disk space

3. **Authentication Issues**
   - Verify JWT secret configuration
   - Check token expiration settings
   - Ensure proper CORS configuration

### Health Checks
```bash
# Check application health
curl http://localhost:8080/actuator/health

# Check database connection
psql -U note0_user -d notes_app -c "SELECT 1;"
```

## Scaling Considerations

1. **Load Balancing**: Use nginx or Apache for multiple instances
2. **Database Clustering**: PostgreSQL replication for high availability
3. **File Storage**: Use cloud storage (AWS S3, Google Cloud Storage)
4. **Caching**: Implement Redis for session and data caching
5. **Monitoring**: Use tools like Prometheus and Grafana

## Support

For deployment issues or questions:
1. Check application logs
2. Verify all prerequisites are met
3. Test database connectivity
4. Review security configurations
