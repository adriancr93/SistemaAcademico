package org.example.service.impl;

import org.bson.types.ObjectId;
import org.example.model.Curso;
import org.example.repository.CursoRepository;
import org.example.repository.impl.CursoRepositoryMongoDB;
import org.example.service.CursoService;
import org.example.util.ValidationUtil;

import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio de Curso
 */
public class CursoServiceImpl implements CursoService {
    
    private final CursoRepository cursoRepository;
    
    public CursoServiceImpl() {
        this.cursoRepository = new CursoRepositoryMongoDB();
    }
    
    // Constructor para inyección de dependencias (útil para testing)
    public CursoServiceImpl(CursoRepository cursoRepository) {
        this.cursoRepository = cursoRepository;
    }
    
    @Override
    public Curso crearCurso(Curso curso) throws IllegalArgumentException {
        validarCurso(curso, false);
        
        // Verificar que no exista otro curso con el mismo código
        if (cursoRepository.existsByCodigo(curso.getCodigo())) {
            throw new IllegalArgumentException("Ya existe un curso con el código: " + 
                curso.getCodigo());
        }
        
        return cursoRepository.save(curso);
    }
    
    @Override
    public Optional<Curso> obtenerCursoPorId(ObjectId id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }
        return cursoRepository.findById(id);
    }
    
    @Override
    public List<Curso> obtenerTodosLosCursos() {
        return cursoRepository.findAll();
    }
    
    @Override
    public boolean actualizarCurso(Curso curso) throws IllegalArgumentException {
        if (curso.getId() == null) {
            throw new IllegalArgumentException("El curso debe tener un ID para ser actualizado");
        }
        
        validarCurso(curso, true);
        
        // Verificar que no exista otro curso con el mismo código
        if (cursoRepository.existsByCodigoAndNotId(curso.getCodigo(), curso.getId())) {
            throw new IllegalArgumentException("Ya existe otro curso con el código: " + 
                curso.getCodigo());
        }
        
        return cursoRepository.update(curso);
    }
    
    @Override
    public boolean eliminarCurso(ObjectId id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }
        
        if (!cursoRepository.existsById(id)) {
            throw new IllegalArgumentException("No existe un curso con el ID proporcionado");
        }
        
        return cursoRepository.deleteById(id);
    }
    
    @Override
    public List<Curso> buscarPorNombre(String nombre) {
        if (!ValidationUtil.isNotEmpty(nombre)) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        return cursoRepository.findByNombreContaining(nombre);
    }
    
    @Override
    public List<Curso> buscarPorCreditos(int creditos) {
        if (creditos < 1 || creditos > 10) {
            throw new IllegalArgumentException("Los créditos deben estar entre 1 y 10");
        }
        return cursoRepository.findByCreditos(creditos);
    }
    
    @Override
    public long contarCursos() {
        return cursoRepository.count();
    }
    
    /**
     * Valida los datos del curso
     */
    private void validarCurso(Curso curso, boolean esActualizacion) {
        if (curso == null) {
            throw new IllegalArgumentException("El curso no puede ser nulo");
        }
        
        // Validar código
        if (!ValidationUtil.isValidLength(curso.getCodigo(), 2)) {
            throw new IllegalArgumentException("El código debe tener al menos 2 caracteres");
        }
        
        // Validar nombre
        if (!ValidationUtil.isValidLength(curso.getNombre(), 2)) {
            throw new IllegalArgumentException("El nombre debe tener al menos 2 caracteres");
        }
        
        // Validar descripción
        if (!ValidationUtil.isValidLength(curso.getDescripcion(), 5)) {
            throw new IllegalArgumentException("La descripción debe tener al menos 5 caracteres");
        }
        
        // Validar créditos
        if (curso.getCreditos() < 1 || curso.getCreditos() > 10) {
            throw new IllegalArgumentException("Los créditos deben estar entre 1 y 10");
        }
        
        // Normalizar datos
        curso.setCodigo(ValidationUtil.cleanText(curso.getCodigo()).toUpperCase());
        curso.setNombre(ValidationUtil.cleanText(curso.getNombre()));
        curso.setDescripcion(ValidationUtil.cleanText(curso.getDescripcion()));
    }
}