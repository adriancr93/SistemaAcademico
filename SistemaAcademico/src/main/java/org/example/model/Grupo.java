package org.example.model;

import org.bson.Document;

/**
 * Grupo - extiende BaseModel para no repetir código del id, toDocument, etc.
 * Se aplicó Template Method: la lógica común queda en BaseModel.
 */
public class Grupo extends BaseModel {
    // Antes tenía: private ObjectId id; → ahora se hereda de BaseModel
    private String nombre;
    private String descripcion;
    private String estado;

    // Constructor por defecto
    public Grupo() {
        this.estado = "activo";
    }

    // Constructor con parámetros
    public Grupo(String nombre, String descripcion, String estado) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        // Ternario: si estado es null se pone "activo" por defecto
        this.estado = estado != null ? estado : "activo";
    }

    // Constructor para crear la base de datos en MongoDB
    public Grupo(Document doc) {
        this.id = doc.getObjectId("_id");
        this.nombre = doc.getString("nombre");
        this.descripcion = doc.getString("descripcion");
        this.estado = doc.getString("estado");
    }

    // Antes cada modelo tenía su propio toDocument(). Ahora BaseModel maneja el _id.
    @Override
    protected void agregarCampos(Document doc) {
        doc.append("nombre", nombre);
        doc.append("descripcion", descripcion);
        doc.append("estado", estado);
    }

    // getId() y setId() ahora se heredan de BaseModel

    // Getters y Setters
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return String.format("Grupo{id=%s, nombre='%s', descripcion='%s', estado='%s'}",
                id, nombre, descripcion, estado);
    }

    // Antes toStringFormatted() estaba aquí completo. Ahora BaseModel lo llama.
    @Override
    protected String obtenerFormatoResumido() {
        return String.format("ID: %s | Nombre: %s | Descripción: %s | Estado: %s",
                getShortId(),
                nombre,
                descripcion,
                estado);
    }
}