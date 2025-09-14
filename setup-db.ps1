# Database Setup Script for Windows
$ErrorActionPreference = "Stop"

function Write-ColorOutput($ForegroundColor) {
    $fc = $host.UI.RawUI.ForegroundColor
    $host.UI.RawUI.ForegroundColor = $ForegroundColor
    if ($args) {
        Write-Output $args
    }
    $host.UI.RawUI.ForegroundColor = $fc
}

function Load-EnvFile {
    if (Test-Path note0-application/.env) {
        Get-Content note0-application/.env | ForEach-Object {
            if ($_ -match '^([^#][^=]+)=(.*)$') {
                $key = $matches[1].Trim()
                $value = $matches[2].Trim()
                Set-Item -Path "env:$key" -Value $value
            }
        }
    }
}

function Find-MySQLPath {
    $commonPaths = @(
        "C:\Program Files\MySQL\MySQL Server 8.0\bin",
        "C:\Program Files (x86)\MySQL\MySQL Server 8.0\bin",
        "${env:ProgramFiles}\MySQL\MySQL Server 8.0\bin",
        "${env:ProgramFiles(x86)}\MySQL\MySQL Server 8.0\bin"
    )
    
    foreach ($path in $commonPaths) {
        if (Test-Path "$path\mysql.exe") {
            return $path
        }
    }
    return $null
}

function Check-MySQL {
    Write-ColorOutput Green "Checking MySQL installation..."
    
    $mysqlPath = Find-MySQLPath
    if ($mysqlPath) {
        $env:PATH = "$mysqlPath;$env:PATH"
        try {
            $null = & mysql --version
            Write-ColorOutput Green "MySQL found at: $mysqlPath"
            return $true
        } catch {
            Write-ColorOutput Red "MySQL found but cannot execute. Please check installation."
            return $false
        }
    } else {
        Write-ColorOutput Red "MySQL is not installed or not found in common locations!"
        Write-ColorOutput Yellow "Please install MySQL from: https://dev.mysql.com/downloads/installer/"
        return $false
    }
}

function Setup-Database {
    Write-ColorOutput Green "Setting up database..."
    
    # Load environment variables
    Load-EnvFile
    
    $DB_NAME = $env:DB_NAME ?? "notes_app"
    $DB_USER = $env:DB_USER ?? "root"
    $DB_PASSWORD = $env:DB_PASSWORD
    
    if (-not $DB_PASSWORD) {
        Write-ColorOutput Red "Database password not set! Please set DB_PASSWORD in .env file"
        exit 1
    }

    # Create database and apply schema
    Write-ColorOutput Cyan "Setting up database $DB_NAME..."
    Set-Location note0-application
    
    # Verify mysql is in path
    $mysqlPath = Find-MySQLPath
    if (-not $mysqlPath) {
        Write-ColorOutput Red "MySQL not found! Please check MySQL installation."
        exit 1
    }
    
    # First create the database
    $env:MYSQL_PWD = $DB_PASSWORD
    try {
        Write-ColorOutput Cyan "Creating database..."
        echo "CREATE DATABASE IF NOT EXISTS $DB_NAME;" | & "$mysqlPath\mysql.exe" -u $DB_USER
        
        if ($LASTEXITCODE -ne 0) {
            Write-ColorOutput Red "Failed to create database! Check your MySQL credentials."
            exit 1
        }

        Write-ColorOutput Cyan "Applying database schema..."
        Get-Content database/schema.sql | & "$mysqlPath\mysql.exe" -u $DB_USER $DB_NAME
        
        if ($LASTEXITCODE -ne 0) {
            Write-ColorOutput Red "Failed to apply schema! Check your MySQL permissions."
            exit 1
        }
        
        Write-ColorOutput Green "Database setup completed successfully!"
    } catch {
        Write-ColorOutput Red "Error executing MySQL commands: $_"
        exit 1
    }
}

# Main execution flow
Write-ColorOutput Cyan "=== Note0 Database Setup ==="

# Check if .env exists, if not copy from example
if (-not (Test-Path note0-application/.env) -and (Test-Path note0-application/.env.example)) {
    Write-ColorOutput Yellow "Creating .env file from template..."
    Copy-Item note0-application/.env.example note0-application/.env
    Write-ColorOutput Yellow "Please update the database password in note0-application/.env"
    exit 1
}

# Check MySQL
if (-not (Check-MySQL)) {
    exit 1
}

# Setup Database
Setup-Database