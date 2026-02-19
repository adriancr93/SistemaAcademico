package org.example.view;

import org.bson.types.ObjectId;
import org.example.model.Profesor;
import org.example.service.ProfesorService;
import org.example.service.impl.ProfesorServiceImpl;

import java.util.List;
import java.util.Optional;

/**
 * Vista para la gestión de profesores
 */
public class ProfesorView {
    
    private final ProfesorService profesorService;
    
    public ProfesorView() {
        this.profesorService = new ProfesorServiceImpl();
    }
    
    /**
     * Mostrar menú de profesores
     */
    public void mostrarMenu() {
        boolean continuar = true;
        
        while (continuar) {
            mostrarTituloProfesores();
            mostrarOpcionesMenu();
            
            int opcion = InputHelper.readInt("👉 Seleccione una opción: ");
            
            try {
                switch (opcion) {
                    case 1:
                        crearProfesor();
                        break;
                    case 2:
                        listarProfesores();
                        break;
                    case 3:
                        buscarProfesorPorIdentificacion();
                        break;
                    case 4:
                        buscarProfesorPorEmail();
                        break;
                    case 5:
                        buscarProfesoresPorDepartamento();
                        break;
                    case 6:
                        actualizarProfesor();
                        break;
                    case 7:
                        eliminarProfesor();
                        break;
                    case 0:
                        continuar = false;
                        break;
                    default:
                        System.out.println("❌ Opción no válida. Intente de nuevo.");
                        InputHelper.pause();
                }
            } catch (Exception e) {
                System.out.println("❌ Error: " + e.getMessage());
                InputHelper.pause();
            }
        }
    }
    
    private void mostrarTituloProfesores() {
        InputHelper.clearScreen();
        System.out.println("╔══════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                            🎓 GESTIÓN DE PROFESORES 👨‍🏫                        ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════════════════╝");
        System.out.println();
    }
    
    private void mostrarOpcionesMenu() {
        System.out.println("┌─────────────────────────────────────────────────────────────────────────────┐");
        System.out.println("│  1. ➕ Crear nuevo profesor                                                   │");
        System.out.println("│  2. 📋 Listar todos los profesores                                            │");
        System.out.println("│  3. 🆔 Buscar profesor por identificación                                     │");
        System.out.println("│  4. 📧 Buscar profesor por email                                              │");
        System.out.println("│  5. 🏢 Buscar profesores por departamento                                     │");
        System.out.println("│  6. ✏️  Actualizar profesor                                                    │");
        System.out.println("│  7. 🗑️  Eliminar profesor                                                     │");
        System.out.println("│  8. 📊 Mostrar estadísticas                                                   │");
        System.out.println("│  0. ⬅️  Volver al menú principal                                               │");
        System.out.println("└─────────────────────────────────────────────────────────────────────────────┘");
        System.out.println();
    }
    
    private void crearProfesor() {
        InputHelper.showSeparator("CREAR NUEVO PROFESOR");
        System.out.println();
        
        try {
            String nombre = InputHelper.readLine("👤 Nombre completo: ");
            String identificacion = InputHelper.readLine("🆔 Número de identificación: ");
            String email = InputHelper.readLine("📧 Email: ");
            String departamento = InputHelper.readLine("🏢 Departamento: ");
            String estado = InputHelper.readLine("🔄 Estado", "activo");
            
            Profesor profesor = new Profesor(nombre, identificacion, email, departamento, estado);
            Profesor profesorCreado = profesorService.crearProfesor(profesor);
            
            System.out.println();
            System.out.println("✅ ¡Profesor creado exitosamente!");
            System.out.println("🔑 ID asignado: " + profesorCreado.getId().toHexString());
            mostrarDetallesProfesor(profesorCreado);
            
        } catch (Exception e) {
            System.out.println("❌ Error al crear profesor: " + e.getMessage());
        }
        
        InputHelper.pause();
    }
    
