@echo off
setlocal enableextensions

REM ------------------------------------------------------------
REM Project helper: Run/build using JDK 17.
REM ------------------------------------------------------------

REM Try common JDK 17 install locations (Temurin/Oracle/Microsoft/Zulu/Corretto).
set "CANDIDATE_1=%ProgramFiles%\Eclipse Adoptium\jdk-17*"
set "CANDIDATE_2=%ProgramFiles%\Java\jdk-17*"
set "CANDIDATE_3=%ProgramFiles%\Microsoft\jdk-17*"
set "CANDIDATE_4=%ProgramFiles%\Zulu\zulu-17*"
set "CANDIDATE_5=%ProgramFiles%\Amazon Corretto\jdk17*"

set "JAVA17_HOME="
for /d %%D in (%CANDIDATE_1% %CANDIDATE_2% %CANDIDATE_3% %CANDIDATE_4% %CANDIDATE_5%) do (
  if not defined JAVA17_HOME (
    if exist "%%D\bin\java.exe" (
      set "JAVA17_HOME=%%D"
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

set "JAVA_HOME=%JAVA17_HOME%"
set "PATH=%JAVA_HOME%\bin;%PATH%"

echo Using JAVA_HOME=%JAVA_HOME%
java -version
echo.

if "%~1"=="" (
  echo No command provided. Running: mvnw test
  call mvnw.cmd test
  exit /b %ERRORLEVEL%
)

call mvnw.cmd %*
exit /b %ERRORLEVEL%
