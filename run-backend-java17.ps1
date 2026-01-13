# Starts the backend using Java 17 in this terminal session.
# Usage: powershell -ExecutionPolicy Bypass -File .\run-backend-java17.ps1

$ErrorActionPreference = 'Stop'

$jdk17 = 'C:\Program Files\Eclipse Adoptium\jdk-17.0.17.10-hotspot'
if (-not (Test-Path $jdk17)) {
  throw "Java 17 not found at '$jdk17'. Update this path in run-backend-java17.ps1."
}

$env:JAVA_HOME = $jdk17
$env:Path = "$env:JAVA_HOME\bin;$env:Path"

Write-Host "Using JAVA_HOME=$env:JAVA_HOME"
java -version

Set-Location -Path $PSScriptRoot

# Run in foreground so logs are visible.
.\mvnw.cmd -DskipTests spring-boot:run