    private void listarProfesores() {
        InputHelper.showSeparator("LISTA DE PROFESORES");
        System.out.println();
        
        try {
            List<Profesor> profesores = profesorService.obtenerTodosLosProfesores();
            
            if (profesores.isEmpty()) {
                System.out.println("📭 No hay profesores registrados.");
            } else {
                System.out.println("📊 Total de profesores: " + profesores.size());
                System.out.println();
                
                for (int i = 0; i < profesores.size(); i++) {
                    Profesor profesor = profesores.get(i);
                    System.out.println((i + 1) + ". " + profesor.toStringFormatted());
                }
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error al listar profesores: " + e.getMessage());
        }
        
        InputHelper.pause();
    }
    
    private void buscarProfesorPorIdentificacion() {
        InputHelper.showSeparator("BUSCAR PROFESOR POR IDENTIFICACIÓN");
        System.out.println();
        
        try {
            String identificacion = InputHelper.readLine("🆔 Ingrese el número de identificación: ");
            
            Optional<Profesor> profesorOpt = profesorService.buscarPorIdentificacion(identificacion);
            
            if (profesorOpt.isPresent()) {
                System.out.println("✅ Profesor encontrado:");
                mostrarDetallesProfesor(profesorOpt.get());
            } else {
                System.out.println("❌ No se encontró un profesor con la identificación proporcionada.");
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error al buscar profesor: " + e.getMessage());
        }
        
        InputHelper.pause();
    }
    
    private void buscarProfesorPorEmail() {
        InputHelper.showSeparator("BUSCAR PROFESOR POR EMAIL");
        System.out.println();
        
        try {
            String email = InputHelper.readLine("📧 Ingrese el email: ");
            
            Optional<Profesor> profesorOpt = profesorService.buscarPorEmail(email);
            
            if (profesorOpt.isPresent()) {
                System.out.println("✅ Profesor encontrado:");
                mostrarDetallesProfesor(profesorOpt.get());
            } else {
                System.out.println("❌ No se encontró un profesor con el email proporcionado.");
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error al buscar profesor: " + e.getMessage());
        }
        
        InputHelper.pause();
    }
    
    private void buscarProfesoresPorDepartamento() {
        InputHelper.showSeparator("BUSCAR PROFESORES POR DEPARTAMENTO");
        System.out.println();
        
        try {
            String departamento = InputHelper.readLine("🏢 Ingrese el departamento: ");
            
            List<Profesor> profesores = profesorService.buscarPorDepartamento(departamento);
            
            if (!profesores.isEmpty()) {
                System.out.println("✅ Profesores encontrados (" + profesores.size() + "):");
                System.out.println();
                for (int i = 0; i < profesores.size(); i++) {
                    Profesor profesor = profesores.get(i);
                    System.out.println((i + 1) + ". " + profesor.toStringFormatted());
                }
            } else {
                System.out.println("❌ No se encontraron profesores en el departamento especificado.");
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error al buscar profesores: " + e.getMessage());
        }
        
        InputHelper.pause();
    }
    
    private void actualizarProfesor() {
        InputHelper.showSeparator("ACTUALIZAR PROFESOR");
        System.out.println();
        
        try {
            String idStr = InputHelper.readLine("🔑 Ingrese el ID del profesor a actualizar: ");
            ObjectId id = new ObjectId(idStr);
            
            Optional<Profesor> profesorOpt = profesorService.obtenerProfesorPorId(id);
            
            if (!profesorOpt.isPresent()) {
                System.out.println("❌ No se encontró un profesor con el ID proporcionado.");
                InputHelper.pause();
                return;
            }
            
            Profesor profesor = profesorOpt.get();
            System.out.println("📋 Profesor actual:");
            mostrarDetallesProfesor(profesor);
            System.out.println();
            
            System.out.println("💡 Ingrese los nuevos datos (presione Enter para mantener el valor actual):");
            System.out.println();
            
            String nombre = InputHelper.readLine("👤 Nombre completo", profesor.getNombre());
            String identificacion = InputHelper.readLine("🆔 Número de identificación", profesor.getIdentificacion());
            String email = InputHelper.readLine("📧 Email", profesor.getEmail());
            String departamento = InputHelper.readLine("🏢 Departamento", profesor.getDepartamento());
            String estado = InputHelper.readLine("🔄 Estado (activo/inactivo)", profesor.getEstado());
            
            profesor.setNombre(nombre);
            profesor.setIdentificacion(identificacion);
            profesor.setEmail(email);
            profesor.setDepartamento(departamento);
            profesor.setEstado(estado);
            
            if (profesorService.actualizarProfesor(profesor)) {
                System.out.println("✅ ¡Profesor actualizado exitosamente!");
                mostrarDetallesProfesor(profesor);
            } else {
                System.out.println("❌ No se pudo actualizar el profesor.");
            }
            
        } catch (IllegalArgumentException e) {
            System.out.println("❌ ID inválido: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Error al actualizar profesor: " + e.getMessage());
        }
        
        InputHelper.pause();
    }
    
    private void eliminarProfesor() {
        InputHelper.showSeparator("ELIMINAR PROFESOR");
        System.out.println();
        
        try {
            String idStr = InputHelper.readLine("🔑 Ingrese el ID del profesor a eliminar: ");
            ObjectId id = new ObjectId(idStr);
            
            Optional<Profesor> profesorOpt = profesorService.obtenerProfesorPorId(id);
            
            if (!profesorOpt.isPresent()) {
                System.out.println("❌ No se encontró un profesor con el ID proporcionado.");
                InputHelper.pause();
                return;
            }
            
            Profesor profesor = profesorOpt.get();
            System.out.println("⚠️  Profesor a eliminar:");
            mostrarDetallesProfesor(profesor);
            System.out.println();
            
            if (InputHelper.confirm("❓ ¿Está seguro de que desea eliminar este profesor?")) {
                if (profesorService.eliminarProfesor(id)) {
                    System.out.println("✅ ¡Profesor eliminado exitosamente!");
                } else {
                    System.out.println("❌ No se pudo eliminar el profesor.");
                }
            } else {
                System.out.println("🔄 Operación cancelada.");
            }
            
        } catch (IllegalArgumentException e) {
            System.out.println("❌ ID inválido: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Error al eliminar profesor: " + e.getMessage());
        }
        
        InputHelper.pause();
    }
    
    private void mostrarDetallesProfesor(Profesor profesor) {
        System.out.println();
        System.out.println("┌─────────────────────────────────────────────────────────────────────────────┐");
        System.out.println("│                             DETALLES DEL PROFESOR                          │");
        System.out.println("├─────────────────────────────────────────────────────────────────────────────┤");
        System.out.println("│ 🔑 ID: " + String.format("%-66s", profesor.getId().toHexString()) + "│");
        System.out.println("│ 👤 Nombre: " + String.format("%-62s", profesor.getNombre()) + "│");
        System.out.println("│ 🆔 Identificación: " + String.format("%-55s", profesor.getIdentificacion()) + "│");
        System.out.println("│ 📧 Email: " + String.format("%-63s", profesor.getEmail()) + "│");
        System.out.println("│ 🏢 Departamento: " + String.format("%-57s", profesor.getDepartamento()) + "│");
        System.out.println("│ 🔄 Estado: " + String.format("%-62s", profesor.getEstado()) + "│");
        System.out.println("└─────────────────────────────────────────────────────────────────────────────┘");
    }
}