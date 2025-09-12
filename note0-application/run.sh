#!/bin/bash

# Note0 Application Startup Script

echo "Starting Note0 Application..."

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "Error: Java is not installed. Please install Java 17 or higher."
    exit 1
fi

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "Error: Maven is not installed. Please install Maven 3.6 or higher."
    exit 1
fi

# Check if PostgreSQL is running
if ! pg_isready -q; then
    echo "Warning: PostgreSQL might not be running. Please ensure PostgreSQL is started."
fi

# Create uploads directory if it doesn't exist
mkdir -p uploads

# Set environment variables
export SPRING_PROFILES_ACTIVE=dev

# Build and run the application
echo "Building application..."
mvn clean install -DskipTests

if [ $? -eq 0 ]; then
    echo "Build successful. Starting application..."
    mvn spring-boot:run
else
    echo "Build failed. Please check the errors above."
    exit 1
fi
