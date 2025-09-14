# Note0 Application Runner for Windows
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
        Write-ColorOutput Yellow "After installation, ensure MySQL is installed in one of these locations:"
        foreach ($path in $commonPaths) {
            Write-ColorOutput Yellow "  - $path"
        }
        Write-ColorOutput Yellow "Or add MySQL's bin directory to your system's PATH"
        return $false
    }
}

function Test-Database {
    Write-ColorOutput Green "Checking database connection..."
    
    # Ensure MySQL is in PATH
    if (-not (Check-MySQL)) {
        return $false
    }
    
    # Load environment variables
    Load-EnvFile

    $DB_NAME = $env:DB_NAME ?? "notes_app"
    $DB_USER = $env:DB_USER ?? "root"
    $DB_PASSWORD = $env:DB_PASSWORD

    if (-not $DB_PASSWORD) {
        Write-ColorOutput Red "Database password not set in .env file!"
        return $false
    }

    # Test database connection
    $env:MYSQL_PWD = $DB_PASSWORD
    $testResult = Write-Output "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = '$DB_NAME';" | 
                 mysql -u $DB_USER --skip-column-names 2>$null

    if ($testResult -eq $DB_NAME) {
        Write-ColorOutput Green "Database connection successful!"
        return $true
    }
    
    Write-ColorOutput Yellow "Database '$DB_NAME' not found."
    return $false
}

# Main execution flow
Write-ColorOutput Cyan "=== Note0 Application Deployment ==="

# Check if .env exists, if not copy from example
if (-not (Test-Path note0-application/.env) -and (Test-Path note0-application/.env.example)) {
    Write-ColorOutput Yellow "Creating .env file from template..."
    Copy-Item note0-application/.env.example note0-application/.env
    Write-ColorOutput Yellow "Please update the database password in note0-application/.env"
    exit 1
}

# Check if database exists and is accessible
if (-not (Test-Database)) {
    Write-ColorOutput Yellow "Database not found or not accessible. Running setup..."
    & "$PSScriptRoot\setup-db.ps1"
    if ($LASTEXITCODE -ne 0) {
        Write-ColorOutput Red "Database setup failed. Please check the errors above."
        exit 1
    }
}

# Build and start the application
Write-ColorOutput Green "Building and starting application..."
Set-Location note0-application
Write-ColorOutput Cyan "Building with Maven..."
mvn clean install -DskipTests
if ($LASTEXITCODE -ne 0) {
    Write-ColorOutput Red "Build failed! Check the Maven build output above."
    exit 1
}

Write-ColorOutput Cyan "Starting Spring Boot application..."
mvn spring-boot:run