package org.example.service.impl;

import org.bson.types.ObjectId;
import org.example.model.Profesor;
import org.example.repository.ProfesorRepository;
import org.example.repository.impl.ProfesorRepositoryMongoDB;
import org.example.service.ProfesorService;
import org.example.util.ValidationUtil;

import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio de Profesor
 */
public class ProfesorServiceImpl implements ProfesorService {
    
    private final ProfesorRepository profesorRepository;
    
    public ProfesorServiceImpl() {
        this.profesorRepository = new ProfesorRepositoryMongoDB();
    }
    
    // Constructor para inyección de dependencias (útil para testing)
    public ProfesorServiceImpl(ProfesorRepository profesorRepository) {
        this.profesorRepository = profesorRepository;
    }
    
    @Override
    public Profesor crearProfesor(Profesor profesor) throws IllegalArgumentException {
        validarProfesor(profesor, false);
        
        // Verificar que no exista otro profesor con la misma identificación
        if (profesorRepository.findByIdentificacion(profesor.getIdentificacion()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un profesor con la identificación: " + 
                profesor.getIdentificacion());
        }
        
        // Verificar que no exista otro profesor con el mismo email
        if (profesorRepository.findByEmail(profesor.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un profesor con el email: " + 
                profesor.getEmail());
        }
        
        return profesorRepository.save(profesor);
    }
    
    @Override
    public Optional<Profesor> obtenerProfesorPorId(ObjectId id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }
        return profesorRepository.findById(id);
    }
    
    @Override
    public List<Profesor> obtenerTodosLosProfesores() {
        return profesorRepository.findAll();
    }
    
    @Override
    public boolean actualizarProfesor(Profesor profesor) throws IllegalArgumentException {
        if (profesor.getId() == null) {
            throw new IllegalArgumentException("El profesor debe tener un ID para ser actualizado");
        }
        
        validarProfesor(profesor, true);
        
        // Verificar que no exista otro profesor con la misma identificación
        if (profesorRepository.existsByIdentificacionAndNotId(
                profesor.getIdentificacion(), profesor.getId())) {
            throw new IllegalArgumentException("Ya existe otro profesor con la identificación: " + 
                profesor.getIdentificacion());
        }
        
        // Verificar que no exista otro profesor con el mismo email
        if (profesorRepository.existsByEmailAndNotId(profesor.getEmail(), profesor.getId())) {
            throw new IllegalArgumentException("Ya existe otro profesor con el email: " + 
                profesor.getEmail());
        }
        
        return profesorRepository.update(profesor);
    }
    
    @Override
    public boolean eliminarProfesor(ObjectId id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }
        
        if (!profesorRepository.existsById(id)) {
            throw new IllegalArgumentException("No existe un profesor con el ID proporcionado");
        }
        
        return profesorRepository.deleteById(id);
    }
    
    @Override
    public Optional<Profesor> buscarPorIdentificacion(String identificacion) {
        if (!ValidationUtil.isNotEmpty(identificacion)) {
            throw new IllegalArgumentException("La identificación no puede estar vacía");
        }
        return profesorRepository.findByIdentificacion(identificacion);
    }
    
    @Override
    public Optional<Profesor> buscarPorEmail(String email) {
        if (!ValidationUtil.isNotEmpty(email)) {
            throw new IllegalArgumentException("El email no puede estar vacío");
        }
        return profesorRepository.findByEmail(email);
    }
    
    @Override
    public List<Profesor> buscarPorDepartamento(String departamento) {
        if (!ValidationUtil.isNotEmpty(departamento)) {
            throw new IllegalArgumentException("El departamento no puede estar vacío");
        }
        return profesorRepository.findByDepartamento(departamento);
    }
    
    @Override
    public long contarProfesores() {
        return profesorRepository.count();
    }
    
    /**
     * Valida los datos del profesor
     */
    private void validarProfesor(Profesor profesor, boolean esActualizacion) {
        if (profesor == null) {
            throw new IllegalArgumentException("El profesor no puede ser nulo");
        }
        
        // Validar nombre
        if (!ValidationUtil.isValidLength(profesor.getNombre(), 2)) {
            throw new IllegalArgumentException("El nombre debe tener al menos 2 caracteres");
        }
        
        // Validar identificación
        if (!ValidationUtil.isValidIdentification(profesor.getIdentificacion())) {
            throw new IllegalArgumentException("La identificación debe tener entre 9 y 12 dígitos");
        }
        
        // Validar email
        if (!ValidationUtil.isValidEmail(profesor.getEmail())) {
            throw new IllegalArgumentException("El formato del email es inválido");
        }
        
        // Validar departamento
        if (!ValidationUtil.isValidLength(profesor.getDepartamento(), 2)) {
            throw new IllegalArgumentException("El departamento debe tener al menos 2 caracteres");
        }
        
        // Validar estado
        if (!ValidationUtil.isValidState(profesor.getEstado())) {
            throw new IllegalArgumentException("El estado debe ser 'activo' o 'inactivo'");
        }
        
        // Normalizar datos
        profesor.setNombre(ValidationUtil.cleanText(profesor.getNombre()));
        profesor.setIdentificacion(ValidationUtil.cleanText(profesor.getIdentificacion()));
        profesor.setEmail(ValidationUtil.cleanText(profesor.getEmail().toLowerCase()));
        profesor.setDepartamento(ValidationUtil.cleanText(profesor.getDepartamento()));
        profesor.setEstado(ValidationUtil.normalizeState(profesor.getEstado()));
    }
}