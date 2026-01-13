<#
Run Maven / Spring Boot using Java 17 for this session.

Why:
- Your machine default JAVA is 1.8
- Spring Boot 3.x requires Java 17

Usage:
  powershell -NoProfile -ExecutionPolicy Bypass -File .\run-java17.ps1 -Task version
  powershell -NoProfile -ExecutionPolicy Bypass -File .\run-java17.ps1 -Task test
  powershell -NoProfile -ExecutionPolicy Bypass -File .\run-java17.ps1 -Task spring
  powershell -NoProfile -ExecutionPolicy Bypass -File .\run-java17.ps1 -Task maven -Args "clean compile"
#>

param(
  [Parameter(Mandatory = $false)]
  [ValidateSet('version', 'test', 'spring', 'maven')]
  [string]$Task = 'version',

  [Parameter(Mandatory = $false)]
  [string]$Args = ''
)

$jdk17 = 'C:\Program Files\Eclipse Adoptium\jdk-17.0.17.10-hotspot'
if (!(Test-Path "$jdk17\bin\java.exe")) {
  Write-Error "JDK 17 not found at: $jdk17. Update run-java17.ps1 with your installed JDK 17 path."
  exit 1
}

$env:JAVA_HOME = $jdk17
$env:Path = "$env:JAVA_HOME\bin;$env:Path"

# Helps child Java processes (surefire forks) use Java 17 as well.
$env:JAVA_TOOL_OPTIONS = "-Djava.home=$env:JAVA_HOME"

Write-Host "Using JAVA_HOME=$env:JAVA_HOME"
java -version

switch ($Task) {
  'version' {
    javac -version
    .\mvnw.cmd -v
  }
  'test' {
    .\mvnw.cmd test
  }
  'spring' {
    .\mvnw.cmd spring-boot:run
  }
  'maven' {
    if ([string]::IsNullOrWhiteSpace($Args)) {
      Write-Error "-Args is required when Task=maven"
      exit 2
    }
    .\mvnw.cmd $Args
  }
}

exit $LASTEXITCODE
