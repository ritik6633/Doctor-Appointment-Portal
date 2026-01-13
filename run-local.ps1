# Runs backend (Spring Boot, Java 17) + frontend (Vite) together for local development.
# Usage: powershell -ExecutionPolicy Bypass -File .\run-local.ps1

$ErrorActionPreference = 'Stop'

function Test-TcpPort {
  param(
    [Parameter(Mandatory = $true)][string]$HostName,
    [Parameter(Mandatory = $true)][int]$Port
  )
  try {
    $client = New-Object System.Net.Sockets.TcpClient
    $iar = $client.BeginConnect($HostName, $Port, $null, $null)
    $ok = $iar.AsyncWaitHandle.WaitOne(800)
    if ($ok -and $client.Connected) { $client.Close(); return $true }
    $client.Close();
    return $false
  } catch {
    return $false
  }
}

$root = $PSScriptRoot
$frontendDir = Join-Path $root 'frontend'

if (-not (Test-Path (Join-Path $root 'mvnw.cmd'))) {
  throw "mvnw.cmd not found in $root"
}
if (-not (Test-Path (Join-Path $frontendDir 'package.json'))) {
  throw "frontend/package.json not found in $frontendDir"
}

# ---- Java 17 for backend ----
$jdk17 = 'C:\Program Files\Eclipse Adoptium\jdk-17.0.17.10-hotspot'
if (-not (Test-Path $jdk17)) {
  throw "Java 17 not found at '$jdk17'. Update run-local.ps1 with your JDK17 path."
}
$env:JAVA_HOME = $jdk17
$env:Path = "$env:JAVA_HOME\bin;$env:Path"

Write-Host "Backend JAVA_HOME=$env:JAVA_HOME"

# ---- Start backend in a new PowerShell window ----
$backendCmd = "cd /d `"$root`" && .\\mvnw.cmd -DskipTests spring-boot:run"
Start-Process -FilePath 'powershell.exe' -ArgumentList @('-NoProfile','-ExecutionPolicy','Bypass','-Command',$backendCmd) -WorkingDirectory $root | Out-Null

# ---- Start frontend in a new PowerShell window ----
$frontendCmd = "cd /d `"$frontendDir`" && npm install && npm run dev"
Start-Process -FilePath 'powershell.exe' -ArgumentList @('-NoProfile','-ExecutionPolicy','Bypass','-Command',$frontendCmd) -WorkingDirectory $frontendDir | Out-Null

Write-Host "Waiting for servers to start..."

# Expected ports: backend 8080, frontend 5173 (per vite.config.ts / package.json)
$backendOk = $false
$frontendOk = $false

for ($i = 0; $i -lt 90; $i++) {
  if (-not $backendOk) { $backendOk = Test-TcpPort -HostName '127.0.0.1' -Port 8080 }
  if (-not $frontendOk) { $frontendOk = Test-TcpPort -HostName '127.0.0.1' -Port 5173 }
  if ($backendOk -and $frontendOk) { break }
  Start-Sleep -Milliseconds 500
}

if (-not $backendOk) {
  Write-Warning "Backend did not open port 8080. Check the backend PowerShell window for errors (often Java version or MySQL)."
} else {
  Write-Host "Backend is listening on http://localhost:8080"
}

if (-not $frontendOk) {
  Write-Warning "Frontend did not open port 5173. Check the frontend PowerShell window for npm/Vite errors."
} else {
  Write-Host "Frontend is listening on http://localhost:5173"
}

if ($backendOk -and $frontendOk) {
  $url = 'http://localhost:5173'
  Write-Host "Opening $url in your default browser..."
  Start-Process $url
}

Write-Host "Done. Close the two spawned PowerShell windows to stop servers."
