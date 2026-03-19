package org.example.model;

import org.bson.Document;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Estudiante - extiende BaseModel para no repetir código del id, toDocument, etc.
 * Se aplicó Template Method: BaseModel tiene la lógica común y cada modelo agrega sus campos.
 */
public class Estudiante extends BaseModel {
    // Antes tenía: private ObjectId id; → ahora se hereda de BaseModel
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
        // Ternario: si fechaStr no es null lo parsea, si no queda null
        this.fechaNacimiento = fechaStr != null ? LocalDate.parse(fechaStr) : null;
        this.estado = doc.getString("estado");
    }

    /*
     * Antes cada modelo tenía su propio toDocument() completo.
     * Ahora BaseModel maneja el _id y solo agregamos los campos propios aquí.
     */
    @Override
    protected void agregarCampos(Document doc) {
        doc.append("nombre", nombre);
        doc.append("identificacion", identificacion);
        doc.append("email", email);
        doc.append("fecha_nacimiento", fechaNacimiento != null ? fechaNacimiento.toString() : null);
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

    /*
     * Antes toStringFormatted() estaba completo aquí, ahora BaseModel lo llama
     * y este método solo define el formato propio del estudiante.
     */
    @Override
    protected String obtenerFormatoResumido() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return String.format("ID: %s | Nombre: %s | Identificación: %s | Email: %s | Fecha Nac.: %s | Estado: %s",
                getShortId(),
                nombre,
                identificacion,
                email,
                fechaNacimiento != null ? fechaNacimiento.format(formatter) : "N/A",
                estado);
    }
}