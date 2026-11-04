package org.example.practicas.practica6;

import org.example.estructuradedatos.ColaArreglo;

/**
 * Dispensador de turnos sobre la cola circular de arreglos.
 * Encolar escribe en {@code (frente + tamaño) % capacidad} y desencolar
 * avanza el frente con el mismo módulo, así el anillo reutiliza el inicio.
 */
public class ColaCircularBanco {

    private final ColaArreglo<Cliente> turnos;

    public ColaCircularBanco(int capacidad) {
        this.turnos = new ColaArreglo<>(capacidad);
    }

    public boolean estaVacia() {
        return turnos.estaVacia();
    }

    public boolean estaLlena() {
        return turnos.estaLlena();
    }

    /**
     * Recorre solo los turnos activos. La celda física es
     * {@code (frente + paso) % capacidad}.
     */
    public void mostrarCola() {
        if (estaVacia()) {
            System.out.println("[ Cola del banco vacía: no hay clientes en espera ]");
            return;
        }
        int frente = turnos.indiceFrente();
        int capacidad = turnos.getCapacidad();
        System.out.println("  frente=" + frente + "  fin=" + turnos.indiceFin() + "  tamaño=" + size());
        int paso = 0;
        for (Cliente cliente : turnos) {
            int celda = (frente + paso) % capacidad;
            String marca = paso == 0 ? " <-- [EN ATENCIÓN]" : "";
            System.out.println("  [" + celda + "] " + cliente + marca);
            paso++;
        }
    }

    /** Emite un turno. Con el anillo lleno no pisa a quien sigue en espera. */
    public boolean encolar(Cliente nuevoCliente) {
        if (nuevoCliente == null) {
            throw new IllegalArgumentException("El cliente no puede ser nulo.");
        }
        if (estaLlena()) {
            System.out.println("  Dispensador lleno: no se emite el turno de " + nuevoCliente.getNombre() + ".");
            return false;
        }
        turnos.encolar(nuevoCliente);
        return true;
    }

    /** Mira quién sigue en ventanilla sin mover frente ni el tamaño. */
    public Cliente peek() {
        if (estaVacia()) {
            return null;
        }
        return turnos.frente();
    }

    public int size() {
        return turnos.tamano();
    }

    /** Atiende al cliente del frente y libera esa celda. Vacía, devuelve null. */
    public Cliente desencolar() {
        if (estaVacia()) {
            System.out.println("  Ventanilla libre: no hay clientes en espera.");
            return null;
        }
        Cliente atendido = turnos.desencolar();
        System.out.println("  Atendido: " + atendido);
        return atendido;
    }
}
