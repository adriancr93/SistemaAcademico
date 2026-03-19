package org.example.model;

import org.bson.Document;

/**
 * Curso - extiende BaseModel para no repetir código del id, toDocument, etc.
 * Se aplicó Template Method: la lógica común queda en BaseModel.
 */
public class Curso extends BaseModel {
    // Antes tenía: private ObjectId id; → ahora se hereda de BaseModel
    private String codigo;
    private String nombre;
    private String descripcion;
    private int creditos;

    // Constructor por defecto
    public Curso() {
        this.creditos = 1;
    }

    // Constructor con parámetros
    public Curso(String codigo, String nombre, String descripcion, int creditos) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.creditos = creditos;
    }

    // Constructor desde Document de MongoDB
    public Curso(Document doc) {
        this.id = doc.getObjectId("_id");
        this.codigo = doc.getString("codigo");
        this.nombre = doc.getString("nombre");
        this.descripcion = doc.getString("descripcion");
        this.creditos = doc.getInteger("creditos", 1);
    }

    // Antes cada modelo tenía su propio toDocument(). Ahora BaseModel maneja el _id.
    @Override
    protected void agregarCampos(Document doc) {
        doc.append("codigo", codigo);
        doc.append("nombre", nombre);
        doc.append("descripcion", descripcion);
        doc.append("creditos", creditos);
    }

    // getId() y setId() ahora se heredan de BaseModel

    // Getters y Setters
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getCreditos() {
        return creditos;
    }

    public void setCreditos(int creditos) {
        this.creditos = creditos;
    }

    @Override
    public String toString() {
        return String.format("Curso{id=%s, codigo='%s', nombre='%s', descripcion='%s', creditos=%d}",
                id, codigo, nombre, descripcion, creditos);
    }

    // Antes toStringFormatted() estaba aquí completo. Ahora BaseModel lo llama.
    @Override
    protected String obtenerFormatoResumido() {
        return String.format("ID: %s | Código: %s | Nombre: %s | Créditos: %d",
                getShortId(),
                codigo,
                nombre,
                creditos);
    }
}