package org.example.repository;

import org.example.model.Curso;
import org.bson.types.ObjectId;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz específica para operaciones de Curso
 */
public interface CursoRepository extends Repository<Curso> {
    
    /**
     * Buscar curso por nombre exacto
     */
    Optional<Curso> findByNombre(String nombre);
    
    /**
     * Buscar cursos por nombre (coincidencia parcial)
     */
    List<Curso> findByNombreContaining(String nombre);
    
    /**
     * Buscar cursos por cantidad de créditos
     */
    List<Curso> findByCreditos(int creditos);
    
    /**
     * Verificar si existe un curso con el código dado
     */
    boolean existsByCodigo(String codigo);
    
    /**
     * Verificar si existe un curso con el código dado (excluyendo un ID específico)
     */
    boolean existsByCodigoAndNotId(String codigo, ObjectId excludeId);
    
    /**
     * Verificar si existe un curso con el nombre dado (excluyendo un ID específico)
     */
    boolean existsByNombreAndNotId(String nombre, ObjectId excludeId);
}