package org.example.practicas.juegoRPG;

import org.example.estructuradedatos.ListaDoblementeEnlazada;

/**
 * Inventario de un jugador. Los items viven en una lista doblemente enlazada:
 * al quitar uno del medio, su anterior y su siguiente se vuelven a enganchar.
 */
public class Inventario {

    private final ListaDoblementeEnlazada<Item> items;

    public Inventario() {
        this.items = new ListaDoblementeEnlazada<>();
    }

    public boolean estaVacio() {
        return items.estaVacia();
    }

    public int tamano() {
        return items.tamano();
    }

    public void agregar(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("El item no puede ser nulo.");
        }
        items.insertarAlFinal(item);
    }

    /** Posición de 1 a tamaño. */
    public Item obtener(int posicion) {
        if (!posicionValida(posicion)) {
            return null;
        }
        return items.obtener(posicion - 1);
    }

    /** Saca el item de esa posición. El nodo se desengancha por sus dos enlaces. */
    public Item quitar(int posicion) {
        if (!posicionValida(posicion)) {
            return null;
        }
        return items.eliminarEn(posicion - 1);
    }

    public void mostrar() {
        if (items.estaVacia()) {
            System.out.println("  [ Inventario vacío ]");
            return;
        }
        int posicion = 1;
        for (Item item : items) {
            System.out.println("  " + posicion + ". " + item);
            posicion++;
        }
    }

    private boolean posicionValida(int posicion) {
        return posicion >= 1 && posicion <= items.tamano();
    }
}
