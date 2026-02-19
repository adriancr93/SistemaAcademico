package org.example.model;

import org.bson.Document;
import org.bson.types.ObjectId;

/**
 * Entidad que representa un Grupo en el sistema académico
 */
public class Grupo {
    private ObjectId id;
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
        this.estado = estado != null ? estado : "activo";
    }

    // Constructor desde Document de MongoDB
    public Grupo(Document doc) {
        this.id = doc.getObjectId("_id");
        this.nombre = doc.getString("nombre");
        this.descripcion = doc.getString("descripcion");
        this.estado = doc.getString("estado");
    }

    // Método para convertir a Document de MongoDB
    public Document toDocument() {
        Document doc = new Document();
        if (id != null) {
            doc.append("_id", id);
        }
        doc.append("nombre", nombre);
        doc.append("descripcion", descripcion);
        doc.append("estado", estado);
        return doc;
    }

    // Getters y Setters
    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
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

    // Método para mostrar información resumida
    public String toStringFormatted() {
        return String.format("ID: %s | Nombre: %s | Descripción: %s | Estado: %s",
                id != null ? id.toHexString().substring(18) : "N/A",
                nombre,
                descripcion,
                estado);
    }
}