package org.example.estructuradedatos;

import java.util.Iterator;
import java.util.NoSuchElementException;
/**
 * Lista circular simplemente enlazada. El último nodo vuelve a apuntar
 * al primero. La referencia guardada es el último: su siguiente es la cabeza.
 */
public class ListaCircular<T> implements Iterable<T> {

    private Nodo<T> ultimo;
    private int cantidad;

    public void insertarAlInicio(T dato) {
        validar(dato);
        Nodo<T> nuevo = new Nodo<>(dato);
        if (ultimo == null) {
            nuevo.siguiente = nuevo;
            ultimo = nuevo;
        } else {
            nuevo.siguiente = ultimo.siguiente;
            ultimo.siguiente = nuevo;
        }
        cantidad++;
    }

    public void insertarAlFinal(T dato) {
        insertarAlInicio(dato);
        ultimo = ultimo.siguiente;
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
        Nodo<T> previo = nodoPrevio(indice);
        Nodo<T> nuevo = new Nodo<>(dato);
        nuevo.siguiente = previo.siguiente;
        previo.siguiente = nuevo;
        cantidad++;
    }

    public T eliminarAlInicio() {
        if (ultimo == null) {
            throw new NoSuchElementException("La lista está vacía.");
        }
        Nodo<T> primero = ultimo.siguiente;
        T dato = primero.dato;
        if (primero == ultimo) {
            ultimo = null;
        } else {
            ultimo.siguiente = primero.siguiente;
        }
        cantidad--;
        return dato;
    }

    public T eliminarAlFinal() {
        if (ultimo == null) {
            throw new NoSuchElementException("La lista está vacía.");
        }
        if (ultimo.siguiente == ultimo) {
            return eliminarAlInicio();
        }
        Nodo<T> previo = ultimo.siguiente;
        while (previo.siguiente != ultimo) {
            previo = previo.siguiente;
        }
        T dato = ultimo.dato;
        previo.siguiente = ultimo.siguiente;
        ultimo = previo;
        cantidad--;
        return dato;
    }

    public T eliminarEn(int indice) {
        validarIndice(indice);
        if (indice == 0) {
            return eliminarAlInicio();
        }
        Nodo<T> previo = nodoPrevio(indice);
        Nodo<T> actual = previo.siguiente;
        previo.siguiente = actual.siguiente;
        if (actual == ultimo) {
            ultimo = previo;
        }
        cantidad--;
        return actual.dato;
    }

    /** Quita la primera aparición del dato. Devuelve false si no está. */
    public boolean eliminar(T dato) {
        if (dato == null || ultimo == null) {
            return false;
        }
        Nodo<T> previo = ultimo;
        Nodo<T> actual = ultimo.siguiente;
        for (int i = 0; i < cantidad; i++) {
            if (dato.equals(actual.dato)) {
                if (cantidad == 1) {
                    ultimo = null;
                } else {
                    previo.siguiente = actual.siguiente;
                    if (actual == ultimo) {
                        ultimo = previo;
                    }
                }
                cantidad--;
                return true;
            }
            previo = actual;
            actual = actual.siguiente;
        }
        return false;
    }

    /** Avanza la cabeza una posición. El antiguo primero pasa al final. */
    public void rotar() {
        if (ultimo != null) {
            ultimo = ultimo.siguiente;
        }
    }

    public int buscar(T dato) {
        if (dato == null || ultimo == null) {
            return -1;
        }
        Nodo<T> actual = ultimo.siguiente;
        for (int indice = 0; indice < cantidad; indice++) {
            if (dato.equals(actual.dato)) {
                return indice;
            }
            actual = actual.siguiente;
        }
        return -1;
    }

    public boolean contiene(T dato) {
        return buscar(dato) >= 0;
    }

    public T obtener(int indice) {
        validarIndice(indice);
        Nodo<T> actual = ultimo.siguiente;
        for (int i = 0; i < indice; i++) {
            actual = actual.siguiente;
        }
        return actual.dato;
    }

    public boolean estaVacia() {
        return ultimo == null;
    }

    public int tamano() {
        return cantidad;
    }

    public void vaciar() {
        ultimo = null;
        cantidad = 0;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            private Nodo<T> actual = ultimo == null ? null : ultimo.siguiente;
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
                T dato = actual.dato;
                actual = actual.siguiente;
                vistos++;
                return dato;
            }
        };
    }

    @Override
    public String toString() {
        return Texto.de(this);
    }

    /** Nodo anterior a la posición indicada. El último es el previo de la cabeza. */
    private Nodo<T> nodoPrevio(int indice) {
        Nodo<T> previo = ultimo;
        for (int i = 0; i < indice; i++) {
            previo = previo.siguiente;
        }
        return previo;
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
