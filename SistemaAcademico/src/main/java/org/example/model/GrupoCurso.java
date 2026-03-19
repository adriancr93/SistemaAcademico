package org.example.model;

import org.bson.Document;
import org.bson.types.ObjectId;

/**
 * GrupoCurso - relación entre Grupo y Curso.
 * Extiende BaseModel para no repetir código del id, toDocument, etc.
 */
public class GrupoCurso extends BaseModel {
    // Antes tenía: private ObjectId id; → ahora se hereda de BaseModel
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

    // Antes cada modelo tenía su propio toDocument(). Ahora BaseModel maneja el _id.
    @Override
    protected void agregarCampos(Document doc) {
        doc.append("grupo_id", grupoId);
        doc.append("curso_id", cursoId);
    }

    // getId() y setId() ahora se heredan de BaseModel

    // Getters y Setters
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

    // Antes toStringFormatted() estaba aquí. Ahora usa getShortId() de BaseModel.
    @Override
    protected String obtenerFormatoResumido() {
        return String.format("ID: %s | Grupo ID: %s | Curso ID: %s",
                getShortId(),
                getShortId(grupoId),
                getShortId(cursoId));
    }
}