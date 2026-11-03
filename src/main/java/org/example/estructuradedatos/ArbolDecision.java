package org.example.estructuradedatos;

/**
 * Árbol binario de decisión. Cada nodo es una pregunta o una hoja.
 * La pregunta tiene dos hijos: el izquierdo es el sí y el derecho es el no.
 * La hoja no tiene hijos y guarda el resultado.
 */
public class ArbolDecision {

    private final Nodo raiz;

    public ArbolDecision(Nodo raiz) {
        if (raiz == null) {
            throw new IllegalArgumentException("La raíz no puede ser nula.");
        }
        this.raiz = raiz;
    }

    public Nodo getRaiz() {
        return raiz;
    }

    public static final class Nodo {

        private final String pregunta;
        private final String resultado;
        private final Nodo izquierdo;
        private final Nodo derecho;

        private Nodo(String pregunta, String resultado, Nodo izquierdo, Nodo derecho) {
            this.pregunta = pregunta;
            this.resultado = resultado;
            this.izquierdo = izquierdo;
            this.derecho = derecho;
        }

        /** Hoja: el texto que se devuelve al llegar aquí. */
        public static Nodo hoja(String resultado) {
            if (resultado == null || resultado.isBlank()) {
                throw new IllegalArgumentException("El resultado de la hoja no puede estar vacío.");
            }
            return new Nodo(null, resultado, null, null);
        }

        /** Pregunta con su hijo izquierdo (sí) y su hijo derecho (no). */
        public static Nodo pregunta(String pregunta, Nodo izquierdo, Nodo derecho) {
            if (pregunta == null || pregunta.isBlank()) {
                throw new IllegalArgumentException("La pregunta no puede estar vacía.");
            }
            if (izquierdo == null || derecho == null) {
                throw new IllegalArgumentException("La pregunta necesita los dos hijos.");
            }
            return new Nodo(pregunta, null, izquierdo, derecho);
        }

        public boolean esHoja() {
            return izquierdo == null && derecho == null;
        }

        public String getPregunta() {
            return pregunta;
        }

        public String getResultado() {
            return resultado;
        }

        public Nodo getIzquierdo() {
            return izquierdo;
        }

        public Nodo getDerecho() {
            return derecho;
        }
    }
}
