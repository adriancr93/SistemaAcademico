package org.example.model;

import org.bson.Document;
import org.bson.types.ObjectId;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Entidad que representa un Estudiante en el sistema académico
 */
public class Estudiante {
    private ObjectId id;
    private String nombre;
    private String identificacion;
    private String email;
    private LocalDate fechaNacimiento;
    private String estado;

    // Constructor por defecto
    public Estudiante() {
        this.estado = "activo";
    }

    // Constructor con parámetros
    public Estudiante(String nombre, String identificacion, String email, 
                     LocalDate fechaNacimiento, String estado) {
        this.nombre = nombre;
        this.identificacion = identificacion;
        this.email = email;
        this.fechaNacimiento = fechaNacimiento;
        this.estado = estado != null ? estado : "activo";
    }

    // Constructor desde Document de MongoDB
    public Estudiante(Document doc) {
        this.id = doc.getObjectId("_id");
        this.nombre = doc.getString("nombre");
        this.identificacion = doc.getString("identificacion");
        this.email = doc.getString("email");
        String fechaStr = doc.getString("fecha_nacimiento");
        if (fechaStr != null) {
            this.fechaNacimiento = LocalDate.parse(fechaStr);
        }
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
        if (fechaNacimiento != null) {
            doc.append("fecha_nacimiento", fechaNacimiento.toString());
        }
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

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return String.format("Estudiante{id=%s, nombre='%s', identificacion='%s', email='%s', fechaNacimiento=%s, estado='%s'}",
                id, nombre, identificacion, email, fechaNacimiento, estado);
    }

    // Método para mostrar información resumida
    public String toStringFormatted() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return String.format("ID: %s | Nombre: %s | Identificación: %s | Email: %s | Fecha Nac.: %s | Estado: %s",
                id != null ? id.toHexString().substring(18) : "N/A",
                nombre,
                identificacion,
                email,
                fechaNacimiento != null ? fechaNacimiento.format(formatter) : "N/A",
                estado);
    }
}