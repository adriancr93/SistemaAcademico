# Sistema Académico de Gestión Universitaria

Un sistema completo de gestión académica desarrollado en Java con MongoDB, utilizando arquitectura por capas y principios de programación orientada a objetos.

## 🏗️ Arquitectura

El sistema está organizado en las siguientes capas:

- **Model** (`org.example.model`): Entidades del dominio
- **Repository** (`org.example.repository`): Interfaces y acceso a datos
- **Service** (`org.example.service`): Lógica de negocio
- **Controller** (`org.example.controller`): Orquestación y control de flujo
- **View** (`org.example.view`): Interfaz de usuario en consola
- **Config** (`org.example.config`): Configuraciones de la aplicación
- **Util** (`org.example.util`): Utilidades y validaciones

## 📋 Funcionalidades

### Módulo de Estudiantes
- ✅ Crear, leer, actualizar y eliminar estudiantes
- ✅ Búsqueda por ID, identificación y email
- ✅ Validaciones de datos
- ✅ Estadísticas

### Módulo de Profesores
- ✅ Crear, leer, actualizar y eliminar profesores
- ✅ Búsqueda por ID, identificación, email y departamento
- ✅ Validaciones de datos
- ✅ Estadísticas

### Módulo de Grupos
- ✅ Crear, leer, actualizar y eliminar grupos
- ✅ Búsqueda por ID y nombre
- ✅ Validaciones de datos
- ✅ Estadísticas

### Módulo de Cursos
- ✅ Crear, leer, actualizar y eliminar cursos
- ✅ Búsqueda por ID y nombre
- ✅ Validaciones de datos
- ✅ Estadísticas

### Módulo de Relaciones Grupo-Curso
- ✅ Asociar grupos con cursos
- ✅ Gestión de relaciones
- ✅ Consultas por grupo o curso

## 🛠️ Requisitos

### Software Requerido
- **Java 25** o superior
- **Maven 3.8+**
- **MongoDB 4.4+**

### Dependencias
- MongoDB Java Driver 4.11.1
- Jackson Databind 2.16.1
- Jakarta Validation API 3.0.2

## 📦 Instalación

### 1. Clonar o descargar el proyecto

```bash
# Si tienes git instalado
git clone [URL-del-repositorio]

# O descomprimir el archivo ZIP del proyecto
```

### 2. Instalar MongoDB

#### macOS (usando Homebrew)
```bash
brew tap mongodb/brew
brew install mongodb-community
brew services start mongodb/brew/mongodb-community
```

#### Ubuntu/Debian
```bash
sudo apt-get install mongodb
sudo systemctl start mongodb
sudo systemctl enable mongodb
```

