package org.example.controller;

import org.example.config.MongoConfig;
import org.example.interfaces.DatabaseConnection;
import org.example.view.*;

/**
 * Controlador principal que maneja la navegación entre vistas
 * y valida la conexión a la base de datos
 */
public class MainViewController {
    
    private final DatabaseConnection dbConnection;
    private final EstudianteView estudianteView;
    private final ProfesorView profesorView;
    private final GrupoView grupoView;
    private final CursoView cursoView;
    
    public MainViewController() {
        // Inicializar conexión a base de datos
        this.dbConnection = MongoConfig.getInstance();
        
        // Inicializar vistas
        this.estudianteView = new EstudianteView();
        this.profesorView = new ProfesorView();
        this.grupoView = new GrupoView();
        this.cursoView = new CursoView();
    }
    
    /**
     * Iniciar la aplicación con validación de conexión
     */
    public void iniciarAplicacion() {
        mostrarBienvenida();
        
        // Validar conexión a la base de datos
        if (!validarConexionDB()) {
            mostrarErrorConexion();
            return;
        }
        
        // Mostrar información de conexión
        mostrarInformacionConexion();
        InputHelper.pause();
        
        // Iniciar menú principal
        mostrarMenuPrincipal();
    }
    
    /**
     * Validar que la conexión a la base de datos esté activa
     */
    private boolean validarConexionDB() {
        System.out.println("🔍 Validando conexión a MongoDB Atlas...");
        
        if (dbConnection.isConnected()) {
            System.out.println("✅ Conexión inicial verificada");
            
            if (dbConnection.testConnection()) {
                System.out.println("✅ Prueba de conectividad exitosa");
                return true;
            } else {
                System.out.println("❌ Fallo en prueba de conectividad");
                return false;
            }
        } else {
            System.out.println("❌ No hay conexión activa");
            return false;
        }
    }
    
    /**
     * Mostrar menú principal y manejar navegación
     */
    public void mostrarMenuPrincipal() {
        boolean continuar = true;
        
        while (continuar) {
            mostrarTituloPrincipal();
            mostrarOpcionesMenu();
            
            int opcion = InputHelper.readInt("👉 Seleccione una opción: ");
            
            try {
                switch (opcion) {
                    case 1:
                        navegarAEstudiantes();
                        break;
                    case 2:
                        navegarAProfesores();
                        break;
                    case 3:
                        navegarAGrupos();
                        break;
                    case 4:
                        navegarACursos();
                        break;
                    case 5:
                        mostrarEstadoConexion();
                        break;
                    case 6:
                        mostrarAcercaDe();
                        break;
                    case 0:
                        continuar = confirmarSalida();
                        break;
                    default:
                        System.out.println("❌ Opción no válida. Intente de nuevo.");
                        InputHelper.pause();
                }
            } catch (Exception e) {
                System.out.println("❌ Error inesperado: " + e.getMessage());
                InputHelper.pause();
            }
        }
        
        // Cerrar aplicación
        cerrarAplicacion();
    }
    
    private void navegarAEstudiantes() {
        validarConexionAntes(() -> estudianteView.gestionarEstudiantes());
    }
    
    private void navegarAProfesores() {
        validarConexionAntes(() -> profesorView.mostrarMenu());
    }
    
    private void navegarAGrupos() {
        validarConexionAntes(() -> grupoView.mostrarMenu());
    }
    
    private void navegarACursos() {
        validarConexionAntes(() -> cursoView.mostrarMenu());
    }
    
    /**
     * Validar conexión antes de ejecutar una operación
     */
    private void validarConexionAntes(Runnable operacion) {
        if (!dbConnection.isConnected()) {
            System.out.println("⚠️ Conexión perdida. Intentando reconectar...");
            try {
                MongoConfig.getInstance().reconnect();
                if (dbConnection.isConnected()) {
                    System.out.println("✅ Reconexión exitosa");
                    operacion.run();
                } else {
                    System.out.println("❌ No se pudo reconectar. Operación cancelada.");
                    InputHelper.pause();
                }
            } catch (Exception e) {
                System.out.println("❌ Error en reconexión: " + e.getMessage());
                InputHelper.pause();
            }
        } else {
            operacion.run();
        }
    }
    
    private void mostrarBienvenida() {
        InputHelper.clearScreen();
        System.out.println("╔══════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                    🎓 SISTEMA ACADÉMICO UNIVERSITARIO 📚                      ║");
        System.out.println("║                         Iniciando aplicación...                            ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════════════════╝");
        System.out.println();
    }
    
