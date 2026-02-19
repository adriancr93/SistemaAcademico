package org.example.repository;

import org.example.model.Estudiante;
import org.bson.types.ObjectId;
import java.util.Optional;

/**
 * Interfaz específica para operaciones de Estudiante
 */
public interface EstudianteRepository extends Repository<Estudiante> {
    
    /**
     * Buscar estudiante por número de identificación
     */
    Optional<Estudiante> findByIdentificacion(String identificacion);
    
    /**
     * Buscar estudiante por email
     */
    Optional<Estudiante> findByEmail(String email);
    
    /**
     * Verificar si existe un estudiante con la identificación dada (excluyendo un ID específico)
     */
    boolean existsByIdentificacionAndNotId(String identificacion, ObjectId excludeId);
    
    /**
     * Verificar si existe un estudiante con el email dado (excluyendo un ID específico)
     */
    boolean existsByEmailAndNotId(String email, ObjectId excludeId);
}