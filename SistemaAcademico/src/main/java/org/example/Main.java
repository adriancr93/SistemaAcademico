package org.example;

import org.example.controller.MainViewController;

/**
 * Clase principal para iniciar el Sistema Académico
 * Ahora utiliza el nuevo controlador principal con validación de conexión
 * 
 * @author Adrian Obregon  
 * @version 2.0
 * @since 2026-02-18
 */
public class Main {
    
    /**
     * Método principal de la aplicación
     * 
     * @param args argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        try {
            // Inicializar el controlador principal
            MainViewController mainViewController = new MainViewController();
            
            // Iniciar la aplicación con validación de conexión
            mainViewController.iniciarAplicacion();
            
        } catch (Exception e) {
            System.err.println("❌ Error fatal en la aplicación: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}