package org.example.estructuradedatos;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

/**
 * Cola (FIFO): el primero en entrar es el primero en salir.
 * Se encola por el final y se desencola por el frente.
 */
public class Cola<T> implements Iterable<T> {

    private Nodo<T> frente;
    private Nodo<T> fin;
    private int cantidad;

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

    public T frente() {
        if (frente == null) {
            throw new NoSuchElementException("La cola está vacía.");
        }
        return frente.dato;
    }

    public T fin() {
        if (fin == null) {
            throw new NoSuchElementException("La cola está vacía.");
        }
        return fin.dato;
    }

    public boolean estaVacia() {
        return frente == null;
    }

    public int tamano() {
        return cantidad;
    }

    public void vaciar() {
        frente = null;
        fin = null;
        cantidad = 0;
    }

    public void paraCada(Consumer<T> accion) {
        if (accion == null) {
            throw new IllegalArgumentException("La acción no puede ser nula.");
        }
        for (T dato : this) {
            accion.accept(dato);
        }
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
