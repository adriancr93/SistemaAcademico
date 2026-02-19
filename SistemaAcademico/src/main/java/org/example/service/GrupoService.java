package org.example.service;

import org.example.model.Grupo;
import org.bson.types.ObjectId;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz de servicio para operaciones de negocio de Grupo
 */
public interface GrupoService {
    
    /**
     * Crear un nuevo grupo
     */
    Grupo crearGrupo(Grupo grupo) throws IllegalArgumentException;
    
    /**
     * Obtener grupo por ID
     */
    Optional<Grupo> obtenerGrupoPorId(ObjectId id);
    
    /**
     * Obtener todos los grupos
     */
    List<Grupo> obtenerTodosLosGrupos();
    
    /**
     * Actualizar un grupo existente
     */
    boolean actualizarGrupo(Grupo grupo) throws IllegalArgumentException;
    
    /**
     * Eliminar un grupo por ID
     */
    boolean eliminarGrupo(ObjectId id);
    
    /**
     * Buscar grupo por nombre
     */
    Optional<Grupo> buscarPorNombre(String nombre);
    
    /**
     * Contar total de grupos
     */
    long contarGrupos();
}