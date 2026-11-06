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

    /** Una marca sobre el primer nodo, o vacía si la lista no tiene elementos. */
    public Posicion posicionInicial() {
        if (cabeza == null) {
            return new Posicion(null, -1);
        }
        return new Posicion(cabeza, 0);
    }

    /**
     * Se queda en un nodo. Adelante y atrás siguen {@code siguiente} y {@code anterior}
     * de ese nodo, sin recorrer la lista desde la cabeza.
     */
    public final class Posicion {
        private Nodo<T> nodo;
        private int indice;

        private Posicion(Nodo<T> nodo, int indice) {
            this.nodo = nodo;
            this.indice = indice;
        }

        public T ver() {
            if (nodo == null) {
                return null;
            }
            return nodo.dato;
        }

        /** Posición de 1 a tamaño. Cero si no hay nodo. */
        public int getPosicion() {
            if (nodo == null) {
                return 0;
            }
            return indice + 1;
        }

        public void fijarAlInicio() {
            nodo = cabeza;
            indice = cabeza == null ? -1 : 0;
        }

        /** Pasa al nodo siguiente. En el último no se mueve y devuelve null. */
        public T avanzar() {
            if (nodo == null || nodo.siguiente == null) {
                return null;
            }
            nodo = nodo.siguiente;
            indice++;
            return nodo.dato;
        }

        /** Pasa al nodo anterior. En el primero no se mueve y devuelve null. */
        public T retroceder() {
            if (nodo == null || nodo.anterior == null) {
                return null;
            }
            nodo = nodo.anterior;
            indice--;
            return nodo.dato;
        }

        /** Engancha un nodo justo después del actual, en un paso. */
        public void insertarDespues(T dato) {
            validar(dato);
            if (nodo == null) {
                insertarAlFinal(dato);
                fijarAlInicio();
                return;
            }
            Nodo<T> nuevo = new Nodo<>(dato);
            Nodo<T> despues = nodo.siguiente;
            nuevo.anterior = nodo;
            nuevo.siguiente = despues;
            nodo.siguiente = nuevo;
            if (despues == null) {
                ultimo = nuevo;
            } else {
                despues.anterior = nuevo;
            }
            cantidad++;
        }

        /**
         * Desengancha este nodo usando su anterior y su siguiente.
         * La marca pasa al siguiente, o al anterior si este era el último.
         */
        public T eliminar() {
            if (nodo == null) {
                return null;
            }
            T dato = nodo.dato;
            Nodo<T> previo = nodo.anterior;
            Nodo<T> posterior = nodo.siguiente;
            if (previo == null) {
                cabeza = posterior;
            } else {
                previo.siguiente = posterior;
            }
            if (posterior == null) {
                ultimo = previo;
            } else {
                posterior.anterior = previo;
            }
            cantidad--;
            if (posterior != null) {
                nodo = posterior;
            } else if (previo != null) {
                nodo = previo;
                indice--;
            } else {
                nodo = null;
                indice = -1;
            }
            return dato;
        }
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
