#!/bin/sh

# Note0 Application Startup Script

# Use POSIX-compliant shell commands for better compatibility
printf "Starting Note0 Application...\n"

# Check if Java is installed
if ! command -v java > /dev/null 2>&1; then
    printf "Error: Java is not installed. Please install Java 17 or higher.\n"
    exit 1
fi

# Check Java version
java_version=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
if [ "$java_version" -lt 17 ]; then
    printf "Error: Java 17 or higher is required. Current version: %s\n" "$java_version"
    exit 1
fi

# Check if Maven is installed
if ! command -v mvn > /dev/null 2>&1; then
    printf "Error: Maven is not installed. Please install Maven 3.6 or higher.\n"
    exit 1
fi

# Check if PostgreSQL client exists before checking server
if command -v pg_isready > /dev/null 2>&1; then
    if ! pg_isready -q; then
        printf "Warning: PostgreSQL might not be running. Please ensure PostgreSQL is started.\n"
    fi
else
    printf "Warning: PostgreSQL client utilities not found. Skipping PostgreSQL check.\n"
fi

# Create uploads directory if it doesn't exist
mkdir -p uploads

# Set environment variables
export SPRING_PROFILES_ACTIVE=dev

# Build and run the application
printf "Building application...\n"
mvn clean install -DskipTests

if [ $? -eq 0 ]; then
    printf "Build successful. Starting application...\n"
    mvn spring-boot:run
else
    printf "Build failed. Please check the errors above.\n"
    exit 1
fi
