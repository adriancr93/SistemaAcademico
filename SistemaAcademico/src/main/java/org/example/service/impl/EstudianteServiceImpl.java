package org.example.service.impl;

import org.bson.types.ObjectId;
import org.example.model.Estudiante;
import org.example.repository.EstudianteRepository;
import org.example.repository.impl.EstudianteRepositoryMongoDB;
import org.example.service.EstudianteService;
import org.example.util.ValidationUtil;

import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio de Estudiante
 */
public class EstudianteServiceImpl implements EstudianteService {
    
    private final EstudianteRepository estudianteRepository;
    
    public EstudianteServiceImpl() {
        this.estudianteRepository = new EstudianteRepositoryMongoDB();
    }
    
    // Constructor para inyección de dependencias (útil para testing)
    public EstudianteServiceImpl(EstudianteRepository estudianteRepository) {
        this.estudianteRepository = estudianteRepository;
    }
    
    @Override
    public Estudiante crearEstudiante(Estudiante estudiante) throws IllegalArgumentException {
        validarEstudiante(estudiante, false);
        
        // Verificar que no exista otro estudiante con la misma identificación
        if (estudianteRepository.findByIdentificacion(estudiante.getIdentificacion()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un estudiante con la identificación: " + 
                estudiante.getIdentificacion());
        }
        
        // Verificar que no exista otro estudiante con el mismo email
        if (estudianteRepository.findByEmail(estudiante.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un estudiante con el email: " + 
                estudiante.getEmail());
        }
        
        return estudianteRepository.save(estudiante);
    }
    
    @Override
    public Optional<Estudiante> obtenerEstudiantePorId(ObjectId id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }
        return estudianteRepository.findById(id);
    }
    
    @Override
    public List<Estudiante> obtenerTodosLosEstudiantes() {
        return estudianteRepository.findAll();
    }
    
    @Override
    public boolean actualizarEstudiante(Estudiante estudiante) throws IllegalArgumentException {
        if (estudiante.getId() == null) {
            throw new IllegalArgumentException("El estudiante debe tener un ID para ser actualizado");
        }
        
        validarEstudiante(estudiante, true);
        
        // Verificar que no exista otro estudiante con la misma identificación
        if (estudianteRepository.existsByIdentificacionAndNotId(
                estudiante.getIdentificacion(), estudiante.getId())) {
            throw new IllegalArgumentException("Ya existe otro estudiante con la identificación: " + 
                estudiante.getIdentificacion());
        }
        
        // Verificar que no exista otro estudiante con el mismo email
        if (estudianteRepository.existsByEmailAndNotId(estudiante.getEmail(), estudiante.getId())) {
            throw new IllegalArgumentException("Ya existe otro estudiante con el email: " + 
                estudiante.getEmail());
        }
        
        return estudianteRepository.update(estudiante);
    }
    
    @Override
    public boolean eliminarEstudiante(ObjectId id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }
        
        if (!estudianteRepository.existsById(id)) {
            throw new IllegalArgumentException("No existe un estudiante con el ID proporcionado");
        }
        
        return estudianteRepository.deleteById(id);
    }
    
    @Override
    public Optional<Estudiante> buscarPorIdentificacion(String identificacion) {
        if (!ValidationUtil.isNotEmpty(identificacion)) {
            throw new IllegalArgumentException("La identificación no puede estar vacía");
        }
        return estudianteRepository.findByIdentificacion(identificacion);
    }
    
    @Override
    public Optional<Estudiante> buscarPorEmail(String email) {
        if (!ValidationUtil.isNotEmpty(email)) {
            throw new IllegalArgumentException("El email no puede estar vacío");
        }
        return estudianteRepository.findByEmail(email);
    }
    
    @Override
    public long contarEstudiantes() {
        return estudianteRepository.count();
    }
    
    /**
     * Valida los datos del estudiante
     */
    private void validarEstudiante(Estudiante estudiante, boolean esActualizacion) {
        if (estudiante == null) {
            throw new IllegalArgumentException("El estudiante no puede ser nulo");
        }
        
        // Validar nombre
        if (!ValidationUtil.isValidLength(estudiante.getNombre(), 2)) {
            throw new IllegalArgumentException("El nombre debe tener al menos 2 caracteres");
        }
        
        // Validar identificación
        if (!ValidationUtil.isValidIdentification(estudiante.getIdentificacion())) {
            throw new IllegalArgumentException("La identificación debe tener entre 9 y 12 dígitos");
        }
        
        // Validar email
        if (!ValidationUtil.isValidEmail(estudiante.getEmail())) {
            throw new IllegalArgumentException("El formato del email es inválido");
        }
        
        // Validar fecha de nacimiento
        if (estudiante.getFechaNacimiento() == null || 
            !ValidationUtil.isValidBirthDate(estudiante.getFechaNacimiento())) {
            throw new IllegalArgumentException("La fecha de nacimiento es inválida");
        }
        
        // Validar estado
        if (!ValidationUtil.isValidState(estudiante.getEstado())) {
            throw new IllegalArgumentException("El estado debe ser 'activo' o 'inactivo'");
        }
        
        // Normalizar datos
        estudiante.setNombre(ValidationUtil.cleanText(estudiante.getNombre()));
        estudiante.setIdentificacion(ValidationUtil.cleanText(estudiante.getIdentificacion()));
        estudiante.setEmail(ValidationUtil.cleanText(estudiante.getEmail().toLowerCase()));
        estudiante.setEstado(ValidationUtil.normalizeState(estudiante.getEstado()));
    }
}