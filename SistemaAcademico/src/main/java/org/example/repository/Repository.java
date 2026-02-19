package org.example.repository;

import org.bson.types.ObjectId;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz genérica para operaciones CRUD
 */
public interface Repository<T> {
    
    /**
     * Insertar un nuevo registro
     */
    T save(T entity);
    
    /**
     * Buscar por ID
     */
    Optional<T> findById(ObjectId id);
    
    /**
     * Obtener todos los registros
     */
    List<T> findAll();
    
    /**
     * Actualizar un registro existente
     */
    boolean update(T entity);
    
    /**
     * Eliminar un registro por ID
     */
    boolean deleteById(ObjectId id);
    
    /**
     * Verificar si existe un registro por ID
     */
    boolean existsById(ObjectId id);
    
    /**
     * Contar total de registros
     */
    long count();
}