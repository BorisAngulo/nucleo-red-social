package org.example.estructuradedatos;

/**
 * Pregunta sobre un estado. Verdadero baja por la izquierda del árbol de decisión
 * y falso baja por la derecha.
 */
@FunctionalInterface
public interface Condicion<T> {

    boolean cumple(T estado);
}
