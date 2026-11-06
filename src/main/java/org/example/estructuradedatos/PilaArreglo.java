package org.example.estructuradedatos;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Pila sobre un arreglo de tamaño fijo. El tope empieza en -1 (vacía)
 * y la última celda ocupada es la cima. Apilar y desapilar son O(1).
 */
public class PilaArreglo<T> implements Pila<T> {

    private final T[] elementos;
    private int tope;

    @SuppressWarnings("unchecked")
    public PilaArreglo(int capacidad) {
        if (capacidad <= 0) {
            throw new IllegalArgumentException("La capacidad de la pila debe ser mayor que cero.");
        }
        this.elementos = (T[]) new Object[capacidad];
        this.tope = -1;
    }

    @Override
    public void apilar(T dato) {
        if (dato == null) {
            throw new IllegalArgumentException("El dato no puede ser nulo.");
        }
        if (estaLlena()) {
            throw new IllegalStateException("Stack Overflow: la pila está llena.");
        }
        tope++;
        elementos[tope] = dato;
    }

    @Override
    public T desapilar() {
        if (estaVacia()) {
            throw new NoSuchElementException("La pila está vacía.");
        }
        T dato = elementos[tope];
        elementos[tope] = null;
        tope--;
        return dato;
    }

    @Override
    public T cima() {
        if (estaVacia()) {
            throw new NoSuchElementException("La pila está vacía.");
        }
        return elementos[tope];
    }

    @Override
    public boolean estaVacia() {
        return tope == -1;
    }

    public boolean estaLlena() {
        return tope == elementos.length - 1;
    }

    @Override
    public int tamano() {
        return tope + 1;
    }

    @Override
    public void vaciar() {
        for (int i = 0; i <= tope; i++) {
            elementos[i] = null;
        }
        tope = -1;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            private int indice = tope;

            @Override
            public boolean hasNext() {
                return indice >= 0;
            }

            @Override
            public T next() {
                if (indice < 0) {
                    throw new NoSuchElementException();
                }
                T dato = elementos[indice];
                indice--;
                return dato;
            }
        };
    }

    @Override
    public String toString() {
        return Texto.de(this);
    }
}
