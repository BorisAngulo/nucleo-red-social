package org.example.estructuradedatos;

/**
 * Pila (LIFO): el último dato en entrar es el primero en salir.
 * La cima es el único extremo por el que se apila y se desapila.
 * El recorrido de {@code Iterable} va de la cima hacia el fondo y no modifica la pila.
 */
public interface Pila<T> extends Iterable<T> {

    void apilar(T dato);

    T desapilar();

    T cima();

    boolean estaVacia();

    int tamano();

    void vaciar();
}
