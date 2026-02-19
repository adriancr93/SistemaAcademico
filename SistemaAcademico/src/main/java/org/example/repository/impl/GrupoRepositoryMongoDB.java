package org.example.repository.impl;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.example.config.MongoConfig;
import org.example.model.Grupo;
import org.example.repository.GrupoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.mongodb.client.model.Filters.*;

/**
 * Implementación de GrupoRepository para MongoDB
 */
public class GrupoRepositoryMongoDB implements GrupoRepository {
    
    private final MongoCollection<Document> collection;
    private static final String COLLECTION_NAME = "grupos";
    
    public GrupoRepositoryMongoDB() {
        this.collection = MongoConfig.getInstance()
                .getDatabase()
                .getCollection(COLLECTION_NAME);
    }
    
    @Override
    public Grupo save(Grupo grupo) {
        try {
            Document doc = grupo.toDocument();
            
            if (grupo.getId() == null) {
                // Inserción nueva
                collection.insertOne(doc);
                grupo.setId(doc.getObjectId("_id"));
            } else {
                // Actualización
                collection.replaceOne(eq("_id", grupo.getId()), doc);
            }
            
            return grupo;
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar grupo: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Optional<Grupo> findById(ObjectId id) {
        try {
            Document doc = collection.find(eq("_id", id)).first();
            return doc != null ? Optional.of(new Grupo(doc)) : Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar grupo por ID: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Grupo> findAll() {
        List<Grupo> grupos = new ArrayList<>();
        try {
            try (MongoCursor<Document> cursor = collection.find().iterator()) {
                while (cursor.hasNext()) {
                    grupos.add(new Grupo(cursor.next()));
                }
            }
            return grupos;
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener todos los grupos: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean update(Grupo grupo) {
        try {
            if (grupo.getId() == null) {
                throw new IllegalArgumentException("El grupo debe tener un ID para ser actualizado");
            }
            
            Document doc = grupo.toDocument();
            UpdateResult result = collection.replaceOne(eq("_id", grupo.getId()), doc);
            return result.getMatchedCount() > 0;
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar grupo: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean deleteById(ObjectId id) {
        try {
            DeleteResult result = collection.deleteOne(eq("_id", id));
            return result.getDeletedCount() > 0;
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar grupo: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean existsById(ObjectId id) {
        try {
            return collection.countDocuments(eq("_id", id)) > 0;
        } catch (Exception e) {
            throw new RuntimeException("Error al verificar existencia de grupo: " + e.getMessage(), e);
        }
    }
    
    @Override
    public long count() {
        try {
            return collection.countDocuments();
        } catch (Exception e) {
            throw new RuntimeException("Error al contar grupos: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Optional<Grupo> findByNombre(String nombre) {
        try {
            Document doc = collection.find(eq("nombre", nombre)).first();
            return doc != null ? Optional.of(new Grupo(doc)) : Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar grupo por nombre: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean existsByNombreAndNotId(String nombre, ObjectId excludeId) {
        try {
            return collection.countDocuments(and(
                eq("nombre", nombre),
                ne("_id", excludeId)
            )) > 0;
        } catch (Exception e) {
            throw new RuntimeException("Error al verificar nombre único: " + e.getMessage(), e);
        }
    }
}