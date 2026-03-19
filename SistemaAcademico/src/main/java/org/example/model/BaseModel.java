package org.example.model;

import org.bson.Document;
import org.bson.types.ObjectId;


public abstract class BaseModel {

    protected ObjectId id;

    // ─── Getters/Setters comunes ──────────────────────────────────────
    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public final Document toDocument() {
        Document doc = new Document();
        // Operador ternario: reemplaza el if (id != null) { doc.append(...) }
        doc = id != null ? doc.append("_id", id) : doc;
        agregarCampos(doc);
        return doc;
    }

    protected abstract void agregarCampos(Document doc);

    public final String toStringFormatted() {
        return obtenerFormatoResumido();
    }

    protected abstract String obtenerFormatoResumido();

    // Antes se repetía la misma lógica en dos métodos.
    // Ahora getShortId() reutiliza el método estático para no duplicar código.
    public String getShortId() {
        return getShortId(id);
    }

    protected static String getShortId(ObjectId objectId) {
        return objectId != null ? objectId.toHexString().substring(18) : "N/A";
    }
}
