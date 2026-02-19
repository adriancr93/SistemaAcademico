package org.example.repository;

import org.example.model.Grupo;
import org.bson.types.ObjectId;
import java.util.Optional;

/**
 * Interfaz específica para operaciones de Grupo
 */
public interface GrupoRepository extends Repository<Grupo> {
    
    /**
     * Buscar grupo por nombre
     */
    Optional<Grupo> findByNombre(String nombre);
    
    /**
     * Verificar si existe un grupo con el nombre dado (excluyendo un ID específico)
     */
    boolean existsByNombreAndNotId(String nombre, ObjectId excludeId);
}