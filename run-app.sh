#!/bin/bash

# Color definitions
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
CYAN='\033[0;36m'
NC='\033[0m'

echo_color() {
    color=$1
    shift
    echo -e "${color}$@${NC}"
}

load_env() {
    if [ -f note0-application/.env ]; then
        export $(cat note0-application/.env | grep -v '^#' | xargs)
    fi
}

check_mysql() {
    echo_color $GREEN "Checking MySQL installation..."
    if ! command -v mysql >/dev/null; then
        echo_color $RED "MySQL client is not installed!"
        echo_color $YELLOW "Please install MySQL client:"
        echo_color $YELLOW "Ubuntu/Debian: sudo apt-get install mysql-client"
        echo_color $YELLOW "CentOS/RHEL: sudo yum install mysql"
        echo_color $YELLOW "macOS: brew install mysql"
        return 1
    fi
    
    echo_color $GREEN "MySQL client found"
    return 0
}

test_database() {
    echo_color $GREEN "Checking database connection..."
    
    # Load environment variables
    load_env
    
    DB_NAME="${DB_NAME:-notes_app}"
    DB_USER="${DB_USER:-root}"
    
    if [ -z "$DB_PASSWORD" ]; then
        echo_color $RED "Database password not set! Please set DB_PASSWORD in .env file"
        return 1
    fi
    
    # Test database connection and existence
    if MYSQL_PWD=$DB_PASSWORD mysql -u$DB_USER --skip-column-names 2>/dev/null -e \
        "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = '$DB_NAME';" | grep -q "^$DB_NAME$"; then
        echo_color $GREEN "Database connection successful!"
        return 0
    else
        echo_color $YELLOW "Database '$DB_NAME' not found."
        return 1
    fi
}

# Main execution flow
echo_color $CYAN "=== Note0 Application Deployment ==="

# Check if .env exists, if not copy from example
if [ ! -f note0-application/.env ] && [ -f note0-application/.env.example ]; then
    echo_color $YELLOW "Creating .env file from template..."
    cp note0-application/.env.example note0-application/.env
    echo_color $YELLOW "Please update the database password in note0-application/.env"
    exit 1
fi

# Check if MySQL is installed
check_mysql || exit 1

# Check if database exists and is accessible
if ! test_database; then
    echo_color $YELLOW "Database not found or not accessible. Running setup..."
    bash "./setup-db.sh"
    if [ $? -ne 0 ]; then
        echo_color $RED "Database setup failed. Please check the errors above."
        exit 1
    fi
fi

# Build and start the application
echo_color $GREEN "Building and starting application..."
cd note0-application

echo_color $CYAN "Building with Maven..."
# Use mvnw if it exists, otherwise use mvn
if [ -f "./mvnw" ]; then
    ./mvnw clean install -DskipTests
else
    mvn clean install -DskipTests
fi

if [ $? -ne 0 ]; then
    echo_color $RED "Build failed! Check the Maven build output above."
    exit 1
fi

echo_color $CYAN "Starting Spring Boot application..."
if [ -f "./mvnw" ]; then
    ./mvnw spring-boot:run
else
    mvn spring-boot:run
fi