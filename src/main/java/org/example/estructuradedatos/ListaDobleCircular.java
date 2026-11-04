package org.example.estructuradedatos;

import java.util.Iterator;
import java.util.NoSuchElementException;
/**
 * Lista circular doblemente enlazada. El primero y el último se apuntan
 * entre sí, y cada nodo conoce a sus dos vecinos.
 */
public class ListaDobleCircular<T> implements Iterable<T> {

    private Nodo<T> cabeza;
    private int cantidad;

    public void insertarAlInicio(T dato) {
        validar(dato);
        Nodo<T> nuevo = new Nodo<>(dato);
        if (cabeza == null) {
            nuevo.siguiente = nuevo;
            nuevo.anterior = nuevo;
            cabeza = nuevo;
        } else {
            Nodo<T> cola = cabeza.anterior;
            nuevo.siguiente = cabeza;
            nuevo.anterior = cola;
            cabeza.anterior = nuevo;
            cola.siguiente = nuevo;
            cabeza = nuevo;
        }
        cantidad++;
    }

    public void insertarAlFinal(T dato) {
        insertarAlInicio(dato);
        cabeza = cabeza.siguiente;
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
        desconectar(cabeza);
        return dato;
    }

    public T eliminarAlFinal() {
        if (cabeza == null) {
            throw new NoSuchElementException("La lista está vacía.");
        }
        T dato = cabeza.anterior.dato;
        desconectar(cabeza.anterior);
        return dato;
    }

    public T eliminarEn(int indice) {
        validarIndice(indice);
        Nodo<T> actual = nodoEn(indice);
        T dato = actual.dato;
        desconectar(actual);
        return dato;
    }

    /** Quita la primera aparición del dato. Devuelve false si no está. */
    public boolean eliminar(T dato) {
        if (dato == null || cabeza == null) {
            return false;
        }
        Nodo<T> actual = cabeza;
        for (int i = 0; i < cantidad; i++) {
            if (dato.equals(actual.dato)) {
                desconectar(actual);
                return true;
            }
            actual = actual.siguiente;
        }
        return false;
    }

    /** La cabeza avanza. El antiguo primero queda al final. */
    public void rotar() {
        if (cabeza != null) {
            cabeza = cabeza.siguiente;
        }
    }

    /** La cabeza retrocede. El antiguo último pasa a ser el primero. */
    public void rotarInverso() {
        if (cabeza != null) {
            cabeza = cabeza.anterior;
        }
    }

    public int buscar(T dato) {
        if (dato == null || cabeza == null) {
            return -1;
        }
        Nodo<T> actual = cabeza;
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
        cantidad = 0;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            private Nodo<T> actual = cabeza;
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

    /** Marca el primer nodo. En el anillo, avanzar y retroceder no tienen tope. */
    public Posicion marcarInicio() {
        if (cabeza == null) {
            throw new NoSuchElementException("La lista está vacía.");
        }
        return new Posicion(cabeza);
    }

    /**
     * Se queda en un nodo del anillo.
     * Adelante sigue {@code siguiente} y atrás sigue {@code anterior}.
     */
    public final class Posicion {
        private Nodo<T> nodo;

        private Posicion(Nodo<T> nodo) {
            this.nodo = nodo;
        }

        public T ver() {
            return nodo.dato;
        }

        public T avanzar() {
            nodo = nodo.siguiente;
            return nodo.dato;
        }

        public T retroceder() {
            nodo = nodo.anterior;
            return nodo.dato;
        }
    }

    private void desconectar(Nodo<T> actual) {
        if (cantidad == 1) {
            cabeza = null;
        } else {
            Nodo<T> siguiente = actual.siguiente;
            Nodo<T> anterior = actual.anterior;
            anterior.siguiente = siguiente;
            siguiente.anterior = anterior;
            if (actual == cabeza) {
                cabeza = siguiente;
            }
        }
        actual.siguiente = null;
        actual.anterior = null;
        cantidad--;
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
