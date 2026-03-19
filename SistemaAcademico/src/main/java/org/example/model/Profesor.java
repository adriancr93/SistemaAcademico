package org.example.model;

import org.bson.Document;

/**
 * Profesor - extiende BaseModel para no repetir código del id, toDocument, etc.
 * Se aplicó Template Method: la lógica común queda en BaseModel.
 */
public class Profesor extends BaseModel {
    // Antes tenía: private ObjectId id; → ahora se hereda de BaseModel
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
        // Ternario: si estado es null se pone "activo" por defecto
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

    // Antes cada modelo tenía su propio toDocument(). Ahora BaseModel maneja el _id.
    @Override
    protected void agregarCampos(Document doc) {
        doc.append("nombre", nombre);
        doc.append("identificacion", identificacion);
        doc.append("email", email);
        doc.append("departamento", departamento);
        doc.append("estado", estado);
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

    // Antes toStringFormatted() estaba aquí completo. Ahora BaseModel lo llama.
    @Override
    protected String obtenerFormatoResumido() {
        return String.format("ID: %s | Nombre: %s | Identificación: %s | Email: %s | Departamento: %s | Estado: %s",
                getShortId(),
                nombre,
                identificacion,
                email,
                departamento,
                estado);
    }
}