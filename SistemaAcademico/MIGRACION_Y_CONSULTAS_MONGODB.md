# 🔄 Sistema de Migración y Consultas MongoDB - Análisis Completo

Este documento explica paso a paso cómo funciona el sistema de migración de datos y consultas en el proyecto Sistema Académico usando MongoDB.

## 📋 Tabla de Contenidos
1. [Concepto de "Migración" en MongoDB](#concepto-de-migración-en-mongodb)
2. [Flujo de Inicialización de la Base de Datos](#flujo-de-inicialización-de-la-base-de-datos)
3. [Proceso Paso a Paso de Migración](#proceso-paso-a-paso-de-migración)
4. [Sistema de Consultas](#sistema-de-consultas)
5. [Mapeo Objeto-Documento (ODM Manual)](#mapeo-objeto-documento-odm-manual)
6. [Ejemplos Prácticos](#ejemplos-prácticos)
7. [Comparación con Sistemas Tradicionales](#comparación-con-sistemas-tradicionales)

---

## 1. 🎯 Concepto de "Migración" en MongoDB

### Diferencias con Bases de Datos Relacionales

**Base de Datos Tradicional (SQL):**
```sql
-- 1. Crear tabla con esquema fijo
CREATE TABLE estudiantes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    identificacion VARCHAR(20) UNIQUE,
    email VARCHAR(100) UNIQUE,
    fecha_nacimiento DATE,
    estado VARCHAR(20)
);

-- 2. Insertar datos
INSERT INTO estudiantes (nombre, identificacion, email, fecha_nacimiento, estado)
VALUES ('Juan Pérez', '123456789', 'juan@email.com', '1995-03-15', 'activo');
```

**MongoDB (NoSQL):**
```java
// 1. NO necesita esquema predefinido
// 2. Las colecciones se crean automáticamente al insertar el primer documento
// 3. Cada documento puede tener estructura diferente (flexible)

// Ejemplo de documento insertado automáticamente:
{
  "_id": ObjectId("507f1f77bcf86cd799439011"),
  "nombre": "Juan Pérez",
  "identificacion": "123456789",
  "email": "juan@email.com",
  "fecha_nacimiento": "1995-03-15",
  "estado": "activo"
}
```

### ¿Qué es "Migración" en este Proyecto?

En este proyecto, **migración** se refiere a:

1. **Inicialización de la conexión** a MongoDB Atlas
2. **Población inicial de datos** (seeding)
3. **Creación automática de colecciones** al insertar datos
4. **Validación** de que el sistema funciona correctamente

---

## 2. 🚀 Flujo de Inicialización de la Base de Datos

### Arquitectura del Sistema de Migración

```
TestDataRunner (main)
    ↓
validarConexion()
    ↓
MongoConfig.getInstance() → Singleton Pattern
    ↓
MongoDB Atlas Connection
    ↓
poblarDatosPrueba()
    ↓
┌─────────────────┬─────────────────┬─────────────────┬─────────────────┐
│ crearEstudiantes│ crearProfesores │ crearCursos     │ crearGrupos     │
│     ↓           │       ↓         │       ↓         │       ↓         │
│EstudianteService│ProfesorService  │CursoService     │GrupoService     │
│     ↓           │       ↓         │       ↓         │       ↓         │
│Repository.save()│Repository.save()│Repository.save()│Repository.save()│
│     ↓           │       ↓         │       ↓         │       ↓         │
│MongoDB Collection│MongoDB Collection│MongoDB Collection│MongoDB Collection│
└─────────────────┴─────────────────┴─────────────────┴─────────────────┘
```

---

## 3. 📝 Proceso Paso a Paso de Migración

### Paso 1: Inicialización de la Conexión

**Archivo:** `MongoConfig.java`

```java
// 1. Configuración de conexión (Singleton)
private static final String CONNECTION_STRING = "mongodb+srv://root:aobregonr1918@cluster0.oofaaro.mongodb.net/";
private static final String DATABASE_NAME = "sistema_academico";

// 2. Crear conexión automáticamente al instanciar
private MongoConfig() {
    inicializarConexion(); // Se ejecuta una sola vez
}

// 3. Establecer conexión con timeout
private void inicializarConexion() {
    ConnectionString connectionString = new ConnectionString(CONNECTION_STRING);
    MongoClientSettings settings = MongoClientSettings.builder()
        .applyConnectionString(connectionString)
        .applyToSocketSettings(builder ->
            builder.connectTimeout(10, TimeUnit.SECONDS)
                   .readTimeout(10, TimeUnit.SECONDS))
        .build();
        
    this.mongoClient = MongoClients.create(settings);
    this.database = mongoClient.getDatabase(DATABASE_NAME); // ← Base de datos se selecciona aquí
    
    if (testConnection()) {
        this.connectionActive = true;
        // ✅ Conexión exitosa - MongoDB Atlas está listo
    }
}
```

### Paso 2: Validación de Conexión

**Archivo:** `TestDataRunner.java`

```java
private static boolean validarConexion() {
    try {
        MongoConfig config = MongoConfig.getInstance();
        return config.testConnection(); // Hace ping a MongoDB
    } catch (Exception e) {
        return false;
    }
}

// En MongoConfig.java - Prueba de conectividad
@Override
public boolean testConnection() {
    try {
        // Envía comando "ping" a MongoDB
        Document pingResult = mongoClient.getDatabase("admin")
            .runCommand(new Document("ping", 1));
            
        Object okValue = pingResult.get("ok");
        return (okValue != null && (
            (okValue instanceof Integer && ((Integer) okValue) == 1) ||
            (okValue instanceof Double && ((Double) okValue) == 1.0)
        ));
    } catch (Exception e) {
        return false;
    }
}
```

### Paso 3: Población de Datos (Seeding)

**Archivo:** `TestDataRunner.java`

```java
private static void poblarDatosPrueba() {
    System.out.println("=== Poblando Base de Datos con Datos de Prueba ===\n");
    
    try {
        // Ejecuta en orden secuencial
        crearEstudiantesPrueba();  // Crea colección "estudiantes"
        crearProfesoresPrueba();   // Crea colección "profesores"
        crearCursosPrueba();       // Crea colección "cursos"
        crearGruposPrueba();       // Crea colección "grupos"
        
        System.out.println("✓ Datos de prueba creados exitosamente\n");
    } catch (Exception e) {
        System.err.println("Error poblando datos: " + e.getMessage());
    }
}
```

### Paso 4: Creación de Datos Específicos

**Ejemplo con Estudiantes:**

```java
private static void crearEstudiantesPrueba() {
    // 1. Instanciar el servicio (que internamente crea el repositorio)
    EstudianteServiceImpl estudianteService = new EstudianteServiceImpl();
    
    System.out.println("Creando estudiantes...");
    
    // 2. Crear objetos Java en memoria
    Estudiante[] estudiantes = {
        new Estudiante("Juan Carlos Pérez", "123456789", 
            "juan.perez@estudiante.ucenfotec.ac.cr", LocalDate.of(1995, 3, 15), "activo"),
        new Estudiante("María Elena González", "234567890", 
            "maria.gonzalez@estudiante.ucenfotec.ac.cr", LocalDate.of(1996, 7, 22), "activo"),
        // ... más estudiantes
    };
    
    // 3. Guardar cada estudiante (esto dispara la cadena de persistencia)
    for (Estudiante estudiante : estudiantes) {
        try {
            // ← Aquí comienza el flujo de persistencia
            estudianteService.crearEstudiante(estudiante);
            System.out.println("  ✓ Estudiante creado: " + estudiante.getNombre());
        } catch (Exception e) {
            System.out.println("  ✗ Error: " + e.getMessage());
        }
    }
}
```

---

## 4. 🔍 Sistema de Consultas

### Flujo de Persistencia Completo

#### 4.1 Guardar (CREATE)

**Flujo:** `Service → Repository → MongoDB`

```java
// 1. En EstudianteServiceImpl.java
@Override
public Estudiante crearEstudiante(Estudiante estudiante) throws IllegalArgumentException {
    // Validaciones de negocio
    validarEstudiante(estudiante, false);
    
    // Verificar unicidad de identificación
    if (estudianteRepository.findByIdentificacion(estudiante.getIdentificacion()).isPresent()) {
        throw new IllegalArgumentException("Ya existe un estudiante con la identificación: " + 
            estudiante.getIdentificacion());
    }
    
    // Verificar unicidad de email
    if (estudianteRepository.findByEmail(estudiante.getEmail()).isPresent()) {
        throw new IllegalArgumentException("Ya existe un estudiante con el email: " + 
            estudiante.getEmail());
    }
    
    // Delegar al repositorio para persistir
    return estudianteRepository.save(estudiante);
}

// 2. En EstudianteRepositoryMongoDB.java
@Override
public Estudiante save(Estudiante estudiante) {
    try {
        // Convertir objeto Java → Document BSON
        Document doc = estudiante.toDocument();
        
        if (estudiante.getId() == null) {
            // INSERCIÓN NUEVA
            collection.insertOne(doc); // ← MongoDB crea la colección automáticamente si no existe
            estudiante.setId(doc.getObjectId("_id")); // MongoDB genera automáticamente el _id
        } else {
            // ACTUALIZACIÓN
            collection.replaceOne(eq("_id", estudiante.getId()), doc);
        }
        
        return estudiante;
    } catch (Exception e) {
        throw new RuntimeException("Error al guardar estudiante: " + e.getMessage(), e);
    }
}

// 3. En Estudiante.java - Serialización Manual
public Document toDocument() {
    Document doc = new Document();
    if (id != null) {
        doc.append("_id", id);
    }
    doc.append("nombre", nombre);
    doc.append("identificacion", identificacion);
    doc.append("email", email);
    if (fechaNacimiento != null) {
        doc.append("fecha_nacimiento", fechaNacimiento.toString()); // LocalDate → String
    }
    doc.append("estado", estado);
    return doc;
}
```

#### 4.2 Leer (READ)

**Consultas Básicas:**

```java
// 1. BUSCAR POR ID
@Override
public Optional<Estudiante> findById(ObjectId id) {
    try {
        // Usar filtro predefinido eq() en lugar de escribir JSON
        Document doc = collection.find(eq("_id", id)).first();
        return doc != null ? Optional.of(new Estudiante(doc)) : Optional.empty();
    } catch (Exception e) {
        throw new RuntimeException("Error al buscar estudiante por ID: " + e.getMessage(), e);
    }
}

// 2. BUSCAR TODOS
@Override
public List<Estudiante> findAll() {
    List<Estudiante> estudiantes = new ArrayList<>();
    try {
        // Usar cursor para eficiencia en memoria
        try (MongoCursor<Document> cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                estudiantes.add(new Estudiante(cursor.next())); // Document → Objeto Java
            }
        }
        return estudiantes;
    } catch (Exception e) {
        throw new RuntimeException("Error al obtener todos los estudiantes: " + e.getMessage(), e);
    }
}

// 3. BUSCAR POR CAMPO ESPECÍFICO
@Override
public Optional<Estudiante> findByIdentificacion(String identificacion) {
    try {
        Document doc = collection.find(eq("identificacion", identificacion)).first();
        return doc != null ? Optional.of(new Estudiante(doc)) : Optional.empty();
    } catch (Exception e) {
        throw new RuntimeException("Error al buscar estudiante por identificación: " + e.getMessage(), e);
    }
}

// 4. CONSULTAS COMPLEJAS CON MÚLTIPLES FILTROS
@Override
public boolean existsByIdentificacionAndNotId(String identificacion, ObjectId excludeId) {
    try {
        return collection.countDocuments(and(
            eq("identificacion", identificacion),  // WHERE identificacion = ?
            ne("_id", excludeId)                   // AND _id != ?
        )) > 0;
    } catch (Exception e) {
        throw new RuntimeException("Error al verificar identificación única: " + e.getMessage(), e);
    }
}

// En Estudiante.java - Deserialización Manual
public Estudiante(Document doc) {
    this.id = doc.getObjectId("_id");
    this.nombre = doc.getString("nombre");
    this.identificacion = doc.getString("identificacion");
    this.email = doc.getString("email");
    String fechaStr = doc.getString("fecha_nacimiento");
    if (fechaStr != null) {
        this.fechaNacimiento = LocalDate.parse(fechaStr); // String → LocalDate
    }
    this.estado = doc.getString("estado");
}
```

#### 4.3 Actualizar (UPDATE)

```java
@Override
public boolean update(Estudiante estudiante) {
    try {
        if (estudiante.getId() == null) {
            throw new IllegalArgumentException("El estudiante debe tener un ID para ser actualizado");
        }
        
        Document doc = estudiante.toDocument();
        // replaceOne() reemplaza completamente el documento
        UpdateResult result = collection.replaceOne(eq("_id", estudiante.getId()), doc);
        return result.getMatchedCount() > 0; // ¿Se encontró el documento?
    } catch (Exception e) {
        throw new RuntimeException("Error al actualizar estudiante: " + e.getMessage(), e);
    }
}
```

#### 4.4 Eliminar (DELETE)

```java
@Override
public boolean deleteById(ObjectId id) {
    try {
        DeleteResult result = collection.deleteOne(eq("_id", id));
        return result.getDeletedCount() > 0; // ¿Se eliminó algo?
    } catch (Exception e) {
        throw new RuntimeException("Error al eliminar estudiante: " + e.getMessage(), e);
    }
}
```

### Filtros de Consulta Disponibles

El proyecto usa los filtros predefinidos de MongoDB:

```java
import static com.mongodb.client.model.Filters.*;

// FILTROS BÁSICOS
eq("campo", valor)              // campo = valor
ne("campo", valor)              // campo != valor
gt("campo", valor)              // campo > valor
gte("campo", valor)             // campo >= valor
lt("campo", valor)              // campo < valor
lte("campo", valor)             // campo <= valor

// FILTROS DE TEXTO
regex("campo", "patrón")        // LIKE en SQL
in("campo", Arrays.asList(v1, v2)) // campo IN (v1, v2)

// FILTROS LÓGICOS
and(filtro1, filtro2)          // filtro1 AND filtro2
or(filtro1, filtro2)           // filtro1 OR filtro2
not(filtro)                    // NOT filtro

// EXISTENCIA
exists("campo")                // campo IS NOT NULL
exists("campo", false)         // campo IS NULL
```

---

## 5. 🔄 Mapeo Objeto-Documento (ODM Manual)

### Serialización: Java Object → MongoDB Document

```java
// En cada modelo (ejemplo: Estudiante.java)
public Document toDocument() {
    Document doc = new Document();
    
    // 1. ID opcional (MongoDB lo genera automáticamente si es null)
    if (id != null) {
        doc.append("_id", id);
    }
    
    // 2. Campos primitivos - mapeo directo
    doc.append("nombre", nombre);
    doc.append("identificacion", identificacion);
    doc.append("email", email);
    doc.append("estado", estado);
    
    // 3. Campos complejos - conversión manual
    if (fechaNacimiento != null) {
        doc.append("fecha_nacimiento", fechaNacimiento.toString()); // LocalDate → String
    }
    
    return doc;
}
```

### Deserialización: MongoDB Document → Java Object

```java
// Constructor desde Document
public Estudiante(Document doc) {
    // 1. ID obligatorio en documentos existentes
    this.id = doc.getObjectId("_id");
    
    // 2. Campos primitivos - mapeo directo
    this.nombre = doc.getString("nombre");
    this.identificacion = doc.getString("identificacion");
    this.email = doc.getString("email");
    this.estado = doc.getString("estado");
    
    // 3. Campos complejos - conversión manual
    String fechaStr = doc.getString("fecha_nacimiento");
    if (fechaStr != null) {
        this.fechaNacimiento = LocalDate.parse(fechaStr); // String → LocalDate
    }
}
```

### Ejemplo de Documento Resultante

```json
{
  "_id": ObjectId("65a1b2c3d4e5f6789abcdef0"),
  "nombre": "Juan Carlos Pérez",
  "identificacion": "123456789",
  "email": "juan.perez@estudiante.ucenfotec.ac.cr",
  "fecha_nacimiento": "1995-03-15",
  "estado": "activo"
}
```

---

## 6. 💡 Ejemplos Prácticos

### Ejemplo 1: Flujo Completo de Creación de Estudiante

```java
// 1. ENTRADA: TestDataRunner crea objeto Java
Estudiante estudiante = new Estudiante(
    "Juan Carlos Pérez", 
    "123456789", 
    "juan.perez@estudiante.ucenfotec.ac.cr", 
    LocalDate.of(1995, 3, 15), 
    "activo"
);

// 2. SERVICIO: Validaciones de negocio
EstudianteServiceImpl service = new EstudianteServiceImpl();
Estudiante estudianteGuardado = service.crearEstudiante(estudiante);

// 3. REPOSITORIO: Persistencia
// Internamente:
Document doc = estudiante.toDocument(); // Java → BSON
collection.insertOne(doc);              // Guardar en MongoDB
ObjectId id = doc.getObjectId("_id");   // Obtener ID generado
estudiante.setId(id);                   // Actualizar objeto Java

// 4. RESULTADO: MongoDB contiene:
{
  "_id": ObjectId("..."),
  "nombre": "Juan Carlos Pérez",
  "identificacion": "123456789",
  "email": "juan.perez@estudiante.ucenfotec.ac.cr",
  "fecha_nacimiento": "1995-03-15",
  "estado": "activo"
}
```

### Ejemplo 2: Consulta con Filtros

```java
// BUSCAR estudiantes activos nacidos después de 1995
public List<Estudiante> findEstudiantesActivosRecientes() {
    List<Estudiante> estudiantes = new ArrayList<>();
    
    // Construir filtro compuesto
    var filter = and(
        eq("estado", "activo"),
        gte("fecha_nacimiento", "1995-01-01")
    );
    
    // Ejecutar consulta
    try (MongoCursor<Document> cursor = collection.find(filter).iterator()) {
        while (cursor.hasNext()) {
            estudiantes.add(new Estudiante(cursor.next()));
        }
    }
    
    return estudiantes;
}

// Equivalente SQL:
// SELECT * FROM estudiantes 
// WHERE estado = 'activo' AND fecha_nacimiento >= '1995-01-01';
```

### Ejemplo 3: Actualización Parcial

```java
// CAMBIAR estado de un estudiante específico
public boolean cambiarEstadoEstudiante(ObjectId id, String nuevoEstado) {
    try {
        // MongoDB permite updateOne para actualizaciones parciales
        UpdateResult result = collection.updateOne(
            eq("_id", id),
            new Document("$set", new Document("estado", nuevoEstado))
        );
        
        return result.getModifiedCount() > 0;
    } catch (Exception e) {
        throw new RuntimeException("Error actualizando estado: " + e.getMessage(), e);
    }
}

// Equivalente SQL:
// UPDATE estudiantes SET estado = ? WHERE _id = ?;
```

---

## 7. ⚖️ Comparación con Sistemas Tradicionales

### Sistema Tradicional (Hibernate/JPA)

```java
// 1. CONFIGURACIÓN REQUERIDA
// persistence.xml, hibernate.cfg.xml
// Mapeo de entidades con anotaciones

@Entity
@Table(name = "estudiantes")
public class Estudiante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String nombre;
    
    // ... más anotaciones
}

// 2. CONFIGURACIÓN DE ESQUEMA
CREATE TABLE estudiantes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    identificacion VARCHAR(20) UNIQUE,
    -- etc.
);

// 3. REPOSITORIO
@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {
    Optional<Estudiante> findByIdentificacion(String identificacion);
}

// 4. MIGRACIÓN REQUERIDA
-- V1__Create_estudiantes_table.sql
-- V2__Add_email_column.sql
-- etc.
```

### Sistema MongoDB Actual

```java
// 1. CONFIGURACIÓN MÍNIMA
// Solo connection string

public class Estudiante {
    private ObjectId id;
    private String nombre;
    // ... no requiere anotaciones
}

// 2. SIN ESQUEMA
// MongoDB crea la colección automáticamente

// 3. REPOSITORIO SIMPLE
public class EstudianteRepositoryMongoDB implements EstudianteRepository {
    public Optional<Estudiante> findByIdentificacion(String identificacion) {
        Document doc = collection.find(eq("identificacion", identificacion)).first();
        return doc != null ? Optional.of(new Estudiante(doc)) : Optional.empty();
    }
}

// 4. SIN MIGRACIONES
// Solo población de datos inicial
```

### Ventajas del Sistema Actual

| Aspecto | MongoDB (Actual) | SQL + Hibernate |
|---------|------------------|-----------------|
| **Configuración** | Mínima (connection string) | Compleja (XML, anotaciones) |
| **Esquema** | Flexible, sin predefinir | Rígido, requiere DDL |
| **Migraciones** | No necesarias | Scripts SQL obligatorios |
| **Cambios de modelo** | Automáticos | Requieren ALTER TABLE |
| **Consultas complejas** | Filtros predefinidos | HQL/JPQL/SQL nativo |
| **Performance** | Excelente para documentos | Excelente para relaciones |
| **Curva de aprendizaje** | Baja | Media-Alta |

### Desventajas del Sistema Actual

| Aspecto | MongoDB (Actual) | SQL + Hibernate |
|---------|------------------|-----------------|
| **Relaciones complejas** | Manual, sin FK | Automáticas con @JoinColumn |
| **Transacciones ACID** | Limitadas | Completo soporte |
| **Consultas ad-hoc** | Requiere código Java | SQL directo |
| **Herramientas BI** | Limitadas | Excelente ecosistema |
| **Validación de esquema** | Opcional | Automática |

---

## 🎯 Resumen del Sistema de Migración y Consultas

### Proceso de "Migración" (Inicialización)

1. **Conexión** → `MongoConfig` establece conexión única (Singleton)
2. **Validación** → `TestDataRunner` verifica conectividad
3. **Población** → Crea objetos Java y los guarda vía servicios/repositorios
4. **Auto-creación** → MongoDB crea colecciones automáticamente al insertar

### Sistema de Consultas

1. **Serialización** → `toDocument()` convierte Java → BSON
2. **Persistencia** → Driver MongoDB guarda documentos
3. **Consulta** → Filtros predefinidos (`eq`, `and`, `or`, etc.)
4. **Deserialización** → Constructor desde `Document` convierte BSON → Java

### Características Clave

- ✅ **Sin esquema fijo** - MongoDB es schema-less
- ✅ **Auto-creación de colecciones** - No requiere DDL
- ✅ **Mapeo manual** - Control total sobre serialización
- ✅ **Filtros type-safe** - `Filters.eq()` vs SQL strings
- ✅ **Flexibilidad** - Fácil agregar campos sin migración
- ✅ **Simplicidad** - Menos configuración que ORM tradicional

Este sistema es ideal para desarrollo ágil donde los requisitos del modelo pueden cambiar frecuentemente, ya que MongoDB se adapta automáticamente sin necesidad de scripts de migración complejos.