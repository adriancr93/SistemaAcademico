package org.example.service;

import org.example.model.Profesor;
import org.bson.types.ObjectId;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz de servicio para operaciones de negocio de Profesor
 */
public interface ProfesorService {
    
    /**
     * Crear un nuevo profesor
     */
    Profesor crearProfesor(Profesor profesor) throws IllegalArgumentException;
    
    /**
     * Obtener profesor por ID
     */
    Optional<Profesor> obtenerProfesorPorId(ObjectId id);
    
    /**
     * Obtener todos los profesores
     */
    List<Profesor> obtenerTodosLosProfesores();
    
    /**
     * Actualizar un profesor existente
     */
    boolean actualizarProfesor(Profesor profesor) throws IllegalArgumentException;
    
    /**
     * Eliminar un profesor por ID
     */
    boolean eliminarProfesor(ObjectId id);
    
    /**
     * Buscar profesor por identificación
     */
    Optional<Profesor> buscarPorIdentificacion(String identificacion);
    
    /**
     * Buscar profesor por email
     */
    Optional<Profesor> buscarPorEmail(String email);
    
    /**
     * Buscar profesores por departamento
     */
    List<Profesor> buscarPorDepartamento(String departamento);
    
    /**
     * Contar total de profesores
     */
    long contarProfesores();
}