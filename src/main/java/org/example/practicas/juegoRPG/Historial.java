package org.example.practicas.juegoRPG;

import org.example.estructuradedatos.ListaEnlazada;

/** Bitácora de la partida. Cada suceso se engancha al final de la lista simple. */
public class Historial {

    private final ListaEnlazada<String> sucesos;

    public Historial() {
        this.sucesos = new ListaEnlazada<>();
    }

    public void registrar(String suceso) {
        if (suceso == null || suceso.isBlank()) {
            throw new IllegalArgumentException("El suceso no puede estar vacío.");
        }
        sucesos.insertarAlFinal(suceso);
    }

    public void mostrar() {
        if (sucesos.estaVacia()) {
            System.out.println("[ Historial vacío ]");
            return;
        }
        int numero = 1;
        for (String suceso : sucesos) {
            System.out.println("  " + numero + ". " + suceso);
            numero++;
        }
    }
}
