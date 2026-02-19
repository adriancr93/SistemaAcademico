package org.example.view;

import org.bson.types.ObjectId;
import org.example.model.Grupo;
import org.example.service.GrupoService;
import org.example.service.impl.GrupoServiceImpl;

import java.util.List;
import java.util.Optional;

/**
 * Vista para la gestión de grupos
 */
public class GrupoView {
    
    private final GrupoService grupoService;
    
    public GrupoView() {
        this.grupoService = new GrupoServiceImpl();
    }
    
    /**
     * Mostrar menú de grupos
     */
    public void mostrarMenu() {
        boolean continuar = true;
        
        while (continuar) {
            mostrarTituloGrupos();
            mostrarOpcionesMenu();
            
            int opcion = InputHelper.readInt("👉 Seleccione una opción: ");
            
            try {
                switch (opcion) {
                    case 1:
                        crearGrupo();
                        break;
                    case 2:
                        listarGrupos();
                        break;
                    case 3:
                        buscarGrupoPorNombre();
                        break;
                    case 4:
                        actualizarGrupo();
                        break;
                    case 5:
                        eliminarGrupo();
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
    
    private void mostrarTituloGrupos() {
        InputHelper.clearScreen();
        System.out.println("╔══════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                              👥 GESTIÓN DE GRUPOS 📊                          ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════════════════╝");
        System.out.println();
    }
    
    private void mostrarOpcionesMenu() {
        System.out.println("┌─────────────────────────────────────────────────────────────────────────────┐");
        System.out.println("│  1. ➕ Crear nuevo grupo                                                      │");
        System.out.println("│  2. 📋 Listar todos los grupos                                                │");
        System.out.println("│  3. 🏷️  Buscar grupo por nombre                                               │");
        System.out.println("│  4. ✏️  Actualizar grupo                                                       │");
        System.out.println("│  5. 🗑️  Eliminar grupo                                                        │");
        System.out.println("│  0. ⬅️  Volver al menú principal                                               │");
        System.out.println("└─────────────────────────────────────────────────────────────────────────────┘");
        System.out.println();
    }
    
    private void crearGrupo() {
        InputHelper.showSeparator("CREAR NUEVO GRUPO");
        System.out.println();
        
        try {
            String nombre = InputHelper.readLine("🏷️  Nombre del grupo: ");
            String descripcion = InputHelper.readLine("📝 Descripción: ");
            String estado = InputHelper.readLine("🔄 Estado", "activo");
            
            Grupo grupo = new Grupo(nombre, descripcion, estado);
            Grupo grupoCreado = grupoService.crearGrupo(grupo);
            
            System.out.println();
            System.out.println("✅ ¡Grupo creado exitosamente!");
            System.out.println("🔑 ID asignado: " + grupoCreado.getId().toHexString());
            mostrarDetallesGrupo(grupoCreado);
            
        } catch (Exception e) {
            System.out.println("❌ Error al crear grupo: " + e.getMessage());
        }
        
        InputHelper.pause();
    }
    
    private void listarGrupos() {
        InputHelper.showSeparator("LISTA DE GRUPOS");
        System.out.println();
        
        try {
            List<Grupo> grupos = grupoService.obtenerTodosLosGrupos();
            
            if (grupos.isEmpty()) {
                System.out.println("📭 No hay grupos registrados.");
            } else {
                System.out.println("📊 Total de grupos: " + grupos.size());
                System.out.println();
                
                for (int i = 0; i < grupos.size(); i++) {
                    Grupo grupo = grupos.get(i);
                    System.out.println((i + 1) + ". " + grupo.toStringFormatted());
                }
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error al listar grupos: " + e.getMessage());
        }
        
        InputHelper.pause();
    }
    
    private void buscarGrupoPorNombre() {
        InputHelper.showSeparator("BUSCAR GRUPO POR NOMBRE");
        System.out.println();
        
        try {
            String nombre = InputHelper.readLine("🏷️  Ingrese el nombre del grupo: ");
            
            Optional<Grupo> grupoOpt = grupoService.buscarPorNombre(nombre);
            
            if (grupoOpt.isPresent()) {
                System.out.println("✅ Grupo encontrado:");
                mostrarDetallesGrupo(grupoOpt.get());
            } else {
                System.out.println("❌ No se encontró un grupo con el nombre proporcionado.");
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error al buscar grupo: " + e.getMessage());
        }
        
        InputHelper.pause();
    }
    
    private void actualizarGrupo() {
        InputHelper.showSeparator("ACTUALIZAR GRUPO");
        System.out.println();
        
        try {
            String idStr = InputHelper.readLine("🔑 Ingrese el ID del grupo a actualizar: ");
            ObjectId id = new ObjectId(idStr);
            
            Optional<Grupo> grupoOpt = grupoService.obtenerGrupoPorId(id);
            
            if (!grupoOpt.isPresent()) {
                System.out.println("❌ No se encontró un grupo con el ID proporcionado.");
                InputHelper.pause();
                return;
            }
            
            Grupo grupo = grupoOpt.get();
            System.out.println("📋 Grupo actual:");
            mostrarDetallesGrupo(grupo);
            System.out.println();
            
            System.out.println("💡 Ingrese los nuevos datos (presione Enter para mantener el valor actual):");
            System.out.println();
            
            String nombre = InputHelper.readLine("🏷️  Nombre del grupo", grupo.getNombre());
            String descripcion = InputHelper.readLine("📝 Descripción", grupo.getDescripcion());
            String estado = InputHelper.readLine("🔄 Estado (activo/inactivo)", grupo.getEstado());
            
            grupo.setNombre(nombre);
            grupo.setDescripcion(descripcion);
            grupo.setEstado(estado);
            
            if (grupoService.actualizarGrupo(grupo)) {
                System.out.println("✅ ¡Grupo actualizado exitosamente!");
                mostrarDetallesGrupo(grupo);
            } else {
                System.out.println("❌ No se pudo actualizar el grupo.");
            }
            
        } catch (IllegalArgumentException e) {
            System.out.println("❌ ID inválido: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Error al actualizar grupo: " + e.getMessage());
        }
        
        InputHelper.pause();
    }
    
    private void eliminarGrupo() {
        InputHelper.showSeparator("ELIMINAR GRUPO");
        System.out.println();
        
        try {
            String idStr = InputHelper.readLine("🔑 Ingrese el ID del grupo a eliminar: ");
            ObjectId id = new ObjectId(idStr);
            
            Optional<Grupo> grupoOpt = grupoService.obtenerGrupoPorId(id);
            
            if (!grupoOpt.isPresent()) {
                System.out.println("❌ No se encontró un grupo con el ID proporcionado.");
                InputHelper.pause();
                return;
            }
            
            Grupo grupo = grupoOpt.get();
            System.out.println("⚠️  Grupo a eliminar:");
            mostrarDetallesGrupo(grupo);
            System.out.println();
            
            if (InputHelper.confirm("❓ ¿Está seguro de que desea eliminar este grupo?")) {
                if (grupoService.eliminarGrupo(id)) {
                    System.out.println("✅ ¡Grupo eliminado exitosamente!");
                } else {
                    System.out.println("❌ No se pudo eliminar el grupo.");
                }
            } else {
                System.out.println("🔄 Operación cancelada.");
            }
            
        } catch (IllegalArgumentException e) {
            System.out.println("❌ ID inválido: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Error al eliminar grupo: " + e.getMessage());
        }
        
        InputHelper.pause();
    }
    
    private void mostrarDetallesGrupo(Grupo grupo) {
        System.out.println();
        System.out.println("┌─────────────────────────────────────────────────────────────────────────────┐");
        System.out.println("│                              DETALLES DEL GRUPO                            │");
        System.out.println("├─────────────────────────────────────────────────────────────────────────────┤");
        System.out.println("│ 🔑 ID: " + String.format("%-66s", grupo.getId().toHexString()) + "│");
        System.out.println("│ 🏷️  Nombre: " + String.format("%-62s", grupo.getNombre()) + "│");
        System.out.println("│ 📝 Descripción: " + String.format("%-57s", 
            grupo.getDescripcion().length() > 57 ? 
            grupo.getDescripcion().substring(0, 54) + "..." : 
            grupo.getDescripcion()) + "│");
        System.out.println("│ 🔄 Estado: " + String.format("%-62s", grupo.getEstado()) + "│");
        System.out.println("└─────────────────────────────────────────────────────────────────────────────┘");
        
        // Mostrar descripción completa si es muy larga
        if (grupo.getDescripcion().length() > 57) {
            System.out.println();
            System.out.println("📝 Descripción completa:");
            System.out.println(grupo.getDescripcion());
        }
    }
}