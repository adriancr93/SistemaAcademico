package org.example.view;

import org.bson.types.ObjectId;
import org.example.model.Curso;
import org.example.service.CursoService;
import org.example.service.ServiceFactory;

import java.util.List;
import java.util.Optional;

/**
 * Vista para la gestión de cursos.
 * Ahora obtiene el servicio desde ServiceFactory en vez de instanciar directamente.
 */
public class CursoView {
    
    private final CursoService cursoService;
    
    // Antes: this.cursoService = new CursoServiceImpl();
    // Ahora usa ServiceFactory para desacoplar
    public CursoView() {
        this.cursoService = ServiceFactory.getInstance().getCursoService();
    }
    
    /**
     * Mostrar menú de cursos
     */
    public void mostrarMenu() {
        boolean continuar = true;
        
        while (continuar) {
            mostrarTituloCursos();
            mostrarOpcionesMenu();
            
            int opcion = InputHelper.readInt("👉 Seleccione una opción: ");
            
            try {
                switch (opcion) {
                    case 1:
                        crearCurso();
                        break;
                    case 2:
                        listarCursos();
                        break;
                    case 3:
                        buscarCursosPorNombre();
                        break;
                    case 4:
                        buscarCursosPorCreditos();
                        break;
                    case 5:
                        actualizarCurso();
                        break;
                    case 6:
                        eliminarCurso();
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
    
    private void mostrarTituloCursos() {
        InputHelper.clearScreen();
        System.out.println("╔══════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                              📚 GESTIÓN DE CURSOS 📖                          ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════════════════╝");
        System.out.println();
    }
    
    private void mostrarOpcionesMenu() {
        System.out.println("┌─────────────────────────────────────────────────────────────────────────────┐");
        System.out.println("│  1. ➕ Crear nuevo curso                                                      │");
        System.out.println("│  2. 📋 Listar todos los cursos                                                │");
        System.out.println("│  3. 🏷️  Buscar cursos por nombre                                              │");
        System.out.println("│  4. 🎯 Buscar cursos por créditos                                             │");
        System.out.println("│  5. ✏️  Actualizar curso                                                       │");
        System.out.println("│  6. 🗑️  Eliminar curso                                                        │");
        System.out.println("│  0. ⬅️  Volver al menú principal                                               │");
        System.out.println("└─────────────────────────────────────────────────────────────────────────────┘");
        System.out.println();
    }
    
    private void crearCurso() {
        InputHelper.showSeparator("CREAR NUEVO CURSO");
        System.out.println();
        
        try {
            String codigo = InputHelper.readLine("🔢 Código del curso: ");
            String nombre = InputHelper.readLine("🏷️  Nombre del curso: ");
            String descripcion = InputHelper.readLine("📝 Descripción: ");
            int creditos = InputHelper.readInt("🎯 Créditos (1-10): ");
            
            // Validación de créditos
            if (creditos < 1 || creditos > 10) {
                System.out.println("❌ Los créditos deben estar entre 1 y 10.");
                InputHelper.pause();
                return;
            }
            
            Curso curso = new Curso(codigo, nombre, descripcion, creditos);
            Curso cursoCreado = cursoService.crearCurso(curso);
            
            System.out.println();
            System.out.println("✅ ¡Curso creado exitosamente!");
            System.out.println("🔑 ID asignado: " + cursoCreado.getId().toHexString());
            mostrarDetallesCurso(cursoCreado);
            
        } catch (Exception e) {
            System.out.println("❌ Error al crear curso: " + e.getMessage());
        }
        
        InputHelper.pause();
    }
    
    private void listarCursos() {
        InputHelper.showSeparator("LISTA DE CURSOS");
        System.out.println();
        
        try {
            List<Curso> cursos = cursoService.obtenerTodosLosCursos();
            
            if (cursos.isEmpty()) {
                System.out.println("📭 No hay cursos registrados.");
            } else {
                System.out.println("📊 Total de cursos: " + cursos.size());
                System.out.println();
                
                // Ordenar por código
                cursos.sort((c1, c2) -> c1.getCodigo().compareToIgnoreCase(c2.getCodigo()));
                
                for (int i = 0; i < cursos.size(); i++) {
                    Curso curso = cursos.get(i);
                    System.out.println((i + 1) + ". " + curso.toStringFormatted());
                }
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error al listar cursos: " + e.getMessage());
        }
        
        InputHelper.pause();
    }
    
    private void buscarCursosPorNombre() {
        InputHelper.showSeparator("BUSCAR CURSOS POR NOMBRE");
        System.out.println();
        
        try {
            String nombre = InputHelper.readLine("🏷️  Ingrese parte del nombre del curso: ");
            
            List<Curso> cursos = cursoService.buscarPorNombre(nombre);
            
            if (cursos.isEmpty()) {
                System.out.println("❌ No se encontraron cursos que contengan '" + nombre + "'.");
            } else {
                System.out.println("✅ Se encontraron " + cursos.size() + " curso(s):");
                System.out.println();
                
                for (int i = 0; i < cursos.size(); i++) {
                    System.out.println((i + 1) + ". " + cursos.get(i).toStringFormatted());
                }
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error al buscar cursos: " + e.getMessage());
        }
        
        InputHelper.pause();
    }
    
    private void buscarCursosPorCreditos() {
        InputHelper.showSeparator("BUSCAR CURSOS POR CRÉDITOS");
        System.out.println();
        
        try {
            int creditos = InputHelper.readInt("🎯 Ingrese la cantidad de créditos (1-10): ");
            
            if (creditos < 1 || creditos > 10) {
                System.out.println("❌ Los créditos deben estar entre 1 y 10.");
                InputHelper.pause();
                return;
            }
            
            List<Curso> cursos = cursoService.buscarPorCreditos(creditos);
            
            if (cursos.isEmpty()) {
                System.out.println("❌ No se encontraron cursos con " + creditos + " crédito(s).");
            } else {
                System.out.println("✅ Se encontraron " + cursos.size() + " curso(s) con " + creditos + " crédito(s):");
                System.out.println();
                
                for (int i = 0; i < cursos.size(); i++) {
                    System.out.println((i + 1) + ". " + cursos.get(i).toStringFormatted());
                }
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error al buscar cursos: " + e.getMessage());
        }
        
        InputHelper.pause();
    }
    
    private void actualizarCurso() {
        InputHelper.showSeparator("ACTUALIZAR CURSO");
        System.out.println();
        
        try {
            String idStr = InputHelper.readLine("🔑 Ingrese el ID del curso a actualizar: ");
            ObjectId id = new ObjectId(idStr);
            
            Optional<Curso> cursoOpt = cursoService.obtenerCursoPorId(id);
            
            if (!cursoOpt.isPresent()) {
                System.out.println("❌ No se encontró un curso con el ID proporcionado.");
                InputHelper.pause();
                return;
            }
            
            Curso curso = cursoOpt.get();
            System.out.println("📋 Curso actual:");
            mostrarDetallesCurso(curso);
            System.out.println();
            
            System.out.println("💡 Ingrese los nuevos datos (presione Enter para mantener el valor actual):");
            System.out.println();
            
            String codigo = InputHelper.readLine("🔢 Código del curso", curso.getCodigo());
            String nombre = InputHelper.readLine("🏷️  Nombre del curso", curso.getNombre());
            String descripcion = InputHelper.readLine("📝 Descripción", curso.getDescripcion());
            
            String creditosStr = InputHelper.readLine("🎯 Créditos (1-10)", String.valueOf(curso.getCreditos()));
            int creditos = Integer.parseInt(creditosStr);
            
            // Validación de créditos
            if (creditos < 1 || creditos > 10) {
                System.out.println("❌ Los créditos deben estar entre 1 y 10.");
                InputHelper.pause();
                return;
            }
            
            curso.setCodigo(codigo);
            curso.setNombre(nombre);
            curso.setDescripcion(descripcion);
            curso.setCreditos(creditos);
            
            if (cursoService.actualizarCurso(curso)) {
                System.out.println("✅ ¡Curso actualizado exitosamente!");
                mostrarDetallesCurso(curso);
            } else {
                System.out.println("❌ No se pudo actualizar el curso.");
            }
            
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Dato inválido: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Error al actualizar curso: " + e.getMessage());
        }
        
        InputHelper.pause();
    }
    
    private void eliminarCurso() {
        InputHelper.showSeparator("ELIMINAR CURSO");
        System.out.println();
        
        try {
            String idStr = InputHelper.readLine("🔑 Ingrese el ID del curso a eliminar: ");
            ObjectId id = new ObjectId(idStr);
            
            Optional<Curso> cursoOpt = cursoService.obtenerCursoPorId(id);
            
            if (!cursoOpt.isPresent()) {
                System.out.println("❌ No se encontró un curso con el ID proporcionado.");
                InputHelper.pause();
                return;
            }
            
            Curso curso = cursoOpt.get();
            System.out.println("⚠️  Curso a eliminar:");
            mostrarDetallesCurso(curso);
            System.out.println();
            
            if (InputHelper.confirm("❓ ¿Está seguro de que desea eliminar este curso?")) {
                if (cursoService.eliminarCurso(id)) {
                    System.out.println("✅ ¡Curso eliminado exitosamente!");
                } else {
                    System.out.println("❌ No se pudo eliminar el curso.");
                }
            } else {
                System.out.println("🔄 Operación cancelada.");
            }
            
        } catch (IllegalArgumentException e) {
            System.out.println("❌ ID inválido: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Error al eliminar curso: " + e.getMessage());
        }
        
        InputHelper.pause();
    }
    
    private void mostrarDetallesCurso(Curso curso) {
        System.out.println();
        System.out.println("┌─────────────────────────────────────────────────────────────────────────────┐");
        System.out.println("│                              DETALLES DEL CURSO                            │");
        System.out.println("├─────────────────────────────────────────────────────────────────────────────┤");
        System.out.println("│ 🔑 ID: " + String.format("%-66s", curso.getId().toHexString()) + "│");
        System.out.println("│ 🔢 Código: " + String.format("%-61s", curso.getCodigo()) + "│");
        System.out.println("│ 🏷️  Nombre: " + String.format("%-62s", curso.getNombre()) + "│");
        System.out.println("│ 📝 Descripción: " + String.format("%-57s", 
            curso.getDescripcion().length() > 57 ? 
            curso.getDescripcion().substring(0, 54) + "..." : 
            curso.getDescripcion()) + "│");
        System.out.println("│ 🎯 Créditos: " + String.format("%-60s", curso.getCreditos()) + "│");
        System.out.println("└─────────────────────────────────────────────────────────────────────────────┘");
        
        // Mostrar descripción completa si es muy larga
        if (curso.getDescripcion().length() > 57) {
            System.out.println();
            System.out.println("📝 Descripción completa:");
            System.out.println(curso.getDescripcion());
        }
    }
}