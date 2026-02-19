@echo off
setlocal

echo 🚀 Iniciando Sistema Académico...

REM Verificar Java
java -version >nul 2>&1
if errorlevel 1 (
    echo ❌ Error: Java no está instalado o no está en el PATH
    echo Por favor instale Java 17 o superior
    pause
    exit /b 1
)

echo ✅ Java encontrado
java -version 2>&1 | findstr "version"

REM Verificar MongoDB
echo 🔍 Verificando MongoDB...
tasklist /FI "IMAGENAME eq mongod.exe" 2>NUL | find /I /N "mongod.exe" >NUL
if errorlevel 1 (
    echo ⚠️  MongoDB no parece estar ejecutándose
    echo Por favor inicie MongoDB desde Services o ejecute mongod.exe
    echo O instálelo desde: https://www.mongodb.com/try/download/community
) else (
    echo ✅ MongoDB está ejecutándose
)

REM Compilar si es necesario
if not exist "target\classes" (
    echo 🔨 Compilando proyecto...
    where mvn >nul 2>&1
    if errorlevel 1 (
        echo ⚠️  Maven no encontrado, usando javac...
        mkdir target\classes 2>nul
        
        REM Crear directorio para librerías
        mkdir lib 2>nul
        
        REM Compilar con Java básico (sin dependencias externas por simplicidad)
        echo Compilando clases Java...
        dir /s /b src\main\java\*.java > sources.txt
        javac -d target\classes @sources.txt
        del sources.txt
        
        if errorlevel 1 (
            echo ❌ Error en la compilación
            pause
            exit /b 1
        )
    ) else (
        call mvn clean compile
    )
)

echo ▶️  Ejecutando Sistema Académico...

REM Ejecutar la aplicación
where mvn >nul 2>&1
if errorlevel 1 (
    java -cp "target\classes" org.example.Main
) else (
    mvn exec:java -Dexec.mainClass="org.example.Main"
)

pause