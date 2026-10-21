package org.example.estructuradedatos;

import java.util.Iterator;
import java.util.NoSuchElementException;
/**
 * Lista doblemente enlazada. Cada nodo conoce al anterior y al siguiente,
 * así se recorre y se borra por los dos extremos sin volver a empezar.
 */
public class ListaDoblementeEnlazada<T> implements Iterable<T> {

    private Nodo<T> cabeza;
    private Nodo<T> ultimo;
    private int cantidad;

    public void insertarAlInicio(T dato) {
        validar(dato);
        Nodo<T> nuevo = new Nodo<>(dato);
        nuevo.siguiente = cabeza;
        if (cabeza != null) {
            cabeza.anterior = nuevo;
        } else {
            ultimo = nuevo;
        }
        cabeza = nuevo;
        cantidad++;
    }

    public void insertarAlFinal(T dato) {
        validar(dato);
        Nodo<T> nuevo = new Nodo<>(dato);
        nuevo.anterior = ultimo;
        if (ultimo != null) {
            ultimo.siguiente = nuevo;
        } else {
            cabeza = nuevo;
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
        Nodo<T> actual = nodoEn(indice);
        Nodo<T> previo = actual.anterior;
        Nodo<T> nuevo = new Nodo<>(dato);
        nuevo.anterior = previo;
        nuevo.siguiente = actual;
        previo.siguiente = nuevo;
        actual.anterior = nuevo;
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
        } else {
            cabeza.anterior = null;
        }
        cantidad--;
        return dato;
    }

    public T eliminarAlFinal() {
        if (ultimo == null) {
            throw new NoSuchElementException("La lista está vacía.");
        }
        T dato = ultimo.dato;
        ultimo = ultimo.anterior;
        if (ultimo == null) {
            cabeza = null;
        } else {
            ultimo.siguiente = null;
        }
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
        Nodo<T> actual = nodoEn(indice);
        actual.anterior.siguiente = actual.siguiente;
        actual.siguiente.anterior = actual.anterior;
        cantidad--;
        return actual.dato;
    }

    /** Quita la primera aparición del dato. Devuelve false si no está. */
    public boolean eliminar(T dato) {
        if (dato == null || cabeza == null) {
            return false;
        }
        Nodo<T> actual = cabeza;
        while (actual != null && !dato.equals(actual.dato)) {
            actual = actual.siguiente;
        }
        if (actual == null) {
            return false;
        }
        if (actual == cabeza) {
            cabeza = actual.siguiente;
        } else {
            actual.anterior.siguiente = actual.siguiente;
        }
        if (actual == ultimo) {
            ultimo = actual.anterior;
        } else {
            actual.siguiente.anterior = actual.anterior;
        }
        cantidad--;
        return true;
    }

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

    public T obtenerPrimero() {
        if (cabeza == null) {
            throw new NoSuchElementException("La lista está vacía.");
        }
        return cabeza.dato;
    }

    public T obtenerUltimo() {
        if (ultimo == null) {
            throw new NoSuchElementException("La lista está vacía.");
        }
        return ultimo.dato;
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
        private Nodo<T> anterior;

        private Nodo(T dato) {
            this.dato = dato;
        }
    }
}
