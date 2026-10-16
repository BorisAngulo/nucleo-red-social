package org.example.estructuradedatos;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Cola circular sobre un arreglo de tamaño fijo.
 * El frente avanza al desencolar y el fin da la vuelta al llegar al último casillero.
 * Encolar y desencolar son O(1).
 */
public class ColaArreglo<T> implements Cola<T> {

    private final T[] elementos;
    private int frente;
    private int cantidad;

    @SuppressWarnings("unchecked")
    public ColaArreglo(int capacidad) {
        if (capacidad <= 0) {
            throw new IllegalArgumentException("La capacidad de la cola debe ser mayor que cero.");
        }
        this.elementos = (T[]) new Object[capacidad];
        this.frente = 0;
        this.cantidad = 0;
    }

    @Override
    public void encolar(T dato) {
        if (dato == null) {
            throw new IllegalArgumentException("El dato no puede ser nulo.");
        }
        if (estaLlena()) {
            throw new IllegalStateException("La cola está llena.");
        }
        int indice = (frente + cantidad) % elementos.length;
        elementos[indice] = dato;
        cantidad++;
    }

    @Override
    public T desencolar() {
        if (estaVacia()) {
            throw new NoSuchElementException("La cola está vacía.");
        }
        T dato = elementos[frente];
        elementos[frente] = null;
        frente = (frente + 1) % elementos.length;
        cantidad--;
        if (cantidad == 0) {
            frente = 0;
        }
        return dato;
    }

    @Override
    public T frente() {
        if (estaVacia()) {
            throw new NoSuchElementException("La cola está vacía.");
        }
        return elementos[frente];
    }

    @Override
    public T fin() {
        if (estaVacia()) {
            throw new NoSuchElementException("La cola está vacía.");
        }
        int indice = (frente + cantidad - 1) % elementos.length;
        return elementos[indice];
    }

    @Override
    public boolean estaVacia() {
        return cantidad == 0;
    }

    public boolean estaLlena() {
        return cantidad == elementos.length;
    }

    @Override
    public int tamano() {
        return cantidad;
    }

    @Override
    public void vaciar() {
        for (int i = 0; i < cantidad; i++) {
            elementos[(frente + i) % elementos.length] = null;
        }
        frente = 0;
        cantidad = 0;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            private int vistos;

            @Override
            public boolean hasNext() {
                return vistos < cantidad;
            }

            @Override
            public T next() {
                if (vistos >= cantidad) {
                    throw new NoSuchElementException();
                }
                T dato = elementos[(frente + vistos) % elementos.length];
                vistos++;
                return dato;
            }
        };
    }

    @Override
    public String toString() {
        return Texto.de(this);
    }
}
