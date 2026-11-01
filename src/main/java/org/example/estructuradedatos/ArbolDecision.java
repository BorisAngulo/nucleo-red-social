package org.example.estructuradedatos;

/**
 * Árbol binario de decisión. La raíz apunta a un nodo.
 * Un nodo interno guarda una pregunta y dos hijos: el izquierdo es el sí
 * y el derecho es el no. Una hoja no tiene hijos y guarda el resultado.
 */
public class ArbolDecision<T, R> {

    private final Nodo<T, R> raiz;

    private ArbolDecision(Nodo<T, R> raiz) {
        this.raiz = raiz;
    }

    public static <T, R> ArbolDecision<T, R> hoja(R resultado) {
        if (resultado == null) {
            throw new IllegalArgumentException("El resultado de la hoja no puede ser nulo.");
        }
        return new ArbolDecision<>(new Nodo<>(resultado));
    }

    public static <T, R> ArbolDecision<T, R> pregunta(Condicion<T> condicion,
            ArbolDecision<T, R> siVerdadero, ArbolDecision<T, R> siFalso) {
        if (condicion == null) {
            throw new IllegalArgumentException("La condición no puede ser nula.");
        }
        if (siVerdadero == null || siFalso == null) {
            throw new IllegalArgumentException("La pregunta necesita los dos hijos.");
        }
        return new ArbolDecision<>(new Nodo<>(condicion, siVerdadero.raiz, siFalso.raiz));
    }

    public boolean esHoja() {
        return raiz.esHoja();
    }

    /** Baja desde la raíz, eligiendo el hijo izquierdo o el derecho, hasta una hoja. */
    public R decidir(T estado) {
        if (estado == null) {
            throw new IllegalArgumentException("El estado no puede ser nulo.");
        }
        return decidir(raiz, estado);
    }

    private R decidir(Nodo<T, R> nodo, T estado) {
        if (nodo.esHoja()) {
            return nodo.resultado;
        }
        if (nodo.condicion.cumple(estado)) {
            return decidir(nodo.izquierdo, estado);
        }
        return decidir(nodo.derecho, estado);
    }

    private static final class Nodo<T, R> {

        private final Condicion<T> condicion;
        private final R resultado;
        private final Nodo<T, R> izquierdo;
        private final Nodo<T, R> derecho;

        private Nodo(R resultado) {
            this.condicion = null;
            this.resultado = resultado;
            this.izquierdo = null;
            this.derecho = null;
        }

        private Nodo(Condicion<T> condicion, Nodo<T, R> izquierdo, Nodo<T, R> derecho) {
            this.condicion = condicion;
            this.resultado = null;
            this.izquierdo = izquierdo;
            this.derecho = derecho;
        }

        private boolean esHoja() {
            return izquierdo == null && derecho == null;
        }
    }
}
