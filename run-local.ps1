<#
Run helpers for localhost development.

Why this exists:
- Your machine currently defaults to Java 8 (JAVA_HOME=C:\Program Files\Java\jdk-1.8)
- This project requires Java 17.

This script switches the current PowerShell session to JDK 17 (Temurin install)
without changing global environment variables, then runs Maven.

Usage:
  powershell -NoProfile -ExecutionPolicy Bypass -File .\run-local.ps1 test
  powershell -NoProfile -ExecutionPolicy Bypass -File .\run-local.ps1 spring
#>

param(
  [Parameter(Mandatory=$false)]
  [ValidateSet('version','test','spring')]
  [string]$task = 'version'
)

$jdk17 = 'C:\Program Files\Eclipse Adoptium\jdk-17.0.17.10-hotspot'
if (!(Test-Path "$jdk17\bin\java.exe")) {
  Write-Error "JDK 17 not found at: $jdk17. Update run-local.ps1 with your installed JDK 17 path."
  exit 1
}

$env:JAVA_HOME = $jdk17
$env:Path = "$env:JAVA_HOME\bin;$env:Path"

if ($task -eq 'version') {
  java -version
  javac -version
  .\mvnw.cmd -v
  exit $LASTEXITCODE
}

if ($task -eq 'test') {
  .\mvnw.cmd test
  exit $LASTEXITCODE
}

if ($task -eq 'spring') {
  .\mvnw.cmd spring-boot:run
  exit $LASTEXITCODE
}

