package org.example.interfaces;

/**
 * Interfaz para operaciones CRUD básicas
 */
public interface CrudOperations {
    
    /**
     * Crear un nuevo elemento
     */
    void crear();
    
    /**
     * Listar todos los elementos
     */
    void listar();
    
    /**
     * Buscar elemento por ID
     */
    void buscarPorId();
    
    /**
     * Actualizar un elemento existente
     */
    void actualizar();
    
    /**
     * Eliminar un elemento
     */
    void eliminar();
    
    /**
     * Mostrar estadísticas
     */
    void mostrarEstadisticas();
}