@echo off
cd /d C:\Users\priya\Finacle\DocumentoryCredit
 
echo ===== Running Maven Tests =====
mvn test > test_output.log 2>&1
 
if %errorlevel% neq 0 (
    echo [ERROR] Maven tests failed. Check test_output.log for details.
    pause
    exit /b
)
 
echo ===== Tests Passed. Launching coreRun.exe =====
coreRun.exe > core_output.log 2>&1
 
if %errorlevel% neq 0 (
    echo [ERROR] coreRun.exe failed. Check core_output.log for details.
) else (
    echo coreRun.exe executed successfully. Check core_output.log for output.
)