    private void mostrarTituloPrincipal() {
        InputHelper.clearScreen();
        System.out.println("╔══════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                      🎓 SISTEMA ACADÉMICO UNIVERSITARIO 📚                    ║");
        System.out.println("║                           Gestión Integral de Datos                         ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("                    🔗 Conectado a MongoDB Atlas ☁️");
        System.out.println("               💡 La conexión funciona sin MongoDB Compass");
        System.out.println();
    }
    
    private void mostrarOpcionesMenu() {
        System.out.println("┌─────────────────────────────────────────────────────────────────────────────┐");
        System.out.println("│                              MÓDULOS PRINCIPALES                           │");
        System.out.println("├─────────────────────────────────────────────────────────────────────────────┤");
        System.out.println("│  1. 👥 Gestión de Estudiantes                                                │");
        System.out.println("│  2. 👨‍🏫 Gestión de Profesores                                                │");
        System.out.println("│  3. 📊 Gestión de Grupos                                                     │");
        System.out.println("│  4. 📚 Gestión de Cursos                                                     │");
        System.out.println("├─────────────────────────────────────────────────────────────────────────────┤");
        System.out.println("│  5. 🔍 Verificar conexión DB                                                 │");
        System.out.println("│  6. ℹ️  Acerca del sistema                                                    │");
        System.out.println("│  0. 🚪 Salir del sistema                                                     │");
        System.out.println("└─────────────────────────────────────────────────────────────────────────────┘");
        System.out.println();
    }
    
    private void mostrarInformacionConexion() {
        InputHelper.clearScreen();
        System.out.println("╔══════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                         🔗 INFORMACIÓN DE CONEXIÓN                          ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println(dbConnection.getConnectionInfo());
        System.out.println();
        System.out.println("📝 IMPORTANTE:");
        System.out.println("   • MongoDB Atlas es un servicio en la nube");
        System.out.println("   • No requiere MongoDB Compass para funcionar");
        System.out.println("   • La conexión se mantiene activa durante el uso");
        System.out.println("   • Los datos se sincronizan automáticamente");
        System.out.println();
    }
    
    private void mostrarEstadoConexion() {
        InputHelper.clearScreen();
        MongoConfig.getInstance().mostrarEstadoConexion();
        
        System.out.println("🔧 OPCIONES:");
        System.out.println("1. Probar reconexión");
        System.out.println("0. Volver al menú principal");
        
        int opcion = InputHelper.readInt("Seleccione una opción: ");
        
        if (opcion == 1) {
            System.out.println("🔄 Probando reconexión...");
            MongoConfig.getInstance().reconnect();
            mostrarEstadoConexion();
        }
    }
    
    private void mostrarAcercaDe() {
        InputHelper.clearScreen();
        System.out.println("╔══════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                            ℹ️ ACERCA DEL SISTEMA 📋                           ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════════════════╝");
        System.out.println();
        
        System.out.println("🎓 SISTEMA ACADÉMICO UNIVERSITARIO v2.0");
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println();
        
        System.out.println("🏗️  ARQUITECTURA ACTUALIZADA:");
        System.out.println("   • Interfaces para mejor modularidad");
        System.out.println("   • Vistas autónomas que manejan sus menús");
        System.out.println("   • Controlador principal para navegación");
        System.out.println("   • Validación automática de conexión DB");
        System.out.println();
        
        System.out.println("🔗 CONECTIVIDAD:");
        System.out.println("   • MongoDB Atlas (cluster en la nube)");
        System.out.println("   • Conexión independiente de Compass");
        System.out.println("   • Reconexión automática en caso de fallos");
        System.out.println("   • Timeouts configurados para estabilidad");
        System.out.println();
        
        System.out.println("📦 COMPONENTES:");
        System.out.println("   • DatabaseConnection - Interface de conectividad");
        System.out.println("   • ViewInterface - Interface para vistas");
        System.out.println("   • CrudOperations - Interface para operaciones");
        System.out.println("   • MainViewController - Controlador principal");
        System.out.println();
        
        InputHelper.pause();
    }
    
    private boolean confirmarSalida() {
        return !InputHelper.confirm("❓ ¿Está seguro de que desea salir del sistema?");
    }
    
    private void mostrarErrorConexion() {
        InputHelper.clearScreen();
        System.out.println("╔══════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                            ❌ ERROR DE CONEXIÓN                              ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("⚠️ No se pudo establecer conexión con MongoDB Atlas");
        System.out.println();
        System.out.println("🔧 POSIBLES SOLUCIONES:");
        System.out.println("   1. Verificar conexión a Internet");
        System.out.println("   2. Comprobar credenciales de MongoDB Atlas");
        System.out.println("   3. Verificar que el cluster esté activo");
        System.out.println("   4. Revisar configuración de firewall");
        System.out.println();
        System.out.println("💡 NOTA: El sistema se cerrará por seguridad");
        InputHelper.pause();
    }
    
    private void cerrarAplicacion() {
        InputHelper.clearScreen();
        System.out.println("╔══════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                           👋 ¡HASTA PRONTO! 🎓                              ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("                    🙏 Gracias por usar el Sistema Académico");
        System.out.println("                           💾 Cerrando conexión a la base de datos");
        
        // Cerrar conexión limpiamente
        dbConnection.closeConnection();
        
        System.out.println("                              🔒 Aplicación cerrada con seguridad");
        System.out.println();
        System.out.println("                    ✨ Desarrollado con ❤️ para la educación ✨");
        System.out.println();
    }
}