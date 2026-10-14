package org.example.practicas.practica3;

import org.example.estructuradedatos.Pila;
import org.example.estructuradedatos.PilaEnlazada;

/**
 * Historial de navegación con cupo fijo.
 * Las páginas viven en la {@link Pila} enlazada de estructuras de datos:
 * la cima es la página actual y cada alta o baja es O(1).
 * La capacidad no está dentro de esa pila; este historial la vigila
 * antes de apilar para impedir el desbordamiento.
 */
public class PilaHistorial {

    private final Pila<String> historial;
    private final int capacidad;

    public PilaHistorial(int capacidad) {
        if (capacidad <= 0) {
            throw new IllegalArgumentException("La capacidad del historial debe ser mayor que cero.");
        }
        this.capacidad = capacidad;
        this.historial = new PilaEnlazada<>();
    }

    public boolean estaVacia() {
        return historial.estaVacia();
    }

    public boolean estaLlena() {
        return historial.tamano() == capacidad;
    }

    /**
     * Recorre desde la cima hacia el fondo, que es el orden LIFO.
     * El iterador de {@link Pila} ya entrega primero la página actual.
     */
    public void mostrarHistorial() {
        if (estaVacia()) {
            System.out.println("[ Historial vacío: sin páginas registradas ]");
            return;
        }
        int indice = historial.tamano() - 1;
        for (String url : historial) {
            String marca = indice == historial.tamano() - 1 ? " <-- [PÁGINA ACTUAL]" : "";
            System.out.println("  [" + indice + "] " + url + marca);
            indice--;
        }
    }

    /** Apila una URL en O(1). Con el historial lleno informa Stack Overflow y no inserta. */
    public boolean push(String url) {
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("La URL no puede estar vacía.");
        }
        if (estaLlena()) {
            System.out.println("  Stack Overflow: el historial está lleno (" + capacidad + " páginas).");
            return false;
        }
        historial.apilar(url);
        return true;
    }

    /** Lee la página actual sin desapilar ni mover la cima. */
    public String peek() {
        if (estaVacia()) {
            System.out.println("  No hay páginas en el historial.");
            return null;
        }
        return historial.cima();
    }

    /**
     * Botón Atrás. {@code desapilar} suelta el nodo de la cima y el recolector
     * puede recuperar esa URL. Con la pila vacía informa Stack Underflow y devuelve null.
     */
    public String pop() {
        if (estaVacia()) {
            System.out.println("  Stack Underflow: no hay páginas para retroceder.");
            return null;
        }
        return historial.desapilar();
    }
}