#### Windows
1. Descargar desde [MongoDB Community Server](https://www.mongodb.com/try/download/community)
2. Ejecutar el instalador
3. Iniciar el servicio MongoDB

### 3. Verificar MongoDB
```bash
# Verificar que MongoDB esté ejecutándose
mongo --version

# Conectar a MongoDB (opcional)
mongosh
```

### 4. Compilar el proyecto
```bash
cd SistemaAcademico
mvn clean compile
```

## 🚀 Uso

### 1. Ejecutar la aplicación
```bash
mvn exec:java -Dexec.mainClass="org.example.Main"
```

O desde su IDE favorito ejecutando la clase `Main.java`

### 2. Navegación por menús

El sistema presenta un menú principal con las siguientes opciones:

```
═════════════════════════════════════════════════════════════════
                        MENÚ PRINCIPAL
═════════════════════════════════════════════════════════════════
1. 👨‍🎓 Gestión de Estudiantes
2. 👨‍🏫 Gestión de Profesores  
3. 👥 Gestión de Grupos
4. 📚 Gestión de Cursos
5. 📊 Reportes y Estadísticas
6. ⚙️  Configuración
0. 🚪 Salir
═════════════════════════════════════════════════════════════════
```

### 3. Operaciones CRUD

Cada módulo permite:
- **Crear**: Agregar nuevos registros
- **Leer**: Listar todos o buscar específicos
- **Actualizar**: Modificar registros existentes
- **Eliminar**: Borrar registros

## 📊 Modelo de Datos

### Estudiante
- `id`: Identificador único (ObjectId)
- `nombre`: Nombre completo
- `identificacion`: Número de identificación único
- `email`: Correo electrónico
- `fecha_nacimiento`: Fecha de nacimiento
- `estado`: Estado (activo/inactivo)

### Profesor
- `id`: Identificador único (ObjectId)
- `nombre`: Nombre completo
- `identificacion`: Número de identificación único
- `email`: Correo electrónico
- `departamento`: Departamento al que pertenece
- `estado`: Estado (activo/inactivo)

### Grupo
- `id`: Identificador único (ObjectId)
- `nombre`: Nombre del grupo
- `descripcion`: Descripción del grupo
- `estado`: Estado (activo/inactivo)

### Curso
- `id`: Identificador único (ObjectId)
- `nombre`: Nombre del curso
- `descripcion`: Descripción del curso
- `estado`: Estado (activo/inactivo)

### GrupoCurso (Relación)
- `id`: Identificador único (ObjectId)
- `grupo_id`: Identificador del grupo
- `curso_id`: Identificador del curso

## ✅ Validaciones

El sistema implementa las siguientes validaciones:

### Campos Obligatorios
- Todos los campos marcados como requeridos
- No se permiten campos vacíos o nulos

### Formatos
- **Email**: Formato RFC estándar
- **Identificación**: 9-12 dígitos numéricos
- **Fechas**: dd/MM/yyyy, yyyy-MM-dd, dd-MM-yyyy
- **Estados**: "activo" o "inactivo"

### Unicidad
- **Estudiantes**: Identificación y email únicos
- **Profesores**: Identificación y email únicos
- **Grupos**: Nombre único
- **Cursos**: Nombre único

### Reglas de Negocio
- Fechas de nacimiento no futuras
- Longitud mínima para nombres y descripciones
- No duplicación de relaciones grupo-curso

## 🔧 Configuración

### Base de Datos
- **Host**: localhost
- **Puerto**: 27017
- **Base de datos**: sistema_academico

Para cambiar la configuración, modifique la clase `MongoConfig.java`:

```java
private static final String CONNECTION_STRING = "mongodb://localhost:27017";
private static final String DATABASE_NAME = "sistema_academico";
```

## 🎯 Principios de POO Implementados

### Encapsulamiento
- Atributos privados con getters y setters
- Métodos de validación internos

### Abstracción
- Interfaces para contratos de comportamiento
- Clases abstractas para funcionalidades comunes

### Herencia
- Jerarquía de excepciones personalizada
- Reutilización de código común

### Polimorfismo
- Implementación múltiple de interfaces
- Métodos sobrecargados para diferentes operaciones

## 🏢 Patrones de Diseño

- **Repository Pattern**: Abstracción del acceso a datos
- **Service Layer**: Separación de lógica de negocio
- **Singleton**: Configuración de base de datos
- **MVC**: Separación de responsabilidades

## 🚨 Manejo de Errores

El sistema maneja errores de manera robusta:

- **Validación de entrada**: Verificación antes de procesar
- **Excepciones personalizadas**: Mensajes específicos
- **Recuperación de errores**: Opciones para reintentar
- **Logging**: Registro de errores para depuración

## 🧪 Pruebas

Para probar la aplicación:

1. Iniciar MongoDB
2. Ejecutar la aplicación
3. Crear datos de prueba usando los menús
4. Verificar operaciones CRUD
5. Probar validaciones con datos inválidos

## 📝 Notas Técnicas

- **Conexiones MongoDB**: Se manejan automáticamente
- **Thread Safety**: Configuraciones thread-safe
- **Memory Management**: Cierre automático de recursos
- **Performance**: Índices automáticos en MongoDB

## 📞 Soporte

Para problemas o preguntas:

1. Verificar que MongoDB esté ejecutándose
2. Comprobar la conectividad de red
3. Revisar logs de error en consola
4. Consultar documentación de MongoDB

## 📄 Licencia

Este proyecto es para uso académico en UCenfotec.
Desarrollado para el curso "Programación con Patrones".

---

**Desarrollado por**: [Su Nombre]  
**Universidad**: UCenfotec  
**Curso**: Programación con Patrones  
**Año**: 2026