#!/bin/sh

# Database Setup Script for Linux/macOS
echo "Setting up PostgreSQL database for Note0 Application..."

# Load environment variables from .env file
if [ -f .env ]; then
    export $(cat .env | grep -v '^#' | xargs)
else
    echo "Warning: .env file not found. Using default values."
fi

# Configuration
DB_NAME="${DB_NAME:-notes_app}"
DB_USER="${DB_USER:-postgres}"
DB_HOST="${DB_HOST:-localhost}"
DB_PORT="${DB_PORT:-5432}"

if [ -z "$DB_PASSWORD" ]; then
    echo "Error: Database password not set. Please set DB_PASSWORD in .env file"
    exit 1
fi

# Check if PostgreSQL is installed
if ! command -v psql > /dev/null; then
    echo "Error: PostgreSQL is not installed. Please install PostgreSQL first."
    echo "Ubuntu/Debian: sudo apt-get install postgresql"
    echo "macOS: brew install postgresql"
    exit 1
fi

# Check if PostgreSQL service is running
if ! pg_isready > /dev/null 2>&1; then
    echo "Error: PostgreSQL is not running. Please start PostgreSQL service."
    echo "Linux: sudo systemctl start postgresql"
    echo "macOS: brew services start postgresql"
    exit 1
fi

echo "PostgreSQL found and running. Creating database..."

# Create database
psql -U $DB_USER << EOF
DROP DATABASE IF EXISTS $DB_NAME;
CREATE DATABASE $DB_NAME;
EOF

if [ $? -ne 0 ]; then
    echo "Error: Failed to create database. Make sure PostgreSQL is running and credentials are correct."
    exit 1
fi

# Run schema script
echo "Applying database schema..."
psql -U $DB_USER -d $DB_NAME -f database/schema.sql

if [ $? -ne 0 ]; then
    echo "Error: Failed to apply database schema."
    exit 1
fi

# Run sample data script if it exists
if [ -f "src/main/resources/data.sql" ]; then
    echo "Loading sample data..."
    psql -U $DB_USER -d $DB_NAME -f src/main/resources/data.sql
    
    if [ $? -ne 0 ]; then
        echo "Warning: Failed to load sample data."
    else
        echo "Sample data loaded successfully."
    fi
fi

echo "Database setup completed successfully!"
echo "You can now run the application using: ./run.sh"