package org.example.practicas.practica5;

import org.example.estructuradedatos.ColaEnlazada;

/**
 * Spooler lineal. Los documentos viven en una {@link ColaEnlazada}:
 * el primero en entrar es el primero en salir.
 * {@code frente} y {@code fin} imitan un buffer que solo crece hacia adelante.
 * {@code fin} no retrocede, así que un hueco liberado al inicio no se reutiliza.
 */
public class ColaImpresion {

    private final ColaEnlazada<String> documentos;
    private final int capacidad;
    private int frente;
    private int fin;

    public ColaImpresion(int capacidad) {
        if (capacidad <= 0) {
            throw new IllegalArgumentException("La capacidad de la cola debe ser mayor que cero.");
        }
        this.capacidad = capacidad;
        this.documentos = new ColaEnlazada<>();
        this.frente = 0;
        this.fin = -1;
    }

    /** Vacía cuando el frente ya pasó al fin. Al nacer, 0 > -1. */
    public boolean estaVacia() {
        return frente > fin;
    }

    /** Llena cuando fin ocupa el último índice físico del buffer. */
    public boolean estaLlena() {
        return fin == capacidad - 1;
    }

    public void mostrarCola() {
        if (estaVacia()) {
            System.out.println("[ Cola de impresión vacía: no hay documentos en espera ]");
            return;
        }
        System.out.println("  frente=" + frente + "  fin=" + fin + "  trabajos=" + size());
        int indice = frente;
        for (String documento : documentos) {
            String marca = indice == frente ? " <-- [PRÓXIMO]" : "";
            System.out.println("  [" + indice + "] " + documento + marca);
            indice++;
        }
    }

    /** Encola al final. O(1). Con el buffer lleno informa Overflow y no inserta. */
    public boolean insert(String nombreDocumento) {
        if (nombreDocumento == null || nombreDocumento.isBlank()) {
            throw new IllegalArgumentException("El documento no puede estar vacío.");
        }
        if (estaLlena()) {
            System.out.println("  Overflow: fin llegó al límite del buffer (" + capacidad + " casilleros).");
            return false;
        }
        fin++;
        documentos.encolar(nombreDocumento);
        return true;
    }

    /** Consulta el próximo documento sin mover frente ni fin. */
    public String peek() {
        if (estaVacia()) {
            return null;
        }
        return documentos.frente();
    }

    /** Trabajos activos: fin - frente + 1. Cero si la cola está vacía. */
    public int size() {
        if (estaVacia()) {
            return 0;
        }
        return fin - frente + 1;
    }

    /**
     * Atiende el documento del frente. Desencolar suelta ese nodo
     * para el recolector. Con la cola vacía informa Underflow y devuelve null.
     */
    public String remove() {
        if (estaVacia()) {
            System.out.println("  Underflow: no hay documentos para imprimir.");
            return null;
        }
        String atendido = documentos.desencolar();
        frente++;
        System.out.println("  Atendido: " + atendido);
        return atendido;
    }
}
