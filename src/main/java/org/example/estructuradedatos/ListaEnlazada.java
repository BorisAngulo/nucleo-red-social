package org.example.estructuradedatos;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

/**
 * Lista simplemente enlazada. Cada nodo conoce solo al siguiente.
 * El primer nodo es la cabeza y el último no apunta a nadie.
 */
public class ListaEnlazada<T> implements Iterable<T> {

    private Nodo<T> cabeza;
    private Nodo<T> ultimo;
    private int cantidad;

    public void insertarAlInicio(T dato) {
        validar(dato);
        Nodo<T> nuevo = new Nodo<>(dato);
        nuevo.siguiente = cabeza;
        cabeza = nuevo;
        if (ultimo == null) {
            ultimo = nuevo;
        }
        cantidad++;
    }

    public void insertarAlFinal(T dato) {
        validar(dato);
        Nodo<T> nuevo = new Nodo<>(dato);
        if (cabeza == null) {
            cabeza = nuevo;
        } else {
            ultimo.siguiente = nuevo;
        }
        ultimo = nuevo;
        cantidad++;
    }

    public void insertarEn(int indice, T dato) {
        validarIndiceInsercion(indice);
        if (indice == 0) {
            insertarAlInicio(dato);
            return;
        }
        if (indice == cantidad) {
            insertarAlFinal(dato);
            return;
        }
        validar(dato);
        Nodo<T> previo = nodoEn(indice - 1);
        Nodo<T> nuevo = new Nodo<>(dato);
        nuevo.siguiente = previo.siguiente;
        previo.siguiente = nuevo;
        cantidad++;
    }

    public T eliminarAlInicio() {
        if (cabeza == null) {
            throw new NoSuchElementException("La lista está vacía.");
        }
        T dato = cabeza.dato;
        cabeza = cabeza.siguiente;
        if (cabeza == null) {
            ultimo = null;
        }
        cantidad--;
        return dato;
    }

    public T eliminarAlFinal() {
        if (cabeza == null) {
            throw new NoSuchElementException("La lista está vacía.");
        }
        if (cabeza == ultimo) {
            return eliminarAlInicio();
        }
        Nodo<T> previo = cabeza;
        while (previo.siguiente != ultimo) {
            previo = previo.siguiente;
        }
        T dato = ultimo.dato;
        previo.siguiente = null;
        ultimo = previo;
        cantidad--;
        return dato;
    }

    public T eliminarEn(int indice) {
        validarIndice(indice);
        if (indice == 0) {
            return eliminarAlInicio();
        }
        if (indice == cantidad - 1) {
            return eliminarAlFinal();
        }
        Nodo<T> previo = nodoEn(indice - 1);
        Nodo<T> actual = previo.siguiente;
        previo.siguiente = actual.siguiente;
        cantidad--;
        return actual.dato;
    }

    /** Quita la primera aparición del dato. Devuelve false si no está. */
    public boolean eliminar(T dato) {
        if (dato == null || cabeza == null) {
            return false;
        }
        if (dato.equals(cabeza.dato)) {
            eliminarAlInicio();
            return true;
        }
        Nodo<T> previo = cabeza;
        while (previo.siguiente != null && !dato.equals(previo.siguiente.dato)) {
            previo = previo.siguiente;
        }
        if (previo.siguiente == null) {
            return false;
        }
        if (previo.siguiente == ultimo) {
            ultimo = previo;
        }
        previo.siguiente = previo.siguiente.siguiente;
        cantidad--;
        return true;
    }

    /** Índice de la primera aparición, o -1 si no está. */
    public int buscar(T dato) {
        if (dato == null) {
            return -1;
        }
        Nodo<T> actual = cabeza;
        int indice = 0;
        while (actual != null) {
            if (dato.equals(actual.dato)) {
                return indice;
            }
            actual = actual.siguiente;
            indice++;
        }
        return -1;
    }

    public boolean contiene(T dato) {
        return buscar(dato) >= 0;
    }

    public T obtener(int indice) {
        return nodoEn(indice).dato;
    }

    public boolean estaVacia() {
        return cabeza == null;
    }

    public int tamano() {
        return cantidad;
    }

    public void vaciar() {
        cabeza = null;
        ultimo = null;
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
            private Nodo<T> actual = cabeza;

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

    private Nodo<T> nodoEn(int indice) {
        validarIndice(indice);
        Nodo<T> actual = cabeza;
        for (int i = 0; i < indice; i++) {
            actual = actual.siguiente;
        }
        return actual;
    }

    private void validarIndice(int indice) {
        if (indice < 0 || indice >= cantidad) {
            throw new IndexOutOfBoundsException("Índice " + indice + " fuera de rango para tamaño " + cantidad);
        }
    }

    private void validarIndiceInsercion(int indice) {
        if (indice < 0 || indice > cantidad) {
            throw new IndexOutOfBoundsException("Índice " + indice + " fuera de rango para tamaño " + cantidad);
        }
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
