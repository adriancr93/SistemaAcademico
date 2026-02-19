package org.example.model;

import org.bson.Document;
import org.bson.types.ObjectId;

/**
 * Entidad que representa un Profesor en el sistema académico
 */
public class Profesor {
    private ObjectId id;
    private String nombre;
    private String identificacion;
    private String email;
    private String departamento;
    private String estado;

    // Constructor por defecto
    public Profesor() {
        this.estado = "activo";
    }

    // Constructor con parámetros
    public Profesor(String nombre, String identificacion, String email, 
                   String departamento, String estado) {
        this.nombre = nombre;
        this.identificacion = identificacion;
        this.email = email;
        this.departamento = departamento;
        this.estado = estado != null ? estado : "activo";
    }

    // Constructor desde Document de MongoDB
    public Profesor(Document doc) {
        this.id = doc.getObjectId("_id");
        this.nombre = doc.getString("nombre");
        this.identificacion = doc.getString("identificacion");
        this.email = doc.getString("email");
        this.departamento = doc.getString("departamento");
        this.estado = doc.getString("estado");
    }

    // Método para convertir a Document de MongoDB
    public Document toDocument() {
        Document doc = new Document();
        if (id != null) {
            doc.append("_id", id);
        }
        doc.append("nombre", nombre);
        doc.append("identificacion", identificacion);
        doc.append("email", email);
        doc.append("departamento", departamento);
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

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return String.format("Profesor{id=%s, nombre='%s', identificacion='%s', email='%s', departamento='%s', estado='%s'}",
                id, nombre, identificacion, email, departamento, estado);
    }

    // Método para mostrar información resumida
    public String toStringFormatted() {
        return String.format("ID: %s | Nombre: %s | Identificación: %s | Email: %s | Departamento: %s | Estado: %s",
                id != null ? id.toHexString().substring(18) : "N/A",
                nombre,
                identificacion,
                email,
                departamento,
                estado);
    }
}