package org.example.service;

import org.example.model.Curso;
import org.bson.types.ObjectId;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz de servicio para operaciones de negocio de Curso
 */
public interface CursoService {
    
    /**
     * Crear un nuevo curso
     */
    Curso crearCurso(Curso curso) throws IllegalArgumentException;
    
    /**
     * Obtener curso por ID
     */
    Optional<Curso> obtenerCursoPorId(ObjectId id);
    
    /**
     * Obtener todos los cursos
     */
    List<Curso> obtenerTodosLosCursos();
    
    /**
     * Actualizar un curso existente
     */
    boolean actualizarCurso(Curso curso) throws IllegalArgumentException;
    
    /**
     * Eliminar un curso por ID
     */
    boolean eliminarCurso(ObjectId id);
    
    /**
     * Buscar cursos por nombre (coincidencia parcial)
     */
    List<Curso> buscarPorNombre(String nombre);
    
    /**
     * Buscar cursos por cantidad de créditos
     */
    List<Curso> buscarPorCreditos(int creditos);
    
    /**
     * Contar total de cursos
     */
    long contarCursos();
}