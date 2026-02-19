package org.example.service.impl;

import org.bson.types.ObjectId;
import org.example.model.Grupo;
import org.example.repository.GrupoRepository;
import org.example.repository.impl.GrupoRepositoryMongoDB;
import org.example.service.GrupoService;
import org.example.util.ValidationUtil;

import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio de Grupo
 */
public class GrupoServiceImpl implements GrupoService {
    
    private final GrupoRepository grupoRepository;
    
    public GrupoServiceImpl() {
        this.grupoRepository = new GrupoRepositoryMongoDB();
    }
    
    // Constructor para inyección de dependencias (útil para testing)
    public GrupoServiceImpl(GrupoRepository grupoRepository) {
        this.grupoRepository = grupoRepository;
    }
    
    @Override
    public Grupo crearGrupo(Grupo grupo) throws IllegalArgumentException {
        validarGrupo(grupo, false);
        
        // Verificar que no exista otro grupo con el mismo nombre
        if (grupoRepository.findByNombre(grupo.getNombre()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un grupo con el nombre: " + 
                grupo.getNombre());
        }
        
        return grupoRepository.save(grupo);
    }
    
    @Override
    public Optional<Grupo> obtenerGrupoPorId(ObjectId id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }
        return grupoRepository.findById(id);
    }
    
    @Override
    public List<Grupo> obtenerTodosLosGrupos() {
        return grupoRepository.findAll();
    }
    
    @Override
    public boolean actualizarGrupo(Grupo grupo) throws IllegalArgumentException {
        if (grupo.getId() == null) {
            throw new IllegalArgumentException("El grupo debe tener un ID para ser actualizado");
        }
        
        validarGrupo(grupo, true);
        
        // Verificar que no exista otro grupo con el mismo nombre
        if (grupoRepository.existsByNombreAndNotId(grupo.getNombre(), grupo.getId())) {
            throw new IllegalArgumentException("Ya existe otro grupo con el nombre: " + 
                grupo.getNombre());
        }
        
        return grupoRepository.update(grupo);
    }
    
    @Override
    public boolean eliminarGrupo(ObjectId id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }
        
        if (!grupoRepository.existsById(id)) {
            throw new IllegalArgumentException("No existe un grupo con el ID proporcionado");
        }
        
        return grupoRepository.deleteById(id);
    }
    
    @Override
    public Optional<Grupo> buscarPorNombre(String nombre) {
        if (!ValidationUtil.isNotEmpty(nombre)) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        return grupoRepository.findByNombre(nombre);
    }
    
    @Override
    public long contarGrupos() {
        return grupoRepository.count();
    }
    
    /**
     * Valida los datos del grupo
     */
    private void validarGrupo(Grupo grupo, boolean esActualizacion) {
        if (grupo == null) {
            throw new IllegalArgumentException("El grupo no puede ser nulo");
        }
        
        // Validar nombre
        if (!ValidationUtil.isValidLength(grupo.getNombre(), 2)) {
            throw new IllegalArgumentException("El nombre debe tener al menos 2 caracteres");
        }
        
        // Validar descripción
        if (!ValidationUtil.isValidLength(grupo.getDescripcion(), 5)) {
            throw new IllegalArgumentException("La descripción debe tener al menos 5 caracteres");
        }
        
        // Validar estado
        if (!ValidationUtil.isValidState(grupo.getEstado())) {
            throw new IllegalArgumentException("El estado debe ser 'activo' o 'inactivo'");
        }
        
        // Normalizar datos
        grupo.setNombre(ValidationUtil.cleanText(grupo.getNombre()));
        grupo.setDescripcion(ValidationUtil.cleanText(grupo.getDescripcion()));
        grupo.setEstado(ValidationUtil.normalizeState(grupo.getEstado()));
    }
}