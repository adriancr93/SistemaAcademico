package org.example.view;

import org.bson.types.ObjectId;
import org.example.model.Estudiante;
import org.example.service.EstudianteService;
import org.example.service.ServiceFactory;
import org.example.util.ValidationUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Vista para la gestión de estudiantes.
 * Ahora obtiene el servicio desde ServiceFactory en vez de instanciar directamente.
 */
public class EstudianteView {
    
    private final EstudianteService estudianteService;
    
    // Antes: this.estudianteService = new EstudianteServiceImpl();
    // Ahora usa ServiceFactory para desacoplar la vista del servicio concreto
    public EstudianteView() {
        this.estudianteService = ServiceFactory.getInstance().getEstudianteService();
    }
    
    /**
     * Punto de entrada principal para gestionar estudiantes
     */
    public void gestionarEstudiantes() {
        boolean continuar = true;
        while (continuar) {
            mostrarMenu();
            int opcion = InputHelper.readInt("👉 Seleccione una opción: ");
            continuar = procesarOpcion(opcion);
            System.out.println(); // Espacio entre operaciones
        }
    }
    
    public void mostrarMenu() {
        mostrarTitulo();
        
        System.out.println("┌─────────────────────────────────────────────────────────────────────────────┐");
        System.out.println("│  1. ➕ Crear nuevo estudiante                                                │");
        System.out.println("│  2. 📋 Listar todos los estudiantes                                          │");
        System.out.println("│  3. 🔍 Buscar estudiante por identificación                                  │");
        System.out.println("│  4. 📧 Buscar estudiante por email                                           │");
        System.out.println("│  5. ✏️  Actualizar estudiante                                                 │");
        System.out.println("│  6. 🗑️  Eliminar estudiante                                                  │");
        System.out.println("│  0. ⬅️  Volver al menú principal                                              │");
        System.out.println("└─────────────────────────────────────────────────────────────────────────────┘");
        System.out.println();
    }
    
    public void mostrarTitulo() {
        System.out.println("╔══════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                           👥 GESTIÓN DE ESTUDIANTES 🎓                       ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════════════════╝");
        System.out.println();
    }
    
    public boolean procesarOpcion(int opcion) {
        switch (opcion) {
            case 1: crear(); break;
            case 2: listar(); break;
            case 3: buscarPorIdentificacion(); break;
            case 4: buscarPorEmail(); break;
            case 5: actualizar(); break;
            case 6: eliminar(); break;
            case 0: return false;
            default:
                System.out.println("❌ Opción no válida. Intente nuevamente.");
                InputHelper.pause();
        }
        return true;
    }
    
    /**
     * Crear un nuevo estudiante
     */
    public void crear() {
        System.out.println("════════════════════════════ CREAR NUEVO ESTUDIANTE ════════════════════════════");
        System.out.println();
        
        try {
            String nombre = InputHelper.readLine("👤 Nombre completo: ");
            String identificacion = InputHelper.readLine("🆔 Identificación: ");
            String email = InputHelper.readLine("📧 Email: ");
            String fechaNacimiento = InputHelper.readLine("📅 Fecha nacimiento (YYYY-MM-DD) [2006-02-18]: ", "2006-02-18");
            
            // Validar datos
            if (!ValidationUtil.isValidEmail(email)) {
                System.out.println("❌ Email no válido");
                InputHelper.pause();
                return;
            }
            
            try {
                LocalDate fecha = ValidationUtil.parseDate(fechaNacimiento);
                if (!ValidationUtil.isValidBirthDate(fecha)) {
                    System.out.println("❌ Fecha de nacimiento no válida");
                    InputHelper.pause();
                    return;
                }
            } catch (IllegalArgumentException e) {
                System.out.println("❌ Fecha no válida. Use formato YYYY-MM-DD: " + e.getMessage());
                InputHelper.pause();
                return;
            }
            
            // Verificar si ya existe la identificación
            if (estudianteService.buscarPorIdentificacion(identificacion).isPresent()) {
                System.out.println("❌ Ya existe un estudiante con esa identificación");
                InputHelper.pause();
                return;
            }
            
            Estudiante estudiante = new Estudiante(
                nombre,
                identificacion,
                email,
                ValidationUtil.parseDate(fechaNacimiento),
                "activo"
            );
            
            Estudiante estudianteCreado = estudianteService.crearEstudiante(estudiante);
            System.out.println("✅ ¡Estudiante creado exitosamente!");
            System.out.println();
            mostrarDetallesEstudiante(estudianteCreado);
            
        } catch (Exception e) {
            System.out.println("❌ Error al crear estudiante: " + e.getMessage());
        }
        
        InputHelper.pause();
    }
    
