package org.example.estructuradedatos;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Árbol binario de búsqueda. Los datos menores que un nodo quedan a su
 * izquierda y los mayores a su derecha.
 * Sin comparador, el tipo debe implementar Comparable (Integer, String, Double).
 * Con comparador, sirve para cualquier tipo.
 */
public class ArbolBinario<T> {

    private final Comparator<? super T> comparador;
    private final boolean ordenNatural;
    private Nodo<T> raiz;
    private int cantidad;

    public ArbolBinario() {
        this(ordenNatural(), true);
    }

    public ArbolBinario(Comparator<? super T> comparador) {
        this(comparador, false);
    }

    private ArbolBinario(Comparator<? super T> comparador, boolean ordenNatural) {
        if (comparador == null) {
            throw new IllegalArgumentException("El comparador no puede ser nulo.");
        }
        this.comparador = comparador;
        this.ordenNatural = ordenNatural;
    }

    /** Inserta el dato respetando el orden. Devuelve false si ya estaba. */
    public boolean insertar(T dato) {
        validar(dato);
        if (ordenNatural && !(dato instanceof Comparable<?>)) {
            throw new IllegalArgumentException("Sin comparador, el dato debe implementar Comparable.");
        }
        if (raiz == null) {
            raiz = new Nodo<>(dato);
            cantidad = 1;
            return true;
        }
        if (!insertar(raiz, dato)) {
            return false;
        }
        cantidad++;
        return true;
    }

    /** Quita la primera coincidencia según el orden. Devuelve false si no está. */
    public boolean eliminar(T dato) {
        if (dato == null || raiz == null || buscarNodo(raiz, dato) == null) {
            return false;
        }
        raiz = eliminar(raiz, dato);
        cantidad--;
        return true;
    }

    /** Dato guardado que coincide en el orden, o null si no está. */
    public T buscar(T dato) {
        if (dato == null || raiz == null) {
            return null;
        }
        Nodo<T> nodo = buscarNodo(raiz, dato);
        return nodo == null ? null : nodo.dato;
    }

    public boolean contiene(T dato) {
        return buscar(dato) != null;
    }

    public T minimo() {
        if (raiz == null) {
            throw new NoSuchElementException("El árbol está vacío.");
        }
        return minimo(raiz).dato;
    }

    public T maximo() {
        if (raiz == null) {
            throw new NoSuchElementException("El árbol está vacío.");
        }
        Nodo<T> actual = raiz;
        while (actual.derecho != null) {
            actual = actual.derecho;
        }
        return actual.dato;
    }

    public T datoRaiz() {
        if (raiz == null) {
            throw new NoSuchElementException("El árbol está vacío.");
        }
        return raiz.dato;
    }

    /** Nodos del camino más largo. Un árbol vacío mide 0 y una hoja mide 1. */
    public int altura() {
        return altura(raiz);
    }

    public int contarHojas() {
        return contarHojas(raiz);
    }

    public int tamano() {
        return cantidad;
    }

    public boolean estaVacio() {
        return raiz == null;
    }

    public void vaciar() {
        raiz = null;
        cantidad = 0;
    }

    public List<T> preorden() {
        List<T> recorrido = new ArrayList<>();
        preorden(raiz, recorrido);
        return recorrido;
    }

    public List<T> inorden() {
        List<T> recorrido = new ArrayList<>();
        inorden(raiz, recorrido);
        return recorrido;
    }

    public List<T> postorden() {
        List<T> recorrido = new ArrayList<>();
        postorden(raiz, recorrido);
        return recorrido;
    }

    public List<T> porNiveles() {
        List<T> recorrido = new ArrayList<>();
        if (raiz == null) {
            return recorrido;
        }
        Cola<Nodo<T>> cola = new ColaEnlazada<>();
        cola.encolar(raiz);
        while (!cola.estaVacia()) {
            Nodo<T> actual = cola.desencolar();
            recorrido.add(actual.dato);
            if (actual.izquierdo != null) {
                cola.encolar(actual.izquierdo);
            }
            if (actual.derecho != null) {
                cola.encolar(actual.derecho);
            }
        }
        return recorrido;
    }

