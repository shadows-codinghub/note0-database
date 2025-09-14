#!/bin/bash
set -e

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

# Main execution flow
echo_color $CYAN "=== Setting up Note0 Database ==="

# Check if MySQL client is installed
if ! command -v mysql >/dev/null; then
    echo_color $RED "MySQL client is not installed!"
    echo_color $YELLOW "Please install MySQL client:"
    echo_color $YELLOW "Ubuntu/Debian: sudo apt-get install mysql-client"
    echo_color $YELLOW "CentOS/RHEL: sudo yum install mysql"
    echo_color $YELLOW "macOS: brew install mysql"
    exit 1
fi

# Load environment variables
echo_color $GREEN "Loading environment variables..."
load_env

# Set default values if not in .env
DB_NAME="${DB_NAME:-notes_app}"
DB_USER="${DB_USER:-root}"

if [ -z "$DB_PASSWORD" ]; then
    echo_color $RED "Database password not set! Please set DB_PASSWORD in .env file"
    exit 1
fi

echo_color $GREEN "Setting up database $DB_NAME..."

# Create database
echo_color $CYAN "Creating database if not exists..."
MYSQL_PWD=$DB_PASSWORD mysql -u$DB_USER -e "CREATE DATABASE IF NOT EXISTS $DB_NAME CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"

if [ $? -ne 0 ]; then
    echo_color $RED "Failed to create database! Check your MySQL connection and credentials."
    exit 1
fi

# Apply schema
echo_color $CYAN "Applying database schema..."
MYSQL_PWD=$DB_PASSWORD mysql -u$DB_USER $DB_NAME < note0-application/database/schema.sql

if [ $? -ne 0 ]; then
    echo_color $RED "Failed to apply database schema!"
    exit 1
fi

# Load sample data if exists
if [ -f "note0-application/src/main/resources/data.sql" ]; then
    echo_color $CYAN "Loading sample data..."
    MYSQL_PWD=$DB_PASSWORD mysql -u$DB_USER $DB_NAME < note0-application/src/main/resources/data.sql
    
    if [ $? -ne 0 ]; then
        echo_color $RED "Failed to load sample data!"
        echo_color $YELLOW "The application will still work, but will not have sample data."
    fi
fi

echo_color $GREEN "Database setup completed successfully!"