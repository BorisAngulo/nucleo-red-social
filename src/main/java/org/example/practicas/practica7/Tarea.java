package org.example.practicas.practica7;

/** Una tarea pendiente: identificador, descripción y prioridad. */
public class Tarea {

    private final int id;
    private final String descripcion;
    private final String prioridad;

    public Tarea(int id, String descripcion, String prioridad) {
        if (id <= 0) {
            throw new IllegalArgumentException("El id debe ser mayor que cero.");
        }
        if (descripcion == null || descripcion.isBlank()) {
            throw new IllegalArgumentException("La descripción no puede estar vacía.");
        }
        if (prioridad == null || prioridad.isBlank()) {
            throw new IllegalArgumentException("La prioridad no puede estar vacía.");
        }
        this.id = id;
        this.descripcion = descripcion.trim();
        this.prioridad = prioridad.trim();
    }

    public int getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getPrioridad() {
        return prioridad;
    }

    @Override
    public String toString() {
        return "Tarea " + id + " | " + descripcion + " | prioridad " + prioridad;
    }
}
