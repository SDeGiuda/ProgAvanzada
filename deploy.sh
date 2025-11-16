#!/bin/bash

##############################################
# Script de deployment para Mac/Linux
# Mi Playlist Musical
##############################################

APP_NAME="mi-playlist"
JAR_FILE="target/${APP_NAME}.jar"
PID_FILE="${APP_NAME}.pid"
LOG_FILE="${APP_NAME}.log"
PORT=8080

echo "=========================================="
echo "  Deployment - Mi Playlist Musical"
echo "=========================================="

# Función para detener la aplicación
stop_app() {
    if [ -f "$PID_FILE" ]; then
        PID=$(cat "$PID_FILE")
        echo "Deteniendo aplicación (PID: $PID)..."
        kill -15 $PID 2>/dev/null
        sleep 3
        
        # Verificar si el proceso sigue corriendo
        if kill -0 $PID 2>/dev/null; then
            echo "Forzando detención..."
            kill -9 $PID 2>/dev/null
        fi
        
        rm -f "$PID_FILE"
        echo "Aplicación detenida."
    else
        # Intentar matar por puerto
        EXISTING_PID=$(lsof -ti:$PORT)
        if [ ! -z "$EXISTING_PID" ]; then
            echo "Deteniendo proceso en puerto $PORT (PID: $EXISTING_PID)..."
            kill -15 $EXISTING_PID 2>/dev/null
            sleep 2
        fi
    fi
}

# Función para iniciar la aplicación
start_app() {
    echo "Iniciando aplicación..."
    
    if [ ! -f "$JAR_FILE" ]; then
        echo "ERROR: No se encontró el archivo JAR en $JAR_FILE"
        echo "Ejecute 'mvn package' primero."
        exit 1
    fi
    
    # Iniciar aplicación en background
    nohup java -jar "$JAR_FILE" > "$LOG_FILE" 2>&1 &
    
    # Guardar PID
    echo $! > "$PID_FILE"
    
    echo "Aplicación iniciada con PID: $(cat $PID_FILE)"
    echo "Log disponible en: $LOG_FILE"
    
    # Esperar a que la aplicación esté lista
    echo "Esperando a que la aplicación esté lista..."
    for i in {1..30}; do
        if curl -s http://localhost:$PORT > /dev/null 2>&1; then
            echo ""
            echo "=========================================="
            echo "  ✓ Deployment exitoso!"
            echo "  Aplicación disponible en:"
            echo "  http://localhost:$PORT"
            echo "=========================================="
            return 0
        fi
        echo -n "."
        sleep 1
    done
    
    echo ""
    echo "WARNING: La aplicación tardó más de lo esperado en iniciar."
    echo "Verifique el log en: $LOG_FILE"
}

# Función principal
deploy() {
    echo "Paso 1: Deteniendo aplicación existente..."
    stop_app
    
    echo ""
    echo "Paso 2: Iniciando nueva versión..."
    start_app
}

# Ejecutar deployment
deploy

exit 0
