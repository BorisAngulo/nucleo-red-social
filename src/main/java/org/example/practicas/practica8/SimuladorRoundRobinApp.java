package org.example.practicas.practica8;

/**
 * Demostración round-robin: tres procesos comparten la CPU en un anillo, con quantum 3.
 */
public class SimuladorRoundRobinApp {

    public static void main(String[] args) {
        PlanificadorRoundRobin planificador = new PlanificadorRoundRobin(3);

        seccion("Estado inicial");
        System.out.println("  vacía: " + planificador.estaVacia()
                + " | procesos: " + planificador.getTamano()
                + " | quantum: " + planificador.getQuantum());
        planificador.mostrarCola();
        planificador.ejecutarTurno();

        seccion("Llegan tres procesos");
        planificador.agregar(new Proceso(1, "navegador", 8));
        planificador.agregar(new Proceso(2, "compilador", 4));
        planificador.agregar(new Proceso(3, "editor", 5));
        planificador.mostrarCola();

        seccion("Reparto de la CPU hasta vaciar el anillo");
        planificador.ejecutarHastaVaciar();

        seccion("Cola vacía después del último proceso");
        System.out.println("  vacía: " + planificador.estaVacia() + " | procesos: " + planificador.getTamano());
        planificador.mostrarCola();
        planificador.ejecutarTurno();
    }

    private static void seccion(String titulo) {
        System.out.println();
        System.out.println(titulo);
    }
}
