package org.example.practicas.practica8;

/** Un proceso en la cola de listos: identificador, nombre y ráfaga que le queda. */
public class Proceso {

    private final int id;
    private final String nombre;
    private int rafagaRestante;

    public Proceso(int id, String nombre, int rafaga) {
        if (id <= 0) {
            throw new IllegalArgumentException("El id del proceso debe ser mayor que cero.");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del proceso no puede estar vacío.");
        }
        if (rafaga <= 0) {
            throw new IllegalArgumentException("La ráfaga debe ser mayor que cero.");
        }
        this.id = id;
        this.nombre = nombre.trim();
        this.rafagaRestante = rafaga;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getRafagaRestante() {
        return rafagaRestante;
    }

    /** Resta el tiempo de CPU de este turno. No baja de cero. */
    public void consumir(int unidades) {
        rafagaRestante = Math.max(0, rafagaRestante - unidades);
    }

    public boolean termino() {
        return rafagaRestante == 0;
    }

    @Override
    public String toString() {
        return "P" + id + " " + nombre + " | ráfaga restante " + rafagaRestante;
    }
}
