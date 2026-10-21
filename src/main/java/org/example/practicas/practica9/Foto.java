package org.example.practicas.practica9;

/** Una foto de la galería: nombre de archivo y una descripción. */
public class Foto {

    private final String nombre;
    private final String descripcion;

    public Foto(String nombre, String descripcion) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre de la foto no puede estar vacío.");
        }
        if (descripcion == null || descripcion.isBlank()) {
            throw new IllegalArgumentException("La descripción no puede estar vacía.");
        }
        this.nombre = nombre.trim();
        this.descripcion = descripcion.trim();
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return nombre + " | " + descripcion;
    }
}
