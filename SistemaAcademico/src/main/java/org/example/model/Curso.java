package org.example.model;

import org.bson.Document;
import org.bson.types.ObjectId;

/**
 * Entidad que representa un Curso en el sistema académico
 */
public class Curso {
    private ObjectId id;
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

    // Método para convertir a Document de MongoDB
    public Document toDocument() {
        Document doc = new Document();
        if (id != null) {
            doc.append("_id", id);
        }
        doc.append("codigo", codigo);
        doc.append("nombre", nombre);
        doc.append("descripcion", descripcion);
        doc.append("creditos", creditos);
        return doc;
    }

    // Getters y Setters
    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

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

    // Método para mostrar información resumida
    public String toStringFormatted() {
        return String.format("ID: %s | Código: %s | Nombre: %s | Créditos: %d",
                id != null ? id.toHexString().substring(18) : "N/A",
                codigo,
                nombre,
                creditos);
    }
}