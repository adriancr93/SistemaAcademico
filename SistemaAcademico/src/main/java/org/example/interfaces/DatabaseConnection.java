package org.example.interfaces;

/**
 * Interfaz para manejo de conexiones a base de datos
 */
public interface DatabaseConnection {
    
    /**
     * Verificar si la conexión a la base de datos está activa
     * @return true si la conexión es exitosa, false en caso contrario
     */
    boolean isConnected();
    
    /**
     * Probar la conexión a la base de datos
     * @return true si puede conectarse, false en caso contrario
     */
    boolean testConnection();
    
    /**
     * Obtener información del estado de la conexión
     * @return String con detalles de la conexión
     */
    String getConnectionInfo();
    
    /**
     * Cerrar la conexión
     */
    void closeConnection();
}