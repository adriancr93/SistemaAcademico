package org.example.interfaces;

/**
 * Interfaz para las vistas del sistema
 */
public interface ViewInterface {
    
    /**
     * Mostrar el menú principal de la vista
     */
    void mostrarMenu();
    
    /**
     * Mostrar el título de la sección
     */
    void mostrarTitulo();
    
    /**
     * Procesar la opción seleccionada por el usuario
     * @param opcion número de opción seleccionada
     * @return true si debe continuar, false si debe salir
     */
    boolean procesarOpcion(int opcion);
}