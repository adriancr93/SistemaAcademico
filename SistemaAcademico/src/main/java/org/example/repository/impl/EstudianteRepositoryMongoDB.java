package org.example.repository.impl;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.example.config.MongoConfig;
import org.example.model.Estudiante;
import org.example.repository.EstudianteRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.mongodb.client.model.Filters.*;

/**
 * Implementación de EstudianteRepository para MongoDB
 */
public class EstudianteRepositoryMongoDB implements EstudianteRepository {
    
    private final MongoCollection<Document> collection;
    private static final String COLLECTION_NAME = "estudiantes";
    
    public EstudianteRepositoryMongoDB() {
        this.collection = MongoConfig.getInstance()
                .getDatabase()
                .getCollection(COLLECTION_NAME);
    }
    
    @Override
    public Estudiante save(Estudiante estudiante) {
        try {
            Document doc = estudiante.toDocument();
            
            if (estudiante.getId() == null) {
                // Inserción nueva
                collection.insertOne(doc);
                estudiante.setId(doc.getObjectId("_id"));
            } else {
                // Actualización
                collection.replaceOne(eq("_id", estudiante.getId()), doc);
            }
            
            return estudiante;
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar estudiante: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Optional<Estudiante> findById(ObjectId id) {
        try {
            Document doc = collection.find(eq("_id", id)).first();
            return doc != null ? Optional.of(new Estudiante(doc)) : Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar estudiante por ID: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Estudiante> findAll() {
        List<Estudiante> estudiantes = new ArrayList<>();
        try {
            try (MongoCursor<Document> cursor = collection.find().iterator()) {
                while (cursor.hasNext()) {
                    estudiantes.add(new Estudiante(cursor.next()));
                }
            }
            return estudiantes;
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener todos los estudiantes: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean update(Estudiante estudiante) {
        try {
            if (estudiante.getId() == null) {
                throw new IllegalArgumentException("El estudiante debe tener un ID para ser actualizado");
            }
            
            Document doc = estudiante.toDocument();
            UpdateResult result = collection.replaceOne(eq("_id", estudiante.getId()), doc);
            return result.getMatchedCount() > 0;
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar estudiante: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean deleteById(ObjectId id) {
        try {
            DeleteResult result = collection.deleteOne(eq("_id", id));
            return result.getDeletedCount() > 0;
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar estudiante: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean existsById(ObjectId id) {
        try {
            return collection.countDocuments(eq("_id", id)) > 0;
        } catch (Exception e) {
            throw new RuntimeException("Error al verificar existencia de estudiante: " + e.getMessage(), e);
        }
    }
    
    @Override
    public long count() {
        try {
            return collection.countDocuments();
        } catch (Exception e) {
            throw new RuntimeException("Error al contar estudiantes: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Optional<Estudiante> findByIdentificacion(String identificacion) {
        try {
            Document doc = collection.find(eq("identificacion", identificacion)).first();
            return doc != null ? Optional.of(new Estudiante(doc)) : Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar estudiante por identificación: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Optional<Estudiante> findByEmail(String email) {
        try {
            Document doc = collection.find(eq("email", email)).first();
            return doc != null ? Optional.of(new Estudiante(doc)) : Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar estudiante por email: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean existsByIdentificacionAndNotId(String identificacion, ObjectId excludeId) {
        try {
            return collection.countDocuments(and(
                eq("identificacion", identificacion),
                ne("_id", excludeId)
            )) > 0;
        } catch (Exception e) {
            throw new RuntimeException("Error al verificar identificación única: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean existsByEmailAndNotId(String email, ObjectId excludeId) {
        try {
            return collection.countDocuments(and(
                eq("email", email),
                ne("_id", excludeId)
            )) > 0;
        } catch (Exception e) {
            throw new RuntimeException("Error al verificar email único: " + e.getMessage(), e);
        }
    }
}