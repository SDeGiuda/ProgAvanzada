@echo off
REM ############################################
REM Script de deployment para Windows
REM Mi Playlist Musical
REM ############################################

setlocal enabledelayedexpansion

set APP_NAME=mi-playlist
set JAR_FILE=target\%APP_NAME%.jar
set PID_FILE=%APP_NAME%.pid
set LOG_FILE=%APP_NAME%.log
set PORT=8080

echo ==========================================
echo   Deployment - Mi Playlist Musical
echo ==========================================
echo.

REM Detener aplicación existente
echo Paso 1: Deteniendo aplicacion existente...
call :stop_app

echo.
echo Paso 2: Iniciando nueva version...
call :start_app

goto :end

REM ============================================
REM Función para detener la aplicación
REM ============================================
:stop_app
    if exist "%PID_FILE%" (
        set /p PID=<"%PID_FILE%"
        echo Deteniendo aplicacion ^(PID: !PID!^)...
        taskkill /PID !PID! /F >nul 2>&1
        del /F "%PID_FILE%" >nul 2>&1
        echo Aplicacion detenida.
    ) else (
        REM Intentar matar proceso por puerto
        for /f "tokens=5" %%a in ('netstat -ano ^| findstr ":%PORT%" ^| findstr "LISTENING"') do (
            echo Deteniendo proceso en puerto %PORT% ^(PID: %%a^)...
            taskkill /PID %%a /F >nul 2>&1
        )
    )
    
    REM Pequeña pausa para asegurar que el puerto se libere
    timeout /t 2 /nobreak >nul
    goto :eof

REM ============================================
REM Función para iniciar la aplicación
REM ============================================
:start_app
    if not exist "%JAR_FILE%" (
        echo ERROR: No se encontro el archivo JAR en %JAR_FILE%
        echo Ejecute 'mvn package' primero.
        exit /b 1
    )
    
    echo Iniciando aplicacion...
    
    REM Iniciar aplicación en background
    start /B java -jar "%JAR_FILE%" > "%LOG_FILE%" 2>&1
    
    REM Obtener el PID del proceso Java recién iniciado
    timeout /t 2 /nobreak >nul
    for /f "tokens=2" %%a in ('tasklist /FI "IMAGENAME eq java.exe" /FO LIST ^| findstr "PID:"') do (
        set LAST_PID=%%a
    )
    
    REM Guardar PID
    echo !LAST_PID! > "%PID_FILE%"
    echo Aplicacion iniciada con PID: !LAST_PID!
    echo Log disponible en: %LOG_FILE%
    
    REM Esperar a que la aplicación esté lista
    echo Esperando a que la aplicacion este lista...
    set RETRY=0
    :wait_loop
        timeout /t 1 /nobreak >nul
        curl -s http://localhost:%PORT% >nul 2>&1
        if !ERRORLEVEL! equ 0 goto :app_ready
        
        set /a RETRY+=1
        if !RETRY! lss 30 (
            echo | set /p="."
            goto :wait_loop
        )
        
        echo.
        echo WARNING: La aplicacion tardo mas de lo esperado en iniciar.
        echo Verifique el log en: %LOG_FILE%
        goto :eof
        
    :app_ready
        echo.
        echo ==========================================
        echo   Deployment exitoso!
        echo   Aplicacion disponible en:
        echo   http://localhost:%PORT%
        echo ==========================================
        goto :eof

:end
endlocal
exit /b 0
