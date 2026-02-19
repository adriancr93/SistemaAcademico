package org.example.repository;

import org.example.model.GrupoCurso;
import org.bson.types.ObjectId;
import java.util.List;

/**
 * Interfaz específica para operaciones de GrupoCurso
 */
public interface GrupoCursoRepository extends Repository<GrupoCurso> {
    
    /**
     * Buscar todas las relaciones de un grupo específico
     */
    List<GrupoCurso> findByGrupoId(ObjectId grupoId);
    
    /**
     * Buscar todas las relaciones de un curso específico
     */
    List<GrupoCurso> findByCursoId(ObjectId cursoId);
    
    /**
     * Verificar si existe una relación entre un grupo y un curso
     */
    boolean existsByGrupoIdAndCursoId(ObjectId grupoId, ObjectId cursoId);
    
    /**
     * Eliminar todas las relaciones de un grupo
     */
    boolean deleteByGrupoId(ObjectId grupoId);
    
    /**
     * Eliminar todas las relaciones de un curso
     */
    boolean deleteByCursoId(ObjectId cursoId);
}