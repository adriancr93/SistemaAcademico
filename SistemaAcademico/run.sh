#!/bin/bash

# Script de inicio para el Sistema Académico
# Este script compila y ejecuta la aplicación

echo "🚀 Iniciando Sistema Académico..."

# Verificar Java
if ! command -v java &> /dev/null; then
    echo "❌ Error: Java no está instalado o no está en el PATH"
    echo "Por favor instale Java 17 o superior"
    exit 1
fi

echo "✅ Java encontrado: $(java -version 2>&1 | head -n 1)"

# Verificar MongoDB
echo "🔍 Verificando MongoDB..."
if ! pgrep -x mongod > /dev/null; then
    echo "⚠️  MongoDB no parece estar ejecutándose"
    echo "Por favor inicie MongoDB con: mongod"
    echo "O instálelo desde: https://www.mongodb.com/try/download/community"
    
    # Intentar iniciar MongoDB si está instalado
    if command -v mongod &> /dev/null; then
        echo "🔄 Intentando iniciar MongoDB..."
        mongod --dbpath ./data --fork --logpath ./mongodb.log 2>/dev/null || true
    fi
else
    echo "✅ MongoDB está ejecutándose"
fi

# Compilar si es necesario
if [ ! -d "target/classes" ]; then
    echo "🔨 Compilando proyecto..."
    if command -v mvn &> /dev/null; then
        mvn clean compile
    else
        echo "⚠️  Maven no encontrado, usando javac..."
        mkdir -p target/classes
        
        # Descargar dependencias básicas si no existen
        LIB_DIR="lib"
        mkdir -p $LIB_DIR
        
        if [ ! -f "$LIB_DIR/mongodb-driver-sync-4.11.1.jar" ]; then
            echo "📦 Descargando dependencias de MongoDB..."
            curl -L -o "$LIB_DIR/mongodb-driver-sync-4.11.1.jar" \
                "https://repo1.maven.org/maven2/org/mongodb/mongodb-driver-sync/4.11.1/mongodb-driver-sync-4.11.1.jar"
            
            curl -L -o "$LIB_DIR/bson-4.11.1.jar" \
                "https://repo1.maven.org/maven2/org/mongodb/bson/4.11.1/bson-4.11.1.jar"
            
            curl -L -o "$LIB_DIR/mongodb-driver-core-4.11.1.jar" \
                "https://repo1.maven.org/maven2/org/mongodb/mongodb-driver-core/4.11.1/mongodb-driver-core-4.11.1.jar"
        fi
        
        # Compilar con dependencias
        CLASSPATH="$LIB_DIR/*:src/main/java"
        find src/main/java -name "*.java" -exec javac -cp "$CLASSPATH" -d target/classes {} +
    fi
fi

echo "▶️  Ejecutando Sistema Académico..."

# Ejecutar la aplicación
if command -v mvn &> /dev/null; then
    mvn exec:java -Dexec.mainClass="org.example.Main"
else
    CLASSPATH="lib/*:target/classes"
    java -cp "$CLASSPATH" org.example.Main
fi