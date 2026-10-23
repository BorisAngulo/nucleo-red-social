package org.example.practicas.practica10;

import org.example.estructuradedatos.ColaEnlazada;
import org.example.estructuradedatos.ListaDobleCircular;

/**
 * Reproductor en bucle.
 * La lista doble circular es el disco: la última canción apunta a la primera
 * y cada nodo conoce al tema anterior y al siguiente.
 * La cola enlazada guarda los pedidos. El más antiguo suena antes de seguir el bucle.
 */
public class ReproductorMusica {

    private final ListaDobleCircular<Cancion> lista;
    private final ColaEnlazada<Cancion> pedidos;

    public ReproductorMusica() {
        this.lista = new ListaDobleCircular<>();
        this.pedidos = new ColaEnlazada<>();
    }

    public boolean listaVacia() {
        return lista.estaVacia();
    }

    public boolean colaVacia() {
        return pedidos.estaVacia();
    }

    public int getTamanoLista() {
        return lista.tamano();
    }

    public int getPedidos() {
        return pedidos.tamano();
    }

    /** La cabeza de la lista es la canción que suena. */
    public Cancion actual() {
        if (lista.estaVacia()) {
            return null;
        }
        return lista.obtener(0);
    }

    /** Entra al final del bucle. La que suena no cambia. */
    public void agregarALista(Cancion cancion) {
        validar(cancion);
        lista.insertarAlFinal(cancion);
    }

    /** El pedido espera en la cola hasta que se pida la siguiente canción. */
    public void pedir(Cancion cancion) {
        validar(cancion);
        pedidos.encolar(cancion);
    }

    /**
     * Avanza un tema. Si hay pedidos, el más antiguo entra justo después
     * del que suena y pasa a ser la cabeza. Si no hay, gira el bucle.
     */
    public Cancion siguiente() {
        if (!pedidos.estaVacia()) {
            Cancion pedida = pedidos.desencolar();
            if (lista.estaVacia()) {
                lista.insertarAlFinal(pedida);
            } else {
                lista.insertarEn(1, pedida);
                lista.rotar();
            }
            return pedida;
        }
        if (lista.estaVacia()) {
            return null;
        }
        lista.rotar();
        return lista.obtener(0);
    }

    /** Retrocede por el enlace anterior. Del primero vuelve a la última. */
    public Cancion anterior() {
        if (lista.estaVacia()) {
            return null;
        }
        lista.rotarInverso();
        return lista.obtener(0);
    }

    /** Quita la que suena. La siguiente del círculo queda en su lugar. */
    public Cancion quitarActual() {
        if (lista.estaVacia()) {
            return null;
        }
        return lista.eliminarAlInicio();
    }

    public void mostrar() {
        if (lista.estaVacia()) {
            System.out.println("[ Lista vacía: no hay canciones en el bucle ]");
        } else {
            System.out.println("  Bucle, " + lista.tamano() + " canciones. Sonando la 1 de esta vuelta.");
            int posicion = 1;
            for (Cancion cancion : lista) {
                String marca = posicion == 1 ? "  <-- [SONANDO]" : "";
                System.out.println("  " + posicion + ". " + cancion + marca);
                posicion++;
            }
        }
        if (pedidos.estaVacia()) {
            System.out.println("[ Cola de pedidos vacía ]");
            return;
        }
        System.out.println("  Pedidos en espera: " + pedidos.tamano() + ". El primero es el próximo.");
        int orden = 1;
        for (Cancion cancion : pedidos) {
            System.out.println("  " + orden + ". " + cancion);
            orden++;
        }
    }

    private static void validar(Cancion cancion) {
        if (cancion == null) {
            throw new IllegalArgumentException("La canción no puede ser nula.");
        }
    }
}
