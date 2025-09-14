# Database Setup Script for Windows
Write-Host "Setting up PostgreSQL database for Note0 Application..."

# Load environment variables from .env file
function Load-EnvFile {
    if (Test-Path .env) {
        Get-Content .env | ForEach-Object {
            if ($_ -match '^([^=]+)=(.*)$') {
                $key = $matches[1]
                $value = $matches[2]
                Set-Item -Path "env:$key" -Value $value
            }
        }
    } else {
        Write-Host "Warning: .env file not found. Using default values."
    }
}

# Load environment variables
Load-EnvFile

# Configuration
$DB_NAME = $env:DB_NAME ?? "notes_app"
$DB_USER = $env:DB_USER ?? "postgres"
$DB_PASSWORD = $env:DB_PASSWORD
$DB_HOST = $env:DB_HOST ?? "localhost"
$DB_PORT = $env:DB_PORT ?? "5432"

if (-not $DB_PASSWORD) {
    Write-Host "Error: Database password not set. Please set DB_PASSWORD in .env file"
    exit 1
}

# Check if PostgreSQL is installed
try {
    $pgVersion = & psql --version 2>&1
    if (-not $?) {
        Write-Host "Error: PostgreSQL is not installed or not in PATH. Please install PostgreSQL first."
        Write-Host "Download from: https://www.postgresql.org/download/windows/"
        exit 1
    }
} catch {
    Write-Host "Error: PostgreSQL is not installed or not in PATH. Please install PostgreSQL first."
    Write-Host "Download from: https://www.postgresql.org/download/windows/"
    exit 1
}

Write-Host "PostgreSQL found. Creating database..."

# Create database
$createDbCommand = @"
DROP DATABASE IF EXISTS $DB_NAME;
CREATE DATABASE $DB_NAME;
"@

Write-Host "Creating database..."
$createDbCommand | psql -U $DB_USER

if ($LASTEXITCODE -ne 0) {
    Write-Host "Error: Failed to create database. Make sure PostgreSQL is running and credentials are correct."
    exit 1
}

# Run schema script
Write-Host "Applying database schema..."
psql -U $DB_USER -d $DB_NAME -f database/schema.sql

if ($LASTEXITCODE -ne 0) {
    Write-Host "Error: Failed to apply database schema."
    exit 1
}

# Run sample data script if it exists
if (Test-Path "src/main/resources/data.sql") {
    Write-Host "Loading sample data..."
    psql -U $DB_USER -d $DB_NAME -f src/main/resources/data.sql
    
    if ($LASTEXITCODE -ne 0) {
        Write-Host "Warning: Failed to load sample data."
    } else {
        Write-Host "Sample data loaded successfully."
    }
}

Write-Host "Database setup completed successfully!"
Write-Host "You can now run the application using: mvn spring-boot:run"