    /**
     * Listar todos los estudiantes
     */
    public void listar() {
        System.out.println("═════════════════════════════ LISTA DE ESTUDIANTES ═════════════════════════════");
        System.out.println();
        
        try {
            List<Estudiante> estudiantes = estudianteService.obtenerTodosLosEstudiantes();
            
            if (estudiantes.isEmpty()) {
                System.out.println("📭 No hay estudiantes registrados");
            } else {
                System.out.println("📊 Total de estudiantes: " + estudiantes.size());
                System.out.println();
                
                int contador = 1;
                for (Estudiante estudiante : estudiantes) {
                    System.out.printf("%d. %s (%s)%n", 
                        contador++, 
                        estudiante.getNombre(), 
                        estudiante.getIdentificacion()
                    );
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Error al listar estudiantes: " + e.getMessage());
        }
        
        InputHelper.pause();
    }
    
    /**
     * Buscar estudiante por identificación
     */
    public void buscarPorIdentificacion() {
        System.out.println("═════════════════════ BUSCAR ESTUDIANTE POR IDENTIFICACIÓN ═════════════════════");
        System.out.println();
        
        try {
            String identificacion = InputHelper.readLine("🆔 Ingrese la identificación: ");
            Optional<Estudiante> estudianteOpt = estudianteService.buscarPorIdentificacion(identificacion);
            
            if (estudianteOpt.isPresent()) {
                System.out.println("✅ Estudiante encontrado:");
                System.out.println();
                mostrarDetallesEstudiante(estudianteOpt.get());
            } else {
                System.out.println("❌ No se encontró estudiante con esa identificación");
            }
        } catch (Exception e) {
            System.out.println("❌ Error en la búsqueda: " + e.getMessage());
        }
        
        InputHelper.pause();
    }
    
    /**
     * Buscar estudiante por email
     */
    public void buscarPorEmail() {
        System.out.println("══════════════════════ BUSCAR ESTUDIANTE POR EMAIL ══════════════════════");
        System.out.println();
        
        try {
            String email = InputHelper.readLine("📧 Ingrese el email: ");
            Optional<Estudiante> estudianteOpt = estudianteService.buscarPorEmail(email);
            
            if (estudianteOpt.isPresent()) {
                System.out.println("✅ Estudiante encontrado:");
                System.out.println();
                mostrarDetallesEstudiante(estudianteOpt.get());
            } else {
                System.out.println("❌ No se encontró estudiante con ese email");
            }
        } catch (Exception e) {
            System.out.println("❌ Error en la búsqueda: " + e.getMessage());
        }
        
        InputHelper.pause();
    }
    
    /**
     * Actualizar un estudiante existente
     */
    public void actualizar() {
        System.out.println("═════════════════════════ ACTUALIZAR ESTUDIANTE ═════════════════════════");
        System.out.println();
        
        try {
            String identificacion = InputHelper.readLine("🆔 Ingrese la identificación del estudiante a actualizar: ");
            Optional<Estudiante> estudianteOpt = estudianteService.buscarPorIdentificacion(identificacion);
            
            if (!estudianteOpt.isPresent()) {
                System.out.println("❌ No se encontró estudiante con esa identificación");
                InputHelper.pause();
                return;
            }
            
            Estudiante estudiante = estudianteOpt.get();
            
            System.out.println("📝 Datos actuales:");
            mostrarDetallesEstudiante(estudiante);
            System.out.println();
            System.out.println("📝 Ingrese los nuevos datos (Enter para mantener el actual):");
            
            String nuevoNombre = InputHelper.readLine("👤 Nuevo nombre [" + estudiante.getNombre() + "]: ", estudiante.getNombre());
            String nuevaIdentificacion = InputHelper.readLine("🆔 Nueva identificación [" + estudiante.getIdentificacion() + "]: ", estudiante.getIdentificacion());
            String nuevoEmail = InputHelper.readLine("📧 Nuevo email [" + estudiante.getEmail() + "]: ", estudiante.getEmail());
            String nuevaFechaNacimiento = InputHelper.readLine("📅 Nueva fecha nacimiento [" + 
                (estudiante.getFechaNacimiento() != null ? estudiante.getFechaNacimiento().toString() : "N/A") + "]: ", 
                estudiante.getFechaNacimiento() != null ? estudiante.getFechaNacimiento().toString() : "2006-02-18");
            String nuevoEstado = InputHelper.readLine("📊 Nuevo estado [" + estudiante.getEstado() + "]: ", estudiante.getEstado());
            
            // Validaciones
            if (!ValidationUtil.isValidEmail(nuevoEmail)) {
                System.out.println("❌ Email no válido");
                InputHelper.pause();
                return;
            }
            
            try {
                LocalDate fecha = ValidationUtil.parseDate(nuevaFechaNacimiento);
                if (!ValidationUtil.isValidBirthDate(fecha)) {
                    System.out.println("❌ Fecha de nacimiento no válida");
                    InputHelper.pause();
                    return;
                }
            } catch (IllegalArgumentException e) {
                System.out.println("❌ Fecha no válida: " + e.getMessage());
                InputHelper.pause();
                return;
            }
            
            // Verificar que la nueva identificación no esté en uso por otro estudiante
            if (!nuevaIdentificacion.equals(estudiante.getIdentificacion())) {
                Optional<Estudiante> otroEstudiante = estudianteService.buscarPorIdentificacion(nuevaIdentificacion);
                if (otroEstudiante.isPresent()) {
                    System.out.println("❌ Ya existe otro estudiante con esa identificación");
                    InputHelper.pause();
                    return;
                }
            }
            
            // Actualizar datos
            estudiante.setNombre(nuevoNombre);
            estudiante.setIdentificacion(nuevaIdentificacion);
            estudiante.setEmail(nuevoEmail);
            estudiante.setFechaNacimiento(ValidationUtil.parseDate(nuevaFechaNacimiento));
            estudiante.setEstado(nuevoEstado);
            
            boolean actualizado = estudianteService.actualizarEstudiante(estudiante);
            if (actualizado) {
                System.out.println("✅ ¡Estudiante actualizado exitosamente!");
                System.out.println();
                mostrarDetallesEstudiante(estudiante);
            } else {
                System.out.println("❌ No se pudo actualizar el estudiante");
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error al actualizar estudiante: " + e.getMessage());
        }
        
        InputHelper.pause();
    }
    
    /**
     * Eliminar un estudiante
     */
    public void eliminar() {
        System.out.println("════════════════════════════ ELIMINAR ESTUDIANTE ════════════════════════════");
        System.out.println();
        
        try {
            String identificacion = InputHelper.readLine("🆔 Ingrese la identificación del estudiante a eliminar: ");
            Optional<Estudiante> estudianteOpt = estudianteService.buscarPorIdentificacion(identificacion);
            
            if (!estudianteOpt.isPresent()) {
                System.out.println("❌ No se encontró estudiante con esa identificación");
                InputHelper.pause();
                return;
            }
            
            Estudiante estudiante = estudianteOpt.get();
            
            System.out.println("📝 Datos del estudiante a eliminar:");
            mostrarDetallesEstudiante(estudiante);
            System.out.println();
            
            boolean confirmar = InputHelper.confirm("⚠️ ¿Está seguro de que desea eliminar este estudiante? (s/N): ");
            
            if (confirmar) {
                boolean eliminado = estudianteService.eliminarEstudiante(estudiante.getId());
                if (eliminado) {
                    System.out.println("✅ ¡Estudiante eliminado exitosamente!");
                } else {
                    System.out.println("❌ No se pudo eliminar el estudiante");
                }
            } else {
                System.out.println("❌ Eliminación cancelada");
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error al eliminar estudiante: " + e.getMessage());
        }
        
        InputHelper.pause();
    }
    
    /**
     * Mostrar detalles de un estudiante en formato tabla
     */
    private void mostrarDetallesEstudiante(Estudiante estudiante) {
        System.out.println("┌─────────────────────────────────────────────────────────────────────────────┐");
        System.out.println("│                           DETALLES DEL ESTUDIANTE                          │");
        System.out.println("├─────────────────────────────────────────────────────────────────────────────┤");
        System.out.printf("│ 🔑 ID: %-65s │%n", estudiante.getId().toHexString());
        System.out.printf("│ 👤 Nombre: %-60s │%n", estudiante.getNombre());
        System.out.printf("│ 🆔 Identificación: %-53s │%n", estudiante.getIdentificacion());
        System.out.printf("│ 📧 Email: %-61s │%n", estudiante.getEmail());
        System.out.printf("│ 📅 Fecha Nac.: %-56s │%n", estudiante.getFechaNacimiento());
        System.out.printf("│ 📊 Estado: %-60s │%n", estudiante.getEstado());
        System.out.println("└─────────────────────────────────────────────────────────────────────────────┘");
    }
}