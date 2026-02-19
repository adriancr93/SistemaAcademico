package org.example.util;

import org.example.config.MongoConfig;
import org.example.model.*;
import org.example.service.impl.*;
import java.time.LocalDate;

/**
 * Clase para ejecutar pruebas de datos y validar el sistema
 */
public class TestDataRunner {
    
    public static void main(String[] args) {
        System.out.println("=== Sistema Académico - Validación de Base de Datos ===\n");
        
        // Validar conexión a la base de datos
        if (validarConexion()) {
            System.out.println("✓ Conexión a MongoDB Atlas exitosa\n");
            
            poblarDatosPrueba();
            ejecutarPruebasBasicas();
            
        } else {
            System.out.println("✗ Error: No se pudo conectar a la base de datos");
            System.out.println("Verifica la configuración de MongoDB Atlas");
        }
    }
    
    private static boolean validarConexion() {
        try {
            MongoConfig config = MongoConfig.getInstance();
            return config.testConnection();
        } catch (Exception e) {
            System.err.println("Error validando conexión: " + e.getMessage());
            return false;
        }
    }
    
    private static void poblarDatosPrueba() {
        System.out.println("=== Poblando Base de Datos con Datos de Prueba ===\n");
        
        try {
            crearEstudiantesPrueba();
            crearProfesoresPrueba();
            crearCursosPrueba();
            crearGruposPrueba();
            
            System.out.println("✓ Datos de prueba creados exitosamente\n");
            
        } catch (Exception e) {
            System.err.println("Error poblando datos: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void crearEstudiantesPrueba() {
        EstudianteServiceImpl estudianteService = new EstudianteServiceImpl();
        
        System.out.println("Creando estudiantes...");
        
        Estudiante[] estudiantes = {
            new Estudiante("Juan Carlos Pérez", "123456789", 
                "juan.perez@estudiante.ucenfotec.ac.cr", LocalDate.of(1995, 3, 15), "activo"),
            new Estudiante("María Elena González", "234567890", 
                "maria.gonzalez@estudiante.ucenfotec.ac.cr", LocalDate.of(1996, 7, 22), "activo"),
            new Estudiante("Roberto Antonio Silva", "345678901", 
                "roberto.silva@estudiante.ucenfotec.ac.cr", LocalDate.of(1994, 11, 8), "activo"),
            new Estudiante("Ana Patricia Ramírez", "456789012", 
                "ana.ramirez@estudiante.ucenfotec.ac.cr", LocalDate.of(1997, 1, 30), "activo"),
            new Estudiante("Carlos Eduardo Morales", "567890123", 
                "carlos.morales@estudiante.ucenfotec.ac.cr", LocalDate.of(1995, 9, 14), "inactivo")
        };
        
        for (Estudiante estudiante : estudiantes) {
            try {
                estudianteService.crearEstudiante(estudiante);
                System.out.println("  ✓ Estudiante creado: " + estudiante.getNombre());
            } catch (Exception e) {
                System.out.println("  ✗ Error creando estudiante " + estudiante.getNombre() + ": " + e.getMessage());
            }
        }
        System.out.println();
    }
    
    private static void crearProfesoresPrueba() {
        ProfesorServiceImpl profesorService = new ProfesorServiceImpl();
        
        System.out.println("Creando profesores...");
        
        Profesor[] profesores = {
            new Profesor("Dr. Roberto Fernández", "100234567", "roberto.fernandez@ucenfotec.ac.cr", 
                "Ingeniería de Software", "activo"),
            new Profesor("MSc. Laura Jiménez", "200345678", "laura.jimenez@ucenfotec.ac.cr", 
                "Ciencias de la Computación", "activo"),
            new Profesor("Ing. Miguel Ángel Castro", "300456789", "miguel.castro@ucenfotec.ac.cr", 
                "Desarrollo Web", "activo"),
            new Profesor("Dra. Carmen Solís", "400567890", "carmen.solis@ucenfotec.ac.cr", 
                "Base de Datos", "activo"),
            new Profesor("Lic. Fernando Mora", "500678901", "fernando.mora@ucenfotec.ac.cr", 
                "Programación", "inactivo")
        };
        
        for (Profesor profesor : profesores) {
            try {
                profesorService.crearProfesor(profesor);
                System.out.println("  ✓ Profesor creado: " + profesor.getNombre());
            } catch (Exception e) {
                System.out.println("  ✗ Error creando profesor " + profesor.getNombre() + ": " + e.getMessage());
            }
        }
        System.out.println();
    }
    
    private static void crearCursosPrueba() {
        CursoServiceImpl cursoService = new CursoServiceImpl();
        
        System.out.println("Creando cursos...");
        
        Curso[] cursos = {
            new Curso("Programación I", "PRG101", "Introducción a la programación", 3),
            new Curso("Programación II", "PRG102", "Programación orientada a objetos", 4),
            new Curso("Base de Datos I", "BDD101", "Fundamentos de bases de datos", 3),
            new Curso("Desarrollo Web", "WEB101", "Desarrollo de aplicaciones web", 4),
            new Curso("Ingeniería de Software", "ING201", "Metodologías de desarrollo", 3)
        };
        
        for (Curso curso : cursos) {
            try {
                cursoService.crearCurso(curso);
                System.out.println("  ✓ Curso creado: " + curso.getNombre());
            } catch (Exception e) {
                System.out.println("  ✗ Error creando curso " + curso.getNombre() + ": " + e.getMessage());
            }
        }
        System.out.println();
    }
    
    private static void crearGruposPrueba() {
        GrupoServiceImpl grupoService = new GrupoServiceImpl();
        
        System.out.println("Creando grupos...");
        
        Grupo[] grupos = {
            createGrupo("Grupo 1 Programación I", "Grupo para el curso PRG101"),
            createGrupo("Grupo 1 Programación II", "Grupo para el curso PRG102"),
            createGrupo("Grupo 1 Base de Datos I", "Grupo para el curso BDD101"),
            createGrupo("Grupo 1 Desarrollo Web", "Grupo para el curso WEB101"),
            createGrupo("Grupo 1 Ingeniería de Software", "Grupo para el curso ING201")
        };
        
        for (Grupo grupo : grupos) {
            try {
                grupoService.crearGrupo(grupo);
                System.out.println("  ✓ Grupo creado: " + grupo.getNombre());
            } catch (Exception e) {
                System.out.println("  ✗ Error creando grupo " + grupo.getNombre() + ": " + e.getMessage());
            }
        }
        System.out.println();
    }
    
    private static Grupo createGrupo(String nombre, String descripcion) {
        Grupo grupo = new Grupo();
        grupo.setNombre(nombre);
        grupo.setDescripcion(descripcion);
        grupo.setEstado("activo");
        return grupo;
    }
    
    private static void ejecutarPruebasBasicas() {
        System.out.println("=== Ejecutando Pruebas Básicas del Sistema ===\n");
        
        // Probar lectura de estudiantes
        probarLecturaEstudiantes();
        
        // Probar lectura de profesores
        probarLecturaProfesores();
        
        // Probar lectura de cursos
        probarLecturaCursos();
        
        // Probar lectura de grupos
        probarLecturaGrupos();
        
        System.out.println("✓ Todas las pruebas básicas completadas\n");
    }
    
    private static void probarLecturaEstudiantes() {
        try {
            EstudianteServiceImpl service = new EstudianteServiceImpl();
            var estudiantes = service.obtenerTodosLosEstudiantes();
            System.out.println("📚 Total de estudiantes en BD: " + estudiantes.size());
            
            if (!estudiantes.isEmpty()) {
                System.out.println("  Primer estudiante: " + estudiantes.get(0).getNombre());
            }
        } catch (Exception e) {
            System.err.println("Error leyendo estudiantes: " + e.getMessage());
        }
    }
    
    private static void probarLecturaProfesores() {
        try {
            ProfesorServiceImpl service = new ProfesorServiceImpl();
            var profesores = service.obtenerTodosLosProfesores();
            System.out.println("👨‍🏫 Total de profesores en BD: " + profesores.size());
            
            if (!profesores.isEmpty()) {
                System.out.println("  Primer profesor: " + profesores.get(0).getNombre());
            }
        } catch (Exception e) {
            System.err.println("Error leyendo profesores: " + e.getMessage());
        }
    }
    
    private static void probarLecturaCursos() {
        try {
            CursoServiceImpl service = new CursoServiceImpl();
            var cursos = service.obtenerTodosLosCursos();
            System.out.println("📖 Total de cursos en BD: " + cursos.size());
            
            if (!cursos.isEmpty()) {
                System.out.println("  Primer curso: " + cursos.get(0).getNombre());
            }
        } catch (Exception e) {
            System.err.println("Error leyendo cursos: " + e.getMessage());
        }
    }
    
    private static void probarLecturaGrupos() {
        try {
            GrupoServiceImpl service = new GrupoServiceImpl();
            var grupos = service.obtenerTodosLosGrupos();
            System.out.println("👥 Total de grupos en BD: " + grupos.size());
            
            if (!grupos.isEmpty()) {
                System.out.println("  Primer grupo: " + grupos.get(0).getNombre());
            }
        } catch (Exception e) {
            System.err.println("Error leyendo grupos: " + e.getMessage());
        }
    }
}