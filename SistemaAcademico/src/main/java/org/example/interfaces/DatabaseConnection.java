package org.example.interfaces;

public interface DatabaseConnection {
    boolean isConnected(); 
    boolean testConnection();
    String getConnectionInfo();
    void closeConnection();
}