package org.example.estructuradedatos;

/**
 * Cola (FIFO): el primero en entrar es el primero en salir.
 * Se encola por el final y se desencola por el frente.
 * El recorrido de {@code Iterable} va del frente hacia el final y no modifica la cola.
 */
public interface Cola<T> extends Iterable<T> {

    void encolar(T dato);

    T desencolar();

    T frente();

    T fin();

    boolean estaVacia();

    int tamano();

    void vaciar();
}
