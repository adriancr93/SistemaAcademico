package org.example.repository.impl;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.example.config.MongoConfig;
import org.example.model.Profesor;
import org.example.repository.ProfesorRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.mongodb.client.model.Filters.*;

/**
 * Implementación de ProfesorRepository para MongoDB
 */
public class ProfesorRepositoryMongoDB implements ProfesorRepository {
    
    private final MongoCollection<Document> collection;
    private static final String COLLECTION_NAME = "profesores";
    
    public ProfesorRepositoryMongoDB() {
        this.collection = MongoConfig.getInstance()
                .getDatabase()
                .getCollection(COLLECTION_NAME);
    }
    
    @Override
    public Profesor save(Profesor profesor) {
        try {
            Document doc = profesor.toDocument();
            
            if (profesor.getId() == null) {
                // Inserción nueva
                collection.insertOne(doc);
                profesor.setId(doc.getObjectId("_id"));
            } else {
                // Actualización
                collection.replaceOne(eq("_id", profesor.getId()), doc);
            }
            
            return profesor;
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar profesor: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Optional<Profesor> findById(ObjectId id) {
        try {
            Document doc = collection.find(eq("_id", id)).first();
            return doc != null ? Optional.of(new Profesor(doc)) : Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar profesor por ID: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Profesor> findAll() {
        List<Profesor> profesores = new ArrayList<>();
        try {
            try (MongoCursor<Document> cursor = collection.find().iterator()) {
                while (cursor.hasNext()) {
                    profesores.add(new Profesor(cursor.next()));
                }
            }
            return profesores;
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener todos los profesores: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean update(Profesor profesor) {
        try {
            if (profesor.getId() == null) {
                throw new IllegalArgumentException("El profesor debe tener un ID para ser actualizado");
            }
            
            Document doc = profesor.toDocument();
            UpdateResult result = collection.replaceOne(eq("_id", profesor.getId()), doc);
            return result.getMatchedCount() > 0;
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar profesor: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean deleteById(ObjectId id) {
        try {
            DeleteResult result = collection.deleteOne(eq("_id", id));
            return result.getDeletedCount() > 0;
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar profesor: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean existsById(ObjectId id) {
        try {
            return collection.countDocuments(eq("_id", id)) > 0;
        } catch (Exception e) {
            throw new RuntimeException("Error al verificar existencia de profesor: " + e.getMessage(), e);
        }
    }
    
    @Override
    public long count() {
        try {
            return collection.countDocuments();
        } catch (Exception e) {
            throw new RuntimeException("Error al contar profesores: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Optional<Profesor> findByIdentificacion(String identificacion) {
        try {
            Document doc = collection.find(eq("identificacion", identificacion)).first();
            return doc != null ? Optional.of(new Profesor(doc)) : Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar profesor por identificación: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Optional<Profesor> findByEmail(String email) {
        try {
            Document doc = collection.find(eq("email", email)).first();
            return doc != null ? Optional.of(new Profesor(doc)) : Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar profesor por email: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Profesor> findByDepartamento(String departamento) {
        List<Profesor> profesores = new ArrayList<>();
        try {
            try (MongoCursor<Document> cursor = collection.find(eq("departamento", departamento)).iterator()) {
                while (cursor.hasNext()) {
                    profesores.add(new Profesor(cursor.next()));
                }
            }
            return profesores;
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar profesores por departamento: " + e.getMessage(), e);
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