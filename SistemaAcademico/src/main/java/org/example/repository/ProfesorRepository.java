package org.example.repository;

import org.example.model.Profesor;
import org.bson.types.ObjectId;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz específica para operaciones de Profesor
 */
public interface ProfesorRepository extends Repository<Profesor> {
    
    /**
     * Buscar profesor por número de identificación
     */
    Optional<Profesor> findByIdentificacion(String identificacion);
    
    /**
     * Buscar profesor por email
     */
    Optional<Profesor> findByEmail(String email);
    
    /**
     * Buscar profesores por departamento
     */
    List<Profesor> findByDepartamento(String departamento);
    
    /**
     * Verificar si existe un profesor con la identificación dada (excluyendo un ID específico)
     */
    boolean existsByIdentificacionAndNotId(String identificacion, ObjectId excludeId);
    
    /**
     * Verificar si existe un profesor con el email dado (excluyendo un ID específico)
     */
    boolean existsByEmailAndNotId(String email, ObjectId excludeId);
}