package org.example.service;

import org.example.model.GrupoCurso;
import org.bson.types.ObjectId;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz de servicio para operaciones de negocio de GrupoCurso
 */
public interface GrupoCursoService {
    
    /**
     * Crear una nueva relación grupo-curso
     */
    GrupoCurso crearRelacion(ObjectId grupoId, ObjectId cursoId) throws IllegalArgumentException;
    
    /**
     * Obtener relación por ID
     */
    Optional<GrupoCurso> obtenerRelacionPorId(ObjectId id);
    
    /**
     * Obtener todas las relaciones
     */
    List<GrupoCurso> obtenerTodasLasRelaciones();
    
    /**
     * Eliminar una relación por ID
     */
    boolean eliminarRelacion(ObjectId id);
    
    /**
     * Obtener todas las relaciones de un grupo
     */
    List<GrupoCurso> obtenerRelacionesPorGrupo(ObjectId grupoId);
    
    /**
     * Obtener todas las relaciones de un curso
     */
    List<GrupoCurso> obtenerRelacionesPorCurso(ObjectId cursoId);
    
    /**
     * Verificar si existe una relación entre un grupo y un curso
     */
    boolean existeRelacion(ObjectId grupoId, ObjectId cursoId);
    
    /**
     * Contar total de relaciones
     */
    long contarRelaciones();
}