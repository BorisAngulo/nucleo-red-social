package org.example.estructuradedatos;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

/**
 * Pila (LIFO): el último dato en entrar es el primero en salir.
 * La cima es el único extremo por el que se apila y se desapila.
 */
public class Pila<T> implements Iterable<T> {

    private Nodo<T> cima;
    private int cantidad;

    public void apilar(T dato) {
        validar(dato);
        Nodo<T> nuevo = new Nodo<>(dato);
        nuevo.siguiente = cima;
        cima = nuevo;
        cantidad++;
    }

    public T desapilar() {
        if (cima == null) {
            throw new NoSuchElementException("La pila está vacía.");
        }
        T dato = cima.dato;
        cima = cima.siguiente;
        cantidad--;
        return dato;
    }

    public T cima() {
        if (cima == null) {
            throw new NoSuchElementException("La pila está vacía.");
        }
        return cima.dato;
    }

    public boolean estaVacia() {
        return cima == null;
    }

    public int tamano() {
        return cantidad;
    }

    public void vaciar() {
        cima = null;
        cantidad = 0;
    }

    public void paraCada(Consumer<T> accion) {
        exigirAccion(accion);
        for (T dato : this) {
            accion.accept(dato);
        }
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

    private static void exigirAccion(Consumer<?> accion) {
        if (accion == null) {
            throw new IllegalArgumentException("La acción no puede ser nula.");
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
