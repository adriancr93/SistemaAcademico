package org.example.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.example.interfaces.DatabaseConnection;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import java.util.concurrent.TimeUnit;

/**
 * Configuración de conexión a MongoDB usando patrón Singleton
 * Implementa la interfaz DatabaseConnection para validación de conexión
 */
public class MongoConfig implements DatabaseConnection {
    private static MongoConfig instance;
    private MongoClient mongoClient;
    private MongoDatabase database;
    private boolean connectionActive = false;
    
    // Configuración de conexión
    private static final String CONNECTION_STRING = "mongodb+srv://root:aobregonr1918@cluster0.oofaaro.mongodb.net/";
    private static final String DATABASE_NAME = "sistema_academico";
    
    // Constructor privado para Singleton
    private MongoConfig() {
        inicializarConexion();
    }
    
    // Método para obtener la instancia única (Singleton)
    public static MongoConfig getInstance() {
        if (instance == null) {
            synchronized (MongoConfig.class) {
                if (instance == null) {
                    instance = new MongoConfig();
                }
            }
        }
        return instance;
    }
    
    /**
     * Inicializar la conexión a MongoDB
     */
    private void inicializarConexion() {
        try {
            // Configurar timeout y opciones de conexión
            ConnectionString connectionString = new ConnectionString(CONNECTION_STRING);
            MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .applyToSocketSettings(builder ->
                    builder.connectTimeout(10, TimeUnit.SECONDS)
                           .readTimeout(10, TimeUnit.SECONDS))
                .build();
                
            this.mongoClient = MongoClients.create(settings);
            this.database = mongoClient.getDatabase(DATABASE_NAME);
            
            // Verificar conexión
            if (testConnection()) {
                this.connectionActive = true;
                System.out.println("✅ Conexión establecida con MongoDB Atlas exitosamente");
                System.out.println("🔗 Base de datos: " + DATABASE_NAME);
            } else {
                this.connectionActive = false;
                System.err.println("❌ No se pudo verificar la conexión con MongoDB Atlas");
            }
            
        } catch (Exception e) {
            this.connectionActive = false;
            System.err.println("❌ Error al conectar con MongoDB: " + e.getMessage());
            throw new RuntimeException("No se pudo establecer conexión con la base de datos", e);
        }
    }
    
    // Método para obtener la instancia de la base de datos
    public MongoDatabase getDatabase() {
        if (!connectionActive) {
            throw new RuntimeException("La conexión a la base de datos no está activa");
        }
        return database;
    }
    
    // Implementación de la interfaz DatabaseConnection
    @Override
    public boolean isConnected() {
        return connectionActive && mongoClient != null;
    }
    
    @Override
    public boolean testConnection() {
        try {
            if (mongoClient == null) {
                return false;
            }
            
            // Intentar hacer una operación simple para verificar conectividad
            Document pingResult = mongoClient.getDatabase("admin")
                .runCommand(new Document("ping", 1));
                
            // Verificar el resultado del ping - puede ser Integer o Double
            Object okValue = pingResult.get("ok");
            return (okValue != null && (
                (okValue instanceof Integer && ((Integer) okValue) == 1) ||
                (okValue instanceof Double && ((Double) okValue) == 1.0)
            ));
            
        } catch (Exception e) {
            System.err.println("❌ Error en prueba de conexión: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public String getConnectionInfo() {
        if (isConnected()) {
            return String.format(
                "🔗 MongoDB Atlas Connected\n" +
                "📊 Database: %s\n" +
                "🌐 Cluster: cluster0.oofaaro.mongodb.net\n" +
                "✅ Status: Active\n" +
                "💡 Nota: La conexión funciona sin MongoDB Compass",
                DATABASE_NAME
            );
        } else {
            return "❌ MongoDB Atlas Disconnected";
        }
    }
    
    @Override
    public void closeConnection() {
        try {
            if (mongoClient != null) {
                mongoClient.close();
                connectionActive = false;
                System.out.println("🔒 Conexión cerrada exitosamente");
            }
        } catch (Exception e) {
            System.err.println("❌ Error al cerrar conexión: " + e.getMessage());
        }
    }
    
    /**
     * Método para reconectar si se pierde la conexión
     */
    public void reconnect() {
        System.out.println("🔄 Intentando reconexión...");
        closeConnection();
        inicializarConexion();
    }
    
    /**
     * Verificar y mostrar el estado de la conexión
     */
    public void mostrarEstadoConexion() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🔍 ESTADO DE CONEXIÓN A MONGODB ATLAS");
        System.out.println("=".repeat(60));
        System.out.println(getConnectionInfo());
        
        if (testConnection()) {
            System.out.println("🎯 Test de conectividad: ✅ EXITOSO");
        } else {
            System.out.println("🎯 Test de conectividad: ❌ FALLIDO");
        }
        
        System.out.println("=".repeat(60) + "\n");
    }
}