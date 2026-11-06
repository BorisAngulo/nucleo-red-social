package org.example.practicas.practica17;

/** Persona de la empresa. El árbol la ordena por su código. */
public class Empleado implements Comparable<Empleado> {

    private final int codigo;
    private final String nombre;
    private final String cargo;

    public Empleado(int codigo, String nombre, String cargo) {
        if (codigo <= 0) {
            throw new IllegalArgumentException("El código debe ser mayor que cero.");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        if (cargo == null || cargo.isBlank()) {
            throw new IllegalArgumentException("El cargo no puede estar vacío.");
        }
        this.codigo = codigo;
        this.nombre = nombre.trim();
        this.cargo = cargo.trim();
    }

    public int getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCargo() {
        return cargo;
    }

    @Override
    public int compareTo(Empleado otro) {
        return Integer.compare(codigo, otro.codigo);
    }

    @Override
    public boolean equals(Object otro) {
        if (this == otro) {
            return true;
        }
        if (!(otro instanceof Empleado empleado)) {
            return false;
        }
        return codigo == empleado.codigo;
    }

    @Override
    public int hashCode() {
        return codigo;
    }

    @Override
    public String toString() {
        return "[" + codigo + "] " + nombre + " — " + cargo;
    }
}
