@echo off
setlocal enabledelayedexpansion

echo Compiling all Java files into .\out...
if not exist out mkdir out
javac -d out src\*.java src\helpers\*.java
if errorlevel 1 (
    echo Compilation failed. Exiting.
    pause
    exit /b 1
)

REM --- Launch each app in its own titled window ---
echo Starting Hub...
start "Hub" cmd /k title Hub ^& java -cp out hub
timeout /t 1 >nul

echo Starting Screen...
start "Screen" cmd /k title Screen ^& java -cp out screen
timeout /t 1 >nul

echo Starting Flowmeter...
start "Flowmeter" cmd /k title Flowmeter ^& java -cp out Flowmeter
timeout /t 1 >nul

echo Starting CardReader...
start "CardReader" cmd /k title CardReader ^& java -cp out CreditCardReader
timeout /t 1 >nul

echo Starting Hose...
start "Hose" cmd /k title Hose ^& java -cp out Hose
timeout /t 1 >nul

echo.
echo Press Q to close all programs...

REM --- Wait for single-key 'Q' (no Enter needed) ---
:wait_for_q
choice /c Q /n >nul
REM The above blocks until 'Q' is pressed

echo Closing all programs...
for %%T in (Hub Screen Flowmeter CardReader Hose) do (
    taskkill /fi "WINDOWTITLE eq %%T" /t /f >nul 2>&1
)

echo Done.
exit /b 0
