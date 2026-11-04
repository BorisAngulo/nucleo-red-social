package org.example.estructuradedatos;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Pila sobre nodos enlazados. Crece en el Heap con cada alta y no tiene cupo fijo.
 * Apilar y desapilar son O(1).
 */
public class PilaEnlazada<T> implements Pila<T> {

    private Nodo<T> cima;
    private int cantidad;

    @Override
    public void apilar(T dato) {
        validar(dato);
        Nodo<T> nuevo = new Nodo<>(dato);
        nuevo.siguiente = cima;
        cima = nuevo;
        cantidad++;
    }

    @Override
    public T desapilar() {
        if (cima == null) {
            throw new NoSuchElementException("La pila está vacía.");
        }
        T dato = cima.dato;
        cima = cima.siguiente;
        cantidad--;
        return dato;
    }

    @Override
    public T cima() {
        if (cima == null) {
            throw new NoSuchElementException("La pila está vacía.");
        }
        return cima.dato;
    }

    @Override
    public boolean estaVacia() {
        return cima == null;
    }

    @Override
    public int tamano() {
        return cantidad;
    }

    @Override
    public void vaciar() {
        cima = null;
        cantidad = 0;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            private Nodo<T> actual = cima;

            @Override
            public boolean hasNext() {
                return actual != null;
            }

            @Override
            public T next() {
                if (actual == null) {
                    throw new NoSuchElementException();
                }
                T dato = actual.dato;
                actual = actual.siguiente;
                return dato;
            }
        };
    }

    @Override
    public String toString() {
        return Texto.de(this);
    }

    private static <T> void validar(T dato) {
        if (dato == null) {
            throw new IllegalArgumentException("El dato no puede ser nulo.");
        }
    }

    private static final class Nodo<T> {
        private final T dato;
        private Nodo<T> siguiente;

        private Nodo(T dato) {
            this.dato = dato;
        }
    }
}