    @Override
    public String toString() {
        return inorden().toString();
    }

    private boolean insertar(Nodo<T> nodo, T dato) {
        int comparacion = comparador.compare(dato, nodo.dato);
        if (comparacion == 0) {
            return false;
        }
        if (comparacion < 0) {
            if (nodo.izquierdo == null) {
                nodo.izquierdo = new Nodo<>(dato);
                return true;
            }
            return insertar(nodo.izquierdo, dato);
        }
        if (nodo.derecho == null) {
            nodo.derecho = new Nodo<>(dato);
            return true;
        }
        return insertar(nodo.derecho, dato);
    }

    private Nodo<T> eliminar(Nodo<T> nodo, T dato) {
        if (nodo == null) {
            return null;
        }
        int comparacion = comparador.compare(dato, nodo.dato);
        if (comparacion < 0) {
            nodo.izquierdo = eliminar(nodo.izquierdo, dato);
        } else if (comparacion > 0) {
            nodo.derecho = eliminar(nodo.derecho, dato);
        } else if (nodo.izquierdo == null) {
            return nodo.derecho;
        } else if (nodo.derecho == null) {
            return nodo.izquierdo;
        } else {
            Nodo<T> sucesor = minimo(nodo.derecho);
            nodo.dato = sucesor.dato;
            nodo.derecho = eliminar(nodo.derecho, sucesor.dato);
        }
        return nodo;
    }

    private Nodo<T> buscarNodo(Nodo<T> nodo, T dato) {
        if (nodo == null) {
            return null;
        }
        int comparacion = comparador.compare(dato, nodo.dato);
        if (comparacion == 0) {
            return nodo;
        }
        if (comparacion < 0) {
            return buscarNodo(nodo.izquierdo, dato);
        }
        return buscarNodo(nodo.derecho, dato);
    }

    private Nodo<T> minimo(Nodo<T> nodo) {
        Nodo<T> actual = nodo;
        while (actual.izquierdo != null) {
            actual = actual.izquierdo;
        }
        return actual;
    }

    private int altura(Nodo<T> nodo) {
        if (nodo == null) {
            return 0;
        }
        return 1 + Math.max(altura(nodo.izquierdo), altura(nodo.derecho));
    }

    private int contarHojas(Nodo<T> nodo) {
        if (nodo == null) {
            return 0;
        }
        if (nodo.izquierdo == null && nodo.derecho == null) {
            return 1;
        }
        return contarHojas(nodo.izquierdo) + contarHojas(nodo.derecho);
    }

    private void preorden(Nodo<T> nodo, List<T> recorrido) {
        if (nodo == null) {
            return;
        }
        recorrido.add(nodo.dato);
        preorden(nodo.izquierdo, recorrido);
        preorden(nodo.derecho, recorrido);
    }

    private void inorden(Nodo<T> nodo, List<T> recorrido) {
        if (nodo == null) {
            return;
        }
        inorden(nodo.izquierdo, recorrido);
        recorrido.add(nodo.dato);
        inorden(nodo.derecho, recorrido);
    }

    private void postorden(Nodo<T> nodo, List<T> recorrido) {
        if (nodo == null) {
            return;
        }
        postorden(nodo.izquierdo, recorrido);
        postorden(nodo.derecho, recorrido);
        recorrido.add(nodo.dato);
    }

    @SuppressWarnings("unchecked")
    private static <T> Comparator<T> ordenNatural() {
        Comparator<T> natural = (Comparator<T>) Comparator.naturalOrder();
        return (a, b) -> {
            try {
                return natural.compare(a, b);
            } catch (ClassCastException excepcion) {
                throw new IllegalArgumentException(
                        "Sin comparador, el dato debe implementar Comparable.", excepcion);
            }
        };
    }

    private static <T> void validar(T dato) {
        if (dato == null) {
            throw new IllegalArgumentException("El dato no puede ser nulo.");
        }
    }

    private static final class Nodo<T> {
        private T dato;
        private Nodo<T> izquierdo;
        private Nodo<T> derecho;

        private Nodo(T dato) {
            this.dato = dato;
        }
    }
}
