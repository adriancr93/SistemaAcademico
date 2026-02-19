package org.example.model;

import org.bson.Document;
import org.bson.types.ObjectId;

/**
 * Entidad que representa la relación entre un Grupo y un Curso
 */
public class GrupoCurso {
    private ObjectId id;
    private ObjectId grupoId;
    private ObjectId cursoId;

    // Constructor por defecto
    public GrupoCurso() {}

    // Constructor con parámetros
    public GrupoCurso(ObjectId grupoId, ObjectId cursoId) {
        this.grupoId = grupoId;
        this.cursoId = cursoId;
    }

    // Constructor desde Document de MongoDB
    public GrupoCurso(Document doc) {
        this.id = doc.getObjectId("_id");
        this.grupoId = doc.getObjectId("grupo_id");
        this.cursoId = doc.getObjectId("curso_id");
    }

    // Método para convertir a Document de MongoDB
    public Document toDocument() {
        Document doc = new Document();
        if (id != null) {
            doc.append("_id", id);
        }
        doc.append("grupo_id", grupoId);
        doc.append("curso_id", cursoId);
        return doc;
    }

    // Getters y Setters
    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getGrupoId() {
        return grupoId;
    }

    public void setGrupoId(ObjectId grupoId) {
        this.grupoId = grupoId;
    }

    public ObjectId getCursoId() {
        return cursoId;
    }

    public void setCursoId(ObjectId cursoId) {
        this.cursoId = cursoId;
    }

    @Override
    public String toString() {
        return String.format("GrupoCurso{id=%s, grupoId=%s, cursoId=%s}",
                id, grupoId, cursoId);
    }

    // Método para mostrar información resumida
    public String toStringFormatted() {
        return String.format("ID: %s | Grupo ID: %s | Curso ID: %s",
                id != null ? id.toHexString().substring(18) : "N/A",
                grupoId != null ? grupoId.toHexString().substring(18) : "N/A",
                cursoId != null ? cursoId.toHexString().substring(18) : "N/A");
    }
}