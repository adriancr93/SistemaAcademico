package org.example.service;

import org.example.model.Estudiante;
import org.bson.types.ObjectId;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz de servicio para operaciones de negocio de Estudiante
 */
public interface EstudianteService {
    
    /**
     * Crear un nuevo estudiante
     */
    Estudiante crearEstudiante(Estudiante estudiante) throws IllegalArgumentException;
    
    /**
     * Obtener estudiante por ID
     */
    Optional<Estudiante> obtenerEstudiantePorId(ObjectId id);
    
    /**
     * Obtener todos los estudiantes
     */
    List<Estudiante> obtenerTodosLosEstudiantes();
    
    /**
     * Actualizar un estudiante existente
     */
    boolean actualizarEstudiante(Estudiante estudiante) throws IllegalArgumentException;
    
    /**
     * Eliminar un estudiante por ID
     */
    boolean eliminarEstudiante(ObjectId id);
    
    /**
     * Buscar estudiante por identificación
     */
    Optional<Estudiante> buscarPorIdentificacion(String identificacion);
    
    /**
     * Buscar estudiante por email
     */
    Optional<Estudiante> buscarPorEmail(String email);
    
    /**
     * Contar total de estudiantes
     */
    long contarEstudiantes();
}