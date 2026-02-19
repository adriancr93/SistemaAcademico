package org.example.repository.impl;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.example.config.MongoConfig;
import org.example.model.GrupoCurso;
import org.example.repository.GrupoCursoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.mongodb.client.model.Filters.*;

/**
 * Implementación de GrupoCursoRepository para MongoDB
 */
public class GrupoCursoRepositoryMongoDB implements GrupoCursoRepository {
    
    private final MongoCollection<Document> collection;
    private static final String COLLECTION_NAME = "grupos_cursos";
    
    public GrupoCursoRepositoryMongoDB() {
        this.collection = MongoConfig.getInstance()
                .getDatabase()
                .getCollection(COLLECTION_NAME);
    }
    
    @Override
    public GrupoCurso save(GrupoCurso grupoCurso) {
        try {
            Document doc = grupoCurso.toDocument();
            
            if (grupoCurso.getId() == null) {
                // Inserción nueva
                collection.insertOne(doc);
                grupoCurso.setId(doc.getObjectId("_id"));
            } else {
                // Actualización
                collection.replaceOne(eq("_id", grupoCurso.getId()), doc);
            }
            
            return grupoCurso;
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar relación grupo-curso: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Optional<GrupoCurso> findById(ObjectId id) {
        try {
            Document doc = collection.find(eq("_id", id)).first();
            return doc != null ? Optional.of(new GrupoCurso(doc)) : Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar relación por ID: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<GrupoCurso> findAll() {
        List<GrupoCurso> relaciones = new ArrayList<>();
        try {
            try (MongoCursor<Document> cursor = collection.find().iterator()) {
                while (cursor.hasNext()) {
                    relaciones.add(new GrupoCurso(cursor.next()));
                }
            }
            return relaciones;
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener todas las relaciones: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean update(GrupoCurso grupoCurso) {
        try {
            if (grupoCurso.getId() == null) {
                throw new IllegalArgumentException("La relación debe tener un ID para ser actualizada");
            }
            
            Document doc = grupoCurso.toDocument();
            UpdateResult result = collection.replaceOne(eq("_id", grupoCurso.getId()), doc);
            return result.getMatchedCount() > 0;
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar relación: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean deleteById(ObjectId id) {
        try {
            DeleteResult result = collection.deleteOne(eq("_id", id));
            return result.getDeletedCount() > 0;
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar relación: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean existsById(ObjectId id) {
        try {
            return collection.countDocuments(eq("_id", id)) > 0;
        } catch (Exception e) {
            throw new RuntimeException("Error al verificar existencia de relación: " + e.getMessage(), e);
        }
    }
    
    @Override
    public long count() {
        try {
            return collection.countDocuments();
        } catch (Exception e) {
            throw new RuntimeException("Error al contar relaciones: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<GrupoCurso> findByGrupoId(ObjectId grupoId) {
        List<GrupoCurso> relaciones = new ArrayList<>();
        try {
            try (MongoCursor<Document> cursor = collection.find(eq("grupo_id", grupoId)).iterator()) {
                while (cursor.hasNext()) {
                    relaciones.add(new GrupoCurso(cursor.next()));
                }
            }
            return relaciones;
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar relaciones por grupo: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<GrupoCurso> findByCursoId(ObjectId cursoId) {
        List<GrupoCurso> relaciones = new ArrayList<>();
        try {
            try (MongoCursor<Document> cursor = collection.find(eq("curso_id", cursoId)).iterator()) {
                while (cursor.hasNext()) {
                    relaciones.add(new GrupoCurso(cursor.next()));
                }
            }
            return relaciones;
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar relaciones por curso: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean existsByGrupoIdAndCursoId(ObjectId grupoId, ObjectId cursoId) {
        try {
            return collection.countDocuments(and(
                eq("grupo_id", grupoId),
                eq("curso_id", cursoId)
            )) > 0;
        } catch (Exception e) {
            throw new RuntimeException("Error al verificar relación existente: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean deleteByGrupoId(ObjectId grupoId) {
        try {
            DeleteResult result = collection.deleteMany(eq("grupo_id", grupoId));
            return result.getDeletedCount() > 0;
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar relaciones por grupo: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean deleteByCursoId(ObjectId cursoId) {
        try {
            DeleteResult result = collection.deleteMany(eq("curso_id", cursoId));
            return result.getDeletedCount() > 0;
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar relaciones por curso: " + e.getMessage(), e);
        }
    }
}