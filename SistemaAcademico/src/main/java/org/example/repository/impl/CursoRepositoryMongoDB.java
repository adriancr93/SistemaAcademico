package org.example.repository.impl;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.example.config.MongoConfig;
import org.example.model.Curso;
import org.example.repository.CursoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Filters.regex;

/**
 * Implementación de CursoRepository para MongoDB
 */
public class CursoRepositoryMongoDB implements CursoRepository {
    
    private final MongoCollection<Document> collection;
    private static final String COLLECTION_NAME = "cursos";
    
    public CursoRepositoryMongoDB() {
        this.collection = MongoConfig.getInstance()
                .getDatabase()
                .getCollection(COLLECTION_NAME);
    }
    
    @Override
    public Curso save(Curso curso) {
        try {
            Document doc = curso.toDocument();
            
            if (curso.getId() == null) {
                // Inserción nueva
                collection.insertOne(doc);
                curso.setId(doc.getObjectId("_id"));
            } else {
                // Actualización
                collection.replaceOne(eq("_id", curso.getId()), doc);
            }
            
            return curso;
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar curso: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Optional<Curso> findById(ObjectId id) {
        try {
            Document doc = collection.find(eq("_id", id)).first();
            return doc != null ? Optional.of(new Curso(doc)) : Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar curso por ID: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Curso> findAll() {
        List<Curso> cursos = new ArrayList<>();
        try {
            try (MongoCursor<Document> cursor = collection.find().iterator()) {
                while (cursor.hasNext()) {
                    cursos.add(new Curso(cursor.next()));
                }
            }
            return cursos;
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener todos los cursos: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean update(Curso curso) {
        try {
            if (curso.getId() == null) {
                throw new IllegalArgumentException("El curso debe tener un ID para ser actualizado");
            }
            
            Document doc = curso.toDocument();
            UpdateResult result = collection.replaceOne(eq("_id", curso.getId()), doc);
            return result.getMatchedCount() > 0;
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar curso: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean deleteById(ObjectId id) {
        try {
            DeleteResult result = collection.deleteOne(eq("_id", id));
            return result.getDeletedCount() > 0;
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar curso: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean existsById(ObjectId id) {
        try {
            return collection.countDocuments(eq("_id", id)) > 0;
        } catch (Exception e) {
            throw new RuntimeException("Error al verificar existencia de curso: " + e.getMessage(), e);
        }
    }
    
    @Override
    public long count() {
        try {
            return collection.countDocuments();
        } catch (Exception e) {
            throw new RuntimeException("Error al contar cursos: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Optional<Curso> findByNombre(String nombre) {
        try {
            Document doc = collection.find(eq("nombre", nombre)).first();
            return doc != null ? Optional.of(new Curso(doc)) : Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar curso por nombre: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Curso> findByNombreContaining(String nombre) {
        List<Curso> cursos = new ArrayList<>();
        try {
            // Usar expresión regular para búsqueda insensible a mayúsculas
            Pattern pattern = Pattern.compile(nombre, Pattern.CASE_INSENSITIVE);
            try (MongoCursor<Document> cursor = collection.find(regex("nombre", pattern)).iterator()) {
                while (cursor.hasNext()) {
                    cursos.add(new Curso(cursor.next()));
                }
            }
            return cursos;
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar cursos por nombre: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Curso> findByCreditos(int creditos) {
        List<Curso> cursos = new ArrayList<>();
        try {
            try (MongoCursor<Document> cursor = collection.find(eq("creditos", creditos)).iterator()) {
                while (cursor.hasNext()) {
                    cursos.add(new Curso(cursor.next()));
                }
            }
            return cursos;
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar cursos por créditos: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean existsByCodigo(String codigo) {
        try {
            return collection.countDocuments(eq("codigo", codigo)) > 0;
        } catch (Exception e) {
            throw new RuntimeException("Error al verificar existencia por código: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean existsByCodigoAndNotId(String codigo, ObjectId excludeId) {
        try {
            return collection.countDocuments(and(
                eq("codigo", codigo),
                ne("_id", excludeId)
            )) > 0;
        } catch (Exception e) {
            throw new RuntimeException("Error al verificar código único: " + e.getMessage(), e);
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