package org.example.practicas.practica8;

import org.example.estructuradedatos.ListaCircular;

/**
 * Asigna la CPU por turnos sobre una lista circular simple.
 * El proceso en la cabeza recibe un quantum. Si aún le queda ráfaga,
 * {@code rotar} lo pasa al final y el siguiente queda en la cabeza.
 * Si terminó, sale del anillo y no vuelve a competir.
 */
public class PlanificadorRoundRobin {

    private final ListaCircular<Proceso> listos;
    private final int quantum;

    public PlanificadorRoundRobin(int quantum) {
        if (quantum <= 0) {
            throw new IllegalArgumentException("El quantum debe ser mayor que cero.");
        }
        this.quantum = quantum;
        this.listos = new ListaCircular<>();
    }

    public boolean estaVacia() {
        return listos.estaVacia();
    }

    public int getTamano() {
        return listos.tamano();
    }

    public int getQuantum() {
        return quantum;
    }

    /** El proceso nuevo entra al final. La cabeza, si ya hay uno en CPU, no cambia. */
    public void agregar(Proceso proceso) {
        if (proceso == null) {
            throw new IllegalArgumentException("El proceso no puede ser nulo.");
        }
        listos.insertarAlFinal(proceso);
    }

    public void mostrarCola() {
        if (estaVacia()) {
            System.out.println("[ Cola de listos vacía: no hay procesos en espera ]");
            return;
        }
        int posicion = 0;
        for (Proceso proceso : listos) {
            String marca = posicion == 0 ? " <-- [SIGUIENTE EN CPU]" : "";
            System.out.println("  " + proceso + marca);
            posicion++;
        }
    }

    /**
     * Ejecuta un turno. Devuelve false si no había procesos.
     * El tiempo usado es el mínimo entre el quantum y la ráfaga que queda.
     */
    public boolean ejecutarTurno() {
        if (estaVacia()) {
            System.out.println("  No hay procesos. El turno no se ejecuta.");
            return false;
        }
        Proceso actual = listos.obtener(0);
        int uso = Math.min(quantum, actual.getRafagaRestante());
        actual.consumir(uso);
        System.out.println("  CPU -> " + actual.getNombre() + " usa " + uso
                + " | restante " + actual.getRafagaRestante());
        if (actual.termino()) {
            listos.eliminarAlInicio();
            System.out.println("  " + actual.getNombre() + " terminó y sale del anillo.");
        } else {
            listos.rotar();
            System.out.println("  " + actual.getNombre() + " vuelve al final del anillo.");
        }
        return true;
    }

    /** Reparte turnos hasta que la cola circular quede vacía. */
    public void ejecutarHastaVaciar() {
        int turno = 1;
        while (!estaVacia()) {
            System.out.println("  turno " + turno);
            ejecutarTurno();
            mostrarCola();
            turno++;
        }
    }
}
