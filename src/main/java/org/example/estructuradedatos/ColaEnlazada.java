package org.example.estructuradedatos;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Cola sobre nodos enlazados. Crece en el Heap con cada alta y no tiene cupo fijo.
 * Encolar y desencolar son O(1): el frente y el fin se guardan aparte.
 */
public class ColaEnlazada<T> implements Cola<T> {

    private Nodo<T> frente;
    private Nodo<T> fin;
    private int cantidad;

    @Override
    public void encolar(T dato) {
        validar(dato);
        Nodo<T> nuevo = new Nodo<>(dato);
        if (fin == null) {
            frente = nuevo;
        } else {
            fin.siguiente = nuevo;
        }
        fin = nuevo;
        cantidad++;
    }

    @Override
    public T desencolar() {
        if (frente == null) {
            throw new NoSuchElementException("La cola está vacía.");
        }
        T dato = frente.dato;
        frente = frente.siguiente;
        if (frente == null) {
            fin = null;
        }
        cantidad--;
        return dato;
    }

    @Override
    public T frente() {
        if (frente == null) {
            throw new NoSuchElementException("La cola está vacía.");
        }
        return frente.dato;
    }

    @Override
    public T fin() {
        if (fin == null) {
            throw new NoSuchElementException("La cola está vacía.");
        }
        return fin.dato;
    }

    @Override
    public boolean estaVacia() {
        return frente == null;
    }

    @Override
    public int tamano() {
        return cantidad;
    }

    @Override
    public void vaciar() {
        frente = null;
        fin = null;
        cantidad = 0;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            private Nodo<T> actual = frente;

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
