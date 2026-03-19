# 📋 Guía Paso a Paso: Conexión a MongoDB Atlas con MongoDB Compass

Esta guía te ayudará a conectarte a la base de datos MongoDB Atlas del Sistema Académico utilizando MongoDB Compass, y a inicializar la base de datos con datos de prueba.

## 📋 Tabla de Contenidos
1. [Requisitos Previos](#requisitos-previos)
2. [Descarga e Instalación de MongoDB Compass](#descarga-e-instalación-de-mongodb-compass)
3. [Conexión a MongoDB Atlas](#conexión-a-mongodb-atlas)
4. [Verificar la Conexión](#verificar-la-conexión)
5. [Inicializar la Base de Datos](#inicializar-la-base-de-datos)
6. [Explorar los Datos](#explorar-los-datos)
7. [Solución de Problemas](#solución-de-problemas)

---

## 1. 📦 Requisitos Previos

Antes de comenzar, asegúrate de tener:

- ✅ **Conexión a Internet estable**
- ✅ **Java 17 o superior instalado**
- ✅ **El proyecto Sistema Académico descargado**
- ✅ **Permisos de administrador** (para instalar MongoDB Compass)

### Verificar Java
```bash
java -version
```
Debería mostrar algo como: `openjdk version "17.0.x" o superior`

---

## 2. 💻 Descarga e Instalación de MongoDB Compass

### Para Windows:

1. **Descargar MongoDB Compass:**
   - Ve a: https://www.mongodb.com/try/download/compass
   - Selecciona "Windows x64 (.exe)"
   - Haz clic en "Download"

2. **Instalar:**
   - Ejecuta el archivo `.exe` descargado
   - Sigue el asistente de instalación
   - Acepta los términos y condiciones
   - Haz clic en "Install"
   - Espera a que termine la instalación
   - Haz clic en "Finish"

### Para macOS:

1. **Descargar MongoDB Compass:**
   - Ve a: https://www.mongodb.com/try/download/compass
   - Selecciona "macOS x64 (.dmg)"
   - Haz clic en "Download"

2. **Instalar:**
   - Abre el archivo `.dmg` descargado
   - Arrastra "MongoDB Compass" a la carpeta "Applications"
   - Abre Launchpad y busca "MongoDB Compass"
   - La primera vez, haz clic derecho > "Open" para permitir la ejecución

### Para Ubuntu/Linux:

1. **Descargar MongoDB Compass:**
   - Ve a: https://www.mongodb.com/try/download/compass
   - Selecciona "Ubuntu 20.04+ x64 (.deb)" o "RHEL/CentOS 8+ x64 (.rpm)"

2. **Instalar (.deb):**
   ```bash
   sudo dpkg -i mongodb-compass_*_amd64.deb
   sudo apt-get install -f  # Si hay dependencias faltantes
   ```

3. **Instalar (.rpm):**
   ```bash
   sudo rpm -i mongodb-compass-*.x86_64.rpm
   ```

---

## 3. 🔗 Conexión a MongoDB Atlas

### Paso 3.1: Abrir MongoDB Compass

1. **Iniciar MongoDB Compass:**
   - Windows: Busca "MongoDB Compass" en el menú inicio
   - macOS: Busca "MongoDB Compass" en Launchpad
   - Linux: Ejecuta `mongodb-compass` en la terminal

2. **Pantalla de Bienvenida:**
   - Aparecerá la pantalla principal de MongoDB Compass
   - Verás un campo para "Connection String"

### Paso 3.2: Configurar la Conexión

1. **Obtener la Cadena de Conexión:**
   ```
   mongodb+srv://root:aobregonr1918@cluster0.oofaaro.mongodb.net/sistema_academico
   ```

2. **Introducir la Cadena de Conexión:**
   - En el campo "Connection String", pega la cadena completa
   - **Importante:** Incluye `/sistema_academico` al final para conectarte directamente a la base de datos

3. **Configuración Avanzada (Opcional):**
   - Haz clic en "Advanced Connection Options" si quieres ver más detalles
   - **Host:** `cluster0.oofaaro.mongodb.net`
   - **Username:** `root`
   - **Password:** `aobregonr1918`
   - **Database:** `sistema_academico`

### Paso 3.3: Conectar

1. **Conectar a la Base de Datos:**
   - Haz clic en el botón "Connect"
   - Espera unos segundos mientras se establece la conexión
   - Si es exitosa, verás la interfaz principal de MongoDB Compass

2. **Verificar Conexión Exitosa:**
   - Deberías ver "sistema_academico" en la lista de bases de datos
   - En la barra superior verás: "Connected to cluster0.oofaaro.mongodb.net"

---

## 4. ✅ Verificar la Conexión

### Paso 4.1: Explorar la Base de Datos

1. **Ver Bases de Datos:**
   - En el panel izquierdo, deberías ver "sistema_academico"
   - Haz clic en "sistema_academico" para expandirla

2. **Ver Colecciones:**
   - Al inicio, la base de datos estará vacía
   - Después de inicializar, verás las colecciones:
     - `estudiantes`
     - `profesores`
     - `cursos`
     - `grupos`
     - `grupos_cursos`

### Paso 4.2: Probar la Conexión desde la Aplicación

1. **Ejecutar el Validador de Conexión:**
   ```bash
   cd SistemaAcademico
   java -cp "target/classes:lib/*" org.example.config.MongoConfig
   ```

2. **Resultado Esperado:**
   ```
   ✅ Conexión establecida con MongoDB Atlas exitosamente
   🔗 Base de datos: sistema_academico
   ```

---

## 5. 🏗️ Inicializar la Base de Datos

### Paso 5.1: Compilar el Proyecto (si no está compilado)

```bash
# Si tienes Maven instalado:
mvn clean compile

# O usar el script incluido:
# En Windows:
run.bat

# En macOS/Linux:
chmod +x run.sh
./run.sh
```

### Paso 5.2: Ejecutar el Inicializador de Datos

1. **Ejecutar TestDataRunner:**
   ```bash
   cd SistemaAcademico
   java -cp "target/classes:lib/*" org.example.util.TestDataRunner
   ```

2. **Salida Esperada:**
   ```
   === Sistema Académico - Validación de Base de Datos ===

   ✓ Conexión a MongoDB Atlas exitosa

   === Poblando Base de Datos con Datos de Prueba ===

   Creando estudiantes...
     ✓ Estudiante creado: Juan Carlos Pérez
     ✓ Estudiante creado: María Elena González
     ✓ Estudiante creado: Roberto Antonio Silva
     ✓ Estudiante creado: Ana Patricia Ramírez
     ✓ Estudiante creado: Carlos Eduardo Morales

   Creando profesores...
     ✓ Profesor creado: Dr. Luis Rodríguez
     ✓ Profesor creado: Ing. Ana Morales
     ✓ Profesor creado: MSc. Carlos Fernández
     ✓ Profesor creado: Dra. Patricia Vega
     ✓ Profesor creado: Ing. Roberto Castillo

   Creando cursos...
     ✓ Curso creado: Programación I
     ✓ Curso creado: Programación II
     ✓ Curso creado: Base de Datos I
     ✓ Curso creado: Desarrollo Web
     ✓ Curso creado: Ingeniería de Software

   Creando grupos...
     ✓ Grupo creado: Grupo 1 Programación I
     ✓ Grupo creado: Grupo 1 Programación II
     ✓ Grupo creado: Grupo 1 Base de Datos I
     ✓ Grupo creado: Grupo 1 Desarrollo Web
     ✓ Grupo creado: Grupo 1 Ingeniería de Software

   ✓ Datos de prueba creados exitosamente

   === Ejecutando Pruebas Básicas ===

   Probando lectura de estudiantes...
     - Total estudiantes encontrados: 5
     - Primer estudiante: Juan Carlos Pérez

   Probando lectura de profesores...
     - Total profesores encontrados: 5
     - Primer profesor: Dr. Luis Rodríguez

   ✓ Todas las pruebas básicas completadas exitosamente
   ```

### Paso 5.3: Verificar en MongoDB Compass

1. **Refrescar la Vista:**
   - En MongoDB Compass, haz clic en el botón "Refresh" (🔄)
   - O presiona `Ctrl+R` / `Cmd+R`

2. **Ver las Colecciones Creadas:**
   - Deberías ver las siguientes colecciones bajo "sistema_academico":
     - 📁 `estudiantes`
     - 📁 `profesores` 
     - 📁 `cursos`
     - 📁 `grupos`
     - 📁 `grupos_cursos`

---

## 6. 🔍 Explorar los Datos

### Paso 6.1: Explorar Estudiantes

1. **Abrir la Colección:**
   - Haz clic en "estudiantes"
   - Verás la vista de documentos

2. **Ver Datos:**
   ```json
   {
     "_id": ObjectId("..."),
     "nombre": "Juan Carlos Pérez",
     "identificacion": "123456789",
     "email": "juan.perez@estudiante.ucenfotec.ac.cr",
     "fecha_nacimiento": "1995-03-15",
     "estado": "activo"
   }
   ```

### Paso 6.2: Explorar Cursos

1. **Abrir la Colección:**
   - Haz clic en "cursos"

2. **Ver Datos:**
   ```json
   {
     "_id": ObjectId("..."),
     "nombre": "Programación I",
     "codigo": "PRG101",
     "descripcion": "Introducción a la programación",
     "creditos": 3
   }
   ```

### Paso 6.3: Usar Consultas Básicas

1. **Filtrar Datos:**
   - En el campo "Filter", puedes usar consultas como:
   ```json
   {"estado": "activo"}
   {"creditos": {"$gt": 3}}
   {"nombre": {"$regex": "Programación"}}
   ```

2. **Ordenar Datos:**
   - En el campo "Sort":
   ```json
   {"nombre": 1}  // Ascendente
   {"fecha_nacimiento": -1}  // Descendente
   ```

---

## 7. 🛠️ Solución de Problemas

### Problema 1: No se puede conectar a MongoDB Atlas

**Síntomas:**
- Error: "Server selection timed out"
- MongoDB Compass no conecta

**Soluciones:**
1. **Verificar Internet:**
   ```bash
   ping google.com
   ```

2. **Verificar Firewall:**
   - Asegúrate de que MongoDB Compass tenga acceso a Internet
   - Puertos necesarios: 27017, 443

3. **Verificar la Cadena de Conexión:**
   - Copia exactamente: `mongodb+srv://root:aobregonr1918@cluster0.oofaaro.mongodb.net/sistema_academico`
   - No incluyas espacios adicionales

### Problema 2: Error de Autenticación

**Síntomas:**
- Error: "Authentication failed"

**Soluciones:**
1. **Verificar Credenciales:**
   - Usuario: `root`
   - Contraseña: `aobregonr1918`

2. **Verificar URL:**
   - Cluster: `cluster0.oofaaro.mongodb.net`

### Problema 3: Base de Datos Vacía

**Síntomas:**
- La conexión es exitosa pero no hay datos

**Soluciones:**
1. **Ejecutar el Inicializador:**
   ```bash
   java -cp "target/classes:lib/*" org.example.util.TestDataRunner
   ```

2. **Verificar Compilación:**
   ```bash
   mvn clean compile
   ```

### Problema 4: Java no Encontrado

**Síntomas:**
- Error: "java: command not found"

**Soluciones:**
1. **Instalar Java:**
   - Descarga desde: https://adoptium.net/
   - Instala Java 17 o superior

2. **Verificar PATH:**
   ```bash
   echo $JAVA_HOME
   java -version
   ```

### Problema 5: Maven no Encontrado

**Síntomas:**
- Error: "mvn: command not found"

**Soluciones:**
1. **Usar el Script Incluido:**
   ```bash
   ./run.sh  # macOS/Linux
   run.bat   # Windows
   ```

2. **Instalar Maven:**
   - Descarga desde: https://maven.apache.org/download.cgi

---

## 📞 Soporte Adicional

### Recursos Útiles:
- 📖 **Documentación MongoDB Compass:** https://docs.mongodb.com/compass/
- 🎓 **Tutorial MongoDB:** https://docs.mongodb.com/manual/tutorial/
- 💬 **Comunidad MongoDB:** https://community.mongodb.com/

### Verificación Final:
Para confirmar que todo está funcionando correctamente:

1. ✅ MongoDB Compass conectado exitosamente
2. ✅ Base de datos "sistema_academico" visible
3. ✅ 5 colecciones creadas (estudiantes, profesores, cursos, grupos, grupos_cursos)
4. ✅ Datos de prueba visibles en cada colección
5. ✅ Aplicación Java conecta sin errores

### Comandos de Verificación Rápida:

```bash
# Verificar Java
java -version

# Verificar conexión desde la aplicación
cd SistemaAcademico
java -cp "target/classes:lib/*" org.example.util.TestDataRunner

# Ejecutar la aplicación principal
java -cp "target/classes:lib/*" org.example.Main
```

---

**¡Felicidades! 🎉**

Tu base de datos MongoDB Atlas está ahora configurada y lista para usar con el Sistema Académico. Puedes usar tanto MongoDB Compass para explorar visualmente los datos como la aplicación Java para interactuar programáticamente con la base de datos.