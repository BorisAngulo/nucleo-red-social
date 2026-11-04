package org.example.redsocial.perfiles;

import org.example.estructuradedatos.ListaEnlazada;
import org.example.redsocial.separacion.Usuario;

/** Arreglo fijo de perfiles construidos desde los usuarios de la red existente. */
public final class ArregloPerfiles {
    public static final int CAPACIDAD = 100;
    private final Perfil[] perfiles = new Perfil[CAPACIDAD];
    private int cantidad;

    public ArregloPerfiles(ListaEnlazada<Usuario> usuarios) {
        if (usuarios == null) throw new IllegalArgumentException("La red de usuarios es nula.");
        for (Usuario usuario : usuarios) {
            if (cantidad == CAPACIDAD) {
                throw new IllegalStateException("Se superó la capacidad fija de " + CAPACIDAD + " perfiles.");
            }
            perfiles[cantidad] = new Perfil(cantidad + 1, usuario);
            cantidad++;
        }
    }

    public int getCantidad() { return cantidad; }
    public Perfil get(int indice) {
        if (indice < 0 || indice >= cantidad) throw new IndexOutOfBoundsException("Índice inválido.");
        return perfiles[indice];
    }

    /** Devuelve copia exacta del tramo ocupado; sigue siendo un arreglo. */
    public Perfil[] copiarOcupados() {
        Perfil[] copia = new Perfil[cantidad];
        for (int i = 0; i < cantidad; i++) copia[i] = perfiles[i];
        return copia;
    }
}
