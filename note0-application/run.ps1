# Note0 Application Startup Script

Write-Host "Starting Note0 Application..."

# Check if Java is installed
try {
    $javaVersion = java -version 2>&1
    if (-not $?) {
        Write-Host "Error: Java is not installed. Please install Java 17 or higher."
        exit 1
    }
} catch {
    Write-Host "Error: Java is not installed. Please install Java 17 or higher."
    exit 1
}

# Check if Maven is installed
try {
    $mvnVersion = mvn -version 2>&1
    if (-not $?) {
        Write-Host "Error: Maven is not installed. Please install Maven 3.6 or higher."
        exit 1
    }
} catch {
    Write-Host "Error: Maven is not installed. Please install Maven 3.6 or higher."
    exit 1
}

# Create uploads directory if it doesn't exist
if (-not (Test-Path "uploads")) {
    New-Item -ItemType Directory -Path "uploads"
}

# Set environment variables
$env:SPRING_PROFILES_ACTIVE = "dev"

# Build and run the application
Write-Host "Building application..."
mvn clean install -DskipTests

if ($LASTEXITCODE -eq 0) {
    Write-Host "Build successful. Starting application..."
    mvn spring-boot:run
} else {
    Write-Host "Build failed. Please check the errors above."
    exit 1
}