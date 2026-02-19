package org.example.service.impl;

import org.bson.types.ObjectId;
import org.example.model.GrupoCurso;
import org.example.repository.GrupoCursoRepository;
import org.example.repository.GrupoRepository;
import org.example.repository.CursoRepository;
import org.example.repository.impl.GrupoCursoRepositoryMongoDB;
import org.example.repository.impl.GrupoRepositoryMongoDB;
import org.example.repository.impl.CursoRepositoryMongoDB;
import org.example.service.GrupoCursoService;

import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio de GrupoCurso
 */
public class GrupoCursoServiceImpl implements GrupoCursoService {
    
    private final GrupoCursoRepository grupoCursoRepository;
    private final GrupoRepository grupoRepository;
    private final CursoRepository cursoRepository;
    
    public GrupoCursoServiceImpl() {
        this.grupoCursoRepository = new GrupoCursoRepositoryMongoDB();
        this.grupoRepository = new GrupoRepositoryMongoDB();
        this.cursoRepository = new CursoRepositoryMongoDB();
    }
    
    // Constructor para inyección de dependencias (útil para testing)
    public GrupoCursoServiceImpl(GrupoCursoRepository grupoCursoRepository, 
                               GrupoRepository grupoRepository, 
                               CursoRepository cursoRepository) {
        this.grupoCursoRepository = grupoCursoRepository;
        this.grupoRepository = grupoRepository;
        this.cursoRepository = cursoRepository;
    }
    
    @Override
    public GrupoCurso crearRelacion(ObjectId grupoId, ObjectId cursoId) throws IllegalArgumentException {
        validarRelacion(grupoId, cursoId);
        
        // Verificar que no exista ya la relación
        if (grupoCursoRepository.existsByGrupoIdAndCursoId(grupoId, cursoId)) {
            throw new IllegalArgumentException("Ya existe una relación entre el grupo y el curso especificados");
        }
        
        GrupoCurso grupoCurso = new GrupoCurso(grupoId, cursoId);
        return grupoCursoRepository.save(grupoCurso);
    }
    
    @Override
    public Optional<GrupoCurso> obtenerRelacionPorId(ObjectId id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }
        return grupoCursoRepository.findById(id);
    }
    
    @Override
    public List<GrupoCurso> obtenerTodasLasRelaciones() {
        return grupoCursoRepository.findAll();
    }
    
    @Override
    public boolean eliminarRelacion(ObjectId id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }
        
        if (!grupoCursoRepository.existsById(id)) {
            throw new IllegalArgumentException("No existe una relación con el ID proporcionado");
        }
        
        return grupoCursoRepository.deleteById(id);
    }
    
    @Override
    public List<GrupoCurso> obtenerRelacionesPorGrupo(ObjectId grupoId) {
        if (grupoId == null) {
            throw new IllegalArgumentException("El ID del grupo no puede ser nulo");
        }
        return grupoCursoRepository.findByGrupoId(grupoId);
    }
    
    @Override
    public List<GrupoCurso> obtenerRelacionesPorCurso(ObjectId cursoId) {
        if (cursoId == null) {
            throw new IllegalArgumentException("El ID del curso no puede ser nulo");
        }
        return grupoCursoRepository.findByCursoId(cursoId);
    }
    
    @Override
    public boolean existeRelacion(ObjectId grupoId, ObjectId cursoId) {
        if (grupoId == null || cursoId == null) {
            return false;
        }
        return grupoCursoRepository.existsByGrupoIdAndCursoId(grupoId, cursoId);
    }
    
    @Override
    public long contarRelaciones() {
        return grupoCursoRepository.count();
    }
    
    /**
     * Valida que la relación sea válida
     */
    private void validarRelacion(ObjectId grupoId, ObjectId cursoId) {
        if (grupoId == null) {
            throw new IllegalArgumentException("El ID del grupo no puede ser nulo");
        }
        
        if (cursoId == null) {
            throw new IllegalArgumentException("El ID del curso no puede ser nulo");
        }
        
        // Verificar que el grupo existe
        if (!grupoRepository.existsById(grupoId)) {
            throw new IllegalArgumentException("No existe un grupo con el ID proporcionado");
        }
        
        // Verificar que el curso existe
        if (!cursoRepository.existsById(cursoId)) {
            throw new IllegalArgumentException("No existe un curso con el ID proporcionado");
        }
    }
}