@echo off
setlocal enableextensions

REM ------------------------------------------------------------
REM Project helper: Run/build using JDK 17.
REM ------------------------------------------------------------

REM Prefer a known Temurin (Eclipse Adoptium) install path first.
set "JAVA17_HOME=%ProgramFiles%\Eclipse Adoptium\jdk-17.0.17.10-hotspot"

REM Fallback: try to auto-detect best matching JDK 17 under Program Files
if not exist "%JAVA17_HOME%\bin\java.exe" (
  set "JAVA17_HOME="
  for /f "delims=" %%D in ('dir /b /ad "%ProgramFiles%\Eclipse Adoptium\jdk-17*" 2^>nul') do (
    if not defined JAVA17_HOME set "JAVA17_HOME=%ProgramFiles%\Eclipse Adoptium\%%D"
  )
  if not defined JAVA17_HOME (
    for /f "delims=" %%D in ('dir /b /ad "%ProgramFiles%\Java\jdk-17*" 2^>nul') do (
      if not defined JAVA17_HOME set "JAVA17_HOME=%ProgramFiles%\Java\%%D"
    )
  )
)

if not defined JAVA17_HOME (
  echo.
  echo ERROR: JDK 17 was not found on this machine.
  echo.
  echo Current JAVA_HOME is: %JAVA_HOME%
  echo.
  echo Install a JDK 17 (Temurin recommended):
  echo   https://adoptium.net/temurin/releases/?version=17
  echo.
  echo After install, open a new terminal and run this again.
  echo.
  exit /b 1
)

if not exist "%JAVA17_HOME%\bin\java.exe" (
  echo.
  echo ERROR: JAVA17_HOME was detected but java.exe was not found:
  echo   %JAVA17_HOME%
  echo.
  exit /b 1
)

set "JAVA_HOME=%JAVA17_HOME%"
set "PATH=%JAVA_HOME%\bin;%PATH%"

echo Using JAVA_HOME=%JAVA_HOME%
java -version
javac -version
echo.

if "%~1"=="" (
  echo No command provided. Running: mvnw test
  call mvnw.cmd test
  exit /b %ERRORLEVEL%
)

call mvnw.cmd %*
exit /b %ERRORLEVEL%
